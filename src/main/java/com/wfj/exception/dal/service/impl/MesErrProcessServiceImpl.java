package com.wfj.exception.dal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wfj.exception.dal.dao.MesErrProcessDao;
import com.wfj.exception.dal.entity.MesErrProcessReq;
import com.wfj.exception.dal.service.MesProcessService;

@Service("mesErrProcessService")
public class MesErrProcessServiceImpl implements MesProcessService<MesErrProcessReq>{

	private MesErrProcessDao mesErrProcessDao;
	
	@Override
	public MesErrProcessReq selectBySysCode(String sysCode) throws Exception {
		
		return null;
	}

	@Override
	public void insertMesSys(MesErrProcessReq sysReq) throws Exception {
		mesErrProcessDao.insertMesErrProcess(sysReq);
		
	}

	@Override
	public void insertMesSysList(List<MesErrProcessReq> sysReqs) throws Exception {
		for(int i=0;i<sysReqs.size();i++){
			mesErrProcessDao.insertMesErrProcess(sysReqs.get(i));
		}
	}

	public MesErrProcessDao getMesErrProcessDao() {
		return mesErrProcessDao;
	}

	public void setMesErrProcessDao(MesErrProcessDao mesErrProcessDao) {
		this.mesErrProcessDao = mesErrProcessDao;
	}

}
