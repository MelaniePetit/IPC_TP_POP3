package Model.Method;

import Model.Connexion;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MethodsQUIT extends Methods {

    private int nbMessages;
    public MethodsQUIT(Connexion server, String command) {
        super(server, command);
    }

    @Override
    String makeAnswer(String content) {
        if (server.isStateTransaction()){
            if (checkMarked()){
                server.setState(server.STATE_UPDATE);
                if(!deleteMessages())
                    return "-ERR some deleted messages not removed";
            }
            server.setClose(true);
            String s = "+OK alpha POP3 server signing off (";
            if (nbMessages != 0)
                s += nbMessages + " left)";
            else
                s += "maildrop empty)";

            return s;
        }
        return null;
    }

    @Override
    String[] extractContent(String content) {
        return new String[0];
    }

    private boolean checkMarked() {
        JSONParser parser = new JSONParser();
        try {

            Object obj = parser.parse(new FileReader("ressources\\users\\" + server.getUserfile()));
            JSONObject jsonObject = (JSONObject) obj;
            nbMessages = Integer.parseInt(jsonObject.get("number_messages").toString());
            
            JSONArray messages = (JSONArray) jsonObject.get("messages");

            for(Object message : messages)
            {
                JSONObject slide = (JSONObject) message;
                if (slide.get("marked").equals(true)) {
                    return true;
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean deleteMessages(){
        JSONParser parser = new JSONParser();
        ArrayList<JSONObject> toDeleteMessages = new ArrayList<>();
        try {
            Object obj = parser.parse(new FileReader("ressources\\users\\" + server.getUserfile()));
            JSONObject jsonObject = (JSONObject) obj;
            nbMessages = Integer.parseInt(jsonObject.get("number_messages").toString());

            JSONArray messages = (JSONArray) jsonObject.get("messages");

            for(Object message : messages)
            {
                JSONObject slide = (JSONObject) message;
                if (slide.get("marked").equals(true)) {
                    toDeleteMessages.add((JSONObject) message);
                    nbMessages --;
                    jsonObject.put("number_messages", nbMessages);
                }
            }

            for(JSONObject message : toDeleteMessages){
                messages.remove(message);
            }

            try (FileWriter file = new FileWriter("ressources\\users\\" + server.getUserfile())) {
                file.write(jsonObject.toJSONString());
                System.out.println("Successfully Copied JSON Object to File...");
                System.out.println("\nJSON Object: " + jsonObject);
            }
            return true;
        } catch (ParseException | IOException e) {
            return false;
        }
    }

}
