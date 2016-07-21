package com.wfj.exception.hadoop.hadoop.job;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import com.alibaba.fastjson.JSONObject;
import com.wfj.exception.dal.entity.MesSysProcessReq;
import com.wfj.exception.hadoop.hadoop.common.TableParamCommon;
import com.wfj.exception.hadoop.hadoop.mapper.MesCountMapper;
import com.wfj.exception.hadoop.hadoop.reduce.MesAllCodeReduct;
import com.wfj.exception.hadoop.hadoop.reduce.MesCountReduce;
import com.wfj.exception.hadoop.hadoop.util.ScanUtil;
import com.wfj.exception.spring.SpringHandlerBean;

/**
 * 统计所有的有效的异常数据
 * @ClassName: MesAllCountJob
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author ZJHao
 * @date 2015-8-28 下午4:26:33
 *
 */
public class MesAllCountJob {

	private static String tableName="mesReq";
	public void runJob(String sysCode) throws Exception{
		 Configuration HBASE_CONFIG = new Configuration();      
         HBASE_CONFIG.set("hbase.zookeeper.quorum", "h1,h2,h3");    
        
		long startTime=System.currentTimeMillis();
//		HTable htb=new HTable(conf, tableName);
		
		
		Job job=new Job(HBASE_CONFIG,"messageStatistics  job");
		job.setJarByClass(MesAllCountJob.class);
		
//		Scan scan=new ScanBase().findSysMess("01");
		Scan scan=new ScanUtil().findBeforeTenMinuteMesScan(sysCode);
		System.out.println(JSONObject.toJSONString(scan));
		TableMapReduceUtil.initTableMapperJob(tableName, scan, MesCountMapper.class, Text.class, Text.class, job);
		TableMapReduceUtil.initTableReducerJob(tableName,MesAllCodeReduct.class , job);
		System.out.println("takes1: "+(System.currentTimeMillis()-startTime));
		
		job.waitForCompletion(true);
		System.out.println("takes: "+(System.currentTimeMillis()-startTime));
		
//		MesSysProcessReq sysReq=new MessageDaoImpl().selectAll(sysCode);
//		System.out.println(JSONObject.toJSONString(sysReq));
	}
	public static void main(String[] args) throws Exception {
		SpringHandlerBean.init();
		final MesAllCountJob mesCountJob=(MesAllCountJob) SpringHandlerBean.getContext("mesAllCountJob");
		mesCountJob.runJob("01");
		mesCountJob.runJob("02");
		mesCountJob.runJob("03");
		mesCountJob.runJob("04");
		mesCountJob.runJob("05");
		mesCountJob.runJob("07");
		
		for(int i=0;i<100;i++){
//			mesCountJob.runJob("01");
			//mesCountJob.runJob("02");
			
			Thread.sleep(1000*60*5);
		}
		
	}
}
