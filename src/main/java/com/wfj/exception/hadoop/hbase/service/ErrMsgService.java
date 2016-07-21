package com.wfj.exception.hadoop.hbase.service;

import java.util.List;

import com.wfj.exception.dal.cond.MesAllProcessReqCond;
import com.wfj.exception.dal.entity.MesBusiProcessReq;
import com.wfj.exception.dal.vo.MesErrProcessReqVo;
import com.wfj.exception.util.HbasePage;
import com.wfj.exception.vo.ClientExceptionReq;

public interface ErrMsgService {

	Boolean insertMsg(ClientExceptionReq req, String tableName) throws Exception;

	ClientExceptionReq findExceptionReqById(String errId) throws Exception;

	Boolean updateMsgFlagById(String id, String flag, String tableName) throws Exception;
	
	List<ClientExceptionReq> findMessList(HbasePage page) throws Exception;
	/**
	 * 修改系统下的所有异常信息的处理状态
	 * @Title: updateMesProcessStatusBySysCode
	 * @author ZJHao
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
	
	List<ClientExceptionReq> findMessByErrReq(HbasePage page) throws Exception;
	
	/**
	 * 根据code、dateType、currentDate、codeType查询异常信息列表
	 * @Title: showMesListFromChartOneHour
	 * @author ZJHao
	 * @param mesAllReqCond
	 * @return
	 * @throws Exception
	 * @return List<ClientExceptionReq>
	 * @throws
	 * @date 2015-8-29 下午5:42:09
	 */
	List<ClientExceptionReq> showMesListFromChartOneHour(HbasePage page) throws Exception;
	
	/**
	 * 获取当前登陆的用户未处理的异常信息列表
	 * @Title: showMessListByUserNoProcess
	 * @author ZJHao
	 * @param mesAllReqCond
	 * @return
	 * @throws Exception
	 * @return List<ClientExceptionReq>
	 * @throws
	 * @date 2015-9-18 下午2:01:49
	 */
	List<ClientExceptionReq> showMessListByUserNoProcess(HbasePage page) throws Exception;
	
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
	List<ClientExceptionReq> selectMessageInfoForSysMonitorShow(HbasePage page) throws Exception;
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
	boolean updateMesOneStatus(ClientExceptionReq req) throws Exception;
	
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
	public boolean updateMoreMesStatus(ClientExceptionReq req) throws Exception;
}
