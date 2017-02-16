package Model;

class MethodsAPOP extends Methods {

    MethodsAPOP(Connexion server) {
        super(server);
    }

    @Override
    String makeAnswer() {
        //check if user is in db
        //if ok then
        server.setState(server.STATE_AUTHORIZATION);
        return "+OK " + "client" + "’s maildrop has " + "x " + "messages" + "y" ;
    }
}
