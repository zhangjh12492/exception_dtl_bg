package com.wfj.exception.dal.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wfj.exception.dal.dao.MesSysProcessDao;
import com.wfj.exception.dal.entity.MesSysProcessReq;
import com.wfj.exception.dal.service.MesProcessService;

@Service("mesSysProcessService")
public class MesSysProcessServiceImpl implements MesProcessService<MesSysProcessReq> {

	private MesSysProcessDao sysProcessDao;
	
	@Override
	public MesSysProcessReq selectBySysCode(String sysCode) throws Exception{
		return sysProcessDao.selectBySysCode(sysCode);
	}

	@Override
	public void insertMesSys(MesSysProcessReq sysReq) throws Exception {
		sysProcessDao.insert(sysReq);
		System.out.println(sysReq.getId()+"      iddddddd=================");
	}

	@Override
	public void insertMesSysList(List<MesSysProcessReq> sysReqs) throws Exception {
		
		
	}

	public MesSysProcessDao getSysProcessDao() {
		return sysProcessDao;
	}

	public void setSysProcessDao(MesSysProcessDao sysProcessDao) {
		this.sysProcessDao = sysProcessDao;
	}


}
