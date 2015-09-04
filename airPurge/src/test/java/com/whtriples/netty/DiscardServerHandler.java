package com.whtriples.netty;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.LoggerFactory;

@Sharable 
public class DiscardServerHandler extends SimpleChannelHandler {

	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(DiscardServerHandler.class);
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		 ChannelBuffer  buf = (ChannelBuffer) e.getMessage();
		 logger.info("");
		 System.out.println(new String(buf.array()));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		super.exceptionCaught(ctx, e);
	}

	
}
