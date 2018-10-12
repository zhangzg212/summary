package com.sobey.mhq.cctv.assist.service.impl;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


@Service
public class SocketServer {
	public static void main(String[] args) throws Exception {
		ServerSocket ss = new ServerSocket(6666);
		System.out.println("监听客户端数据，监听端口6666: ");
		boolean flag = true;
		while (flag){
			Socket sc = ss.accept();
			InputStream is = sc.getInputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			len = is.read(buffer);
			String date = new String(buffer,0,len);
			System.out.println("接收到数据： "+date);
			String outDate = date.toUpperCase();
			OutputStream os = sc.getOutputStream();
			os.write(outDate.getBytes("UTF-8"));
			os.close();
			is.close();
			sc.close();
		}
		ss.close();
	}
}
