package Model;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * Created by jeremy on 19/03/2017.
 */
public class SServer extends Server {

    private String[] cipherList;
    private static Server server;
    private ArrayList<Connexion> connections = new ArrayList<>();

    public static synchronized Server getInstance( ) {
        if (server == null)
            server=new SServer();
        return server;
    }

    @Override
    public void run() {
        tcpConnection();
    }

    private void tcpConnection()
    {
        try{
            int serverPort = 1026;
            //Creaet a SSLServersocket
            SSLServerSocketFactory factory=(SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslserversocket=(SSLServerSocket) factory.createServerSocket(serverPort);
            System.err.println("server start listening...");

            cipherList = sslserversocket.getSupportedCipherSuites();

            sslserversocket.setEnabledCipherSuites(sslserversocket.getSupportedCipherSuites());

            while(serverIsRunning) {
                SSLSocket sslClientSocket=(SSLSocket) sslserversocket.accept();
                sslClientSocket.setEnabledCipherSuites(sslClientSocket.getSupportedCipherSuites());
                if(serverIsRunning)
                {
                    SConnection connection = new SConnection(sslClientSocket);
                    connections.add(connection);
                    new Thread(connection).start();
                }
            }
        }
        catch(IOException e) {
            System.err.println("Listen :"+e.getMessage());}
    }
}
