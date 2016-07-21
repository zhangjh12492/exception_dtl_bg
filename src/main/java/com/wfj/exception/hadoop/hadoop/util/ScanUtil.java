package com.wfj.exception.hadoop.hadoop.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;

import com.mysql.jdbc.log.Log;
import com.wfj.exception.common.ProcessStatusEnum;
import com.wfj.exception.common.MesEarlyWarnStatusEnum;
import com.wfj.exception.common.MessageFlagEnum;
import com.wfj.exception.hadoop.hadoop.common.TableParamCommon;
import com.wfj.exception.util.DateUtils;

public class ScanUtil extends TableParamCommon {
	/**
	 * find messages by sysCode
	 * @param sysCode
	 * @return
	 */
	public Scan findSysMessScan(String sysCode) {
		Scan scan = new Scan();
		scan.setStartRow(findSysMessStartRow(sysCode).getBytes());
		scan.setStopRow(findSysMessStopRow(sysCode).getBytes());
		//未处理的消息
		FilterList fList=new FilterList(Operator.MUST_PASS_ALL);
		fList.addFilter(new RowFilter(CompareOp.EQUAL,new RegexStringComparator("^\\d\\d"+sysCode+".*"))); //设置row的过滤器,必须含有当前时间精确到日
		fList.addFilter(new SingleColumnValueFilter("message".getBytes(), "process_status".getBytes(), CompareOp.EQUAL, ProcessStatusEnum.UNDISPOSED.getCode().getBytes()));
		scan.setFilter(fList);
		return scan;
	}
	/**
	 * 获取当前时间10分钟以前的数据
	 * @Title: findBeforeTenMinuteMes
	 * @author ZJHao
	 * @param sysCode
	 * @return
	 * @return Scan
	 * @throws
	 * @date 2015-8-28 下午6:59:36
	 */
	public Scan findBeforeTenMinuteMesScan(String sysCode) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -10);
		//获取当前时间十分钟之前的时间
		String rowStartDate=DateUtils.format(calendar.getTime(), DateUtils.YMDHMS).substring(0,8);	//rowKey中含有的时间
		String rowStopDate=DateUtils.format(new Date(), DateUtils.YMDHMS).substring(0,8);	
		String startTime=DateUtils.format(calendar.getTime(), DateUtils.YYDDMMHHMMSS);
		startTime=startTime.substring(0,startTime.lastIndexOf(":"))+":00";	//开始时间
		String endTime=DateUtils.format(new Date(), DateUtils.YYDDMMHHMMSS);	
		endTime=endTime.substring(0,endTime.lastIndexOf(":"))+":59";	//结束时间
		Scan scan = new Scan();
		String startRow=findBeforeTenMinuteMesStartRow(sysCode, rowStartDate);
		String stopRow=findBeforeTenMinuteMesStopRow(sysCode,rowStopDate);
		System.out.println("startRow:"+startRow);
		System.out.println("stopRow:"+stopRow);
		scan.setStartRow(startRow.getBytes());
		scan.setStopRow(stopRow.getBytes());
		FilterList fList=new FilterList(Operator.MUST_PASS_ALL);
//		fList.addFilter(new RowFilter(CompareOp.EQUAL,new BinaryPrefixComparator(startRow.substring(0,4).getBytes()))); //设置row的过滤器,必须含有当前时间精确到日
		fList.addFilter(new RowFilter(CompareOp.EQUAL,new RegexStringComparator("^\\d\\d"+sysCode+".*"))); //设置row的过滤器,必须含有当前时间精确到日
//		fList.addFilter(new RowFilter(CompareOp.EQUAL,new BinaryPrefixComparator(stopRow.substring(0,4).getBytes()))); //设置row的过滤器,必须含有当前时间精确到日
		fList.addFilter(new SingleColumnValueFilter("message".getBytes(), "create_date".getBytes(), CompareOp.GREATER_OR_EQUAL, startTime.getBytes()));
		fList.addFilter(new SingleColumnValueFilter("message".getBytes(), "create_date".getBytes(), CompareOp.LESS_OR_EQUAL, endTime.getBytes()));
		scan.setFilter(fList);
		return scan;
	}

	private String findBeforeTenMinuteMesStartRow(String sysCode,String currentDate) {
		
		String row = "";
		StringBuffer sb = new StringBuffer();
		sb.append("1");
		sb.append("0");
		sb.append(sysCode);
		sb.append("000000000");
		sb.append(currentDate+"000000000");
		row += sb.toString();
		return row;
	}

	private String findBeforeTenMinuteMesStopRow(String sysCode,String rowStopDate) {
		String row = "";
		StringBuffer sb = new StringBuffer();
		sb.append("2");
		sb.append("1");
		sb.append(sysCode);
		sb.append("999999999");
		sb.append(rowStopDate+"999999999");
		row += sb.toString();
		return row;
	}

	//errId、row 生成规则,errLevel+flag+sysCode+busiCode+errCode+sysErrCode+timeStamp	
	private String findSysMessStartRow(String sysCode) {
		String row = "";
		StringBuffer sb = new StringBuffer();
		sb.append("1");
		sb.append(MessageFlagEnum.VALID.getCode());
		sb.append(sysCode);
		sb.append("000000000");
		for (int i = 0; i < 17; i++) {
			sb.append("0");
		}
		row += sb.toString();
		return row;
	}

	private String findSysMessStopRow(String sysCode) {
		String row = "";
		StringBuffer sb = new StringBuffer();
		sb.append("2");
		sb.append(MessageFlagEnum.VALID.getCode());
		sb.append(sysCode);
		sb.append("999999999");
		for (int i = 0; i < 17; i++) {
			sb.append("9");
		}
		row += sb.toString();
		return row;
	}
	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -10);
		//获取当前时间十分钟之前的时间
		String currentDate=DateUtils.format(calendar.getTime(), DateUtils.YMDHMS).substring(0,8);
		String row = "";
		StringBuffer sb = new StringBuffer();
		sb.append("0");
		sb.append("0");
		sb.append("03");
		sb.append("000000000");
		sb.append(currentDate+"000000000");
		row += sb.toString();
		System.out.println(row);
		
		
		String startTime=DateUtils.format(calendar.getTime(), DateUtils.YYDDMMHHMMSS);
		startTime=startTime.substring(0,startTime.lastIndexOf(":"))+":00";
		String endTime=DateUtils.format(new Date(), DateUtils.YYDDMMHHMMSS);
		endTime=endTime.substring(0,endTime.lastIndexOf(":"))+":59";
		System.out.println(startTime);
		System.out.println(endTime);
		
	}
}
