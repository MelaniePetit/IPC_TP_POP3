package Model;

import Model.Utils.Md5;

import javax.net.SocketFactory;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class Client {
    static Socket socket;
    static BufferedReader input;
    static DataOutputStream output;
    static String ipAdress;
    private static String timeStamp="";

    public Client(String ip){
        int port = 110;
        ipAdress = ip;
        try {
            socket = new Socket(ipAdress, port);
            // Open stream
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new DataOutputStream(socket.getOutputStream());
            // Show the server response
            gettimeStamp();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Client() {
    }

    public void sendRequest(String data)
    {
        String command = data.split("\\s+")[0];
        command = command.toUpperCase();
        try {
            if(Objects.equals(command,"APOP"))
            {
                String toEncode = timeStamp + data.split("\\s+")[2];
                String pass = Md5.encode(toEncode);
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

    static void gettimeStamp() throws IOException {
        System.out.print("s: ");
        String line = "";
        int num;
        char ch;
        while(true)
        {
            num = input.read();
            ch = (char)num;
            line += ch;
            if(line.contains("<") && !line.contains(">"))
            {
                timeStamp+=ch;
            }
            if(line.contains("\r\n"))
                break;
        }
        timeStamp+=">";
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

    public static String getTimeStamp() {
        return timeStamp;
    }
}
