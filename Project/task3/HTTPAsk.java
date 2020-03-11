import java.net.*;
import java.io.*;

public class HTTPAsk {
    public static void main( String[] args) throws IOException{
	int port = Integer.parseInt(args[0]);
	ServerSocket server = new ServerSocket(port);
	String inputBuffer;
	while(true){
	    Socket conn = server.accept();
	    OutputStream os = conn.getOutputStream();
	    InputStreamReader is = new InputStreamReader(conn.getInputStream());
	    BufferedReader clientInput = new BufferedReader(is);
	    DataOutputStream serverOutput = new DataOutputStream(os);
	    int i;
	    inputBuffer = clientInput.readLine();
	    try{
		String[] arrOfStrings = inputBuffer.split(" ", 3);
		//serverOutput.writeBytes(arrOfStrings[1]);
		String[] askAndPar = arrOfStrings[1].split("\\?", 2);
		if(askAndPar[0].equals("/ask") == false){
		    serverOutput.writeBytes("HTTP/1.1 400 Bad Request\r\n\r\n");
		    conn.close();
		    continue;
		}
	   
		String[] pars = askAndPar[1].split("&");
		for(i = 0; i < pars.length; i++){
		    String[] arg = pars[i].split("=");
		    pars[i] = arg[1];
		}
		String finaloutput;
		try{
		    if(i == 2){
			finaloutput = TCPClient.askServer(pars[0], Integer.parseInt(pars[1]));}
		    else{
			finaloutput = TCPClient.askServer(pars[0], Integer.parseInt(pars[1]), pars[2]);}
		}
		catch(Exception e){
		    serverOutput.writeBytes("HTTP/1.1 404 Not Found\r\n\r\n");
		    conn.close();
		    continue;
		}
		serverOutput.writeBytes("HTTP/1.1 200 OK\r\n\r\n" + finaloutput);

	    }
	    catch(Exception e){}
	    
	    conn.close();
	}
    }
}
