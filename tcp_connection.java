import java.util.*;
import java.io.*;

class tcp_connection
{
  String server_ip, client_ip;
  int server_port, client_port, start_no, end_no, client_to_server_bytes, server_to_client_bytes,
      bytes_client_to_server, bytes_server_to_client;
  double t_open,t_close;
  tcp_connection(String server_ip, String client_ip, int server_port, int client_port, double t_open, int no)
  {
    this.server_ip = server_ip;
    this.client_ip = client_ip;
    this.server_port = server_port;
    this.client_port = client_port;
    this.t_open = t_open;
    this.start_no = no;
    this.t_close = -1.0;
    this.client_to_server_bytes=0;
    this.server_to_client_bytes=0;
    this.end_no = -1;
    this.bytes_client_to_server = 0;
    this.bytes_server_to_client = 0;
  }

  boolean check_complete_flow()
  {
	  return (t_close != -1);
  }
  void print()
	{
		System.out.println("server_ip: 			" + server_ip);
		System.out.println("client_ip: 			" + client_ip);
		System.out.println("server_port: 		" + server_port);
		System.out.println("client_port: 	" + client_port);
		System.out.println("t_open:		" + t_open);
		System.out.println("t_close: 		" + t_close);
		System.out.println("start_no: 	"+ start_no);
		System.out.println("end_no: 	"+ end_no);
		System.out.println("client to server bytes: " + bytes_client_to_server);
		System.out.println("server to client bytes: " + bytes_server_to_client);
    		System.out.println("------------------------------");
	}
}
