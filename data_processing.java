import java.util.*;
import java.io.*;

class packet
{
	int no, time, length;
	String source_ip, destination_ip, protocol, info;
	packet(int no, int time, int length, String source_ip, String destination_ip, String info)
	{
		this.no = no;
		this.time = time;
		this.length = length;
		this.source_ip = source_ip;
		this.destination_ip = destination_ip;
		this.info = info;
	}
}

public class data_processing
{
	public static void main(String args[])
	{
		if(args.length == 0)	{ System.out.println("expecting path to csv file."); return;}
		try
		{
			// creating buffered reader and reading the file
			BufferedReader csv_reader = new BufferedReader(new FileReader(args[0]));
			String row = null;
			while( (row = csv_reader.readLine()) != null)
			{
				String[] data = row.split(",");
				// info data gets splitted if it contains comma, may need to handle
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
