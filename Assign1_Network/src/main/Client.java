package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
 
public class Client {
    public final static String SERVER_IP = "127.0.0.1";
    public final static int SERVER_PORT = 8; // Cổng mặc định của GateWay
    public final static byte[] BUFFER = new byte[4096]; // Vùng đệm chứa dữ liệu cho gói tin nhận
 
    public static void main(String[] args) {
    	String username;
    	Integer thoigian;
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	DatagramPacket incoming;
    	DatagramPacket dp;
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(); // Tạo DatagramSocket
            
            InetAddress server = InetAddress.getByName(SERVER_IP);	// Client đã sẵn sàng.
            
            System.out.println("Nhập vào tên Client:");
            InputStreamReader input = new InputStreamReader(System.in); // Nhập
            BufferedReader brtemp = new BufferedReader(input); // một chuỗi
            String stemp = brtemp.readLine(); // từ bàn phím
            username = stemp;
            
            byte[] d = stemp.getBytes(); // Đổi chuỗi ra mảng bytes
            dp = new DatagramPacket(d, d.length, server, SERVER_PORT);
            ds.send(dp); // Send gói tin (Tên client) sang GateWay
            
            // Gói tin nhận
            incoming = new DatagramPacket(BUFFER, BUFFER.length);
            ds.receive(incoming); // Chờ nhận dữ liệu từ GateWay gởi về
            
            //// Nhập thời gian client gửi
            Scanner scan = new Scanner(System.in);
            System.out.println("Nhập vào thời gian Client gửi đến (ms):");
            thoigian = scan.nextInt();
            
            while (true) {
            	
            	Random ran = new Random();
            	int value = ran.nextInt(41);
            	String theString = sdf.format(new Date()) + " " + username + " " + value; // prototol
            	byte[] data = theString.getBytes(); // Đổi chuỗi ra mảng bytes
            	
            	// Tạo gói tin gởi
				dp = new DatagramPacket(data, data.length, server, SERVER_PORT);
              	ds.send(dp); // Send gói tin sang GateWay

              	// Gói tin nhận
              	incoming = new DatagramPacket(BUFFER, BUFFER.length);
              	ds.receive(incoming); // Chờ nhận dữ liệu từ GateWay
				
				try {
					Thread.sleep(thoigian);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            }
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            if (ds != null) {
                ds.close();
            }
        }
    }
}