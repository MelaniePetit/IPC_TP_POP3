package Model.Method;

import Model.Connexion;

public class MethodsLIST extends Methods {

    public MethodsLIST(Connexion server, String command) {
        super(server, command);
    }

    @Override
    public String makeAnswer(String content) {
        if(server.isStateTransaction())
            return "+OK 2" + "x" + "messages" + "y" + "\n" + "1 x\n...";
        else
            return "–ERR Permission refused " + server.getNbOfChances() + " chances left";
    }

    @Override
    String[] extractContent(String content) {
        return new String[0];
    }

}
