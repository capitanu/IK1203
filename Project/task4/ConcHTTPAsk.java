import java.net.*;
import java.io.*;

public class ConcHTTPAsk {
    public static void main( String[] args) throws IOException{
	int port = Integer.parseInt(args[0]);
	ServerSocket server = new ServerSocket(port);
	while(true){
	    Socket conn = server.accept();
	    Runnable r = new MyRunnable(conn);
	    new Thread(r).start();
	}
    }
}
