package Model;

import Model.Method.*;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.sql.Timestamp;

public class Connexion implements Runnable {

    //FIELD
    BufferedReader input;
    DataOutputStream output;
    Socket clientSocket;
    private int nbOfChances = 3;
    private boolean close;
    private String userfile;

    //CONSTANT
    private final String STATE_AUTHORIZATION = "authorization";
    private final String STATE_TRANSACTION = "transaction";
    public final String STATE_UPDATE = "update";

    private String state = STATE_AUTHORIZATION;

    private String timeStamp;

    //CONSTRUCTOR

    private ArrayList<Methods> methodsList = new ArrayList<>();
    private void setMethodsList(){
        methodsList.add(new MethodsAPOP(this,"APOP"));
        methodsList.add(new MethodsLIST(this,"LIST"));
        methodsList.add(new MethodsSTAT(this,"STAT"));
        methodsList.add(new MethodsRETR(this,"RETR"));
        methodsList.add(new MethodsDELE(this,"DELE"));
        methodsList.add(new MethodsQUIT(this,"QUIT"));
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

    public Connexion() {
        setMethodsList();
    }

    //
//    Connexion(SSLSocket aClientSocket){
//        setMethodsList();
//        try {
//            clientSocket = aClientSocket;
//            input = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
//            output =new DataOutputStream( clientSocket.getOutputStream());
//        }
//        catch(IOException e) {
//            System.out.println("Connection: "+e.getMessage());
//        }
//    }

    //GETTER & SETTER
    public void setState(String state){ this.state = state; }
//    public int getNbOfChances(){ return nbOfChances; }
    public String getSTATE_TRANSACTION() {
        return STATE_TRANSACTION;
    }
    public void setClose(boolean close) {
        this.close = close;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void run(){
        try {
            timeStamp = "<" + generateTimeStamp() + ">";
            // an echo server
            String data = "+OK alpha POP3 server Ready " + timeStamp + "\r\n";

            System.out.println ("New connection: " + clientSocket.getPort() + ", " + clientSocket.getInetAddress());
            output.writeBytes(data); // UTF is a string encoding
            output.flush();
            System.out.println ("send: " + data);
            if(clientSocket.isConnected())
                readCommand();
        }
        catch(EOFException e) {
            System.err.println("EOF: "+e.getMessage()); }
        catch(IOException e) {
            System.err.println("IO: "+e.getMessage());}
    }

    private void readCommand(){
        System.out.println("Reading from stream:");
        try {
            String command;
            while ((command = input.readLine()) != null && !close) {
                if(!Objects.equals(command, ""))
                {
                    System.out.println ("receive from : " + clientSocket.getInetAddress() + " : " + clientSocket.getPort() + ", command : " + command);
                    answerCommand(command);
                }
                if(close)
                    break;
            }
            if(close)
            {
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
        data += "\r\n";
        System.out.println("send: " + data);
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

    public String generateTimeStamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        String domain = ManagementFactory.getRuntimeMXBean().getName().split("@")[1];
        return pid + "." + timestamp.getTime() + "@" + "petitdolle.polytech";
    }
}
