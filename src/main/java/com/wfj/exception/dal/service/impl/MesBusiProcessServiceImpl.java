package com.wfj.exception.dal.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wfj.exception.dal.dao.MesBusiProcessDao;
import com.wfj.exception.dal.entity.MesBusiProcessReq;
import com.wfj.exception.dal.service.MesProcessService;

@Service("mesBusiProcessService")
public class MesBusiProcessServiceImpl implements MesProcessService<MesBusiProcessReq> {

	private MesBusiProcessDao mesBusiDao;

	@Override
	public MesBusiProcessReq selectBySysCode(String sysCode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertMesSys(MesBusiProcessReq busiReq) throws Exception {
		mesBusiDao.insert(busiReq);
		
	}

	@Override
	public void insertMesSysList(List<MesBusiProcessReq> busiMesList) throws Exception {
		if(busiMesList!=null&&busiMesList.size()>0){
			for(int i=0;i<busiMesList.size();i++){
				mesBusiDao.insert(busiMesList.get(i));
			}
		}
	}

	public MesBusiProcessDao getMesBusiDao() {
		return mesBusiDao;
	}

	public void setMesBusiDao(MesBusiProcessDao mesBusiDao) {
		this.mesBusiDao = mesBusiDao;
	}

}
