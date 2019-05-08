package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class GateWayThread extends Thread {
	private Socket socket;
	private String username;
	private BufferedReader cin;
	private PrintStream cout;

	private SimpleDateFormat sdf;
	private int phut; // Gui theo phut
	private String message;
	private Scanner input;

	public GateWayThread(Socket socket) {
		this.socket = socket;
	}

	public void close() {
		try {
			if (socket != null)
				socket.close();
		} catch (Exception e) {
		}
	}

	public void display(String s) {
		System.out.println(s);
	}

	public void run() {
		System.out.println("Processing: " + socket.getPort());
		try {
			BufferedReader cin = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Nhan du lieu tu
																										// client gui
																										// len
			PrintStream cout = new PrintStream(socket.getOutputStream()); // Gui du lieu ve client

			// Nhan username
			username = cin.readLine();
			cout.println("Đã nhận username");

			System.out.println("Nhập vào thời gian GateWay gửi đến (ms): ");
			input = new Scanner(System.in);
			cout.println(input.nextInt()); // Gui ve client

			while (true) {
				message = cin.readLine(); // Doc tu client
				display("GateWay_" + socket.getPort() + "_" + username + " : " + message);

				Phantich(message);

				cout.println("Đã nhận dữ liệu.");
			}

		} catch (IOException e) {
			//System.err.println("Request Processing Error: " + e);
		}

		System.out.println("Complete processing: " + socket);
	}

	private void Phantich(String mes) {
		// TODO Auto-generated method stub
		// mes: time client nhietdo-time client nhietdo-....
		try {
			String[] str = mes.split("#");

			String[] strTemp = str[0].split(" ");
			if (strTemp.length == 2)
				return;

			for (int i = 0; i < str.length; i++) {
				String[] str_n = str[i].split(" ");
				Integer t = Integer.parseInt(str_n[str_n.length-1]);
				if (t > 30) {
					if(i == 0) {
						System.out.println("Cảnh báo! Client " + str_n[3] + " có nhiệt độ cao " + t.toString() + " độ. Thời điểm " + str_n[2]);
					}
					else {
						System.out.println("Cảnh báo! Client " + str_n[1] + " có nhiệt độ cao " + t.toString() + " độ. Thời điểm " + str_n[0]);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
