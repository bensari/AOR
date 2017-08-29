package com.jive.server.location.io.network;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jive.server.location.store.AORPRovider;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class AORRequestHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LogManager.getLogger(AORRequestHandler.class);

	private AORPRovider aorPRovider;

	public AORRequestHandler(AORPRovider aorPRovider) {
		this.aorPRovider = aorPRovider;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			if (msg != null) {
				String aorKey = (String) msg;
				logger.debug("Recieved AOR request {} from {}", aorKey, ctx.channel().remoteAddress());
				String splitter = null;
				if (aorKey.contains("\r\n")) {
					splitter = "\r\n";
				} else if (aorKey.contains("\n")) {
					splitter = "\n";
				}
				String[] keys = null;
				if (splitter != null) {
					keys = aorKey.split(splitter);
				} else {
					keys = new String[1];
					keys[0] = aorKey;
				}
				StringBuilder response = new StringBuilder("\r\n");
				for (String key : keys) {
					List<String> list = aorPRovider.getAOR(key);
					if ((list != null) && (!list.isEmpty())) {
						for (String aorAsJsonFormat : list) {
							response.append(aorAsJsonFormat).append("\r\n");
						}
					}
				}
				logger.debug("Response for AOR request {} is {}", aorKey, response.toString());
				ctx.writeAndFlush(response.toString()).addListener(ChannelFutureListener.CLOSE);
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Exception Caught from client {} with message {}", ctx.channel().remoteAddress(), cause);
		ctx.close();
	}
}
