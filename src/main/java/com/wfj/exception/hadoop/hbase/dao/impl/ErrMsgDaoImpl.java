package com.wfj.exception.hadoop.hbase.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.joda.time.Hours;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.ResultsExtractor;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.wfj.exception.common.DateTypeEnum;
import com.wfj.exception.common.ErrLevelEnum;
import com.wfj.exception.common.MessageFlagEnum;
import com.wfj.exception.dal.cond.MesAllProcessReqCond;
import com.wfj.exception.dal.entity.MesBusiProcessReq;
import com.wfj.exception.dal.vo.MesErrProcessReqVo;
import com.wfj.exception.hadoop.hbase.dao.ErrMsgDao;
import com.wfj.exception.util.DateUtils;
import com.wfj.exception.util.HbasePage;
import com.wfj.exception.util.StringUtils;
import com.wfj.exception.vo.ClientExceptionReq;

@Service(value = "errMsgDao")
public class ErrMsgDaoImpl implements ErrMsgDao {

	private static final Logger log = LoggerFactory.getLogger(ErrMsgDaoImpl.class);
	private static final String tableName = "mesReq";
	private static final String family = "message";
	@Resource(name="hbaseTemplate")
	private HbaseTemplate hbaseTemplate;

	public Boolean insertMsg(final ClientExceptionReq req, final String tableNamesNone) throws Exception {
		return hbaseTemplate.execute(tableName, new TableCallback<Boolean>() {
			@Override
			public Boolean doInTable(HTableInterface htable) throws Throwable {
				System.out.println(req.toString());
				log.info("Starting to insert data to " + tableName);
				byte[] row2 = Bytes.toBytes(String.valueOf(req.getErrId()));
				Put p2 = new Put(row2);
				p2.add("message".getBytes(), "mes_value".getBytes(), JSONObject.toJSONString(req).getBytes()); //异常的整个信息
				p2.add("message".getBytes(), "process_status".getBytes(), req.getProcessStatus().getBytes()); //异常的处理状态
				p2.add("message".getBytes(), "flag".getBytes(), req.getFlag().getBytes()); //有效值
				p2.add("message".getBytes(), "create_date".getBytes(), req.getCreateDate().getBytes()); //有效值
				htable.put(p2);
				log.info("insert successed!");
				return true;
			}
		});
	}

	@Override
	public ClientExceptionReq findExceptionReqById(String errId) throws Exception {
		log.info("Starting use hbaseTemplate.find");
		return (ClientExceptionReq) hbaseTemplate.get(tableName, errId, "message", new RowMapper<ClientExceptionReq>() {

			@Override
			public ClientExceptionReq mapRow(Result result, int rowNum) throws Exception {
				ClientExceptionReq clientReq = null;
				KeyValue kv = result.getColumnLatest("message".getBytes(), "mes_value".getBytes());
				clientReq = JSONObject.parseObject(new String(kv.getValue()), ClientExceptionReq.class);
				System.out.println("rowNum   :   " + rowNum);
				return clientReq;
			}
		});
		//		return (ClientExceptionReq) hbaseTemplate.get(tableName,scan,new ResultsExtractor<ClientExceptionReq>(){
		//			@Override
		//			public ClientExceptionReq extractData(ResultScanner rs) throws Exception {
		//				ClientExceptionReq req=new ClientExceptionReq();
		//				for(Result result:rs){
		//					req=getQualifierValue(result);
		//				}
		//				return req;
		//			}
		//		});
	}

	@Override
	public Boolean updateMsgFlagById(final String errId, final String flag, final String tableNameNone) throws Exception {
		final ClientExceptionReq req = (ClientExceptionReq) hbaseTemplate.get(tableName, errId, "message", new RowMapper<ClientExceptionReq>() {

			@Override
			public ClientExceptionReq mapRow(Result result, int rowNum) throws Exception {
				ClientExceptionReq clientReq = null;
				KeyValue kv = result.getColumnLatest("message".getBytes(), "mes_value".getBytes());
				clientReq = JSONObject.parseObject(new String(kv.getValue()), ClientExceptionReq.class);
				clientReq.setFlag(flag);
				System.out.println("rowNum   :   " + rowNum);
				return clientReq;
			}
		});

		hbaseTemplate.execute("mesReq", new TableCallback<Boolean>() {
			@Override
			public Boolean doInTable(HTableInterface table) throws Throwable {
				log.info("Starting to insert data to " + tableName);
				byte[] row2 = Bytes.toBytes(String.valueOf(req.getErrId()));
				Put p2 = new Put(row2);
				p2.add("message".getBytes(), "mes_value".getBytes(), JSONObject.toJSONString(req).getBytes()); //异常的整个信息
				p2.add("message".getBytes(), "flag".getBytes(), req.getFlag().getBytes());
				table.put(p2);
				return true;
			}

		});
		return true;
	}

