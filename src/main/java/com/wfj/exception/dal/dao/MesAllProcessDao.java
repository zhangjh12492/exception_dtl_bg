package com.wfj.exception.dal.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.wfj.exception.dal.entity.MesAllProcessReq;

@Repository
public interface MesAllProcessDao {

	/**
	 * 插入根据所有code统计的异常数据
	 * @Title: insert
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @author ZJHao
	 * @param allProce
	 * @throws Exception
	 * @return void
	 * @throws
	 * @date 2015-8-28 下午3:48:34
	 */
	public void insert(@Param("param") MesAllProcessReq allProce) throws Exception;
}
