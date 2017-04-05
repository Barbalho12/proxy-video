import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

class ProxyMain {

	
	private static DatagramSocket serverSocket;
	private static DatagramSocket wrServerSocket;
	
	static List<Map> maps = new ArrayList<>();
	static List<Machine> machines = new ArrayList<>();
	static List<MediaServer> mediaServers = new ArrayList<>();

	public static void main(String args[]) throws Exception {
		wrServerSocket = new DatagramSocket(9001);
		
		Thread workloadThread = new Thread(){
		    public void run(){
		    	while(true){
		    		receiveWorkload();
		    	}
		    }
		};
	
//		workloadThread.start();
		workloadThread.run();
//		Instant end = Instant.now();
//		Duration timeElapsed = Duration.between(start, end);
//		System.out.println("Time taken: "+ timeElapsed.toMillis() +" milliseconds");
		
		int limit = 10000;
		
		serverSocket = new DatagramSocket(9000);
		byte[] receiveData = new byte[2048];
		DatagramSocket clientSocket = new DatagramSocket();
		DatagramPacket receivePacket;
		Instant start = Instant.now();
		
		double soma = 0;	
		while (limit-- > 0) {
			if(Duration.between(start, Instant.now()).toMillis() >= 1000){
				start = Instant.now();
				System.out.println(((soma/1024.0)*8) + "Kbs");
				soma = 0;
			}
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			receivePacket.setPort(10000);
//			System.out.println(receivePacket.getAddress() +" : " +receivePacket.getPort() +"  ->  " + receivePacket.getSocketAddress() );
			soma += receivePacket.getLength();
			clientSocket.send(receivePacket);
		}
		workloadThread.join();
		clientSocket.close();
	}
	
	
	static byte[] wrData = new byte[1024];
	static DatagramPacket wrPacket;
	static String entrada = "";
	static Machine machine;
	public static void receiveWorkload(){
		try {
			System.out.println("wait");
			
			wrData = new byte[1024];
			wrPacket = new DatagramPacket(wrData, wrData.length);
			wrServerSocket.receive(wrPacket);
			entrada = new String(wrPacket.getData());
			machine = new Machine(wrPacket.getAddress(), wrPacket.getPort());
			int indexMachine = machines.indexOf(machine);
			if(indexMachine != -1){
				int workload = Double.valueOf(entrada).intValue();
				machines.get(indexMachine).setWorkload(workload);
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
			System.err.println(entrada);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}