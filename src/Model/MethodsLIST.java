package Model;

class MethodsLIST extends Methods {

    MethodsLIST(Connexion server){ super(server); }

    @Override
    String makeAnswer() {
        if(!server.isStateLock())
            return "+OK 2" + "x" + "messages" + "y" + "\n" + "1 x\n...";
        else
            return "–ERR Permission refused " + server.getNbOfChances() + " chances left";
    }
}
