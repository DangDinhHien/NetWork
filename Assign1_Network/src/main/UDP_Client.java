package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

public class UDP_Client extends Thread{
	public String message_ser = "";
	
	private Thread t;
	private String threadName;
	private DatagramSocket ds;
	private DatagramPacket dp;
	
    public final static int SERVER_PORT = 8;
    public final static byte[] BUFFER = new byte[4096];	
	
	public UDP_Client(String name) {
		this.threadName = name;
	}
	
	
	public void run() {
		DatagramSocket ds = null;
	    try {
	        System.out.println("Binding to port " + SERVER_PORT + ", please wait  ...");
	        ds = new DatagramSocket(SERVER_PORT); // Tạo Socket với cổng là 8
	        System.out.println("GateWay started ");
	        System.out.println("Waiting for messages from Client ... ");
	        
	        DatagramPacket temp = new DatagramPacket(BUFFER, BUFFER.length);
            ds.receive(temp); // Chờ nhận gói tin gởi đến (Username client)
            
            // Lấy dữ liệu khỏi gói tin nhận
            String scli = new String(temp.getData(), 0, temp.getLength());
            System.out.println("Đã nhận Client: " + scli);

            // Tạo gói tin gởi chứa dữ liệu vừa nhận được
            // Gửi lại cho client
            DatagramPacket temp2 = new DatagramPacket(scli.getBytes(), temp.getLength(),
                    temp.getAddress(), temp.getPort());
            ds.send(temp2);
            
	        while (true) { // Tạo gói tin nhận
	            DatagramPacket incoming = new DatagramPacket(BUFFER, BUFFER.length);
	            ds.receive(incoming); // Chờ nhận gói tin gởi đến

	            // Lấy dữ liệu khỏi gói tin nhận
	            String message = new String(incoming.getData(), 0, incoming.getLength());
	            System.out.println("Received: " + message);
	            if(message_ser == "") {
	            	message_ser = message;
	            }
	            else {
	            	message_ser += "#" + message;
	            }
	            
	            

	            // Tạo gói tin gởi chứa dữ liệu vừa nhận được
	            DatagramPacket outsending = new DatagramPacket(message.getBytes(), incoming.getLength(),
	                    incoming.getAddress(), incoming.getPort());
	            ds.send(outsending);
	        }
	    } catch (IOException e) {
	        //e.printStackTrace();
	    } finally {
	        if (ds != null) {
	            ds.close();
	        }
	    
	    }
	}
	
	public void start() {
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
	
}
