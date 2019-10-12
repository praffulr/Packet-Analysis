import java.util.*;
import java.io.*;

class packet
{
	int no, length, source_port, destination_port;
	double time;
	String source_ip, destination_ip, protocol, info;
	boolean client_to_server;

	packet(int no, double time, int length, String source_ip, String destination_ip, String protocol, String info)
	{
		this.no = no;
		this.time = time;
		this.length = length;
		this.source_ip = source_ip;
		this.destination_ip = destination_ip;
		this.protocol = protocol;
		this.info = info;
		// fetching from info string
		// must be a tcp flow
		if(this.protocol.equals("TCP"))
		{
			// looping through char by char in info string
			int info_length = info.length(), counter=0;
			String s_port="", d_port="";
			// ignoring initial spaces
			while(info.charAt(counter) == ' ')	counter++;
			// getting source port
			while(info.charAt(counter) != ' ')	{ s_port += info.charAt(counter); counter++;}
			// ignoring spaces
			while(info.charAt(counter) == ' ')	counter++;
			// ignoring > sign
			counter++;
			while(info.charAt(counter) == ' ')	counter++;
			// getting destination port
			while(info.charAt(counter) != ' ')	{ d_port += info.charAt(counter); counter++;}
			source_port = Integer.parseInt(s_port);
			destination_port = Integer.parseInt(d_port);
			// incoming or outgoing
			if(source_port == 21)	client_to_server = false;
			else			client_to_server = true;
		}
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
		System.out.println("------------------------------");
	}
}
