package com.xuming.protocol.bio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BIOClient implements Runnable{

	private String host;
	private int port;
	private BufferedReader reader;
	
	private BufferedWriter writer;
	private Socket socket;
	
	/**
	 * @param host
	 * @param port
	 */
	public BIOClient(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	public void init() throws IOException {
		socket = new Socket();
		SocketAddress address = new InetSocketAddress(host, port);
		socket.setKeepAlive(true);
		socket.connect(address);
		if(socket.isConnected())  {
			OutputStream outputStream = socket.getOutputStream();
			InputStream inputStream = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
		}else {
			throw new RuntimeException("连接失败");
		}
	}
	@Override
	public void run() {
		while (true) {
			try {
				String readLine = reader.readLine();
				log.info("收到服务端响应：" + readLine);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void send(String msg) throws IOException {
		writer.write(msg+"\r\n");
		writer.flush();
	}

	public static void main(String[] args) throws IOException {
		BIOClient sender = new BIOClient("127.0.0.1", 8080);
		sender.init();
		new Thread(sender).start();
		Scanner scan = new Scanner(System.in);
		while (scan.hasNextLine()) {
			String nextLine = scan.nextLine();
			if ("exit".equals(nextLine)) {
				System.exit(0);
			}
			sender.send(nextLine);
		}
	}
}
