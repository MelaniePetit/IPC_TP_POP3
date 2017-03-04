package Model.Method;

import Model.Connexion;

public class MethodsQUIT extends Methods {

    MethodsQUIT(Connexion server, String command) {
        super(server, command);
    }

    @Override
    String makeAnswer(String content) {
        return null;
    }

    @Override
    String[] extractContent(String content) {
        return new String[0];
    }
}
