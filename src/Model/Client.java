package Model;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class Client {
    private static Socket socket;
    private static BufferedReader input;
    private static DataOutputStream output;

    public static void main(String[] args) {
        try {
            int port = 110;
            socket = new Socket("127.0.0.1", port);
            // Open stream
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new DataOutputStream(socket.getOutputStream());
            // Show the server response
            readStream();
            sendRequest("APOP jeremy 123456789");
            sendRequest("STAT");
            sendRequest("LIST");
            sendRequest("QUIT");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendRequest(String data)
    {
        try {
            output.writeBytes(data + "\r\n"); // UTF is a string encoding
            output.flush();
            readStream();
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
}
