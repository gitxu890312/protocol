package com.xuming.protocol.channel;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.SortedMap;

import org.junit.Test;

public class CharsetTest {

	@Test
	public void test() {
		Charset defaultCharset = Charset.defaultCharset();
		System.out.println(defaultCharset);
		
		SortedMap<String, Charset> charsets = Charset.availableCharsets();
		charsets.forEach((x,y)->System.out.println(x+":"+y));
	
	}
	@Test
	public void test1() throws CharacterCodingException {
		Charset forName = Charset.forName("GBK");
		CharsetEncoder newEncoder = forName.newEncoder();
		CharsetDecoder newDecoder = forName.newDecoder();
		
		CharBuffer charBuffer = CharBuffer.allocate(1024);
		charBuffer.put("编解码测试");
		charBuffer.flip();
		System.out.println(charBuffer);
		
		ByteBuffer encode = newEncoder.encode(charBuffer);
		for(int i=0;i<encode.limit();i++) {
			System.out.println(encode.get());;
		}
		encode.rewind();//重新读取
		CharBuffer decode = newDecoder.decode(encode);
		System.out.println(decode);
		encode.rewind();//重新读取
		
		Charset gbk = Charset.forName("UTF-8");
		CharBuffer gbkDecode = gbk.decode(encode);
		System.out.println(gbkDecode);
	}
}
