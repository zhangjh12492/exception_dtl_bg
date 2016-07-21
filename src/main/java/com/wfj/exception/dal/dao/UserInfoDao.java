package com.wfj.exception.dal.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.wfj.exception.vo.UserInfoVo;
@Repository
public interface UserInfoDao {

	public List<UserInfoVo> selectAllUserVo(@Param("sysCode") String sysCode) throws Exception;
	
	public String[] selectSendType(Map<String,String> param) throws Exception;
}