	@Override
	public List<ClientExceptionReq> findMessList(HbasePage page) throws Exception {
		MesBusiProcessReq busiReq=JSONObject.parseObject(page.getObject().toString(),MesBusiProcessReq.class); 
		log.info("\nfindMessList,param  :  {}", page.toString());
		Scan scan = new Scan();
		scan.setCaching(page.getPageSize());	//每页条数
		String startRow = getStartRow(busiReq);
		String stopRow = getEndRow(busiReq);
		log.info("startRow : {}, stopRow : {}", startRow, stopRow);
		if(StringUtils.isBlank(page.getStartRow()))
			scan.setStartRow(startRow.getBytes());
		else
			scan.setStartRow(page.getStartRow().getBytes());
		scan.setStopRow(stopRow.getBytes());
		scan.setFilter(new SingleColumnValueFilter("message".getBytes(), "flag".getBytes(), CompareOp.EQUAL, MessageFlagEnum.VALID.getCode().getBytes()));
		scan.setCaching(30);
		return (List<ClientExceptionReq>) hbaseTemplate.find(tableName, scan, new ResultsExtractor<List<ClientExceptionReq>>() {
			List<ClientExceptionReq> exceList = new ArrayList<ClientExceptionReq>();

			@Override
			public List<ClientExceptionReq> extractData(ResultScanner result) throws Exception {
				for (Result re : result) {
					KeyValue key = re.getColumnLatest("message".getBytes(), "mes_value".getBytes());
					String kval = new String(key.getValue());
					ClientExceptionReq clientReq = JSONObject.parseObject(kval, ClientExceptionReq.class);
					exceList.add(clientReq);
				}
				log.info("exceList.size  :  {}-----", exceList.size());
				return exceList;
			}
		});
	}

	/**
	 * 根据errId修改处理状态
	 * hbase修改数据是在原先的基础上新增了一条数据，旧的数据被覆盖了
	 * @Title: updateMsgProcessStatusById
	 * @author ZJHao
	 * @return
	 * @throws Exception
	 * @throws
	 * @date 2015-8-20 上午10:23:15
	 */
	@Override
	public Boolean updateMsgProcessStatusById(final String errId, String processStatus) throws Exception {
		final ClientExceptionReq req = (ClientExceptionReq) hbaseTemplate.get(tableName, errId, "message", new RowMapper<ClientExceptionReq>() {

			@Override
			public ClientExceptionReq mapRow(Result result, int rowNum) throws Exception {
				ClientExceptionReq clientReq = null;
				KeyValue kv = result.getColumnLatest("message".getBytes(), "mes_value".getBytes());
				clientReq = JSONObject.parseObject(kv.getValue().toString(), ClientExceptionReq.class);
				return clientReq;
			}
		});
		req.setProcessStatus(processStatus);

		hbaseTemplate.execute("mesReq", new TableCallback<Boolean>() {
			@Override
			public Boolean doInTable(HTableInterface table) throws Throwable {
				log.info("Starting to update updateMsgProcessStatusById for " + tableName);
				byte[] row2 = Bytes.toBytes(String.valueOf(req.getErrId()));
				Put p2 = new Put(row2);
				p2.add("message".getBytes(), "mes_value".getBytes(), JSONObject.toJSONString(req).getBytes()); //异常的整个信息
				p2.add("message".getBytes(), "process_status".getBytes(), req.getProcessStatus().getBytes());
				table.put(p2);
				return true;
			}
		});
		return true;
	}

