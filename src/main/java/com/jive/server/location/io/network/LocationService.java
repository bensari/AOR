package com.jive.server.location.io.network;

import java.net.InetSocketAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jive.server.location.store.AORPRovider;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class LocationService implements Service {

	private static final Logger logger = LogManager.getLogger(LocationService.class);
	private NioEventLoopGroup acceptorGroup;
	private NioEventLoopGroup handlerGroup;
	private ChannelFuture channelFuture;
	private AORPRovider aorPRovider;
	private InetSocketAddress address;

	public LocationService(AORPRovider aorPRovider, String ipAddress, int port) {
		address = new InetSocketAddress(ipAddress, port);
		this.aorPRovider = aorPRovider;
		acceptorGroup = new NioEventLoopGroup();
		handlerGroup = new NioEventLoopGroup();
	}

	public boolean start() throws Exception {

		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(acceptorGroup, handlerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new AORSocketInitialiser(aorPRovider)).option(ChannelOption.SO_BACKLOG, 15)
				.childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
		channelFuture = serverBootstrap.localAddress(address).bind().sync();
		// channelFuture = serverBootstrap.localAddress(port).bind().sync();
		logger.info("Started on address {}", address);

		return true;
	}

	public boolean stop() throws Exception {

		acceptorGroup.shutdownGracefully().sync();
		handlerGroup.shutdownGracefully().sync();
		channelFuture.channel().closeFuture().sync(); // close port
		logger.info("Stopped on address {}", address);

		return true;
	}
}
