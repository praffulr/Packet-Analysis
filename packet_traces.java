import java.util.*;
import java.io.*;

public class packet_traces
{
	List<packet> all_packets;
	List<tcp_connection> tcp_packets;
	List<String> unique_client_ips, unique_server_ips;
	List<Double> client_to_server_packets_time, server_to_client_packets_time;
	List<Integer> client_to_server_packet_lengths, server_to_client_packet_lengths;

	packet_traces()
	{
		all_packets = new ArrayList<packet>();
		tcp_packets = new ArrayList<tcp_connection>();
		unique_client_ips = new ArrayList<String>();
		unique_server_ips = new ArrayList<String>();
		client_to_server_packets_time = new ArrayList<Double>();
		server_to_client_packets_time = new ArrayList<Double>();
		client_to_server_packet_lengths = new ArrayList<Integer>();
		server_to_client_packet_lengths = new ArrayList<Integer>();
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
				if(	parts_info.get(3).equals("[SYN]")|| 
					(parts_info.get(3).equals("[SYN,") && !parts_info.get(4).equals("ACK]")))
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
									tcp_connection new_connection = new tcp_connection(s_ip, c_ip, s_port, c_port, p.time, i+1);
									tcp_packets.add(new_connection);
									break;
								}
								else if(parts_info_temp.get(3).equals("[SYN]") || 
									(parts_info_temp.get(3).equals("[SYN,") && !parts_info_temp.get(4).equals("ACK]")))
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
				tcp.end_no = index+1;
				//c++;
				return;
			}
		}
		//System.out.println(time + " "+(index+1)+ " Some mistake 3");
		// modifing unique client and server ips: FIN packets exist but SYN packets not captured	
		if(! unique_client_ips.contains(client_ip)) 		unique_client_ips.add(client_ip);		// ###
		if(! unique_server_ips.contains(server_ip))		unique_server_ips.add(server_ip);		// ###
	}

	// extracting unique server ips and client ips 					// ###
	void extract_unique_server_client_ips()
	{
		int tcp_packets_size = tcp_packets.size();
		// traversing through all unique tcp packets
		for(int i=0; i< tcp_packets_size; i++)
		{
			// getting tcp packet header content
			tcp_connection tcp_packet = tcp_packets.get(i);
			String server_ip = tcp_packet.server_ip, client_ip = tcp_packet.client_ip;
			int unique_client_ips_size = unique_client_ips.size(), unique_server_ips_size = unique_server_ips.size();
			boolean client_is_unique = true, server_is_unique = true;
			//// searching for ips in existing list
			// for client ips
			if(! unique_client_ips.contains(client_ip)) 		unique_client_ips.add(client_ip);
			if(! unique_server_ips.contains(server_ip))		unique_server_ips.add(server_ip);
		}
	}
	// get histogram of # of connections opened to any server 		// ###
	void histogram_tcp_connections()
	{
		try
		{
			// creating a file: historgram_tcp_connections.txt
			FileWriter fw = new FileWriter("histogram_tcp_connections.txt");
			// writing tcp connection opening time
			int tcp_packets_size = tcp_packets.size();
			for(int i=0; i<tcp_packets_size; i++)
				fw.write(Double.toString(tcp_packets.get(i).t_open/3600) + "\n");
			fw.flush();
			fw.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
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
	// updating data transfered over each tcp flow
	void update_cdf_infos()
	{
		// considering each tcp flow 
		int tcp_packets_size = tcp_packets.size(), all_packets_size = all_packets.size();
		for(int i=0; i<tcp_packets_size; i++)
		{
			tcp_connection tcp_packet = tcp_packets.get(i);
			// check if it is a complete tcp flow
			if(tcp_packet.check_complete_flow())
			{
				// finding packets belonging to the same tcp_packet
				// searching in the original database 
				for(int j=tcp_packet.start_no-1; j<all_packets_size; j++)
				{
					// checking time constraints
					packet p = all_packets.get(j);
					if(p.time >= tcp_packet.t_open && p.time <= tcp_packet.t_close)
					{
						// check for tcp protocol
						if(p.check_protocol("TCP"))
						{
							// check if packet belongs to same tcp flow or not
							// checking for client to server packet
							// syn packet is client to server packet
							if(	p.client_to_server && 
								p.source_ip.equals(tcp_packet.client_ip) &&
							 	p.destination_ip.equals(tcp_packet.server_ip) && 
								p.source_port == tcp_packet.client_port && 
								p.destination_port == tcp_packet.server_port)
							{
								// incoming packet: client to server
								// modify: incoming time, bytes transfered, packet lengths
								client_to_server_packets_time.add(p.time);
								tcp_packets.get(i).bytes_client_to_server += p.length;
								client_to_server_packet_lengths.add(p.length);
							}
							// checking for server to client packet
							else if(!p.client_to_server &&
								p.source_ip.equals(tcp_packet.server_ip) &&
								p.destination_ip.equals(tcp_packet.client_ip) && 
								p.source_port == tcp_packet.server_port &&
								p.destination_port == tcp_packet.client_port)
							{
								// outgoing packet: server to client
								// modify: bytes transfered, packet length
								server_to_client_packets_time.add(p.time);
								tcp_packets.get(i).bytes_server_to_client += p.length;
								server_to_client_packet_lengths.add(p.length);
							}
						}
					}
					else	break;	
				}
			}
		}
	}
	// write interarrival time into a file to calculate cdf
	void cdf_of_interarrival_time()
	{
		// opening a file
		try
		{
			// creating a file: historgram_tcp_connections.txt
			FileWriter fw = new FileWriter("interarrivals.txt");
			// first need to sort client_to_server_packets_time
			Collections.sort(client_to_server_packets_time);
			double prev_time = client_to_server_packets_time.get(0);
			int cutoff = client_to_server_packets_time.size();
			for(int i=1; i<cutoff; i++)
			{
				fw.write(Double.toString(client_to_server_packets_time.get(i) - prev_time) + "\n");
				prev_time = client_to_server_packets_time.get(i);
			}

			fw.flush();
			fw.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	// writing packet lengths to 2 diff files for incoming and outgoing packets
	void cdf_of_packet_lengths()
	{
		try
		{
			// for client to server packets
			// creating a file: historgram_tcp_connections.txt
			FileWriter fw = new FileWriter("client_to_server_packet_lengths.txt");
			int cutoff = client_to_server_packet_lengths.size();
			for(int i=0; i<cutoff; i++)	fw.write(Integer.toString(client_to_server_packet_lengths.get(i)) + "\n");
			fw.flush();
			fw.close();
			// for server to client packets
			// creating a file: historgram_tcp_connections.txt
			fw = new FileWriter("server_to_client_packet_lengths.txt");
			cutoff = server_to_client_packet_lengths.size();
			for(int i=0; i<cutoff; i++)	fw.write(Integer.toString(server_to_client_packet_lengths.get(i)) + "\n");
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
		int unique_server_ips_size = unique_server_ips.size(), unique_client_ips_size = unique_client_ips.size(),
		    tcp_packets_size = tcp_packets.size();
		System.out.println("server ips: " + unique_server_ips_size);
		System.out.println("client ips: " + unique_client_ips_size);
		System.out.println("number of packets: " + all_packets.size());
		System.out.println("number of unique tcp flows: " + tcp_packets_size);
	}
}
