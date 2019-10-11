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

	// search for a packet from "offset"(0 indexed offset), having "word" in info, having given "source ip" and "destination ip"
	// boolean packet_search(int offset, String query_word, String source_ip, String destination_ip)
	// {
	// 	int all_packets_size = all_packets.size();
	// 	for(int i=offset; i<all_packets_size; i++)
	// 	{
	//
	// 	}
	// 	return false;
	// }

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
									tcp_connection new_connection = new tcp_connection(s_ip, c_ip, s_port, c_port, p.get_time());
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
