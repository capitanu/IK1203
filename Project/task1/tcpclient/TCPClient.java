package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    private static int BUFFERSIZE = 28;
    
    public static String askServer(String hostname, int port, String ToServer) throws  IOException {
	if(ToServer == null)
	    return askServer(hostname, port);

	byte[] outputServer = new byte[BUFFERSIZE];
	byte[] inputClient = new byte[BUFFERSIZE];
	Socket socket = new Socket(hostname, port);
	socket.getOutputStream().write(ToServer.getBytes());
	socket.getOutputStream().write('\n');
	socket.setSoTimeout(1*1000);
	int outputLength = 0;
	StringBuilder output = new StringBuilder("");
	boolean ok = true;
	while(ok){	    
	    try{outputLength = socket.getInputStream().read(outputServer);
		if(outputLength == -1){ok = false;break;}}
	    catch(java.net.SocketTimeoutException e){ok = false;break;}
	    byte[] onlytext = new byte[outputLength];
	    for(int i = 0; i < outputLength; i++)
		onlytext[i] = outputServer[i];
	    String text = new String(onlytext, "UTF-8");
	    output.append(text);
	    outputServer = new byte[BUFFERSIZE];

	}
	socket.close();
	return output.toString();
    }
    public static String askServer(String hostname, int port) throws  IOException {

	byte[] outputServer = new byte[BUFFERSIZE];
	
	Socket socket = new Socket(hostname, port);
	socket.setSoTimeout(1*1000);
	int outputLength = 0;
	StringBuilder output = new StringBuilder("");
	boolean ok = true;
	while(ok){
	    try{outputLength = socket.getInputStream().read(outputServer);
		if(outputLength == -1){ok = false;break;}}
	    catch(java.net.SocketTimeoutException e){ok = false;break;}
	    byte[] onlytext = new byte[outputLength];
	    for(int i = 0; i < outputLength; i++)
		onlytext[i] = outputServer[i];
	    String text = new String(onlytext, "UTF-8");
	    output.append(text);
	    outputServer = new byte[BUFFERSIZE];

	}
	socket.close();
	return output.toString();
    }
}

