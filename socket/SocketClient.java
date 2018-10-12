package com.sobey.service.impl;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;


@Service
public class SocketClient {
	public static void main(String[] args) throws Exception {

		Scanner scanner = new Scanner(System.in);
		System.out.println("请输入数据： " );
		String input = scanner.nextLine();
		System.out.println("客户输入数据： " +input);
		Socket ss = new Socket("127.0.0.1",6666);
		OutputStream os = ss.getOutputStream();
		os.write(input.getBytes());
		InputStream is = ss.getInputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		len = is.read(buffer);
		String date = new String(buffer,0,len);
		System.out.println("服务端传回的数据： "+date);
		is.close();
		os.close();
		ss.close();
	}
}
