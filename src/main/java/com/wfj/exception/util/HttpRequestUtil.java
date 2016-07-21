package com.wfj.exception.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.wfj.exception.common.SysConstants;


/**
 * <p>
 * Description: [http请求工具类]
 * </p>
 * Created on 2012-6-26
 * 
 * @author <a href="mailto: zh-fan@neusoft.com">zh-fan</a>
 * @version 1.0
 */
public class HttpRequestUtil {

	/**
	 * <p>
	 * Discription:[发doPost请求]
	 * </p>
	 * 
	 * @param destUrl
	 * @param postData
	 * @return
	 * @throws Exception
	 * @author:[zh-fan]
	 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
	 */
	public static String send(String destUrl, String postData) throws Exception {
		String recString = "";
		URL url = null;
		HttpURLConnection urlconn = null;
		url = new URL(destUrl);
		urlconn = (HttpURLConnection) url.openConnection();
		urlconn.setRequestProperty("content-type", "application/json;charset=UTF-8");
		urlconn.setRequestMethod("POST");
		urlconn.setDoInput(true);
		urlconn.setDoOutput(true);
		urlconn.setConnectTimeout(SysConstants.CONN_TIMEOUT_SECOND);
		OutputStream out = urlconn.getOutputStream();
		out.write(postData.getBytes("UTF-8"));

		out.flush();
		out.close();

		BufferedReader rd = new BufferedReader(new InputStreamReader(
				urlconn.getInputStream(), "UTF-8"));
		StringBuffer sb = new StringBuffer();
		int ch;
		while ((ch = rd.read()) > -1) {
			sb.append((char) ch);
		}
		recString = sb.toString();
		rd.close();
		urlconn.disconnect();

		return recString;
	}

	/**
	 * 发送post请求
	 * 
	 * @Title: sendJson
	 * @author ZhangJH
	 * @param destUrl
	 * @param postData
	 * @param type
	 * @return
	 * @throws Exception
	 * @return String
	 * @throws
	 * @date 2014-11-28 下午3:49:15
	 */
	public static String sendJson(String destUrl, String postData, String type)
			throws Exception {
		String recString = "";
		URL url = null;
		HttpURLConnection urlconn = null;
		url = new URL(destUrl);
		urlconn = (HttpURLConnection) url.openConnection();
		urlconn.setRequestProperty("Content-Type", "application/json");
		urlconn.setRequestProperty("Accept", "application/json");
		urlconn.setRequestProperty("X-tenantId", "single");
		urlconn.setRequestMethod(type);
		urlconn.setDoInput(true);
		urlconn.setDoOutput(true);
		urlconn.setConnectTimeout(SysConstants.CONN_TIMEOUT_SECOND);
		OutputStream out = urlconn.getOutputStream();
		out.write(postData.getBytes("UTF-8"));

		out.flush();
		out.close();

		BufferedReader rd = new BufferedReader(new InputStreamReader(
				urlconn.getInputStream(), "UTF-8"));
		StringBuffer sb = new StringBuffer();
		int ch;
		while ((ch = rd.read()) > -1) {
			sb.append((char) ch);
		}
		recString = sb.toString();
		rd.close();
		urlconn.disconnect();

		return recString;
	}

	/*
	 * public static void main(String[] args) { try { StringBuffer sb = new
	 * StringBuffer(
	 * "timestamp=1381889190407&method=rrs.order.accept.acceptwodata&access_token=NDBhODhhMGMtOGEzOS00Zjk0LTg1ZmQtNDIxYWQwZDNhMjRm&apply_id=8700002301TJDB2013071900010,8700002301TJDB2013071900010,8700002301TJDB2013071900010&product_id=null,null,null&product_model=BH02Z0090,B702P108N,BH02L0080&service_type_code=T26,T26,T26&service_mode_code=null,null,null&buy_date=null,null,null&require_service_date=null,null,null&customer_name=null,null,null&sex=null,null,null&career=null,null,null&mobile_phone=null,null,null&phone=null,null,null&area_code=null,null,null&district_code=null,null,null&address=山东青岛胶州市,山东青岛胶州市,山东青岛胶州市&require_service_desc=null,null,null&service_time=null,null,null&userip=null,null,null&login_username=null,null,null&email=null,null,null&parent_apply_id=87000023011307195376,87000023011307195377,87000023011307195378&source_code=null,null,null&source_code_desc=TCCPCSJZ,TCCPCSJZ,TCCPCSJZ&order_type=null,null,null&dy_type=2,2,2&order_count=-2,-1,-2&poi_id=null,null,null&customer_id=null,null,null&order_date=2013-07-19 13:49:22,2013-07-19 13:49:22,2013-07-19 13:49:22&recvbl_amt=null,null,null&tail_recvle_amt=null,null,null&pay_status=P1,P1,P1&jnbt_id=null,null,null&product_price=null,null,null&seller_no=8700002301,8700002301,8700002301&rcvbl_tail_amt=-124260,-124260,-124260&store_loc_type=AHM1,AHM1,AHM1&es_order_type=null,null,null&machine_type=10,10,10&es_sell_no=null,null,null&department_name=null,null,null&is_overtime=null,null,null&sercos=1,1,1&sermod=1,1,1&cookie_id=null,null,null&fhf_code=null,null,null&fhf_customer_name=null,null,null&fhf_telephone=null,null,null&fhf_mobile_phone=null,null,null&fhf_prov=null,null,null&fhf_city=null,null,null&fhf_region=null,null,null&fhf_address=null,null,null"
	 * ); String string2 =
	 * HttpRequestUtil.send("http://10.135.106.85:8000/rrs/center",
	 * sb.toString()); System.out.println(string2); } catch (Exception e) {
	 * e.printStackTrace(); } }
	 */
	public static String sendTenantId(String destUrl, String postData,
			String type) throws Exception {
		String recString = "";
		URL url = null;
		HttpURLConnection urlconn = null;
		url = new URL(destUrl);
		urlconn = (HttpURLConnection) url.openConnection();
		urlconn.setRequestProperty("Content-Type", "application/json");
		urlconn.setRequestProperty("Accept", "application/json");
		urlconn.setRequestProperty("X-tenantId", "single");
		urlconn.setRequestMethod(type);
		urlconn.setDoInput(true);
		urlconn.setDoOutput(true);
		urlconn.setConnectTimeout(SysConstants.CONN_TIMEOUT_SECOND);
		OutputStream out = urlconn.getOutputStream();
		out.write(postData.getBytes("UTF-8"));

		out.flush();
		out.close();

		BufferedReader rd = new BufferedReader(new InputStreamReader(
				urlconn.getInputStream(), "UTF-8"));
		StringBuffer sb = new StringBuffer();
		int ch;
		while ((ch = rd.read()) > -1) {
			sb.append((char) ch);
		}
		recString = sb.toString();
		rd.close();
		urlconn.disconnect();

		return recString;
	}
}
