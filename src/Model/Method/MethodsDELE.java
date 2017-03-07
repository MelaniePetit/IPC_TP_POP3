package Model.Method;

import Model.Connexion;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by jeremy on 07/03/2017.
 */
public class MethodsDELE extends Methods {
    public MethodsDELE(Connexion server, String command) {
        super(server, command);
    }

    @Override
    String makeAnswer(String content) {
        if (server.isStateTransaction()){
            int id = Integer.parseInt(extractContent(content)[0]);
            return markedMessage(id);
        }
        return null;
    }

    @Override
    String[] extractContent(String content) {
        return new String[] {content.split("\\s+")[1]};
    }

    private String markedMessage(int id) {
        if (getJsonContent(id)){
            return "+OK message "+ id +" marked delete";
        }
        else
            return "-ERR message doesn't exist";
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
                    slide.replace("marked",true);
                    print(jsonObject);

                    try (FileWriter file = new FileWriter("ressources\\users\\" + server.getUserfile())) {
                        file.write(jsonObject.toJSONString());
                        System.out.println("Successfully Copied JSON Object to File...");
                        System.out.println("\nJSON Object: " + jsonObject);
                    }
                    return true;
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void print(JSONObject messages) {
        System.out.println(messages.toString());
    }

}
