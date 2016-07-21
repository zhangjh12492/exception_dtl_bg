package com.wfj.exception.dal.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.wfj.exception.dal.entity.MesEarlyWarnInfo;
@Repository
public interface MesEarlyWarnInfoDao {

	public List<MesEarlyWarnInfo> selectAllEarlyWarn(String id) throws Exception;
	
	public int insertEarlyWarn(@Param("param") MesEarlyWarnInfo param) throws Exception;
	
	public List<MesEarlyWarnInfo> selectAllSysStartEW(Map<String,String> map) throws Exception;
	
	public List<MesEarlyWarnInfo> selectAllBusiStartEW(Map<String,String> map) throws Exception;
	
}
