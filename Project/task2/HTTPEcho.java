import java.net.*;
import java.io.*;

public class HTTPEcho {
    public static void main( String[] args) throws IOException {
	int port = Integer.parseInt(args[0]);
	ServerSocket server = new ServerSocket(port);
	String inputBuffer;
	while(true){
	    Socket conn = server.accept();
	    OutputStream os = conn.getOutputStream();
	    InputStreamReader is = new InputStreamReader(conn.getInputStream());
	    BufferedReader clientInput = new BufferedReader(is);
	    DataOutputStream serverOutput = new DataOutputStream(os);
	    StringBuilder output = new StringBuilder("HTTP/1.1 200 OK\r\n");
	    output.append("\r\n");
	    while((inputBuffer = clientInput.readLine()) != null && inputBuffer.length() != 0){
		output.append(inputBuffer + "\r\n");}
	    serverOutput.writeBytes(output.toString());
	    conn.close();
	}
    }
}
			 
	    
    


