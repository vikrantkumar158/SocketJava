import java.net.*;
import java.io.*;
import java.util.*;
public class ServerChat implements Runnable
{
	int serverport=3000;
	DatagramSocket ds;
	Thread recThread;
	HashMap<String,Integer> map=new HashMap<>();
	ServerChat()throws Exception
	{
		ds=new DatagramSocket(serverport);
		recThread=new Thread(this);
		recThread.start();
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		for(Map.Entry<String,Integer> m:map.entrySet())
		{
			String data="Server Started";
			DatagramPacket dp=new DatagramPacket(data.getBytes(),data.length(),InetAddress.getLocalHost(),m.getValue());
			ds.send(dp);
		}
		while(true)
		{
			String data=br.readLine();
			String temp=data;
			data="Server: "+data;
			for(Map.Entry<String,Integer> m:map.entrySet())
			{
				DatagramPacket dp=new DatagramPacket(data.getBytes(),data.length(),InetAddress.getLocalHost(),m.getValue());
				ds.send(dp);
			}
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
				int x=data.indexOf(" ");
				String senderName=data.substring(0,x);
				int senderPort=Integer.parseInt(data.substring(x+1,data.indexOf(" ",x+1)));
				x=data.indexOf(" ",x+1);
				data=data.substring(x+1);
				if(!map.containsKey(senderName))
				{
					System.out.println(senderName+" "+senderPort+" Connected");
					map.put(senderName,senderPort);
					data="Connected";
					dp=new DatagramPacket(data.getBytes(),data.length(),InetAddress.getLocalHost(),senderPort);
					ds.send(dp);
				}	
				else if(data.equalsIgnoreCase("exit"))
				{
					System.out.println(senderName+" "+map.get(senderName)+" Disconnected");
					map.remove(senderName);
				}
				else if(data.substring(0,3).equalsIgnoreCase("SND"))
				{
					data=data.substring(4);
					String receiverName=data.substring(0,1);
					data=senderName+": "+data.substring(2);
					if(map.containsKey(receiverName))
					{
						dp=new DatagramPacket(data.getBytes(),data.length(),InetAddress.getLocalHost(),map.get(receiverName));
						ds.send(dp);
					}
					else
					{
						data="Error: Unable to send message. User offline.";
						dp=new DatagramPacket(data.getBytes(),data.length(),InetAddress.getLocalHost(),senderPort);
						ds.send(dp);
					}
				}
				else if(data.equalsIgnoreCase("LIVE"))
				{
					for(Map.Entry<String,Integer> m:map.entrySet())
					{
						data=m.getKey()+" "+Integer.toString(m.getValue());
						if(m.getKey().equals(senderName)&&m.getValue()==senderPort)
							data=data+" "+"(You)";
						dp=new DatagramPacket(data.getBytes(),data.length(),InetAddress.getLocalHost(),senderPort);
						ds.send(dp);
					}
				}
				else
				{
					System.out.println(senderName+": "+data);
				}
			}
			catch(Exception e)
			{

			}
		}
	}
	public static void main(String args[])throws Exception
	{
		new ServerChat();
	}
}