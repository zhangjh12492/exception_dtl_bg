package com.wfj.exception.hadoop.hbase.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wfj.exception.hadoop.hbase.dao.ErrMsgDao;
import com.wfj.exception.hadoop.hbase.service.ErrMsgService;
import com.wfj.exception.util.HbasePage;
import com.wfj.exception.vo.ClientExceptionReq;

@Service(value="errMsgService")
public class ErrMsgServiceImpl implements ErrMsgService{

	private static final Logger log=LoggerFactory.getLogger(ErrMsgServiceImpl.class);
	@Resource(name="errMsgDao")
	private ErrMsgDao errMsgDao;
	@Override
	public Boolean insertMsg(ClientExceptionReq req, String tableName) throws Exception {
		log.info("Starting invoking errmsgService...  ");
		boolean flag=errMsgDao.insertMsg(req, "mesReq");
		log.info("End of the method is called...");
		return flag;
	}
	@Override
	public ClientExceptionReq findExceptionReqById(String errId) throws Exception {
		log.info("\nStarting findExceptionReqById...  ");
		ClientExceptionReq req=errMsgDao.findExceptionReqById(errId);
		log.info("\nEnding findExceptionReqById,value : {} ",req.toString());
		return req;
	}
	@Override
	public Boolean updateMsgFlagById(String id, String flag, String tableName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<ClientExceptionReq> findMessList(HbasePage page) throws Exception {
		log.info("findMessListt ,param:{}",page.toString());
		return errMsgDao.findMessList(page);
	}
	@Override
	public Boolean updateMesProcessStatusBySysCode(String sysCode, String beforeProcessStatus, String afterProcessStatus, String errLevel) throws Exception {
		boolean flag=errMsgDao.updateMesProcessStatusBySysCode(sysCode, beforeProcessStatus, afterProcessStatus, errLevel);
		return flag;
	}
	
	@Override
	public List<ClientExceptionReq> findMessByErrReq(HbasePage page) throws Exception {
		log.info("findMessListt ,param:{}",page.toString());
		return errMsgDao.findMessByErrReq(page);
	}
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
	public List<ClientExceptionReq> showMesListFromChartOneHour(HbasePage page) throws Exception {
		return errMsgDao.selectMesListFromChart(page);
	}
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
	@Override
	public boolean updateMesOneStatus(ClientExceptionReq req) throws Exception {
		
		return errMsgDao.updateMesOneStatus(req);
	}
	
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
	public List<ClientExceptionReq> selectMessageInfoForSysMonitorShow(HbasePage page) throws Exception{
		return errMsgDao.selectMessageInfoForSysMonitorShow(page);
	}
	
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
	public boolean updateMoreMesStatus(ClientExceptionReq req) throws Exception{
		String [] errIds=req.getErrId().split(",");
		return errMsgDao.updateMoreMesStatus(errIds, req.getProcessStatus());
	}
	/**
	 * 获取当前登陆的用户未处理的异常信息的列表
	 * @Title: showMessListByUserNoProcess
	 * @author ZJHao
	 * @param mesAllReqCond
	 * @return
	 * @throws Exception
	 * @throws
	 * @date 2015-9-18 下午2:02:41
	 */
	@Override
	public List<ClientExceptionReq> showMessListByUserNoProcess(HbasePage page) throws Exception {
		List<ClientExceptionReq> list=errMsgDao.showMessListByUserNoProcess(page);
		return list;
	}
	public ErrMsgDao getErrMsgDao() {
		return errMsgDao;
	}
	public void setErrMsgDao(ErrMsgDao errMsgDao) {
		this.errMsgDao = errMsgDao;
	}

}
