import java.net.*;
import java.io.*;
public class ClientChat2 implements Runnable
{
	int clientport=2002,serverport=3000;
	DatagramSocket ds;
	Thread recThread;
	ClientChat2()throws Exception
	{
		ds=new DatagramSocket(clientport);
		recThread=new Thread(this);
		recThread.start();
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		String data="B "+Integer.toString(clientport)+" ";
		DatagramPacket dp=new DatagramPacket(data.getBytes(),data.length(),InetAddress.getLocalHost(),serverport);
		ds.send(dp);
		while(true)
		{
			data=br.readLine();
			String temp=data;
			data="B "+Integer.toString(clientport)+" "+data;
			dp=new DatagramPacket(data.getBytes(),data.length(),InetAddress.getLocalHost(),serverport);
			ds.send(dp);
			if(temp.equalsIgnoreCase("exit"))
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
					System.out.println("Server Closed");
				else
				System.out.println(data);
			}
			catch(Exception e)
			{

			}
		}
	}
	public static void main(String args[])throws Exception
	{
		new ClientChat2();
	}
}