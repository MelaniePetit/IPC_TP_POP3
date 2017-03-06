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

/**
 * Created by jeremy on 06/03/2017.
 */
public class MethodsRETR extends Methods {

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
            return "-ERR";
    }

    @Override
    String[] extractContent(String content) {
        return new String[] {content.split("\\s+")[1]};
    }

    private String messageFactory(int id){
        String answer = "";
        String message = getJsonContent(id);
        try {
             answer = "+OK " + message.getBytes("UTF-8").length + " bytes\n" + message;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return answer;
    }

    private String getJsonContent(int id) {
        String answer = "";
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
                    String content = (String) slide.get("content");
                    answer += content;
                    return answer;
                }

            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return "-ERR message doesn't exist";
    }
}
