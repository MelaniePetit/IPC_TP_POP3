package Model;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * Created by jeremy on 18/03/2017.
 */
public class SConnection extends Connexion{

    public SConnection(SSLSocket aClientSocket) {
        super();
        try {
            clientSocket = aClientSocket;
            input = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
            output =new DataOutputStream( clientSocket.getOutputStream());
        }
        catch(IOException e) {
            System.out.println("Connection: "+e.getMessage());
        }
    }
}
