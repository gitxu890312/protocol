package com.xuming.protocol.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Scanner;

import org.junit.Test;

/**
 * 
 * @author xum890312
 * udp通信
 */
public class DataGramChannelDemo {

	@Test
	public void send() throws IOException {
		DatagramChannel server = DatagramChannel.open();
		server.configureBlocking(false);
		Scanner scanner = new Scanner(System.in);
		ByteBuffer buf = ByteBuffer.allocate(1024);
		while (scanner.hasNext()) {
			String line = scanner.next();
			if ("quit".equals(line)) {
				break;
			}
			buf.put(line.getBytes());
			buf.flip();
			server.send(buf, new InetSocketAddress("127.0.0.1", 9898));
			buf.clear();
		}
		scanner.close();
		server.close();
	}
	@Test
	public void receive() throws IOException {
		DatagramChannel receive = DatagramChannel.open();
		receive.configureBlocking(false);

		receive.bind(new InetSocketAddress("127.0.0.1", 9898));

		Selector select = Selector.open();
		receive.register(select, SelectionKey.OP_READ);

		ByteBuffer buffer = ByteBuffer.allocate(1024);
		while (select.select() > 0) {
			Iterator<SelectionKey> iterator = select.selectedKeys().iterator();
			while (iterator.hasNext()) {
				SelectionKey selectkey = iterator.next();
				if (selectkey.isReadable()) {
					receive.receive(buffer);
					buffer.flip();
					String msg = new String(buffer.array(), 0, buffer.limit());
					System.out.println("收到广播消息：" + msg);
					buffer.clear();
				}
				iterator.remove();
			}
		}
	}
}
