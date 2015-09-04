package com.whtriples.airPurge.mobile.push;

import com.whtriples.airPurge.mobile.push.PushService;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.jboss.netty.util.CharsetUtil;

/**
 * 解码器
 * 
 * @author wj
 *
 */
public class ServerReaderDecoder extends FrameDecoder {

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		PushResources.removeChannel(e.getChannel());
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) {
		if (buffer.readableBytes() < PushService.HEADER_LENGTH) {
			return null;
		}

		buffer.markReaderIndex();
		int dataLength = buffer.readInt();
		if (dataLength == 0) {
			// 数据包读取异常关闭连接
			PushResources.removeChannel(channel);
		}
		if (buffer.readableBytes() < dataLength) {
			buffer.resetReaderIndex();
			return null;
		}

		byte body[] = new byte[dataLength];
		buffer.readBytes(body);
		String msgBody = new String(body, CharsetUtil.UTF_8);
		return msgBody;
	}
}
