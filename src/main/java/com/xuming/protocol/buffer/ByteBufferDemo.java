package com.xuming.protocol.buffer;

import java.nio.ByteBuffer;

import org.junit.Test;

/**
 * 
 * @author xum890312
 * java ByteBuffer缓冲区
 */
public class ByteBufferDemo {
	/**
	 * buffer中有4个记录位置的字段。
	 * mark 标记的位置
	 * position 位置 游标的位置
	 * limit 界限 可以处理的最大数据位置
	 * capacity 容量 buffer容量 不可改变
	 * mark <position<limit<capacity
	 */
	@Test
	public void test() {
		byte[] arrs = "abcde".getBytes();
		ByteBuffer buffer = ByteBuffer.allocate(100);
		System.out.println("-------init--------");
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
		System.out.println("-------put--------");
		buffer.put(arrs);
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
		
		buffer.flip();//切换为读模式  将limit=position position=0;
		System.out.println("-------flip--------");
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
		
		System.out.println("-------get--------");
		byte[] read = new byte[2];
		buffer.get(read);
		System.out.println(new String(read));
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
		
		
		buffer.clear();//回归到初始化状态
		System.out.println("-------clear--------");
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
	}
	//reset 
	@Test
	public void test2() {
		ByteBuffer buffer = ByteBuffer.allocate(100);
		buffer.put("abcde".getBytes());
		buffer.flip();
		
		byte[] read = new byte[2];
		buffer.get(read);
		System.out.println("-----read-------");
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
		buffer.mark();
		buffer.get(read);
		System.out.println("----mark-read-------");
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
		buffer.reset();//将position 重置到mark的位置 
		System.out.println("-----reset-------");
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
		/**
		 * rewind 重新读取 position=0 mark=-1
		 */
		buffer.rewind();// 
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
	}
	// 直接缓冲区和非直接缓冲区
	@Test
	public void test3() {
		ByteBuffer allocate = ByteBuffer.allocate(1024);
		ByteBuffer allocateDirect = ByteBuffer.allocateDirect(1024);
		System.out.println(allocate.isDirect());
		System.out.println(allocateDirect.isDirect());
		System.out.println(allocate.isReadOnly());
		System.out.println(allocateDirect.isReadOnly());
	}
	//wrap()
	@Test
	public void test4() {
		
		ByteBuffer buffer = ByteBuffer.wrap("abcde".getBytes());
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
	}
	@Test
	public void test5() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		// duplicate 会共用一个数据存储空间
		ByteBuffer duplicate = buffer.duplicate();
		buffer.put("abcde".getBytes());
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
		duplicate.put("11111".getBytes());
		System.out.println(duplicate.position());
		System.out.println(duplicate.limit());
		System.out.println(duplicate.capacity());

		buffer.flip();
		duplicate.flip();
		byte[] src = new byte[5];
		buffer.get(src);
		System.out.println(new String(src));
		duplicate.get(src);
		System.out.println(new String(src));
	}
}
