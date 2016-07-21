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
import com.wfj.exception.common.ProcessStatusEnum;
import com.wfj.exception.common.SysConstants;
import com.wfj.exception.dal.entity.MesBusiProcessReq;
import com.wfj.exception.dal.entity.MesErrProcessReq;
import com.wfj.exception.dal.entity.MesSysProcessReq;
import com.wfj.exception.dal.service.MesEarlyWarnService;
import com.wfj.exception.mesGate.service.MesServiceGate;
import com.wfj.exception.spring.SpringHandlerBean;
import com.wfj.exception.util.DateUtils;
import com.wfj.exception.util.HttpUtil;
import com.wfj.exception.util.StringUtils;
import com.wfj.exception.vo.ClientExceptionReq;

public class MesCountReduce extends TableReducer<Text, Text, ImmutableBytesWritable>{
	private static final Logger log=LoggerFactory.getLogger(MesCountReduce.class);
	
	private MesServiceGate mesServiceGate=(MesServiceGate) SpringHandlerBean.getContext("mesServiceGate");
	private MesEarlyWarnService mesEarlyWarnService=(MesEarlyWarnService) SpringHandlerBean.getContext("mesEarlyWarnService");
	
	
	private String sysCode="";
	private Map<String,Integer> busiWarnMap=new HashMap<String,Integer>();
	private Map<String,Integer> busiErrMap=new HashMap<String,Integer>();
	
	private Map<String,Integer> errWarnMap=new HashMap<String, Integer>();
	private Map<String,Integer> errErrMap=new HashMap<String, Integer>();
	
	List<String> errIds=new ArrayList<String>();
	List<String> errMessIds=new ArrayList<String>();
	List<String> warnMessIds=new ArrayList<String>();
	
	Map<String,List<String>> errIdMap=new HashMap<String,List<String>>();
	Map<String,List<String>> errMessIdMap=new HashMap<String,List<String>>();
	Map<String,List<String>> warnMessIdMap=new HashMap<String,List<String>>();
	
	
	private String splitStr=":";
	private int sysWarnCount=0;
	private int sysErrCount=0;
	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		String errId=null;
		ClientExceptionReq clientReq=null;
		String keys[] =null;
		for(Text t:values){
			clientReq=JSONObject.parseObject(t.toString(),ClientExceptionReq.class);
			errId=clientReq.getErrId();
		}
		
		if(StringUtils.isBlank(errId)){
			return ;
		}
		keys=qualifierConvert(errId);
		log.info("\nkeys : {}",JSONObject.toJSONString(keys));
		String errLevel=keys[0];
		String busiCode=keys[3];
		sysCode=keys[2];
		
		String errsCode=sysCode+busiCode+keys[4];
		
