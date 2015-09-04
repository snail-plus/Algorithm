package com.whtriples.airPurge.api.protocol;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.whtriples.airPurge.api.param.KvParam;
import com.whtriples.airPurge.api.param.Param;
import com.whtriples.airPurge.util.Exceptions;


public class HttpPostProtocol implements Protocol {
	
	private static Logger logger = LoggerFactory.getLogger(HttpPostProtocol.class);

	private String url;

	private Charset charset;
	
	private HttpPostProtocol() {
		super();
	}
	
	public static HttpPostProtocol builder() {
		HttpPostProtocol protocol = new HttpPostProtocol();
		return protocol;
	}
	
	public HttpPostProtocol url(String url) {
		this.url = url;
		return this;
	}
	
	public HttpPostProtocol charset(Charset charset) {
		this.charset = charset;
		return this;
	}
	

	@Override
	public String send(Param param) {
		logger.debug("\n{}\nparam:\n{}", url, param.toString(true));
		String responseText = null;
		
		Preconditions.checkArgument(!StringUtils.isEmpty(url), "url is empty");
		Preconditions.checkArgument(param instanceof KvParam, "http param is not a kvParam");
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		Map<String, Object> kv = ((KvParam)param).getKv();
		List<NameValuePair> nvps = Lists.newArrayList();
		for(String key : kv.keySet()) {
			nvps.add(new BasicNameValuePair(key, String.valueOf(kv.get(key))));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, charset));
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
			logger.debug(response.getStatusLine().toString());
		    HttpEntity entity = response.getEntity();
		    responseText = EntityUtils.toString(entity);
		    EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw Exceptions.unchecked(e);
		} finally {
			try {
				if(response != null) {
					response.close();
				}
			} catch(Exception e) {
				throw Exceptions.unchecked(e);
			}
		}
		logger.info("HttpPostProtocol response:\n{}", responseText);
		return responseText;
	}

}