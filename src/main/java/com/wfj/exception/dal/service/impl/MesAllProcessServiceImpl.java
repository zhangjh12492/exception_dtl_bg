package com.wfj.exception.dal.service.impl;

import java.util.List;

import org.mortbay.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.wfj.exception.common.SysConstants;
import com.wfj.exception.dal.dao.MesAllProcessDao;
import com.wfj.exception.dal.entity.MesAllProcessReq;
import com.wfj.exception.dal.service.MesProcessService;
import com.wfj.exception.util.HttpRequestUtil;
import com.wfj.exception.util.HttpUtil;
import com.wfj.exception.util.PropertiesLoad;

@Service("mesAllProcessService")
public class MesAllProcessServiceImpl implements MesProcessService<MesAllProcessReq>{

	private static final Logger log=LoggerFactory.getLogger(MesAllProcessServiceImpl.class);

	private MesAllProcessDao mesAllProcessDao;

	@Override
	public MesAllProcessReq selectBySysCode(String sysCode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertMesSys(MesAllProcessReq sysReq) throws Exception {
		mesAllProcessDao.insert(sysReq);
	}

	@Override
	public void insertMesSysList(List<MesAllProcessReq> sysReqs) throws Exception {
		if(sysReqs!=null && sysReqs.size()>0){
			for(int i=0;i<sysReqs.size();i++){
				mesAllProcessDao.insert(sysReqs.get(i));
				
				if(sysReqs.get(i).getCode().length()==2){	//判断是否是系统的,如果是,调用发送短信或邮件的接口
					log.info("开始发送统计后的系统数据,param:{}",sysReqs.get(i).toString());
					
					HttpRequestUtil.send(PropertiesLoad.getProperties(SysConstants.PROCESS_SEND_USER_BY_EW), JSONObject.toJSONString(sysReqs.get(i)));
				}
			}
			
		}
	}
	
	
	public MesAllProcessDao getMesAllProcessDao() {
		return mesAllProcessDao;
	}
	public void setMesAllProcessDao(MesAllProcessDao mesAllProcessDao) {
		this.mesAllProcessDao = mesAllProcessDao;
	}

	public static Logger getLog() {
		return log;
	}

	public static void main(String[] args) throws Exception{
		MesAllProcessReq mesAllReq=new MesAllProcessReq();
		mesAllReq.setCode("01");
		HttpRequestUtil.send("http://172.16.3.202:8080/exceptionSocketPro/mesProcessResult.do?processSendUserByEW",  "{\"code\":\"03\",\"createdTime\":\"2015-08-31 23:53:01\",\"errorCount\":91,\"warnCount\":145}");
	}
	

}
