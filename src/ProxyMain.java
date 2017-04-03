import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.time.Duration;
import java.time.Instant;

class ProxyMain {

	
	private static DatagramSocket serverSocket;

	public static void main(String args[]) throws Exception {
		
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
		clientSocket.close();
	}
	
	
}