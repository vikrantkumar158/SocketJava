import java.net.*;
import java.io.*;
public class ClientChat implements Runnable
{
	int clientport=2000,serverport=3000;
	DatagramSocket ds;
	Thread recThread;
	ClientChat()throws Exception
	{
		ds=new DatagramSocket(clientport);
		recThread=new Thread(this);
		recThread.start();
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		String data="Client is online";
		DatagramPacket dp=new DatagramPacket(data.getBytes(),data.length(),InetAddress.getLocalHost(),serverport);
		ds.send(dp);
		System.out.println(ds.getPort());
		while(true)
		{
			data=br.readLine();
			dp=new DatagramPacket(data.getBytes(),data.length(),InetAddress.getLocalHost(),serverport);
			ds.send(dp);
			if(data.equalsIgnoreCase("exit"))
				break;
		}
		ds.close();
		System.exit(0);
	}
	public void run()
	{
		byte b[]=new byte[1000];
		while(true)
		{
			try{
				DatagramPacket dp=new DatagramPacket(b,b.length);
				ds.receive(dp);
				String data=new String(b,0,dp.getLength());
				if(data.equalsIgnoreCase("exit"))
					System.out.println("Server Exited");
				else
				System.out.println("Server : "+data);
			}
			catch(Exception e)
			{

			}
		}
	}
	public static void main(String args[])throws Exception
	{
		new ClientChat();
	}
}