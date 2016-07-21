package com.wfj.exception.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import com.wfj.exception.util.MessageDto;
import com.wfj.exception.util.MqResboundDto;
import org.apache.hadoop.yarn.webapp.WebApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.wfj.exception.common.RequestResult;
import com.wfj.exception.common.SysConstants;
import com.wfj.exception.dal.cond.MesAllProcessReqCond;
import com.wfj.exception.dal.vo.MesErrProcessReqVo;
import com.wfj.exception.dto.CallbackDto;
import com.wfj.exception.hadoop.hbase.service.ErrMsgService;
import com.wfj.exception.util.HbasePage;
import com.wfj.exception.util.HttpUtil;
import com.wfj.exception.vo.ClientExceptionReq;

@Controller
@RequestMapping("/dataProcess")
public class HbaseDataProcessController {

	private static final Logger log=LoggerFactory.getLogger(HbaseDataProcessController.class);
	
	
	@Resource(name="errMsgService")
	private ErrMsgService errMsgService;


	@RequestMapping(value = "/josnInboundFromMq", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public MqResboundDto getMessageFromMqIntoDt(HttpServletRequest request){
		MqResboundDto mqResboundDto=new MqResboundDto();
		String message = null;
        BufferedReader br = null;
        try {
            request.setCharacterEncoding("UTF-8");
            br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            message = sb.toString();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
//            在这里处理接入的数据
            log.info("payment type receive data:{}", message);
            ClientExceptionReq req= JSONObject.parseObject(message, ClientExceptionReq.class);
            mqResboundDto.setRespStatus("1");
			errMsgService.insertMsg(req,null);

        } catch (Exception e) {
            log.error("=== 调用接入服务出错" + e.getMessage() + " ===");
            mqResboundDto.setRespStatus("0");
        }

		return mqResboundDto;

	}

	/**
	 * 查询异常信息集合
	 * @Title: findMessList
	 * @author ZJHao
	 * @param request
	 * @return
	 * @throws Exception
	 * @return String
	 * @throws
	 * @date 2015-9-27 下午6:38:02
	 */
    @RequestMapping(value = "/findMessList")
    @ResponseBody
	public RequestResult findMessList(HttpServletRequest request) throws Exception{
    	Map<String, String> params=HttpUtil.getParameterMap(request.getParameterMap());
    	RequestResult<String> result=new RequestResult<String>();
    	HbasePage page=JSONObject.parseObject(params.get("data"),HbasePage.class);
		List<ClientExceptionReq> listReq=errMsgService.findMessList(page);
		result.setSuccess(true);
		result.setContent(JSONObject.toJSONString(listReq));
		return result;
	}
    
    /**
	 *  获取登陆后的用户未处理的异常信息
	 */
    @RequestMapping(value = "/showMessListByUserNoProcess")
    @ResponseBody
	public RequestResult showMessListByUserNoProcess(HttpServletRequest request) throws Exception{
    	Map<String, String> params=HttpUtil.getParameterMap(request.getParameterMap());
    	HbasePage page=JSONObject.parseObject(params.get("data"),HbasePage.class);
		RequestResult<Object> result=new RequestResult<Object>();
		log.info("-----showMessListByUserNoProcess-------,param:{}", page.toString());
		List<ClientExceptionReq> listReq=errMsgService.showMessListByUserNoProcess(page);
		result.setSuccess(true);
		result.setContent(JSONObject.toJSONString(listReq));

		String str=JSONObject.toJSONString(result);
		log.info("查询showMessListByUserNoProcess获取异常列表结束,result:{}",str);
		return result;
	}
    
    /**
     * 根据异常编号查询异常信息
     * @Title: findMessByErrId
     * @author ZJHao
     * @return
     * @return String
     * @throws
     * @date 2015-9-27 下午6:38:46
     */
    @RequestMapping(value = "/findMessByErrId")
    @ResponseBody
    public RequestResult findMessByErrId(@RequestParam String errId) throws Exception{
    	RequestResult<String> result=new RequestResult<String>();
    	ClientExceptionReq clientReq=errMsgService.findExceptionReqById(errId);
		result.setSuccess(true);
		result.setContent(JSONObject.toJSONString(clientReq));
		return result;
    }
    
    /**
     * 根据信息修改异常状态
     * @Title: updateMesProcessStatus
     * @author ZJHao
     * @return
     * @throws Exception
     * @return String
     * @throws
     * @date 2015-9-27 下午6:41:08
     */
    @RequestMapping(value = "/updateMesProcessStatus")
    @ResponseBody
    public RequestResult updateMesProcessStatus(@RequestParam String sysCode,@RequestParam String beforeProcessStatus,@RequestParam String afterProcessStatus,@RequestParam String errLevel) throws Exception{
    	RequestResult<String> result=new RequestResult<String>();
    	boolean flag=errMsgService.updateMesProcessStatusBySysCode(sysCode,beforeProcessStatus,afterProcessStatus,errLevel);
    	result.setSuccess(flag);
		return result;
    }
    /**
     * 根据处理结果的对象查询异常信息
     * @Title: findMessesByErrReq
     * @author ZJHao
     * @param request
     * @return
     * @throws Exception
     * @return String
     * @throws
     * @date 2015-9-27 下午6:48:25
     */
    @RequestMapping(value = "/findMessesByErrIds")
    @ResponseBody
    public RequestResult findMessesByErrIds(HttpServletRequest request) throws Exception{
    	RequestResult<String> result=new RequestResult<String>();
    	Map<String,String> params=HttpUtil.getParameterMap(request.getParameterMap());
    	HbasePage page=JSONObject.parseObject(params.get("data"),HbasePage.class);
		List<ClientExceptionReq> listReq=errMsgService.findMessByErrReq(page);
		result.setSuccess(true);
		result.setContent(JSONObject.toJSONString(listReq));
		return result;
    }
    
    /**
     * 查询异常信息用于在chart图单击后展示
     * @Title: showMessListFromChart
     * @author ZJHao
     * @param request
     * @return
     * @throws Exception
     * @return String
     * @throws
     * @date 2015-9-27 下午6:48:25
     */
    @RequestMapping(value = "/showMessListFromChart")
    @ResponseBody
    public RequestResult showMessListFromChart(HttpServletRequest request) throws Exception{
    	RequestResult<String> result=new RequestResult<String>();
    	Map<String,String> params=HttpUtil.getParameterMap(request.getParameterMap());
    	HbasePage page=JSONObject.parseObject(params.get("data"),HbasePage.class);
    	List<ClientExceptionReq> listReq=errMsgService.showMesListFromChartOneHour(page);
    	result.setSuccess(true);
    	result.setContent(JSONObject.toJSONString(listReq));
    	return result;
    }
    
    /**
     * 修改单条异常信息
     * @Title: updateMesOneStatus
     * @author ZJHao
     * @param request
     * @return
     * @throws Exception
     * @return String
     * @throws
     * @date 2015-9-27 下午6:48:25
     */
    @RequestMapping(value = "/updateMesOneStatus")
    @ResponseBody
    public RequestResult updateMesOneStatus(HttpServletRequest request) throws Exception{
    	RequestResult<String> result=new RequestResult<String>();
    	Map<String,String> params=HttpUtil.getParameterMap(request.getParameterMap());
    	ClientExceptionReq clientReqe=JSONObject.parseObject(params.get("data"),ClientExceptionReq.class);
    	boolean flag=errMsgService.updateMesOneStatus(clientReqe);
		result.setSuccess(flag);
    	return result;
    }
    
    /**
     * 修改多条条异常信息
     * @Title: updateMesOneStatus
     * @author ZJHao
     * @param request
     * @return
     * @throws Exception
     * @return String
     * @throws
     * @date 2015-9-27 下午6:48:25
     */
    @RequestMapping(value = "/updateMoreMesStatus")
    @ResponseBody
    public RequestResult updateMoreMesStatus(HttpServletRequest request) throws Exception{
    	RequestResult<String> result=new RequestResult<String>();
    	Map<String,String> params=HttpUtil.getParameterMap(request.getParameterMap());
    	ClientExceptionReq clientReqe=JSONObject.parseObject(params.get("data"),ClientExceptionReq.class);
    	boolean flag=errMsgService.updateMoreMesStatus(clientReqe);
    	result.setSuccess(flag);
    	return result;
    }
    /**
   	 *  查询详细的异常信息用于在系统监控页面展示
   	 */
    @RequestMapping(value = "/selectMessageInfoForSysMonitorShow")
    @ResponseBody
   	public RequestResult selectMessageInfoForSysMonitorShow(HttpServletRequest request) throws Exception{
       	Map<String, String> params=HttpUtil.getParameterMap(request.getParameterMap());
       	HbasePage page=JSONObject.parseObject(params.get("data"),HbasePage.class);
   		RequestResult<String> result=new RequestResult<String>();
   		log.info("param:{}",page.toString());
   		List<ClientExceptionReq> listReq=errMsgService.selectMessageInfoForSysMonitorShow(page);
   		result.setSuccess(true);
   		result.setContent(JSONObject.toJSONString(listReq));
   		log.info("查询selectMessageInfoForSysMonitorShow获取异常列表结束,listReq:{}",result.getContent());
   		return result;
   	}
    
    
    
    
}
