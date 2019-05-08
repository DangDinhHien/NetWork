package main;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	private ExecutorService executor;
	private ServerSocket serverSocket;
	
	public static final int NUM_OF_THREAD = 4;
    public final static int SERVER_PORT = 7;
    
    
    public void Start() {
    	executor = Executors.newFixedThreadPool(NUM_OF_THREAD);
        serverSocket = null;
        try {
            System.out.println("Binding to port " + SERVER_PORT + ", please wait  ...");
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server started: " + serverSocket);
            System.out.println("Waiting for a GateWay ...");
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("GateWay accepted: " + socket);
 
                    GateWayThread handler = new GateWayThread(socket);
                    
                    executor.execute(handler);
                } catch (IOException e) {
                    System.err.println(" Connection Error: " + e);
                }
            }
        } catch (IOException e1) {
            //e1.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
					serverSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
            }
        }
    }
 
    public static void main(String[] args) throws IOException {
        Server sv = new Server();
        sv.Start();
        
        
    }
}