	@Override
	public Boolean updateMesProcessStatusBySysCode(String sysCode, String beforeProcessStatus, final String afterProcessStatus, String errLevel) throws Exception {
		log.info("修改系统下的所有异常信息的状态,sysCode:{},beforeProcessStatus:{},afterProcessStatus:{}", sysCode, beforeProcessStatus, afterProcessStatus);
		Scan scan = new Scan();
		String startRow = errLevel + "0" + sysCode + "00000000000000000000000000";
		String stopRow = errLevel + "9" + sysCode + "99999999999999999999999999";
		log.info("startRow : {}, stopRow : {}", startRow, stopRow);
		scan.setStartRow(startRow.getBytes());
		scan.setStopRow(stopRow.getBytes());
		FilterList flist = new FilterList();
		flist.addFilter(new SingleColumnValueFilter("message".getBytes(), "flag".getBytes(), CompareOp.EQUAL, MessageFlagEnum.VALID.getCode().getBytes()));
		flist.addFilter(new SingleColumnValueFilter("message".getBytes(), "process_status".getBytes(), CompareOp.EQUAL, beforeProcessStatus.getBytes()));
		scan.setFilter(flist);
		scan.setCaching(30);
		final List<ClientExceptionReq> reqList = (List<ClientExceptionReq>) hbaseTemplate.find(tableName, scan, new ResultsExtractor<List<ClientExceptionReq>>() {
			List<ClientExceptionReq> exceList = new ArrayList<ClientExceptionReq>();

			@Override
			public List<ClientExceptionReq> extractData(ResultScanner result) throws Exception {
				for (Result re : result) {
					KeyValue key = re.getColumnLatest("message".getBytes(), "mes_value".getBytes());
					String kval = new String(key.getValue());
					ClientExceptionReq clientReq = JSONObject.parseObject(kval, ClientExceptionReq.class);
					clientReq.setProcessStatus(afterProcessStatus);
					exceList.add(clientReq);
				}
				return exceList;
			}
		});
		boolean flag = hbaseTemplate.execute("mesReq", new TableCallback<Boolean>() {
			@Override
			public Boolean doInTable(HTableInterface table) throws Throwable {
				log.info("Starting to updateMesProcessStatusBySysCode for " + tableName);
				for (ClientExceptionReq req : reqList) {
					byte[] row2 = Bytes.toBytes(String.valueOf(req.getErrId()));
					Put p2 = new Put(row2);
					p2.add("message".getBytes(), "mes_value".getBytes(), JSONObject.toJSONString(req).getBytes()); //异常的整个信息
					p2.add("message".getBytes(), "process_status".getBytes(), req.getProcessStatus().getBytes());
					table.put(p2);
				}
				return true;
			}
		});
		log.info("修改系统下的异常信息状态结束...");
		return flag;
	}

