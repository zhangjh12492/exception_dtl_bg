package com.wfj.exception.dal.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.wfj.exception.dal.entity.MesBusiProcessReq;
@Repository
public interface MesBusiProcessDao {

	public void insert(@Param("param") MesBusiProcessReq param) throws Exception;
}
