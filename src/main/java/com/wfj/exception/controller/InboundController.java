package com.wfj.exception.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.wfj.exception.dto.CallbackDto;
import com.wfj.exception.hadoop.hbase.service.ErrMsgService;
import com.wfj.exception.vo.ClientExceptionReq;

/**
 * Created by MaYong on 2014/12/12.
 */
@Controller
@RequestMapping("/mqResbondData")
public class InboundController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name="errMsgService")
    private ErrMsgService errMsgService;

    //http://127.0.0.1:8080/ITGInput/itgService/jsonInbound.do
    @RequestMapping(value = "/josnInbound", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public CallbackDto jsonInbound(HttpServletRequest request) {

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

        CallbackDto itgCallbackDto =  new CallbackDto();
        itgCallbackDto.setRespStatus("1");
        try {
//            在这里处理接入的数据
            logger.info("payment type receive data:{}", message);
            ClientExceptionReq req= JSONObject.parseObject(message, ClientExceptionReq.class);
            errMsgService.insertMsg(req, "");

        } catch (Exception e) {
            logger.error("=== 调用接入服务出错" + e.getMessage() + " ===");
            itgCallbackDto.setRespStatus("0");
        }
        return itgCallbackDto;
    }
    
}
