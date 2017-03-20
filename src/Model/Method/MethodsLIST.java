package Model.Method;

import Model.Connexion;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MethodsLIST extends Methods {

    public MethodsLIST(Connexion server, String command) {
        super(server, command);
    }

    @Override
    public String makeAnswer(String content) {
        if(server.isStateTransaction())
        {
            ArrayList<String> s = getJsonContent();
            return messageFactory(s);
        }
        else
            return "";
    }

    @Override
    String[] extractContent(String content) {
        return new String[0];
    }

    private ArrayList<String> getJsonContent() {
        ArrayList<String> answer = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {

            Object obj = parser.parse(new FileReader("ressources\\users\\" + server.getUserfile()));
            JSONObject jsonObject = (JSONObject) obj;

            JSONArray messages = (JSONArray) jsonObject.get("messages");

            for(Object message : messages)
            {
                JSONObject slide = (JSONObject) message;
                String messageId = slide.get("message-id").toString();
                String content = (String) slide.get("content");
                boolean marked = (boolean) slide.get("marked");
                if(!marked){
                    answer.add(messageId);
                    answer.add(content);
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    private String messageFactory(ArrayList<String> content) {
        String answer = "+OK " + (content.size() / 2) + " messages " + BytesSize(content) + "\n";

        try {
            for(int i = 0; i < content.size(); i++)
            {
                if(i!=content.size()-1)
                    answer += content.get(i) + " " + (content.get(i+1).getBytes("UTF-8")).length + "\n";
                else
                    answer += content.get(i) + " " + (content.get(i+1).getBytes("UTF-8")).length;
                i++;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return answer;

    }

    private String BytesSize(ArrayList<String> content)
    {
        int answer = 0;
        try {
            for(int i = 1; i < content.size(); i++)
            {
                answer += (content.get(i).getBytes("UTF-8")).length;
                i++;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "(" + answer + " bytes)";
    }
}
