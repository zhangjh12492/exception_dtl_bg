package com.wfj.exception.hadoop.hadoop.reduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.wfj.exception.common.ErrLevelEnum;
import com.wfj.exception.dal.entity.BusiInfo;
import com.wfj.exception.dal.entity.MesAllProcessReq;
import com.wfj.exception.dal.service.MesEarlyWarnService;
import com.wfj.exception.mesGate.service.MesServiceGate;
import com.wfj.exception.spring.SpringHandlerBean;
import com.wfj.exception.util.DateUtils;
import com.wfj.exception.util.HttpClientUtil;
import com.wfj.exception.util.StringUtils;
import com.wfj.exception.vo.ClientExceptionReq;

public class MesAllCodeReduct extends TableReducer<Text, Text, ImmutableBytesWritable>{
	
	private MesServiceGate mesServiceGate=(MesServiceGate) SpringHandlerBean.getContext("mesServiceGate");
	private static Logger log=LoggerFactory.getLogger(MesAllCodeReduct.class);
	/*系统数量统计map*/
	private Map<String,Integer> sysWarnMap=new HashMap<String, Integer>();
	private Map<String,Integer> sysErrorMap=new HashMap<String, Integer>();
	/*业务数量统计map*/
	private Map<String,Integer> busiWarnMap=new HashMap<String, Integer>();
	private Map<String,Integer> busiErrorMap=new HashMap<String, Integer>();
	/*异常数量统计map*/
	private Map<String,Integer> errWarnMap=new HashMap<String, Integer>();
	private Map<String,Integer> errErrorMap=new HashMap<String, Integer>();
	
	private Map<String,BusiInfo> sysBusiMap=new HashMap<String, BusiInfo>();
	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		String errId=null;	//异常信息编号
		String keys[] =null;	//异常信息编号拆分成数组
		ClientExceptionReq clientReq=null;
		
		for(Text t:values){
			clientReq=JSONObject.parseObject(t.toString(),ClientExceptionReq.class);
			errId=clientReq.getErrId();
		}
		if(StringUtils.isBlank(errId)){
			return ;
		}
		BusiInfo busiInfo=new BusiInfo();
		busiInfo.setSysCode(clientReq.getSysCode());
		busiInfo.setBusiCode(clientReq.getBusiCode());
		busiInfo.setBusiDesc(clientReq.getBusiDesc());
		sysBusiMap.put(clientReq.getSysCode()+clientReq.getBusiCode(), busiInfo);
		
		keys=qualifierConvert(errId);
		String errLevel=keys[0];
		String sysCode=keys[2];
		String busiCode=keys[2]+keys[3];
		String errCode=keys[2]+keys[3]+keys[4];
		/*初始化所有的map*/
		if(sysWarnMap.get(sysCode)==null){ 
			sysWarnMap.put(sysCode, 0);
		}
		if(sysErrorMap.get(sysCode)==null){
			sysErrorMap.put(sysCode, 0);
		}
		if(busiWarnMap.get(busiCode)==null){ 
			busiWarnMap.put(busiCode, 0);
		}
		if(busiErrorMap.get(busiCode)==null){
			busiErrorMap.put(busiCode, 0);
		}
		if(errErrorMap.get(errCode)==null){
			errErrorMap.put(errCode, 0);
		}
		if(errWarnMap.get(errCode)==null){
			errWarnMap.put(errCode, 0);
		}
		/**/
		if (StringUtils.isNotBlank(errLevel)) {
			//warn
			if (errLevel.equals(ErrLevelEnum.WARNING.getCode())) {
				sysWarnMap.put(sysCode, sysWarnMap.get(sysCode)+1);
				busiWarnMap.put(busiCode, busiWarnMap.get(busiCode) + 1);
				errWarnMap.put(errCode, errWarnMap.get(errCode) + 1);
				
			}
			//error
			if (errLevel.equals(ErrLevelEnum.ERROR.getCode())) {
				sysErrorMap.put(sysCode, sysErrorMap.get(sysCode)+1);
				busiErrorMap.put(busiCode, busiErrorMap.get(busiCode) + 1);
				errErrorMap.put(errCode, errErrorMap.get(errCode) + 1);
			}
		}
	}
	
	@Override
	protected void cleanup(Context context) throws IOException, InterruptedException {
		List<MesAllProcessReq> mesAllProceList=new ArrayList<MesAllProcessReq>();
		for(Entry<String, Integer> sysEntry:sysWarnMap.entrySet()){
			MesAllProcessReq mesAllproce=new MesAllProcessReq();
			mesAllproce.setCode(sysEntry.getKey());
			mesAllproce.setWarnCount(sysWarnMap.get(sysEntry.getKey()));
			mesAllproce.setErrorCount(sysErrorMap.get(sysEntry.getKey()));
			mesAllproce.setCreatedTime(DateUtils.getCurrentDate());
			mesAllProceList.add(mesAllproce);
		}
		for(Entry<String, Integer> sysEntry:busiWarnMap.entrySet()){
			MesAllProcessReq mesAllproce=new MesAllProcessReq();
			mesAllproce.setCode(sysEntry.getKey());
			mesAllproce.setWarnCount(busiWarnMap.get(sysEntry.getKey()));
			mesAllproce.setErrorCount(busiErrorMap.get(sysEntry.getKey()));
			mesAllproce.setCreatedTime(DateUtils.getCurrentDate());
			mesAllProceList.add(mesAllproce);
		}
		for(Entry<String, Integer> sysEntry:errWarnMap.entrySet()){
			MesAllProcessReq mesAllproce=new MesAllProcessReq();
			mesAllproce.setCode(sysEntry.getKey());
			mesAllproce.setWarnCount(errWarnMap.get(sysEntry.getKey()));
			mesAllproce.setErrorCount(errErrorMap.get(sysEntry.getKey()));
			mesAllproce.setCreatedTime(DateUtils.getCurrentDate());
			mesAllProceList.add(mesAllproce);
		}
		try {
			log.info("开始插入统计后的各类型异常的数量,param:{}",mesAllProceList.toString());
			mesServiceGate.insertMesAllProcessList(mesAllProceList);
			log.info("插入成功!");
		} catch (Exception e) {
			log.info("插入失败");
			e.printStackTrace();
		}
		insertBusiByReduce(sysBusiMap);
		super.cleanup(context);
	}


	public static void insertBusiByReduce(Map<String, BusiInfo> busis){
		try {
			BusiInfo busiInfo=new BusiInfo();
			busiInfo.setBusiCode("02");
			System.out.println(JSONObject.toJSONString(busis));
			HttpClientUtil.postJSON("http://localhost:8080/exceptionSocketPro/busi.do?insertBusiByReduce", JSONObject.toJSONString(busis));
			log.info("发送业务数据成功!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 转换errId
	 * @param errId
	 * 生成规则,errLevel+flag+sysCode+busiCode+errCode+sysErrCode+timeStamp	
	 * @return
	 */
	private static String[] qualifierConvert(String errId){
		System.out.println("errId  :   "+errId);
		String [] str=new String[7];
		str[0]=errId.substring(0,1);	//errLevel
		str[1]=errId.substring(1,2);	//flag
		str[2]=errId.substring(2,4);	//sysCode
		str[3]=errId.substring(4,7);	//busiCode
		str[4]=errId.substring(7,10);	//errCode
		str[5]=errId.substring(10,13);	//sysErrCode
		str[6]=errId.substring(13);		//timeStamp
		return str;
	}
	
	
}
