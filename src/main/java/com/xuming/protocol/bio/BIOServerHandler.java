package com.xuming.protocol.bio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class BIOServerHandler implements Runnable{

	private Socket client;
	
	private BufferedReader reader;
	
	private BufferedWriter writer;
	public BIOServerHandler(Socket client) {
		this.client = client;
		
	}
	
	@Override
	public void run() {
		try {
			OutputStream outputStream = client.getOutputStream();
			InputStream inputStream = client.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
			while (true) {
				String line = reader.readLine();
				log.info("收到客户端信息:" + line);
				if ("exit".equals(line)) {
					log.info("客户端断开连接");
					break;
				}
				writer.write("response:" + line + "\r\n");
				writer.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(client!=null) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
