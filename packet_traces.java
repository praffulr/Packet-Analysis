import java.util.*;
import java.io.*;

class packet_traces
{
	List<packet> all_packets = new ArrayList<packet>();
	//List<packet> syn_packets = new ArrayList<packet>();
	List<packet> syn_packets = null;

	packet_traces(){}
	// search for a packet from "offset"(0 indexed offset), having "word" in info, having given "source ip" and "destination ip"
	boolean packet_search(int offset, String query_word, String source_ip, String destination_ip)
	{
		int all_packets_size = all_packets.size();
		for(int i=offset; i<all_packets_size; i++)
		{

		}
		return false;
	}

	// extract syn packet for 3 way hand shaked packets
	void extract_syn_packets()
	{
		int all_packets_size=all_packets.size();
		// traversing through all packets
		for(int i=0; i<all_packets_size; i++)
		{
			// packet p = all_packets.get(i);
			// if(p.check_protocol("TCP"))
			// {
			// 	String[] parts_info = p.info.split(" ");
			// 	if(parts_info[3].equals("[SYN]"))
			// 		syn_packets.add(p);
			//}
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