	/**
	 * 根据用户自定义的异常码查询下边的所有异常信息
	 * @Title: findMessByErrReq
	 * @author ZJHao
	 * @return
	 * @throws Exception
	 * @throws
	 * @date 2015-8-27 下午6:08:33
	 */
	@Override
	public List<ClientExceptionReq> findMessByErrReq(HbasePage page) throws Exception {
		MesErrProcessReqVo busiReq=JSONObject.parseObject(page.getObject().toString(),MesErrProcessReqVo.class);  
		log.info("\nfindMessList,param  :  {}", page.toString());
		Scan scan = new Scan();
		scan.setCaching(page.getPageSize());	//每页条数
		String startRow = getStartRowByErrCode(busiReq);
		String stopRow = getEndRowByErrCode(busiReq);
		log.info("startRow : {}, stopRow : {}", startRow, stopRow);
		if(StringUtils.isBlank(page.getStartRow()))
			scan.setStartRow(startRow.getBytes());
		else
			scan.setStartRow(page.getStartRow().getBytes());
		scan.setStopRow(stopRow.getBytes());
		scan.setFilter(new SingleColumnValueFilter("message".getBytes(), "flag".getBytes(), CompareOp.EQUAL, MessageFlagEnum.VALID.getCode().getBytes()));
		scan.setCaching(30);
		return (List<ClientExceptionReq>) hbaseTemplate.find(tableName, scan, new ResultsExtractor<List<ClientExceptionReq>>() {
			List<ClientExceptionReq> exceList = new ArrayList<ClientExceptionReq>();

			@Override
			public List<ClientExceptionReq> extractData(ResultScanner result) throws Exception {
				for (Result re : result) {
					KeyValue key = re.getColumnLatest("message".getBytes(), "mes_value".getBytes());
					String kval = new String(key.getValue());
					ClientExceptionReq clientReq = JSONObject.parseObject(kval, ClientExceptionReq.class);
					exceList.add(clientReq);
				}
				log.info("exceList.size  :  {}-----", exceList.size());
				return exceList;
			}
		});
	}

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
	@Override
	public List<ClientExceptionReq> selectMesListFromChart(HbasePage page) throws Exception {
		MesAllProcessReqCond mesAllReqCond=JSONObject.parseObject(page.getObject().toString(),MesAllProcessReqCond.class);  
		String startTime = "";
		String endTime = ""; //结束时间
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		if (StringUtils.isNotBlank(mesAllReqCond.getCurrentTime())) { //如果currentTime不为空，意思是获取折线图的数据,如果为空,则是获取饼图的数据,日期需要修改
			calendar.setTime(DateUtils.parse(mesAllReqCond.getCurrentTime(), DateUtils.YYDDMMHHMMSS));
			endTime = mesAllReqCond.getCurrentTime(); //结束时间
			if (mesAllReqCond.getDateType().equals(DateTypeEnum.HOUR.getCode())) { //如果是小时，获取日期的前五分钟
				calendar.add(Calendar.MINUTE, -5); //
				startTime = DateUtils.format(calendar.getTime(), DateUtils.YYDDMMHHMMSS);
			} else if (mesAllReqCond.getDateType().equals(DateTypeEnum.DAY.getCode())) { //如果是天，获取1小时前的数据
				calendar.add(Calendar.HOUR, -1); //
				startTime = DateUtils.format(calendar.getTime(), DateUtils.YYDDMMHHMMSS);
			} else if (mesAllReqCond.getDateType().equals(DateTypeEnum.WEEK.getCode())) { //如果是一周，获取一天前的数据
				calendar.add(Calendar.DATE, -1); //
				startTime = DateUtils.format(calendar.getTime(), DateUtils.YYDDMMHHMMSS);
			} else if (mesAllReqCond.getDateType().equals(DateTypeEnum.MONTH.getCode())) { //如果是一个月，获取一个月前的数据
				calendar.add(Calendar.MONTH, -1); //
				startTime = DateUtils.format(calendar.getTime(), DateUtils.YYDDMMHHMMSS);
			}
		} else {
			calendar.setTime(date); //currentTime为空,所有事件均是从当前时间开始算起
			endTime = DateUtils.format(date, DateUtils.YYDDMMHHMMSS); //结束时间
			if (mesAllReqCond.getDateType().equals(DateTypeEnum.HOUR.getCode())) { //小时
				calendar.add(Calendar.HOUR, -1); //
				startTime = DateUtils.format(calendar.getTime(), DateUtils.YYDDMMHHMMSS);
			} else if (mesAllReqCond.getDateType().equals(DateTypeEnum.DAY.getCode())) { //天
				calendar.add(Calendar.DATE, -1); //
				startTime = DateUtils.format(calendar.getTime(), DateUtils.YYDDMMHHMMSS);
			} else if (mesAllReqCond.getDateType().equals(DateTypeEnum.WEEK.getCode())) { //一周
				calendar.add(Calendar.WEDNESDAY, -1); //
				startTime = DateUtils.format(calendar.getTime(), DateUtils.YYDDMMHHMMSS);
			} else if (mesAllReqCond.getDateType().equals(DateTypeEnum.MONTH.getCode())) { //一个月
				calendar.add(Calendar.MONTH, -1); //
				startTime = DateUtils.format(calendar.getTime(), DateUtils.YYDDMMHHMMSS);
			} else if (mesAllReqCond.getDateType().equals(DateTypeEnum.ALL.getCode())) { //all 表示不限时,开始时间设置为1年前
				calendar.add(Calendar.YEAR, -1); //
				startTime = DateUtils.format(calendar.getTime(), DateUtils.YYDDMMHHMMSS);
			}
		}

		Scan scan = new Scan();
		scan.setCaching(page.getPageSize());	//每页条数
		if(StringUtils.isBlank(page.getStartRow()))
			scan.setStartRow(selectMesListFromChartStartRow(mesAllReqCond).getBytes());
		else
			scan.setStartRow(page.getStartRow().getBytes());
		scan.setStopRow(selectMesListFromChartStopRow(mesAllReqCond).getBytes());
		FilterList flist = new FilterList(Operator.MUST_PASS_ALL);
		flist.addFilter(new RowFilter(CompareOp.EQUAL, new RegexStringComparator("^\\d" + MessageFlagEnum.VALID.getCode() + mesAllReqCond.getCode() + ".*")));
		flist.addFilter(new SingleColumnValueFilter("message".getBytes(), "create_date".getBytes(), CompareOp.GREATER_OR_EQUAL, startTime.getBytes()));
		flist.addFilter(new SingleColumnValueFilter("message".getBytes(), "create_date".getBytes(), CompareOp.LESS_OR_EQUAL, endTime.getBytes()));
		if (StringUtils.isNotBlank(mesAllReqCond.getProcessStatus())) {
			flist.addFilter(new SingleColumnValueFilter("message".getBytes(), "process_status".getBytes(), CompareOp.EQUAL, mesAllReqCond.getProcessStatus().getBytes()));
		}
		scan.setFilter(flist);
		return (List<ClientExceptionReq>) hbaseTemplate.find(tableName, scan, new ResultsExtractor<List<ClientExceptionReq>>() {
			List<ClientExceptionReq> exceList = new ArrayList<ClientExceptionReq>();

			@Override
			public List<ClientExceptionReq> extractData(ResultScanner result) throws Exception {
				for (Result re : result) {
					KeyValue key = re.getColumnLatest("message".getBytes(), "mes_value".getBytes());
					String kval = new String(key.getValue());
					ClientExceptionReq clientReq = JSONObject.parseObject(kval, ClientExceptionReq.class);
					exceList.add(clientReq);
				}
				log.info("exceList.size  :  {}-----", exceList.size());
				return exceList;
			}
		});
	}

