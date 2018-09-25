package com.xuming.protocol.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import org.junit.Test;

/**
 * 
 * @author xum890312
 * 
 */
public class NIOSocketDemo {

	@Test
	public void client() throws IOException {
		SocketChannel client = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
		client.configureBlocking(false);

		Scanner scanner = new Scanner(System.in);
		
		while (scanner.hasNext()) {
			String next = scanner.next();
			if ("quit".equals(next)) {
				break;
			}
			ByteBuffer wrap = ByteBuffer.wrap((new Date().toString() + next).getBytes());
			client.write(wrap);
		}
		scanner.close();
		client.close();
	}
	@Test
	public void server() throws IOException {
		ServerSocketChannel server = ServerSocketChannel.open();
		server.configureBlocking(false);
		server.bind(new InetSocketAddress(9898));
		
		Selector selector = Selector.open();
		server.register(selector, SelectionKey.OP_ACCEPT);
		while (selector.select() > 0) {
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

			while (iterator.hasNext()) {
				SelectionKey selectKey = iterator.next();
				if (selectKey.isAcceptable()) {
					SocketChannel connector = server.accept();
					connector.configureBlocking(false);
					System.out.println("客户端链接：" + connector.getRemoteAddress());
					connector.register(selector, SelectionKey.OP_READ);
				} else if (selectKey.isReadable()) {
					SocketChannel connector = (SocketChannel) selectKey.channel();
					ByteBuffer buf = ByteBuffer.allocate(1024);
					while ((connector.read(buf)) > 0) {
						buf.flip();
						CharBuffer decode = Charset.forName("UTF8").decode(buf);
						System.out.println("接收到客户端消息:" + decode);
						buf.clear();
					}
				}
				iterator.remove();
			}
		}
	}
}
