package com.whtriples.airPurge.api.protocol;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.whtriples.airPurge.api.param.KvParam;
import com.whtriples.airPurge.api.param.Param;
import com.whtriples.airPurge.util.Exceptions;


public class HttpGetProtocol implements Protocol {
	
	private static Logger logger = LoggerFactory.getLogger(HttpGetProtocol.class);

	private String url;

	private HttpGetProtocol() {
		super();
	}
	
	public static HttpGetProtocol builder() {
		HttpGetProtocol protocol = new HttpGetProtocol();
		return protocol;
	}
	
	public HttpGetProtocol url(String url) {
		this.url = url;
		return this;
	}
	
	@Override
	public String send(Param param) {
		logger.debug("\n{}\nparam:\n{}", url, param.toString(true));
		
		String responseText = null;
		
		Preconditions.checkArgument(!StringUtils.isEmpty(url), "url is empty");
		Preconditions.checkArgument(param instanceof KvParam, "http param is not a kvParam");
		
		URIBuilder uriBuilder = new URIBuilder(URI.create(url));
		Map<String, Object> kv = ((KvParam)param).getKv();
		for(String key : kv.keySet()) {
			uriBuilder.addParameter(key, String.valueOf(kv.get(key)));
		}
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = null;
		try {
			httpGet = new HttpGet(uriBuilder.build());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw Exceptions.unchecked(e);
		}
		
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpGet);
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
		logger.debug("HttpPostProtocol response:\n{}", responseText);
		return responseText;
	}

}