	/**
	 * 修改单条异常信息
	 * @Title: updateMesOneStatus
	 * @author ZJHao
	 * @return
	 * @throws Exception
	 * @return boolean
	 * @throws
	 * @date 2015-8-30 下午5:53:30
	 */
	public boolean updateMesOneStatus(ClientExceptionReq reqExce) throws Exception {
		boolean flag = false;
		final ClientExceptionReq req = (ClientExceptionReq) hbaseTemplate.get(tableName, reqExce.getErrId(), "message", new RowMapper<ClientExceptionReq>() {

			@Override
			public ClientExceptionReq mapRow(Result result, int rowNum) throws Exception {
				ClientExceptionReq clientReq = null;
				KeyValue kv = result.getColumnLatest("message".getBytes(), "mes_value".getBytes());
				clientReq = JSONObject.parseObject(new String(kv.getValue()), ClientExceptionReq.class);
				System.out.println("rowNum   :   " + rowNum);
				return clientReq;
			}
		});
		req.setProcessStatus(reqExce.getProcessStatus());

		flag = hbaseTemplate.execute("mesReq", new TableCallback<Boolean>() {
			@Override
			public Boolean doInTable(HTableInterface table) throws Throwable {
				log.info("Starting to update updateMsgProcessStatusById for " + tableName);
				byte[] row2 = Bytes.toBytes(req.getErrId());
				Put p2 = new Put(row2);
				p2.add("message".getBytes(), "mes_value".getBytes(), JSONObject.toJSONString(req).getBytes()); //异常的整个信息
				p2.add("message".getBytes(), "process_status".getBytes(), req.getProcessStatus().getBytes());
				table.put(p2);
				return true;
			}
		});
		return flag;
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
	public List<ClientExceptionReq> selectMessageInfoForSysMonitorShow(HbasePage page) throws Exception {
		MesAllProcessReqCond mesAllReqCond=JSONObject.parseObject(page.getObject().toString(),MesAllProcessReqCond.class); 
		String startTime = "";
		String endTime = ""; //结束时间
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if (mesAllReqCond.getDateType().equals(DateTypeEnum.TODAY.getCode())) { //获取今天的数据
			startTime = DateUtils.format(date, DateUtils.YMD_DASH)+" 00:00:00";
			endTime=DateUtils.format(date, DateUtils.YYDDMMHHMMSS);
		} else if (mesAllReqCond.getDateType().equals(DateTypeEnum.YESTERDAY.getCode())) { //如获取昨天的数据
			calendar.add(Calendar.DATE, -1); //
			startTime = DateUtils.format(calendar.getTime(), DateUtils.YMD_DASH)+" 00:00:00";
			endTime=DateUtils.format(calendar.getTime(), DateUtils.YMD_DASH)+" 23:59:59";
		} else if (mesAllReqCond.getDateType().equals(DateTypeEnum.MOREEARLY.getCode())) { //获取更早的数据
			calendar.add(Calendar.MONTH, -1); //
			startTime = DateUtils.format(calendar.getTime(), DateUtils.YMD_DASH)+" 00:00:00";
			calendar.add(calendar.MONTH, 1);
			calendar.add(calendar.DATE, -2);
			endTime=DateUtils.format(calendar.getTime(), DateUtils.YDM_DASH)+" 23:59:59";
		}
		
		Scan scan = new Scan();
		scan.setCaching(page.getPageSize());	//每页条数
		if(StringUtils.isBlank(page.getStartRow()))
			scan.setStartRow(selectMessageInfoForSysMonitorShowStartRow(mesAllReqCond).getBytes());
		else
			scan.setStartRow(page.getStartRow().getBytes());
		scan.setStopRow(selectMessageInfoForSysMonitorShowStopRow(mesAllReqCond).getBytes());
		FilterList flist = new FilterList(Operator.MUST_PASS_ALL);
		flist.addFilter(new RowFilter(CompareOp.EQUAL, new RegexStringComparator("^\\d" + MessageFlagEnum.VALID.getCode() + mesAllReqCond.getCode() + ".*")));
		flist.addFilter(new SingleColumnValueFilter("message".getBytes(), "create_date".getBytes(), CompareOp.GREATER_OR_EQUAL, startTime.getBytes()));
		flist.addFilter(new SingleColumnValueFilter("message".getBytes(), "create_date".getBytes(), CompareOp.LESS_OR_EQUAL, endTime.getBytes()));
		if (StringUtils.isNotBlank(mesAllReqCond.getProcessStatus())) {
			flist.addFilter(new SingleColumnValueFilter("message".getBytes(), "process_status".getBytes(), CompareOp.EQUAL, mesAllReqCond.getProcessStatus().getBytes()));
		}
		scan.setFilter(flist);
		return (List<ClientExceptionReq>) hbaseTemplate.find(tableName, scan, new ResultsExtractor<List<ClientExceptionReq>>() {
			List<ClientExceptionReq> exceList = new ArrayList<ClientExceptionReq>();

			@Override
			public List<ClientExceptionReq> extractData(ResultScanner result) throws Exception {
				for (Result re : result) {
					KeyValue key = re.getColumnLatest("message".getBytes(), "mes_value".getBytes());
					String kval = new String(key.getValue());
					ClientExceptionReq clientReq = JSONObject.parseObject(kval, ClientExceptionReq.class);
					exceList.add(clientReq);
				}
				log.info("exceList.size  :  {}-----", exceList.size());
				return exceList;
			}
		});
	}

	/**
	 * 根据errIds修改多条异常信息
	 * @Title: updateMesOneStatus
	 * @author ZJHao
	 * @return
	 * @throws Exception
	 * @return boolean
	 * @throws
	 * @date 2015-8-30 下午5:53:30
	 */
	@Override
	public boolean updateMoreMesStatus(String[] errIds, String processStatus) throws Exception {
		boolean flag = false;
		for (int i = 0; i < errIds.length; i++) {
			if (StringUtils.isNotBlank(errIds[i])) {
				final ClientExceptionReq req = (ClientExceptionReq) hbaseTemplate.get(tableName, errIds[i], "message", new RowMapper<ClientExceptionReq>() {

					@Override
					public ClientExceptionReq mapRow(Result result, int rowNum) throws Exception {
						ClientExceptionReq clientReq = null;
						KeyValue kv = result.getColumnLatest("message".getBytes(), "mes_value".getBytes());
						clientReq = JSONObject.parseObject(new String(kv.getValue()), ClientExceptionReq.class);
						return clientReq;
					}
				});
				req.setProcessStatus(processStatus);

				flag = hbaseTemplate.execute("mesReq", new TableCallback<Boolean>() {
					@Override
					public Boolean doInTable(HTableInterface table) throws Throwable {
						log.info("Starting to update updateMsgProcessStatusById for " + tableName);
						byte[] row2 = Bytes.toBytes(req.getErrId());
						Put p2 = new Put(row2);
						p2.add("message".getBytes(), "mes_value".getBytes(), JSONObject.toJSONString(req).getBytes()); //异常的整个信息
						p2.add("message".getBytes(), "process_status".getBytes(), req.getProcessStatus().getBytes());
						table.put(p2);
						return true;
					}
				});
			}
		}

		return flag;
	}
	
	/**
	 * 查询当前用户未处理的信息
	 * @Title: showMessListByUserNoProcess
	 * @author ZJHao
	 * @return
	 * @throws Exception
	 * @return List<ClientExceptionReq>
	 * @throws
	 * @date 2015-9-14 下午9:06:28
	 */
	public List<ClientExceptionReq> showMessListByUserNoProcess(final HbasePage page) throws Exception{
		MesAllProcessReqCond mesAllReqCond=JSONObject.parseObject(page.getObject().toString(),MesAllProcessReqCond.class);
		String codes[] =mesAllReqCond.getCode().split(",");
		String patternCode="^\\d"+MessageFlagEnum.VALID.getCode()+"(";
		if(codes!=null&&StringUtils.isNotBlank(codes[0])){
			for(int i=0;i<codes.length;i++){
				patternCode+=codes[i]+"|";
			}
			patternCode=patternCode.substring(0,patternCode.length()-1)+").*";
		}
		Scan scan = new Scan();
//		scan.setCaching(300);	//每页条数
//		if(StringUtils.isBlank(page.getStartRow()))
			scan.setStartRow(showMessListByUserNoProcessStartRow(mesAllReqCond).getBytes());
//		else
//			scan.setStartRow(page.getStartRow().getBytes());
		scan.setStopRow(showMessListByUserNoProcessStopRow(mesAllReqCond).getBytes());
		FilterList flist = new FilterList(Operator.MUST_PASS_ALL);
		flist.addFilter(new RowFilter(CompareOp.EQUAL, new RegexStringComparator(patternCode)));
		if (StringUtils.isNotBlank(mesAllReqCond.getProcessStatus())) {
			flist.addFilter(new SingleColumnValueFilter("message".getBytes(), "process_status".getBytes(), CompareOp.EQUAL, mesAllReqCond.getProcessStatus().getBytes()));
		}
		scan.setFilter(flist);
		return (List<ClientExceptionReq>) hbaseTemplate.find(tableName, scan, new ResultsExtractor<List<ClientExceptionReq>>() {
			List<ClientExceptionReq> exceList = new ArrayList<ClientExceptionReq>();

			@Override
			public List<ClientExceptionReq> extractData(ResultScanner result) throws Exception {
//				int i=0;
				for (Result re : result) {
					KeyValue key = re.getColumnLatest("message".getBytes(), "mes_value".getBytes());
					String kval = new String(key.getValue());
					ClientExceptionReq clientReq = JSONObject.parseObject(kval, ClientExceptionReq.class);
					exceList.add(clientReq); 
//					i++;
//					if(i>=page.getPageSize()){
//						break;
//					}
				}
				log.info("exceList.size  :  {}-----", exceList.size());
				return exceList;
			}
		});
	}

	/*-*****************************************************************************************************-*/
	/*private method in this class*/

	//errId、row 生成规则,errLevel+flag+sysCode+busiCode+errCode+sysErrCode+timeStamp	
	private String getStartRowByErrCode(MesErrProcessReqVo errReq) {
		String row = "";
		StringBuffer sb = new StringBuffer();
		sb.append(errReq.getErrLevel());
		sb.append("0"); //flag的判断不在row中,因为flag的值是变化的,这么设计将会改变row的值
		sb.append(errReq.getErrCode());
		for (int i = 0; i < 17; i++) {
			sb.append("0");
		}
		row += sb.toString();
		return row;
	}

	private String getEndRowByErrCode(MesErrProcessReqVo errReq) {
		String row = "";
		StringBuffer sb = new StringBuffer();
		sb.append(errReq.getErrLevel());
		sb.append("9");
		sb.append(errReq.getErrCode());
		sb.append("999999");
		for (int i = 0; i < 17; i++) {
			sb.append("9");
		}
		row += sb.toString();
		return row;
	}

	private String getStartRow(MesBusiProcessReq busiReq) {
		String row = "";
		StringBuffer sb = new StringBuffer();
		sb.append(busiReq.getErrLevel());
		sb.append("0"); //flag的判断不在row中,因为flag的值是变化的,这么设计将会改变row的值
		sb.append(busiReq.getSysCode());
		sb.append(busiReq.getBusiCode());
		sb.append("000000");
		for (int i = 0; i < 17; i++) {
			sb.append("0");
		}
		row += sb.toString();
		return row;
	}

	private String getEndRow(MesBusiProcessReq busiReq) {
		String row = "";
		StringBuffer sb = new StringBuffer();
		sb.append(busiReq.getErrLevel());
		sb.append("9");
		sb.append(busiReq.getSysCode());
		sb.append(busiReq.getBusiCode());
		sb.append("999999");
		for (int i = 0; i < 17; i++) {
			sb.append("9");
		}
		row += sb.toString();
		return row;
	}

	/*开始行*/
	private String selectMesListFromChartStartRow(MesAllProcessReqCond mesAllReqCond) {
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotBlank(mesAllReqCond.getErrLevel())) {
			sb.append(mesAllReqCond.getErrLevel());
		} else {
			sb.append("1");
		}
		sb.append(MessageFlagEnum.VALID.getCode());
		sb.append(mesAllReqCond.getCode());
		if (mesAllReqCond.getCodeType().equals("sys")) {
			sb.append("000000000");
		}
		if (mesAllReqCond.getCodeType().endsWith("busi")) {
			sb.append("000000");
		}
		if (mesAllReqCond.getCodeType().endsWith("err")) {
			sb.append("000");
		}
		for (int i = 0; i < 17; i++) {
			sb.append("0");
		}
		return sb.toString();
	}

