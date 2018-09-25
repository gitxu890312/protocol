package com.xuming.protocol.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author xuming
 * 阻塞io服务端
 */
@Slf4j
public class BIOServer implements Runnable{

	private ServerSocket serverSocker;
	
	private int port = 8888;
	private ExecutorService threadPool;
	public BIOServer(int port) {
		this.port = port;
		threadPool = Executors.newCachedThreadPool();
	}
	public void init() throws IOException {
		serverSocker = new ServerSocket(port);
		//serverSocker.setSoTimeout(2000);设置accept超时时间
	}
	@Override
	public void run() {
		log.info("BIOServer start port=" + port);
		while (true) {
			try {
				Socket client = serverSocker.accept();
				log.info("客户端接入：address=" + client.getInetAddress().getHostAddress() + ",port=" + client.getPort());
				threadPool.execute(new BIOServerHandler(client));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) throws IOException {
		BIOServer bioServer = new BIOServer(8080);
		bioServer.init();
		new Thread(bioServer).start();
	}
}
