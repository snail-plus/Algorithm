package com.whtriples.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class DiscardServer {

	public static void startServer() {
		//ChannelFactory 是一个创建和管理Channel通道及其相关资源的工厂接口，它处理所有的I/O请求并产生相应的I/O ChannelEvent通道事件
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),Executors.newCachedThreadPool()); 
		//ServerBootstrap 是一个设置服务的帮助类
		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		DiscardServerHandler  handler =new DiscardServerHandler();
		ChannelPipeline pipeline = bootstrap.getPipeline(); 
		pipeline.addLast("handler", handler);
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		bootstrap.bind(new InetSocketAddress(9998));
		System.out.println(System.nanoTime());
	}
	
	public static void main(String[] args) {
		startServer();
	}
}
