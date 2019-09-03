import java.net.*;
import java.io.*;
public class ClientProgram implements Runnable
{
	int clientport=2000,serverport=3000;
	DatagramSocket ds;
	Thread recThread;
	ClientProgram()throws Exception
	{
		ds=new DatagramSocket(clientport);
		recThread=new Thread(this);
		recThread.start();
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
					break;
				System.out.println("Server : "+data);
			}
			catch(Exception e)
			{

			}
		}
		ds.close();
	}
	public static void main(String args[])throws Exception
	{
		new ClientProgram();
	}
}