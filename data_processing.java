////// variables used in the main program //////
// all_packets: set of all packets in the original database
import java.util.*;
import java.io.*;

public class data_processing
{
	public static void print(String s)
	{
		System.out.println(s);
	}
	public static void print(int s)
	{
		System.out.println(s);
	}
	public static void main(String args[])
	{
		if(args.length == 0)	{ System.out.println("expecting path to csv file."); return;}
		try
		{
			//////////////////////////////////////// storing packets in data structure ////////////////////////////////////////////
			// set of all packets
			packet_traces all_packets = new packet_traces();
			// creating buffered reader and reading the file
			BufferedReader csv_reader = new BufferedReader(new FileReader(args[0]));
			String row = null;
			// reading header line
			csv_reader.readLine();
//			int counter=1, cutoff=8;
			while( (row = csv_reader.readLine()) != null ) //&& counter <= cutoff)
			{
//				counter++;
				String[] data = row.split(",");
				String info = "";	
				// combining the info
				if(data.length > 7)	for(int i=6; i<data.length; i++) 	info += String.join(",", data[i]);
				else	info = data[6];
				info.trim();
				packet p = new packet( 	Integer.parseInt(data[0].substring(1, data[0].length()-1)), 
							Double.parseDouble(data[1].substring(1, data[1].length()-1)), 
							Integer.parseInt(data[5].substring(1, data[5].length()-1)), 
							data[2].substring(1, data[2].length()-1), 
							data[3].substring(1, data[3].length()-1), 
							data[4].substring(1, data[4].length()-1), 
							info.substring(1, info.length()-1));
				// storing the packet
				all_packets.add(p);
			}
			////////////////////////////////////////	////////////////////////////////////////
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}

class packet
{
	int no, length;
	double time;
	String source_ip, destination_ip, protocol, info;
	packet(int no, double time, int length, String source_ip, String destination_ip, String protocol, String info)
	{
		this.no = no;
		this.time = time;
		this.length = length;
		this.source_ip = source_ip;
		this.destination_ip = destination_ip;
		this.protocol = protocol;
		this.info = info;
	}
	// check type of protocol
	boolean check_protocol(String protocol)
	{
		return this.protocol.equals(protocol);
	}
	void print()
	{
		System.out.println("no: 			" + no);
		System.out.println("time: 			" + time);
		System.out.println("source ip: 		" + source_ip);
		System.out.println("destination ip: 	" + destination_ip);
		System.out.println("protocol:		" + protocol);
		System.out.println("length: 		" + length);
		System.out.println("info: 			" + info);
	}
}

class packet_traces
{
	List<packet> all_packets = new ArrayList<packet>();
	List<packet> syn_packets = new ArrayList<packet>();

	packet_traces(){}
	// search for a packet from "offset"(0 indexed offset), having "word" in info, having given "source ip" and "destination ip"
	boolean packet_search(int offset, String query_word, String source_ip, String destination_ip)
	{
		int all_packets_size = all_packets.size();
		for(int i=offset; i<all_packets_size; i++)
		{
			
		}
	}
	
	// extract syn packet for 3 way hand shaked packets
	void extract_syn_packets()
	{
		int all_packets_size=all_packets.size();
		// traversing through all packets
		for(int i=0; i<all_packets_size; i++)
		{
			packet p = all_packets.get(i);
			if(p.check_protocol("TCP"))
			{

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
