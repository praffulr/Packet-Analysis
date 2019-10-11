import java.util.*;
import java.io.*;

class tcp_connection
{
  String server_ip, client_ip;
  int server_port, client_port;
  double t_open,t_close;
  tcp_connection(String server_ip, String client_ip, int server_port, int client_port, double t_open)
  {
    this.server_ip = server_ip;
    this.client_ip = client_ip;
    this.server_port = server_port;
    this.client_port = client_port;
    this.t_open = t_open;
    this.t_close = -1.0;
  }

  void put_close_time(double t)
  {
    this.t_close = t;
  }

  void print()
	{
		System.out.println("server_ip: 			" + server_ip);
		System.out.println("client_ip: 			" + client_ip);
		System.out.println("server_port: 		" + server_port);
		System.out.println("client_port: 	" + client_port);
		System.out.println("t_open:		" + t_open);
		System.out.println("t_close: 		" + t_close);
    System.out.println("------------------------------");
	}
}
