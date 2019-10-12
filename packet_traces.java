import java.util.*;
import java.io.*;

public class packet_traces
{
	List<packet> all_packets;
	List<tcp_connection> tcp_packets;

	packet_traces()
	{
		all_packets = new ArrayList<packet>();
		tcp_packets = new ArrayList<tcp_connection>();
	}

	// extract syn packet for 3 way hand shaked packets
	void extract_tcp_packets()
	{
		int all_packets_size=all_packets.size();
		// traversing through all packets
		for(int i=0; i<all_packets_size; i++)
		{
			packet p = all_packets.get(i);
			if(p.check_protocol("TCP"))
			{
				String[] parts_info1 = p.info.split(" ");
				List<String> parts_info = new ArrayList<String>();
				for(int j=0; j<parts_info1.length; j++)
				{
					if(parts_info1[j].equals("") || parts_info1[j] == null)  continue;
					else parts_info.add(parts_info1[j]);
				}
				if(parts_info.get(3).equals("[SYN]"))
				{
					String s_ip = p.destination_ip, c_ip = p.source_ip;
					int s_port, c_port;
					s_port = Integer.parseInt(parts_info.get(2));
					c_port = Integer.parseInt(parts_info.get(0));
					for(int j=i+1; j< all_packets_size; j++)
					{
						packet p1= all_packets.get(j);
						if(p1.check_protocol("TCP"))
						{
							String[] parts_info_temp1 = p1.info.split(" ");
							List<String> parts_info_temp = new ArrayList<String>();
							for(int k=0; k<parts_info_temp1.length; k++)
							{
								if(parts_info_temp1[k].equals("") || parts_info_temp1[k] == null)  continue;
								parts_info_temp.add(parts_info_temp1[k]);
							}
							String s_ip_temp = p1.destination_ip, c_ip_temp=p1.source_ip;
							int s_port_temp, c_port_temp;
							s_port_temp = Integer.parseInt(parts_info_temp.get(2));
							c_port_temp = Integer.parseInt(parts_info_temp.get(0));
							if(s_port == s_port_temp && c_port == c_port_temp && s_ip_temp.equals(s_ip) && c_ip_temp.equals(c_ip) )
							{
								if(parts_info_temp.get(3).equals("[ACK]"))
								{
									//add to tcp_packets
									tcp_connection new_connection = new tcp_connection(s_ip, c_ip, s_port, c_port, p.time);
									tcp_packets.add(new_connection);
									break;
								}
								else if(parts_info_temp.get(3).equals("[SYN]"))
								{
									break;
								}
							}
						}
					}
				}
			}
		}
		//Updating the end times of tcp connections
		put_end_times();
	}

	//int c=0,c1=0, c2=0,c3=0;

	//function for updating end times
	void put_end_times()
	{
		int all_packets_size = all_packets.size();
		for(int i=all_packets_size-1; i>=0; i--)
		{
			packet p = all_packets.get(i);
			if(p.check_protocol("TCP"))
			{
				String[] parts_info1 = p.info.split(" ");
				List<String> parts_info = new ArrayList<String>();
				for(int j=0; j<parts_info1.length; j++)
				{
					if(parts_info1[j].equals("") || parts_info1[j] == null)  continue;
					else parts_info.add(parts_info1[j]);
				}
				if(parts_info.get(3).equals("[FIN,") || parts_info.get(3).equals("[RST]") || parts_info.get(3).equals("[RST,"))
				{
					//c3++;
					String s_ip = p.source_ip, d_ip = p.destination_ip;
					int s_port, d_port;
					s_port = Integer.parseInt(parts_info.get(0));
					d_port = Integer.parseInt(parts_info.get(2));
					if(s_port == 21)
					{
						//c1++;
						close_tcp_packet(s_ip, d_ip, s_port, d_port, p.time, i);
					}
					else if(d_port == 21)
					{
						//c2++;
						close_tcp_packet(d_ip, s_ip, d_port, s_port, p.time, i);
					}
					else
					{
						System.out.println("Some mistake 1");
					}
				}
			}
		}
	}

//Find the corrsponding tcp_connection and updating end time in it
	void close_tcp_packet(String server_ip, String client_ip, int server_port, int client_port, double time, int index)
	{
		for(int i=tcp_packets.size()-1; i>=0; i--)
		{
			tcp_connection tcp = tcp_packets.get(i);
			if(tcp.t_close > 0.0)
			{
				continue;
			}
			else if(tcp.server_ip.equals(server_ip) && tcp.client_ip.equals(client_ip) && tcp.server_port == server_port && tcp.client_port == client_port)
			{
				tcp.t_close = time;
				//c++;
				return;
			}
		}
		//System.out.println(time + " "+(index+1)+ " Some mistake 3");
	}

	// print duration of each tcp flow into text file to draw cdf
	void cdf_of_durations()
	{
		try
		{
			// creating a file: historgram_tcp_connections.txt
			FileWriter fw = new FileWriter("durations.txt");
			// writing tcp connection opening time
			int tcp_packets_size = tcp_packets.size();
			for(int i=0; i<tcp_packets_size; i++)
				if(tcp_packets.get(i).t_close != -1)
					fw.write(Double.toString(tcp_packets.get(i).t_close - tcp_packets.get(i).t_open) + "\n");
			fw.flush();
			fw.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	void add(packet p)
	{
		all_packets.add(p);
	}
	void print()
	{
		System.out.println("number of packets: " + all_packets.size());
	}
}
