package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class GateWay {
	public final static byte[] BUFFER = new byte[4096]; // Vùng đệm chứa dữ liệu cho gói tin nhận

	private String username;
	private Socket socket;
	private BufferedReader sin;	// Nhan du lieu tu server
	private PrintStream sout;	// Gui du lieu len server
	private SimpleDateFormat sdf;
	private Integer thoigian;

	public final static String SERVER_IP = "localhost";
	public final static int SERVER_PORT = 7;

	public void Start() throws InterruptedException {

		socket = null;

		System.out.println("Nhap vao username GateWay: ");
		Scanner sc = new Scanner(System.in);
		username = sc.nextLine();

		try {

			socket = new Socket(SERVER_IP, SERVER_PORT); // Connect to server
			System.out.println("Connected: " + socket);

			sdf = new SimpleDateFormat("HH:mm:ss"); // Thoi gian
			
			sin = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Nhận dữ liệu
			sout = new PrintStream(socket.getOutputStream()); // Gửi dữ liệu
			
			String s;

			/// Gui username cho sever
			sout.println(username);
			s = sin.readLine();
			// System.out.println(s);
			//////
			/// Nhan thoi gian de truyen du lieu
			s = sin.readLine();
			thoigian = Integer.parseInt(s);

			// Mo GateWay cho client ket noi den.
			UDP_Client th = new UDP_Client("client");
			th.start();

			//// Gui du lieu tu gateway len server
			while (true) {
				String time = sdf.format(new Date()) + " " + username + " " + th.message_ser; // Protocol
				sout.println(time);
				s = sin.readLine();

				System.out.println("Gửi đến Server: " + th.message_ser); // Mes cua client
				th.message_ser = "";

				Thread.sleep(thoigian); // 10s

			}

		} catch (IOException ie) {
			System.out.println("Can't connect to server");
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public static void main(String[] args) throws IOException, InterruptedException {
		GateWay a = new GateWay();
		a.Start();
	}
}