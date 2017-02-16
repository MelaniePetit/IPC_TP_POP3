package Model;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

class Connexion implements Runnable {

    //FIELD
    private BufferedReader input;
    private DataOutputStream output;
    private Socket clientSocket;
    private int nbOfChances = 3;
    private boolean close;

    //CONSTANT
    private final String STATE_LOCK = "lock";
    final String STATE_AUTHORIZATION = "authorization";
//    final String STATE_TRANSACTION = "transaction";
//    final String STATE_UPDATE = "update";

    private String state = STATE_LOCK;

    //CONSTRUCTOR
    Connexion(Socket aClientSocket){
        try {
            clientSocket = aClientSocket;
            input = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
            output =new DataOutputStream( clientSocket.getOutputStream());
        }
        catch(IOException e) {
            System.out.println("Connection: "+e.getMessage());
        }
    }

    //GETTER & SETTER
    void setState(String state){ this.state = state; }
    int getNbOfChances(){ return nbOfChances; }

    public void run(){
        try {
            // an echo server
            String data = "+OK alpha POP3 server Ready\n\t\n\t";

            System.out.println ("New connection: " + clientSocket.getPort() + ", " + clientSocket.getInetAddress());
            output.writeBytes(data); // UTF is a string encoding
            output.flush();
            System.out.println ("send: " + data);

            if(clientSocket.isConnected())
                readCommand();
        }
        catch(EOFException e) {
            System.out.println("EOF: "+e.getMessage()); }
        catch(IOException e) {
            System.out.println("IO: "+e.getMessage());}
    }

    private void readCommand(){
        System.out.println("Reading from stream:");
        try {
            String command;
            while ((command = input.readLine()) != null && !close) {
                System.out.println ("receive from : " + clientSocket.getInetAddress() + " : " + clientSocket.getPort() + ", command : " + command);
                answerCommand(command);
                if(close)
                    break;
            }
            if(close)
            {
                sendResponse("-ERR number of chances attempt");
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void answerCommand(String data){
        String command = data.split(" ")[0];
        command = command.toUpperCase();

        try {
            switch (command)
            {
                case "APOP":
                    MethodsAPOP apop = new MethodsAPOP(this);
                    apop.answerCommand();
                    break;
                case "LIST":
                    MethodsLIST list = new MethodsLIST(this);
                    list.answerCommand();
                    break;
                case "STAT":
                    MethodsSTAT stat = new MethodsSTAT(this);
                    stat.answerCommand();
                case "QUIT":
                    break;
                default:
                    data = "UNDKNOW COMMAND";
                    output.writeBytes(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendResponse(String data){
        data += "\n\r\n\r";
        try {
            output.writeBytes(data);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean isStateLock(){
        if(Objects.equals(state, STATE_LOCK))
        {
            nbOfChances --;
            if (nbOfChances == 0)
            {
                close = true;
                return true;
            }
            return true;
        }
        else
            return false;
    }
}
