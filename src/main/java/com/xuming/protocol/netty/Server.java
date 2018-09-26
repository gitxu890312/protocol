package com.xuming.protocol.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Server {

	public static void main(String[] args) {

		NioEventLoopGroup parnentLoop = new NioEventLoopGroup(1);
		NioEventLoopGroup childLoop = new NioEventLoopGroup();
		try {
			ServerBootstrap boot = new ServerBootstrap();

			boot.group(parnentLoop, childLoop);
			boot.channel(NioServerSocketChannel.class);

			boot.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel socketChannel) throws Exception {
					ChannelPipeline pipeline = socketChannel.pipeline();
					 //字符串解码器
                    pipeline.addLast(new StringDecoder());
                    //字符串编码器
                    pipeline.addLast(new StringEncoder());
                    //处理类
                    pipeline.addLast(new ServerHandler4());
				}
			});
			// 设置TCP参数
			// 1.链接缓冲池的大小（ServerSocketChannel的设置）
			boot.option(ChannelOption.SO_BACKLOG, 1024);
			// 维持链接的活跃，清除死链接(SocketChannel的设置)
			boot.childOption(ChannelOption.SO_KEEPALIVE, true);
			// 关闭延迟发送
			boot.childOption(ChannelOption.TCP_NODELAY, true);

			// 绑定端口
			ChannelFuture future = boot.bind(8866).sync();
			future.channel().closeFuture().sync();
			System.out.println("server start ...... ");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			parnentLoop.shutdownGracefully();
			childLoop.shutdownGracefully();
		}

	}
}
