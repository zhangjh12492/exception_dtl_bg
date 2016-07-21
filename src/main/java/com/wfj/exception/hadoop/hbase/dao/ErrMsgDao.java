package com.wfj.exception.hadoop.hbase.dao;

import java.util.List;

import com.wfj.exception.dal.cond.MesAllProcessReqCond;
import com.wfj.exception.dal.entity.MesBusiProcessReq;
import com.wfj.exception.dal.vo.MesErrProcessReqVo;
import com.wfj.exception.util.HbasePage;
import com.wfj.exception.vo.ClientExceptionReq;

public interface ErrMsgDao {

	
	Boolean insertMsg(ClientExceptionReq req,String tableName) throws Exception;
	
	ClientExceptionReq findExceptionReqById(String errId) throws Exception;
	
	Boolean updateMsgFlagById(String id,String flag,String tableName) throws Exception;
	
	Boolean updateMsgProcessStatusById(String id,String processStatus ) throws Exception;
	
	List<ClientExceptionReq> findMessList(HbasePage page) throws Exception;
	
	List<ClientExceptionReq> findMessByErrReq(HbasePage page) throws Exception;
	
	/**
	 * 修改系统下的所有异常信息的处理状态
	 * @Title: updateMesProcessStatusBySysCode
	 * @author ZJHao
	 * @param code	系统编码
	 * @param beforeProcessStatus	处理前的信息状体
	 * @param afterProcessStatus	处理后的信息状态
	 * @param errLevel	异常等级
	 * @return
	 * @throws Exception
	 * @return Boolean
	 * @throws
	 * @date 2015-8-20 上午10:48:28
	 */
	Boolean updateMesProcessStatusBySysCode(String sysCode,String beforeProcessStatus,String afterProcessStatus,String errLevel) throws Exception;
	/**
	 * 单击图表后展示的信息列表
	 * @Title: selectMesListFromChart
	 * @author ZJHao
	 * @return
	 * @throws Exception
	 * @return List<ClientExceptionReq>
	 * @throws
	 * @date 2015-8-29 下午5:47:45
	 */
	List<ClientExceptionReq> selectMesListFromChart(HbasePage page) throws Exception;
	/**
	 * 修改单条异常信息
	 * @Title: updateMesOneStatus
	 * @author ZJHao
	 * @param req
	 * @return
	 * @throws Exception
	 * @return boolean
	 * @throws
	 * @date 2015-8-30 下午5:53:30
	 */
	public boolean updateMesOneStatus(ClientExceptionReq req) throws Exception;
	
	/**
	 * 根据errIds修改多条异常信息
	 * @Title: updateMesOneStatus
	 * @author ZJHao
	 * @param req
	 * @return
	 * @throws Exception
	 * @return boolean
	 * @throws
	 * @date 2015-8-30 下午5:53:30
	 */
	public boolean updateMoreMesStatus(String errId[],String processStatus) throws Exception;
	
	 /**
	 * 查询详细的异常信息用于在系统监控页面展示
	 * @Title: selectMessageInfoForSysMonitorShow
	 * @author ZJHao
	 * @return
	 * @throws Exception
	 * @return List<ClientExceptionReq>
	 * @throws
	 * @date 2015-9-14 下午9:06:28
	 */
	public List<ClientExceptionReq> selectMessageInfoForSysMonitorShow(HbasePage page) throws Exception;
	/**
	 * 查询详细的异常信息用于在系统监控页面展示
	 * @Title: showMessListByUserNoProcess
	 * @author ZJHao
	 * @return
	 * @throws Exception
	 * @return List<ClientExceptionReq>
	 * @throws
	 * @date 2015-9-14 下午9:06:28
	 */
	public List<ClientExceptionReq> showMessListByUserNoProcess(HbasePage page) throws Exception;
}
