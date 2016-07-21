package com.wfj.exception.dal.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.wfj.exception.dal.entity.MesErrProcessReq;
import com.wfj.exception.vo.UserInfoVo;
@Repository
public interface MesErrProcessDao {

	public int insertMesErrProcess(@Param("param") MesErrProcessReq mes) throws Exception;
	
	public List<MesErrProcessReq> selectByBusiId(@Param("busiId") String busiId) throws Exception;
	
}
