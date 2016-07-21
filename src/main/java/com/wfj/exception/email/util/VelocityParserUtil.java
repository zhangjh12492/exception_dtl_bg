package com.wfj.exception.email.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MethodInvocationException;

/** 
 * 功能:解析velocity模板 
 * <p> 
 * 用法: 
 *  
 * @version 1.0 
 */  
  
public class VelocityParserUtil {  
    /** 
     * Logger for this class 
     */  
    private static final Logger logger = LoggerFactory.getLogger(VelocityParserUtil.class);  
  
    private static VelocityParserUtil instance = new VelocityParserUtil();  
  
    private VelocityEngine engine = null;  
  
    private VelocityParserUtil() {  
        // init engine  
        engine = new VelocityEngine();  
        try {  
            engine.init();  
        } catch (Exception e) {  
            logger.warn("VelocityParserUtil() - exception ignored", e); //$NON-NLS-1$  
  
        }  
    }  
  
    /** 
     * 返回VelocityParserUtil实例 
     * @return 
     */  
    public static VelocityParserUtil getInstance() {  
        return instance;  
    }  
  
      
  
    /** 
     * 解析velocity模板 
     * @param vtl 
     * @param model 
     * @return String  
     * @throws ParseErrorException 
     * @throws MethodInvocationException 
     * @throws ResourceNotFoundException 
     * @throws IOException 
     */  
    public String parseVelocityTemplate(String vtl, Map model)  
            throws ParseErrorException, MethodInvocationException,  
            ResourceNotFoundException, IOException {  
        if (logger.isDebugEnabled()) {  
            logger.debug("parseVelocityTemplate(String, Map) - start"); //$NON-NLS-1$  
        }  
  
        VelocityContext velocityContext = new VelocityContext(model);  
        StringWriter result = new StringWriter();  
        engine.evaluate(velocityContext, result, null, vtl);  
  
        String returnString = result.toString();  
        if (logger.isDebugEnabled()) {  
            logger.debug("parseVelocityTemplate(String, Map) - end"); //$NON-NLS-1$  
        }  
        return returnString;  
  
    }  
  
}  