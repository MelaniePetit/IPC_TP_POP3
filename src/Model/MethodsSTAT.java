package Model;

class MethodsSTAT extends Methods {

    MethodsSTAT(Connexion server){ super(server); }

    @Override
    String makeAnswer() {
        if(!server.isStateLock())
            return "+OK " + "x " + "y";
        else
            return "–ERR Permission refused " + server.getNbOfChances() + " chances left";
    }
}
