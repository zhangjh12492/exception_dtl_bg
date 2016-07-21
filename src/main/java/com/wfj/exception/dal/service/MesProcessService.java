package com.wfj.exception.dal.service;

import java.util.List;

public  interface MesProcessService <T>{

	T selectBySysCode(String sysCode) throws Exception;
	
	void insertMesSys(T sysReq) throws Exception;
	
	void insertMesSysList(List<T> sysReqs) throws Exception;
}
