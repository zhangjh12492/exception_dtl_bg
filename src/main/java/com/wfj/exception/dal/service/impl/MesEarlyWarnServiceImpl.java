package com.wfj.exception.dal.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mortbay.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.wfj.exception.common.ErrLevelEnum;
import com.wfj.exception.common.ProcessStatusEnum;
import com.wfj.exception.common.MesEarlyWarnStatusEnum;
import com.wfj.exception.dal.dao.MesEarlyWarnInfoDao;
import com.wfj.exception.dal.dao.UserInfoDao;
import com.wfj.exception.dal.entity.BusiInfo;
import com.wfj.exception.dal.entity.MesEarlyWarnInfo;
import com.wfj.exception.dal.entity.MesSysProcessReq;
import com.wfj.exception.dal.service.MesEarlyWarnService;
import com.wfj.exception.vo.UserInfoVo;
import com.wfj.exception.email.util.MailUtil;

@Service("mesEarlyWarnService")
public class MesEarlyWarnServiceImpl implements MesEarlyWarnService {

	
	private Logger log=LoggerFactory.getLogger(MesEarlyWarnServiceImpl.class);
	
	private MesEarlyWarnInfoDao mesEarlyWarnDao;
	
	private UserInfoDao userInfoDao;
	
	/**
	 * 根据系统编码、异常等级、异常数量等获取预警处理的配置文件然后发送预警信息
	 * @Title: proceEarlyWarnConfig
	 * @author ZJHao
	 * @param param
	 * @return 
	 * @throws Exception
	 * @throws
	 * @date 2015-8-18 下午8:23:49
	 */
	@Override
	public void proceSysEarlyWarnConfig(MesSysProcessReq sysProcess) throws Exception {
		
		Map<String,String> param=new HashMap<String, String>();
		param.put("status", MesEarlyWarnStatusEnum.START.getCode());
		param.put("errLevel", ErrLevelEnum.WARNING.getCode());
		param.put("count", sysProcess.getSysWarnCount()+"");
		param.put("sysCode", sysProcess.getSysCode());
		log.info("查询所有的系统预警配置信息...{}",param.toString());
		List<MesEarlyWarnInfo> listWarn=mesEarlyWarnDao.selectAllSysStartEW(param);
		log.info("查询所有的系统预警配置信息结束,{}",listWarn.toString());
		
		log.info("查询所有系统下用户组的用户,并且知道该用户是需要发送邮件还是短信");
		List<UserInfoVo> userVoList=userInfoDao.selectAllUserVo(param.get("sysCode"));
		log.info("查询所有系统下的用户组中的用户结束,{}",JSONObject.toJSONString(userVoList));
		//如果查到数据,获取管理此系统的人员信息,发送短信或者邮件
		if(listWarn!=null&&listWarn.size()>0){
			String subject="异常监控平台";
			String mainBody="警告数据达到预警数量\n" +
					"<a href='http://172.16.3.202:8080/exceptionSocketPro/admin/updateMesProcessStatus.do?sysCode="+sysProcess.getSysCode()+"&beforeProcessStatus="+ProcessStatusEnum.UNDISPOSED.getCode()+"&errLevel="+param.get("errLevel")+"' >http://172.16.3.202:8080/exceptionSocketPro</a>";
			for(UserInfoVo user:userVoList){
				param.put("userId", user.getId()+"");
				String [] sendTypes=userInfoDao.selectSendType(param);
				for(String s:sendTypes){
					if(s.equals("0")){	//0,发送邮件
						MailUtil.sendMail(user.getEmail(), subject, mainBody);
					}else if(s.equals("1")){//1,发送短信
						
					}
				}
				
			}
		}
		
		param.put("errLevel", ErrLevelEnum.ERROR.getCode());
		param.put("count", sysProcess.getSysErrCount()+"");
		param.put("sysCode", sysProcess.getSysCode());
		List<MesEarlyWarnInfo> listErr=mesEarlyWarnDao.selectAllSysStartEW(param);
		if(listWarn!=null&&listErr.size()>0){
			String subject="异常监控平台";
			String mainBody="异常数据达到预警数量\n" +
					"<a href='http://172.16.3.202:8080/exceptionSocketPro/admin/updateMesProcessStatus.do?sysCode="+sysProcess.getSysCode()+"&beforeProcessStatus="+ProcessStatusEnum.UNDISPOSED.getCode()+"&errLevel="+param.get("errLevel")+"' >http://172.16.3.202:8080/exceptionSocketPro</a>";
			for(UserInfoVo user:userVoList){
				param.put("userId", user.getId()+"");
				String [] sendTypes=userInfoDao.selectSendType(param);
				for(String s:sendTypes){ 
					if(s.equals("0")){	//0,发送邮件
						MailUtil.sendMail(user.getEmail(), subject, mainBody);
					}else if(s.equals("1")){//1,发送短信
						
					}
				}
			}
		}
	}
	/**
	 * 根据系统编码、业务编码、异常等级、异常数量等获取预警处理的配置文件然后发送预警信息
	 * @Title: proceEarlyWarnConfig
	 * @author ZJHao
	 * @param param
	 * @return 
	 * @throws Exception
	 * @throws
	 * @date 2015-8-18 下午8:23:49
	 */
	@Override
	public void proceBusiEarlyWarnConfig(Map<String, String> param) throws Exception {
		param.put("status", MesEarlyWarnStatusEnum.START.getCode());
		List<MesEarlyWarnInfo> list=mesEarlyWarnDao.selectAllBusiStartEW(param);
	}
	@Override
	public void insertBusiByReduce(Map<String, BusiInfo> busis) throws Exception {
		
		
	}
	public MesEarlyWarnInfoDao getMesEarlyWarnDao() {
		return mesEarlyWarnDao;
	}
	public void setMesEarlyWarnDao(MesEarlyWarnInfoDao mesEarlyWarnDao) {
		this.mesEarlyWarnDao = mesEarlyWarnDao;
	}
	public UserInfoDao getUserInfoDao() {
		return userInfoDao;
	}
	public void setUserInfoDao(UserInfoDao userInfoDao) {
		this.userInfoDao = userInfoDao;
	}

}
