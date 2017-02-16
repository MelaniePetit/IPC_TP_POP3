package Model;

import java.io.*;
import java.net.Socket;

class Connexion extends Thread{
    private BufferedReader input;
    private DataOutputStream output;
    private Socket clientSocket;

    public Connexion (Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            input = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
            output =new DataOutputStream( clientSocket.getOutputStream());
            this.start();
        }
        catch(IOException e) {
            System.out.println("Connection: "+e.getMessage());
        }
    }

    public void run() {
        try { // an echo server
            String data = "+OK alpha POP3 server Ready";

            System.out.println ("New connection: " + clientSocket.getPort() + ", " + clientSocket.getInetAddress());
            output.writeBytes(data); // UTF is a string encoding
            output.flush();
            System.out.println ("send: " + data);

//            while(true)
                readCommand();


        }
        catch(EOFException e) {
            System.out.println("EOF: "+e.getMessage()); }
        catch(IOException e) {
            System.out.println("IO: "+e.getMessage());}

    }

    private void readCommand()
    {
        System.out.println("Reading from stream:");
        try {
            String command;
            while ((command = input.readLine()) != null) {
                System.out.println ("receive from : " + clientSocket.getInetAddress() + " : " + clientSocket.getPort() + ", command : " + command);
                answerCommand(command);
                if (command.isEmpty()) break;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void answerCommand(String command) {
        String data;
        try {
            switch (command)
            {
                case "APOP":
                    data = "YOU'RE AUTHORIZED";
                    output.writeBytes(data);
                    break;
                case "LIST":
                    data = "YOUR LIST";
                    output.writeBytes(data);
                    break;
                case "QUIT":
                    data = "STOP CONNECTION";
                    output.writeBytes(data);
                    this.interrupt();
                    clientSocket.close();
                    break;
                default:
                    data = "UNDKNOW COMMAND";
                    output.writeBytes(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
