package com.whtriples.airPurge.api.protocol;

import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.whtriples.airPurge.api.param.Param;
import com.whtriples.airPurge.util.Exceptions;


public class HttpBodyProtocol implements Protocol {
	
	private static Logger logger = LoggerFactory.getLogger(HttpBodyProtocol.class);

	private String url;

	private Charset charset;
	
	private ContentType contentType;
	
	private HttpBodyProtocol() {
		super();
	}
	
	public static HttpBodyProtocol builder() {
		HttpBodyProtocol protocol = new HttpBodyProtocol();
		return protocol;
	}
	
	public HttpBodyProtocol url(String url) {
		this.url = url;
		return this;
	}
	
	public HttpBodyProtocol charset(Charset charset) {
		this.charset = charset;
		return this;
	}
	
	public HttpBodyProtocol contentType(ContentType contentType) {
		this.contentType = contentType;
		return this;
	}
	

	@Override
	public String send(Param param) {
		logger.debug("\n{}\nparam:\n{}", url, param.toString(true));
		
		String responseText = null;
		Preconditions.checkArgument(!StringUtils.isEmpty(url), "url is empty");
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		if(contentType == null) {
			contentType = ContentType.create("text/html", charset);
		}
		httpPost.setEntity(new StringEntity(param.toString(), contentType));
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
		logger.debug("HttpBodyProtocol response:\n{}", responseText);
		return responseText;
	}

}
