package Model.Method;

import Model.Connexion;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.util.Objects;

public class MethodsAPOP extends Methods {
    private String login;
    private int nbrMsg;

    public MethodsAPOP(Connexion server, String command) {
        super(server, command);
    }

    @Override
    String makeAnswer(String content) {
        if (server.isStateAuthentified()) {
            //extract login and password
            String[] s = extractContent(content);
            //check if user is in db
            if (checkAuthentification(s)) {
                server.setState(server.getSTATE_TRANSACTION());
                return "+OK " + login + "'s maildrop has " + nbrMsg + " messages";
            }
            return "-ERR user not found";
        }
        else
            return "-ERR user already authenticated";
    }

    @Override
    String[] extractContent(String content) {
        String login = content.split("\\s+")[1];
        this.login = login;
        String password = content.split("\\s+")[2];
        return new String[]{login,password};
    }

    private boolean checkAuthentification(String[] contentSend){
        JSONParser parser = new JSONParser();
        try {

            Object obj = parser.parse(new FileReader("ressources\\users.json"));
            JSONObject jsonObject = (JSONObject) obj;

            JSONArray users = (JSONArray) jsonObject.get("users");
            for (Object user : users) {
                JSONObject slide = (JSONObject) user;
                String username = (String) slide.get("username");
                String password = (String) slide.get("password");
                String file = (String) slide.get("file");

                if(Objects.equals(username, contentSend[0]) && Objects.equals(password, contentSend[1]))
                {
                    setUserFile(file);
                    setNumberOfMessages();
                    return true;
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return false;

    }

    private void setUserFile(String file) {
        server.setUserFile(file);
    }

    private void setNumberOfMessages()
    {
        JSONParser parser = new JSONParser();
        try {

            Object obj = parser.parse(new FileReader("ressources\\users\\" + server.getUserfile()));
            JSONObject jsonObject = (JSONObject) obj;
            nbrMsg = Integer.parseInt(String.valueOf(jsonObject.get("number_messages")));

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }
}
