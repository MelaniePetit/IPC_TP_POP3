package Model;

import Model.Method.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

public class Connexion implements Runnable {

    //FIELD
    private BufferedReader input;
    private DataOutputStream output;
    private Socket clientSocket;
    private int nbOfChances = 3;
    private boolean close;
    private ArrayList<Methods> methodsList = new ArrayList<>();
    private String userfile;

    //CONSTANT
    private final String STATE_AUTHORIZATION = "authorization";
    private final String STATE_TRANSACTION = "transaction";
    final String STATE_UPDATE = "update";

    private String state = STATE_AUTHORIZATION;

    //CONSTRUCTOR
    private void setMethodsList(){
        methodsList.add(new MethodsAPOP(this,"APOP"));
        methodsList.add(new MethodsLIST(this,"LIST"));
        methodsList.add(new MethodsSTAT(this,"STAT"));
        methodsList.add(new MethodsRETR(this,"RETR"));
    }
    Connexion(Socket aClientSocket){
        setMethodsList();
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
    public void setState(String state){ this.state = state; }
    public int getNbOfChances(){ return nbOfChances; }
    public String getSTATE_TRANSACTION() {
        return STATE_TRANSACTION;
    }

    public void run(){
        try {
            // an echo server
            String data = "+OK alpha POP3 server Ready\n\r\n\r";

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
        String command = data.split("\\s+")[0];
        command = command.toUpperCase();

        String content = "";
        for (String s : data.split("\\s+"))
            content += s + " ";

        for (Methods method : methodsList)
        {
            if(Objects.equals(method.getCommand(), command))
            {
                method.answerCommand(content);
                return;
            }
        }
        sendResponse("-ERR unknown command");
    }

    public void sendResponse(String data){
        data += "\n\r\n\r";
        try {
            output.writeBytes(data);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isStateAuthentified(){
        if(Objects.equals(state, STATE_AUTHORIZATION))
        {
            nbOfChances --;
            if (nbOfChances == 0)
            {
                close = true;
            }
            return true;
        }
        else
            return false;
    }

    public boolean isStateTransaction()
    {
        return Objects.equals(state, STATE_TRANSACTION);
    }

    public void setUserFile(String file) {
        this.userfile = file;
    }

    public String getUserfile() {
        return userfile;
    }
}
