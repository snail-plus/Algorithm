package com.whtriples.airPurge.msg;

import java.io.IOException;

import org.apache.commons.codec.Charsets;

import com.whtriples.airPurge.api.param.KvParam;
import com.whtriples.airPurge.api.protocol.HttpPostProtocol;
import com.whtriples.airPurge.mobile.exception.AirPurgeError;


public class SendSms {
	/**
	 * 服务http地址
	 */
	private static String BASE_URI = "http://yunpian.com";
	/**
	 * 服务版本号
	 */
	private static String VERSION = "v1";
	/**
	 * 编码格式
	 */
	private static String ENCODING = "UTF-8";
	/**
	 * 查账户信息的http地址
	 */
	private static String URI_GET_USER_INFO = BASE_URI + "/" + VERSION + "/user/get.json";
	/**
	 * 通用发送接口的http地址
	 */
	private static String URI_SEND_SMS = BASE_URI + "/" + VERSION + "/sms/send.json";
	/**
	 * 模板发送接口的http地址
	 */
	private static String URI_TPL_SEND_SMS = BASE_URI + "/" + VERSION + "/sms/tpl_send.json";

	/**
	 * 网关接口APIKEY
	 */
	
	//private static String APIKEY = ConfigUtil.getConfig("apikey");
	private static String APIKEY = "572afd9db1be814dc5cc985be49b534e";
	/*
	 * 短信模板ID
	 */
	private static final long TPL_ID = 763785; //Long.valueOf(ConfigUtil.getConfig("tpl_id"));

	/**
	 * 取账户信息
	 * 
	 * @return json格式字符串
	 * @throws IOException
	 */
	public static String getUserInfo() throws IOException {
		try {
			return HttpPostProtocol.builder()
					.url(URI_GET_USER_INFO)
					.charset(Charsets.toCharset(ENCODING))
					.send(KvParam.builder().set("apikey", APIKEY));
		} catch (Exception e) {
			return AirPurgeError.CONNECT_SEND_SMS_SERVICE_FAIL.code;
		}
	}

	/**
	 * 发短信
	 * 
	 * @param apikey
	 *            apikey
	 * @param text
	 *            　短信内容　
	 * @param mobile
	 *            　接受的手机号
	 * @return json格式字符串
	 * @throws IOException
	 */
	public static String sendSms(String text, String mobile){
		try {
			return HttpPostProtocol.builder()
					.url(URI_SEND_SMS)
					.charset(Charsets.toCharset(ENCODING))
					.send(KvParam.builder()
							.set("apikey", APIKEY)
							.set("text", text)
							.set("mobile", mobile));
		} catch (Exception e) {
			return AirPurgeError.CONNECT_SEND_SMS_SERVICE_FAIL.code;
		}
	}

	/**
	 * 通过模板发送短信
	 * 
	 * @param apikey
	 *            apikey
	 * @param tpl_id
	 *            　模板id
	 * @param tpl_value
	 *            　模板变量值　
	 * @param mobile
	 *            　接受的手机号
	 * @return json格式字符串
	 * @throws IOException
	 */
	public static String tplSendSms(String tpl_value, String mobile){
		try {
			return HttpPostProtocol.builder()
					.url(URI_TPL_SEND_SMS)
					.charset(Charsets.toCharset(ENCODING))
					.send(KvParam.builder()
							.set("apikey", APIKEY)
							.set("tpl_id", String.valueOf(TPL_ID))
							.set("tpl_value", tpl_value)
							.set("mobile", mobile));
		} catch (Exception e) {
			return AirPurgeError.CONNECT_SEND_SMS_SERVICE_FAIL.code;
		}
	}
	
	public static void main(String[] args)throws Exception {
		System.out.println(tplSendSms(String.valueOf(TPL_ID) ,"13080612932"));
		/*System.out.println(sendSms("【智慧社区】尊敬的某某，您的家人某某在本小区的时尚风食府为您预定了二楼6号餐厅的餐位，"
				+ "请您在2015-03-06下午7点钟到达本餐厅用餐，如收到本短信请务必回复Y，请知晓。","15997438529"));*/
		
	}
	
}
