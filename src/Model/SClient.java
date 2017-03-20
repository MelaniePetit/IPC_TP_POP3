package Model;

import javax.net.SocketFactory;
import javax.net.ssl.SSLServerSocket;
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
public class SClient extends Client{

    public SClient(String ip) throws IOException {
        super();
        int port = 1026;
        ipAdress = ip;
        try {
            SocketFactory socketFactory = SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket) socketFactory.createSocket(ip,port);
            sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());

            socket = sslSocket;
            // Open stream
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new DataOutputStream(socket.getOutputStream());
            // Show the server response
            gettimeStamp();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
