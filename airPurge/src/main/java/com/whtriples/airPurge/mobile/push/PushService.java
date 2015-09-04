package com.whtriples.airPurge.mobile.push;

import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.whtriples.airPurge.util.ConfigUtil;
/**
 * netty服务类
 * @author Administrator
 *
 */
@Component
public class PushService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
    public static final int SERVER_READER_PORT = Integer.parseInt(ConfigUtil.getConfig("push.port", "8011"));

    public static final  int HEADER_LENGTH = Integer.parseInt(ConfigUtil.getConfig("push.headerLength", "4"));

    private ServerBootstrap bootstrap = null;
    
    @PostConstruct
    private void startServerReader() {
    	bootstrap = new ServerBootstrap(PushResources.getChannelFactory(true, true));
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new ServerReaderDecoder(), new ServerReaderHandler());
            }
        });
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.bind(new InetSocketAddress(SERVER_READER_PORT));
        logger.warn("start scoket service finished");
    }

    @PreDestroy
    private void stopServer(){
    	if(bootstrap != null)
    	    bootstrap.shutdown();
    	logger.warn("stop scoket service finished");
    }
    
    public static boolean sendMessage(Channel channel, String msg) {
        try {
            if (channel != null && channel.isConnected() && channel.isOpen() && channel.isWritable()) {
                ChannelBuffer buffer = PushService.buffer(msg.getBytes().length + PushService.HEADER_LENGTH);
                buffer.writeInt(msg.getBytes().length);
                buffer.writeBytes(msg.getBytes());
                channel.write(buffer);
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static ChannelBuffer buffer(int length) {
        return ChannelBuffers.buffer(PushService.HEADER_LENGTH + length);
    }

    
}
