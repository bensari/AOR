package com.jive.server.location.io.network;

import com.jive.server.location.store.AORPRovider;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.CharsetUtil;

public class AORSocketInitialiser extends ChannelInitializer<SocketChannel> {

	private AORPRovider aorPRovider;

	public AORSocketInitialiser(AORPRovider aorPRovider) {
		this.aorPRovider = aorPRovider;
	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(ReadTimeoutHandler.class.getName(), new ReadTimeoutHandler(10));
		pipeline.addLast(StringDecoder.class.getName(), new StringDecoder(CharsetUtil.UTF_8));
		pipeline.addLast(StringEncoder.class.getName(), new StringEncoder(CharsetUtil.UTF_8));
		pipeline.addLast(AORRequestHandler.class.getName(), new AORRequestHandler(aorPRovider));
	}
}