		if(busiWarnMap.get(busiCode)==null){ 
			busiWarnMap.put(busiCode, 0);
		}
		if(busiErrMap.get(busiCode)==null){
			busiErrMap.put(busiCode, 0);
		}
		if(errErrMap.get(errsCode)==null){
			errErrMap.put(errsCode, 0);
		}
		if(errWarnMap.get(errsCode)==null){
			errWarnMap.put(errsCode, 0);
		}
		
		
		if(errLevel!=null){
			//waring
			if(errLevel.equals(ErrLevelEnum.WARNING.getCode())){
				//put warn's id in map
				warnMessIds.add(errId);
				warnMessIdMap.put(busiCode, warnMessIds);
				if(busiWarnMap.get(busiCode)>=0){ 
					busiWarnMap.put(busiCode, busiWarnMap.get(busiCode)+1);
				}
				if(errWarnMap.get(errsCode)>=0){
					errWarnMap.put(errsCode, errWarnMap.get(errsCode)+1);
				}
				sysWarnCount+=1;
			}else if(errLevel.equals(ErrLevelEnum.ERROR.getCode())){	//error
				//put err's id in map
				errMessIds.add(errId);
				errMessIdMap.put(busiCode, errMessIds);
				if(busiErrMap.get(busiCode)>=0){ 
					busiErrMap.put(busiCode, busiErrMap.get(busiCode)+1);
				}
				if(errErrMap.get(errsCode)>=0){
					errErrMap.put(errsCode, errErrMap.get(errsCode)+1);
				}
				sysErrCount+=1;
			}
		}
		//put errIds into errIdMap 
		errIds.add(errId);
		errIdMap.put(busiCode, errIds);
	}


	@Override
	protected void cleanup(Context context) throws IOException, InterruptedException {
		
		System.out.println("errIdMap  :  "+errIdMap);
		System.out.println("errMessIdMap  :  "+errMessIdMap);
		System.out.println("warnMessIdMap  :  "+warnMessIdMap);
		System.out.println();
		
		System.out.println(sysErrCount+"     sysErrCount  ");
		System.out.println(sysWarnCount+"    : sysWarnCount");
		
		for(Entry<String, Integer> entry:busiErrMap.entrySet()){
			System.out.println(sysCode+"   :   "+"busiErrMap:  "+entry.getKey()+","+entry.getValue());
		}
		for(Entry<String, Integer> entry:busiWarnMap.entrySet()){
			System.out.println(sysCode+"   :   "+"busiWarnMap:  "+entry.getKey()+","+entry.getValue());
		}
		
		MesSysProcessReq sysReq=new MesSysProcessReq();
		sysReq.setSysCode(sysCode);
		sysReq.setSysErrCount(sysErrCount);
		sysReq.setSysWarnCount(sysWarnCount);
		sysReq.setCreatedTime(DateUtils.getCurrentDate());
		List<MesBusiProcessReq> listBusi=new ArrayList<MesBusiProcessReq>();
		for(String key:busiErrMap.keySet()){
			MesBusiProcessReq busiReq=new MesBusiProcessReq();
			busiReq.setBusiCode(key);
			busiReq.setBusiErrCount(busiErrMap.get(key));
			busiReq.setBusiWarnCount(busiWarnMap.get(key));
			busiReq.setSysCode(sysCode);
			busiReq.setCreatedTime(DateUtils.getCurrentDate());
			if(errIdMap.get(key)!=null&&errIdMap.get(key).size()>0){
				StringBuffer sb=new StringBuffer();
				String errId="";
				for(String str:errIdMap.get(key)){
					sb.append(str).append(",");
				}
				errId=sb.substring(0,sb.length()-1);
				busiReq.setErrId(errId);
			}
			if(errMessIdMap.get(key)!=null&&errMessIdMap.get(key).size()>0){
				StringBuffer sb=new StringBuffer();
				String errMessId="";
				for(String str:errMessIdMap.get(key)){
					sb.append(str).append(",");
				}
				errMessId=sb.substring(0,sb.length()-1);
				busiReq.setErrMessId(errMessId);
			}
			if(warnMessIdMap.get(key)!=null&&warnMessIdMap.get(key).size()>0){
				StringBuffer sb=new StringBuffer();
				String warnMessId="";
				for(String str:warnMessIdMap.get(key)){
					sb.append(str).append(",");
				}
				warnMessId=sb.substring(0,sb.length()-1);
				busiReq.setWarnMessId(warnMessId);
			}
			List<MesErrProcessReq> errProcessList=new ArrayList<MesErrProcessReq>();
			for(String errsCode:errErrMap.keySet()){
				if(errsCode.substring(2,5).equals(key)){
					MesErrProcessReq errProcess=new MesErrProcessReq();
					errProcess.setSysCode(sysCode);
					errProcess.setBusiCode(key);
					errProcess.setCreatedTime(DateUtils.getCurrentDate());
					errProcess.setErrErrCount(errErrMap.get(errsCode));
					errProcess.setErrWarnCount(errWarnMap.get(errsCode));
					errProcess.setErrCode(errsCode.substring(5,8));
					errProcess.setAllCode(errsCode);
					errProcess.setProcessStatus(ProcessStatusEnum.UNDISPOSED.getCode());
					errProcessList.add(errProcess);
				}
			}
			busiReq.setMesErrprocessList(errProcessList);
			listBusi.add(busiReq);
			
		}
		sysReq.setBusiList(listBusi);
		try {
			mesServiceGate.insertReduceMes(sysReq);		//将处理后的系统和业务的异常统计数放入数据库
		} catch (Exception e1) {
			log.error("insert sysReq to mysql database error");
			e1.printStackTrace();
		}
		//sendEWToUser(sysReq);	//将统计后的信息发送给指定处理的人员	
		
		Map<String, String> param=new HashMap<String, String>();
		param.put("data", JSONObject.toJSONString(sysReq));
		try {
			log.info("\nSend socket message to server,param: {} ",param);
			//将处理后的结果发送到信息展示层的webSocket页面中
			HttpUtil.http(SysConstants.sockeServerUrl, param);
		} catch (Exception e) {
			log.error("Send socket message to server error: {}",e.getMessage());
			e.printStackTrace();
		}
		
		
		super.cleanup(context);
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
	/**
	 * 将处理后的信息做判断发送个指定的用户，另开线程，不影响后续的任务执行
	 * @Title: sendEWToUser
	 * @author ZJHao
	 * @return void
	 * @throws
	 * @date 2015-8-19 上午10:35:58
	 */
	public void sendEWToUser(final MesSysProcessReq req){
		new Thread(){
			public void run() {
				try {
					log.info("开始处理异常信息并发送邮件..,mesEarlyWarnService:{}",mesEarlyWarnService);
					mesEarlyWarnService.proceSysEarlyWarnConfig(req);
				} catch (Exception e) {
					log.error("将处理信息发送给指定人员失败!");
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	
}
