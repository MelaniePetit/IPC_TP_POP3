package Model.Method;

import Model.Connexion;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MethodsRETR extends Methods {

    private String messageContent = "";

    public MethodsRETR(Connexion server, String command) {
        super(server, command);
    }

    @Override
    String makeAnswer(String content) {
        if (server.isStateTransaction()) {
            //extract login and password
            int id = Integer.parseInt(extractContent(content)[0]); //Check try catch
            return messageFactory(id);
        }
        else
            return "";
    }

    @Override
    String[] extractContent(String content) {
        return new String[] {content.split("\\s+")[1]};
    }

    private String messageFactory(int id){
        if(getJsonContent(id)){
            try {
                return "+OK " + messageContent.getBytes("UTF-8").length + " bytes\n" + messageContent;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return "-ERR no such message, invalid message number";
    }

    private boolean getJsonContent(int id) {
        JSONParser parser = new JSONParser();
        try {

            Object obj = parser.parse(new FileReader("ressources\\users\\" + server.getUserfile()));
            JSONObject jsonObject = (JSONObject) obj;

            JSONArray messages = (JSONArray) jsonObject.get("messages");

            for(Object message : messages)
            {
                JSONObject slide = (JSONObject) message;
                int messageId = Integer.parseInt(slide.get("message-id").toString());
                if(id == messageId)
                {
                    String date = (String) slide.get("date");
                    String subject = (String) slide.get("subject");
                    int msgId = Integer.parseInt(slide.get("message-id").toString());
                    String from = (String) slide.get("from");
                    String to = (String) slide.get("to");
                    String content = (String) slide.get("content");
                    messageContent += "From: " + from + "\r\nTo: " + to + "\r\nSubject: " + subject + "\r\nDate: " + date + "\r\nMessage-ID: " + msgId + "\r\n\r\n" + content + "\r\n.";
                    return true;
                }

            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
