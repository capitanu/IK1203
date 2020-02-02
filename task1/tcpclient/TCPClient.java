package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    private static int BUFFERSIZE = 1024;
    
    public static String askServer(String hostname, int port, String ToServer) throws  IOException {
	if(ToServer == null)
	    return askServer(hostname, port);

	byte[] outputServer = new byte[BUFFERSIZE];
	byte[] inputClient = new byte[BUFFERSIZE];
	Socket socket = new Socket(hostname, port);
	
	socket.getOutputStream().write(ToServer.getBytes());
	socket.getOutputStream().write('\n');

	int outputLength = socket.getInputStream().read(outputServer);

	StringBuilder output = new StringBuilder("");

	for(int i = 0; i < outputLength; i++){
	    output.append((char)outputServer[i]);
	}
	//	output.append("\n");
	socket.close();
	return output.toString();
    }

    public static String askServer(String hostname, int port) throws  IOException {

	byte[] outputServer = new byte[BUFFERSIZE];
	
	Socket socket = new Socket(hostname, port);
	int outputLength = socket.getInputStream().read(outputServer);
	StringBuilder output = new StringBuilder("");

	for(int i = 0; i < outputLength; i++){
	    output.append((char)outputServer[i]);
	}
	//	output.append("\n");
	socket.close();
	return output.toString();
    }
}

