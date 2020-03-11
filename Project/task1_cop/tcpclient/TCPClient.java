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
	socket.setSoTimeout(500);
	int outputLength = 0;
	try{
	    outputLength = socket.getInputStream().read(outputServer);
	}
	catch(java.net.SocketTimeoutException e){}
	
	StringBuilder output = new StringBuilder("");
	boolean ok = true;
	while(ok){
	    for(int i = 0; i < outputLength; i++){output.append((char)outputServer[i]);}
	    try{outputLength = socket.getInputStream().read(outputServer);
		if(outputLength == -1)
		    ok = false;}
	    catch(java.net.SocketTimeoutException e){ok = false;}
	}
	socket.close();
	return output.toString();
    }
    public static String askServer(String hostname, int port) throws  IOException {

	byte[] outputServer = new byte[BUFFERSIZE];
	
	Socket socket = new Socket(hostname, port);
	socket.setSoTimeout(1*1000);
	int outputLength = 0;
	try{
	    outputLength = socket.getInputStream().read(outputServer);
	}
	catch(java.net.SocketTimeoutException e){}
	StringBuilder output = new StringBuilder("");
	boolean ok = true;
	while(ok){
	    for(int i = 0; i < outputLength; i++){output.append((char)outputServer[i]);}
	    try{outputLength = socket.getInputStream().read(outputServer);
		if(outputLength == -1)
		    ok = false;}
	    catch(java.net.SocketTimeoutException e){ok = false;}
	}
	socket.close();
	return output.toString();
    }
}