	/*结束行*/
	private String selectMesListFromChartStopRow(MesAllProcessReqCond mesAllReqCond) {
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotBlank(mesAllReqCond.getErrLevel())) {
			sb.append(mesAllReqCond.getErrLevel());
		} else {
			sb.append("2");
		}
		sb.append(MessageFlagEnum.VALID.getCode());
		sb.append(mesAllReqCond.getCode());
		if (mesAllReqCond.getCodeType().equals("sys")) {
			sb.append("999999999");
		}
		if (mesAllReqCond.getCodeType().endsWith("busi")) {
			sb.append("999999");
		}
		if (mesAllReqCond.getCodeType().endsWith("err")) {
			sb.append("999");
		}
		for (int i = 0; i < 17; i++) {
			sb.append("9");
		}
		return sb.toString();
	}
	
	private String selectMessageInfoForSysMonitorShowStartRow(MesAllProcessReqCond mesAllReqCond){
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotBlank(mesAllReqCond.getErrLevel())) {
			sb.append(mesAllReqCond.getErrLevel());
		} else {
			sb.append("1");
		}
		sb.append(MessageFlagEnum.VALID.getCode());
		sb.append(mesAllReqCond.getCode());
		sb.append("000000000");
		for (int i = 0; i < 17; i++) {
			sb.append("0");
		}
		return sb.toString();
	}
	
	private String selectMessageInfoForSysMonitorShowStopRow(MesAllProcessReqCond mesAllReqCond){
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotBlank(mesAllReqCond.getErrLevel())) {
			sb.append(mesAllReqCond.getErrLevel());
		} else {
			sb.append("2");
		}
		sb.append(MessageFlagEnum.VALID.getCode());
		sb.append(mesAllReqCond.getCode());
		sb.append("999999999");
		for (int i = 0; i < 17; i++) {
			sb.append("9");
		}
		return sb.toString();
	}
	
	private String showMessListByUserNoProcessStartRow(MesAllProcessReqCond mesAllReqCond){
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotBlank(mesAllReqCond.getErrLevel())) {
			sb.append(mesAllReqCond.getErrLevel());
		} else {
			sb.append("1");
		}
		sb.append(MessageFlagEnum.VALID.getCode());
		sb.append("00000000000");
		for (int i = 0; i < 17; i++) {
			sb.append("0");
		}
		return sb.toString();
	}
	private String showMessListByUserNoProcessStopRow(MesAllProcessReqCond mesAllReqCond){
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotBlank(mesAllReqCond.getErrLevel())) {
			sb.append(mesAllReqCond.getErrLevel());
		} else {
			sb.append("2");
		}
		sb.append(MessageFlagEnum.VALID.getCode());
		sb.append("99999999999");
		for (int i = 0; i < 17; i++) {
			sb.append("9");
		}
		return sb.toString();
	}

	public ClientExceptionReq getQualifierValue(Result result) {
		ClientExceptionReq req = new ClientExceptionReq();
		for (KeyValue kv : result.raw()) {
			String qualifier = new String(kv.getQualifier());
			String value = new String(kv.getValue());
			Field[] fieldNames = ClientExceptionReq.class.getDeclaredFields();
			for (int i = 0; i < fieldNames.length; i++) {
				String name = fieldNames[i].getName();
				if (name.equals(qualifier)) {
					name = name.substring(0, 1).toUpperCase() + name.substring(1);
					String type = fieldNames[i].getGenericType().toString();
					if (type.equals("class java.lang.String")) {
						try {
							Method m = req.getClass().getMethod("set" + name, String.class);
							m.invoke(req, value);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (type.equals("long")) {
						try {
							Method m = req.getClass().getMethod("set" + name, long.class);
							m.invoke(req, Long.parseLong(value));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return req;
	}

	
	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtils.parse("2015-9-14 11:14:00", DateUtils.YYDDMMHHMMSS));
		calendar.add(Calendar.WEDNESDAY, -1);
		calendar.add(calendar.WEDNESDAY, 1);
		calendar.add(calendar.DATE, 1);
		Date dt = calendar.getTime();
		System.out.println(DateUtils.format(dt, DateUtils.YYDDMMHHMMSS));
	}

}
