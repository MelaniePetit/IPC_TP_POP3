package Model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private boolean serverIsRunning = true;

    public static void main (String args[])
    {
        Server server = new Server();
        server.tcpConnection();
    }

    private void tcpConnection()
    {
        try{
            int serverPort = 110;
            ServerSocket listenSocket = new ServerSocket(serverPort);

            System.out.println("server start listening...");

            while(serverIsRunning) {
                Socket clientSocket = listenSocket.accept();
                new Thread(new Connexion(clientSocket)).start();
            }
        }
        catch(IOException e) {
            System.out.println("Listen :"+e.getMessage());}
    }


}
