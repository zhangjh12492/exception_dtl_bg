package com.wfj.exception.dal.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.wfj.exception.dal.entity.MesSysProcessReq;
@Repository
public interface MesSysProcessDao {

	public void insert(@Param("param") MesSysProcessReq param);
	
	public MesSysProcessReq selectBySysCode(@Param("sysCode") String sysCode) throws Exception;
}
