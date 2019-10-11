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
			all_packets.extract_tcp_packets();
			// for(int i=0; i<all_packets.tcp_packets.size(); i++)
			// {
			// 	all_packets.tcp_packets.get(i).print();
			// }
			////////////////////////////////////////	////////////////////////////////////////
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
