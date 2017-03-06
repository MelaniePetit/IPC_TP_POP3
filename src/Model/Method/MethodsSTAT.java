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

public class MethodsSTAT extends Methods{

    //CONSTRUCTOR
    public MethodsSTAT(Connexion server, String command) {
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
            return "–ERR Permission refused ";
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
                String content = (String) slide.get("content");
                answer.add(content);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    private String messageFactory(ArrayList<String> content) {
        return "+OK " + (content.size())+ " " + BytesSize(content) + "\n";
    }

    private String BytesSize(ArrayList<String> content)
    {
        int answer = 0;
        try {
            for (String aContent : content) {
                answer += (aContent.getBytes("UTF-8")).length;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "" + answer;
    }

}
