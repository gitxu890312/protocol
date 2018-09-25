package com.xuming.protocol.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

/**
 * 
 * @author xum890312 
 * java.nio.channels.Channel 
 * 		   --java.nio.channels.FileChannel
 *         --java.nio.channels.SocketChannel client
 *         --java.nio.channels.ServerSocketChannel tcp server
 *         --io.netty.channel.socket.DatagramChannel udp
 * 
 * 本地io 
 *     FileInputStream/FileOutputStream 
 *     RandomAccessFile
 * 
 *网络io
 */
public class ChannelTest {

	// channel文件复制
	@Test
	public void test() throws Exception {
		
		//创建channel
		FileInputStream fin = new FileInputStream("1.jpg");
		FileOutputStream fos = new FileOutputStream("2.jpg");

		FileChannel inchannel = fin.getChannel();
		FileChannel outchannel = fos.getChannel();

		ByteBuffer buffer = ByteBuffer.allocate(100);
		while (inchannel.read(buffer) != -1) {
			buffer.flip();// 切换读模式
			outchannel.write(buffer);
			buffer.clear();// 清空缓冲区
		}
		fin.close();
		fos.close();
		inchannel.close();
		outchannel.close();
	}

	@Test
	public void test1() throws IOException {
		FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);

		FileChannel outChannel = FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE,
				StandardOpenOption.CREATE);

		MappedByteBuffer inmap = inChannel.map(MapMode.READ_ONLY, 0, inChannel.size());
		MappedByteBuffer outmap = outChannel.map(MapMode.READ_WRITE, 0, inChannel.size());
		byte[] arr = new byte[inmap.limit()];
		inmap.get(arr);
		outmap.put(arr);
		inChannel.close();
		outChannel.close();
	}
	@Test
	public void test2() throws IOException {
		FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);

		FileChannel outChannel = FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE,
				StandardOpenOption.CREATE);
		
//		inChannel.transferTo(0, inChannel.size(), outChannel);通道的数据传输
		outChannel.transferFrom(inChannel, 0, inChannel.size());
		inChannel.close();
		outChannel.close();
	}
}
