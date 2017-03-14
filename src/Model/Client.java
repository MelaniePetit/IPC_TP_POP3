package Model;

import Model.Utils.Md5;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class Client {
    private static Socket socket;
    private static BufferedReader input;
    private static DataOutputStream output;
    private static String ipAdress;

    public Client(String ip){
        int port = 110;
        ipAdress = ip;
        try {
            socket = new Socket(ipAdress, port);
            // Open stream
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new DataOutputStream(socket.getOutputStream());
            // Show the server response
            readStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        try {
//            int port = 110;
//            socket = new Socket("10.42.146.50", port);
//            // Open stream
//            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            output = new DataOutputStream(socket.getOutputStream());
//            // Show the server response
//            readStream();
//            sendRequest("APOP jeremy 123456789");
//            sendRequest("STAT");
//            sendRequest("LIST");
//            sendRequestRetr("RETR 2");
//            sendRequest("QUIT");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void sendRequest(String data)
    {
        String command = data.split("\\s+")[0];
        command = command.toUpperCase();
        try {
            if(Objects.equals(command,"APOP"))
            {
                String pass = Md5.encode(data.split("\\s+")[2]);
                data = data.split("\\s+")[0] + " " + data.split("\\s+")[1] + " " + pass;
            }

            output.writeBytes(data + "\r\n"); // UTF is a string encoding
            output.flush();
            if (Objects.equals(command, "RETR")){
                readStreamRetr();}
            else{
                readStream();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readStream() throws IOException {
        System.out.print("s: ");
        String line = "";
        int num;
        char ch;
        while(true)
        {
            num = input.read();
            ch = (char)num;
            line += ch;
            if(line.contains("\r\n"))
                break;
        }
        System.out.println(line);
    }

    private static void readStreamRetr() throws IOException {
        System.out.print("s: ");
        String line = "";
        int num;
        char ch;
        char next;
        int count = 0;
        while(true)
        {
            num = input.read();
            ch = (char)num;
            line += ch;
            if(ch == '\r' && (next = (char) input.read()) == '\n') {
                line += next;
                count++;
                if (line.contains("-ERR"))
                    break;
            }
            if (count == 8){
                break;
            }
        }
        System.out.println(line);
    }
}
