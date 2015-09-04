package com.whtriples.airPurge.mobile.push;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.oio.OioServerSocketChannelFactory;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PushResources {

	private static final Executor bossExecutorPool = Executors
			.newCachedThreadPool();
	private static final Executor workerExecutorPool = Executors
			.newCachedThreadPool();

	private static final Executor threadPool = Executors.newCachedThreadPool();

	private static final ChannelFactory nioServerChannelFactory = new NioServerSocketChannelFactory(
			bossExecutorPool, workerExecutorPool);

	private static final ChannelFactory oioServerChannelFactory = new OioServerSocketChannelFactory(
			bossExecutorPool, workerExecutorPool);

	private static final ChannelFactory nioClientChannelFactory = new NioClientSocketChannelFactory(
			bossExecutorPool, workerExecutorPool);

	private static final ChannelFactory oioClientChannelFactory = new OioClientSocketChannelFactory(
			workerExecutorPool);

	private static final ChannelGroup allChannels = new DefaultChannelGroup(
			"nio-channel");

	private static final BiMap<String, Channel> channelMap = HashBiMap
			.create(512);

	public static Executor getThreadPool() {
		return threadPool;
	}

	public static ChannelGroup getChannelGroup() {
		return allChannels;
	}

	public static ChannelFactory getChannelFactory(boolean isServer,
			boolean isNio) {
		return isServer ? isNio ? nioServerChannelFactory
				: oioServerChannelFactory : isNio ? nioClientChannelFactory
				: oioClientChannelFactory;
	}

	public static void releaseExternalResources() {
		nioServerChannelFactory.releaseExternalResources();
		oioServerChannelFactory.releaseExternalResources();
		nioClientChannelFactory.releaseExternalResources();
		oioClientChannelFactory.releaseExternalResources();
	}

	public static boolean addChannel(String key, Channel channel) {
		boolean addSuccess = allChannels.add(channel);
		if (addSuccess) {
			channelMap.put(key, channel);
			System.out.println(channelMap + "--------channelMap----------");
		}
		return addSuccess;
	}

	public static Channel getChannel(String key) {
		return channelMap.get(key);
	}

	public static Set<String> getOnlineToken() {
		return channelMap.keySet();
	}

	public static Collection<Channel> getAllChannel() {
		return channelMap.values();
	}

	public static void removeChannel(Channel channel) {
		allChannels.remove(channel);
		channelMap.inverse().remove(channel);
	}

	public static BiMap<String, Channel> getAllChannelMap() {
		return channelMap;
	}

}
