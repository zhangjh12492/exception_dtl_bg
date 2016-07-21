package com.wfj.exception.mesGate.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.wfj.exception.common.SysConstants;
import com.wfj.exception.dal.entity.MesAllProcessReq;
import com.wfj.exception.dal.entity.MesBusiProcessReq;
import com.wfj.exception.dal.entity.MesErrProcessReq;
import com.wfj.exception.dal.entity.MesSysProcessReq;
import com.wfj.exception.dal.service.MesProcessService;
import com.wfj.exception.mesGate.service.MesServiceGate;
import com.wfj.exception.util.HttpClientUtil;

@Service("mesServiceGate")
public class MesServiceImplGate implements MesServiceGate{

	private static final Logger log=LoggerFactory.getLogger(MesServiceImplGate.class);
	@Resource(name="mesBusiProcessService")
	private MesProcessService<MesBusiProcessReq> mesBusiProcessService;
	@Resource(name="mesSysProcessService")
	private MesProcessService<MesSysProcessReq> mesSysProcessService;
	@Resource(name="mesErrProcessService")
	private MesProcessService<MesErrProcessReq> mesErrProcessService;
	@Resource(name="mesAllProcessService")
	private MesProcessService<MesAllProcessReq> mesAllProcessService;
	
	@Override
	public void insertReduceMes(MesSysProcessReq sysReq) throws Exception {
		log.info("\nStarting insert mesSys mesBusi,{}",JSONObject.toJSONString(sysReq));
		mesSysProcessService.insertMesSys(sysReq);
		for(int i=0;i<sysReq.getBusiList().size();i++){
			sysReq.getBusiList().get(i).setSysId(sysReq.getId());
		}
		List<MesBusiProcessReq> busiProcessReqList=sysReq.getBusiList();
		mesBusiProcessService.insertMesSysList(busiProcessReqList);
		for(int i=0;i<busiProcessReqList.size();i++){
			for(int j=0;j<busiProcessReqList.get(i).getMesErrprocessList().size();j++){
				busiProcessReqList.get(i).getMesErrprocessList().get(j).setSysId(sysReq.getId());
				busiProcessReqList.get(i).getMesErrprocessList().get(j).setBusiId(busiProcessReqList.get(i).getId());
			}
			mesErrProcessService.insertMesSysList(busiProcessReqList.get(i).getMesErrprocessList());
		}
		log.info("Ending insert data");
	}

	@Override
	public void insertMesAllProcessList(List<MesAllProcessReq> list) throws Exception {
		log.info("开始插入根据所有编码统计后的异常数量,list:{}",JSONObject.toJSONString(list));
		mesAllProcessService.insertMesSysList(list);
		log.info("插入完成。。。");
	}

}
