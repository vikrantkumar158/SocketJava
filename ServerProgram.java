import java.net.*;
import java.io.*;
public class ServerProgram
{
	int clientport=2000,serverport=3000;
	DatagramSocket ds;
	ServerProgram()throws Exception
	{
		ds=new DatagramSocket(serverport);
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		while(true)
		{
			String data=br.readLine();
			DatagramPacket dp=new DatagramPacket(data.getBytes(),data.length(),InetAddress.getLocalHost(),clientport);
			ds.send(dp);
			if(data.equalsIgnoreCase("exit"))
				break;
		}
		ds.close();
	}
	public static void main(String args[])throws Exception
	{
		new ServerProgram();
	}
}