import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

class ProxyMain {
	
	private static int portReceiver = 9000;
	private static int portWRReceiver = 9001;
	private static int portSend = 10000;
	private static InetAddress ipSend;

	private static InetAddress myIp;
	
	private static DatagramSocket proxyServerSocket;
	private static DatagramSocket wrServerSocket;
	
	private static List<Map> maps = new ArrayList<>();
	private static List<Machine> machines = new ArrayList<>();
	private static List<MediaServer> mediaServers = new ArrayList<>();
	
	private static byte[] wrData = new byte[1024];
	private static DatagramPacket wrPacket;
	private static String entrada = "";
	private static Machine machine;
	
	private static byte[] receiveData = new byte[1024];


	public static void main(String args[]) throws Exception {
		myIp = InetAddress.getByName("localhost");
		ipSend = InetAddress.getByName("localhost");
		
//		if(args.length == 1){
//			myIp = InetAddress.getByName(args[0]);
//		}
		if(args.length == 1){
			portReceiver = Integer.valueOf(args[0]);
		}
		if(args.length == 2){
			ipSend = InetAddress.getByName(args[1]);
		}
		if(args.length == 3){
			portSend = Integer.valueOf(args[2]);
		}
		if(args.length == 4){
			portWRReceiver = Integer.valueOf(args[3]);
		}
		
		wrServerSocket = new DatagramSocket(portWRReceiver);
		proxyServerSocket = new DatagramSocket(portReceiver);
		
		Thread workloadThread = new Thread(){
		    public void run(){
		    	System.out.println("waiting..");
		    	start = Instant.now();
		    	while(true){
		    		proxy();
		    	}
		    }
		};
		
		
		Thread proxyFunction = new Thread(){
		    public void run(){
		    	while(true){
		    		receiveWorkload();
		    	}
		    }
		};
	
		workloadThread.start();
		proxyFunction.start();
			
	}
	
	
	public static void receiveWorkload(){
		try {
			
			
			wrData = new byte[1024];
			wrPacket = new DatagramPacket(wrData, wrData.length);
			wrServerSocket.receive(wrPacket);
			entrada = new String(wrPacket.getData());
			machine = new Machine(wrPacket.getAddress(), wrPacket.getPort());
			int indexMachine = machines.indexOf(machine);
			if(indexMachine != -1){
				System.out.println(entrada);
				int workload = Double.valueOf(entrada).intValue();
				machines.get(indexMachine).setWorkload(workload);
				
				System.out.println("[RECEIVE] "+wrPacket.getAddress()+":"+wrPacket.getPort()+" -> "+workload);
			}else{
				machines.add(machine);
				System.out.println("Create new machine: "+wrPacket.getAddress()+":"+wrPacket.getPort());
			}
			
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.err.println("[ERROR] Workload inválido! ");
//			System.err.println("[ERROR] Workload inválido: " + entrada);
//			e.printStackTrace();
		}
	}
	
	private static DatagramSocket clientSocket;
	private static DatagramPacket receivePacket;
	private static Instant start;
	private static double soma = 0;
	
	public static void proxy(){
		
		try {
			
			receiveData = new byte[2048];
			clientSocket = new DatagramSocket();
			

			if(Duration.between(start, Instant.now()).toMillis() >= 1000){
				start = Instant.now();
				System.out.println(((soma/1024.0)*8) + "Kbs");
				soma = 0;
			}
			
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			proxyServerSocket.receive(receivePacket);
			receivePacket.setPort(portSend);
			receivePacket.setAddress(ipSend);
			soma += receivePacket.getLength();
//			new DatagramPacket(sendData, sendData.length, ipSend, 9001);
			clientSocket.send(receivePacket);
			
		} catch (SocketException e) {
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
}