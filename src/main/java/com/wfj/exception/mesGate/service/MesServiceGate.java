package com.wfj.exception.mesGate.service;

import java.util.List;

import com.wfj.exception.dal.entity.MesAllProcessReq;
import com.wfj.exception.dal.entity.MesSysProcessReq;

public interface MesServiceGate {

	void insertReduceMes(MesSysProcessReq sysReq) throws Exception;
	
	void insertMesAllProcessList(List<MesAllProcessReq> list) throws Exception;
}
