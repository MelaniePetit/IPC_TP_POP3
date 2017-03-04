package Model.Method;

import Model.Connexion;

public class MethodsSTAT extends Methods{

    //CONSTRUCTOR
    public MethodsSTAT(Connexion server, String command) {
        super(server, command);
    }

    @Override
    public String makeAnswer(String content) {
        if(server.isStateTransaction())
            return "+OK " + "x " + "y";
        else
            return "–ERR Permission refused " + server.getNbOfChances() + " chances left";
    }

    @Override
    String[] extractContent(String content) {
        return new String[0];
    }

}
