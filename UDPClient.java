import java.io.*;
import java.net.*;
import java.time.Duration;
import java.time.Instant;

class UDPClient {
	
	private static int portReceiver = 10000;
	private static int portSend = 9001;
	private static InetAddress ipSend;
	
	static DatagramSocket serverSocket;
	static DatagramSocket clientSocket;
	
	static DatagramPacket sendPacket;
	static DatagramPacket receivePacket;
	
	static byte[] sendData = new byte[1024];
	static byte[] receiveData = new byte[2048];
	
	static int workloadValue = 0;
	
//	static InetAddress IPAddress;
	
	static Instant start;
	static double soma = 0;
	
	public static void main(String args[]) throws Exception {
		ipSend = InetAddress.getByName("10.0.0.1");
		
		if(args.length == 1){
			portReceiver = Integer.valueOf(args[0]);
		}
		if(args.length == 2){
			ipSend = InetAddress.getByName(args[1]);
		}
		if(args.length == 3){
			portSend = Integer.valueOf(args[2]);
		}

		
		
		serverSocket = new DatagramSocket(portReceiver);
		clientSocket = new DatagramSocket();
		
		
		Thread workloadThread = new Thread(){
		    public void run(){
		    	while(true){
		    		try {
		    			
		    			sendWr();
						sleep(1000);
						
					} catch (InterruptedException | IOException e) {
						e.printStackTrace();
					} 
		    	}
		    }
		};
		
		Thread mediaThread = new Thread(){
		    public void run(){
		    	start = Instant.now();
		    	while(true){
		    		try {
						receive();
					} catch (IOException e) {
						e.printStackTrace();
					}
		    	}
		    }
		};
		
		workloadThread.start();
		mediaThread.start();
		

	}
	
	static void receive() throws IOException{
		
			if(Duration.between(start, Instant.now()).toMillis() >= 1000){
				start = Instant.now();
				double bitrate = (soma/1024.0)*8;
				System.out.println( bitrate + "Kbs");
				soma = 0;
				workloadValue = (int) bitrate;
			}
			
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			
			soma += receivePacket.getLength();
	}
	
	static void sendWr() throws IOException{
			sendData = String.valueOf(workloadValue).getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, ipSend, portSend);
		
			clientSocket.send(sendPacket);
			System.out.println("[SEND] " + ipSend+":" + portSend + " -> " + workloadValue);
			workloadValue = 0;
	}
	
}
