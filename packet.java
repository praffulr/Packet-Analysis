import java.util.*;
import java.io.*;

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
		System.out.println("------------------------------");
	}

	// String get_source_ip()
	// {
	// 	return source_ip;
	// }
	//
	// String get_dest_ip()
	// {
	// 	return destination_ip;
	// }
	//
	// double get_time()
	// {
	// 	return time;
	// }
	//
	// String get_info()
	// {
	// 	return info;
	// }
	//
	// int get_no()
	// {
	// 	return no;
	// }

}
