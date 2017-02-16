package Model;

abstract class Methods {
    Connexion server;

    Methods(Connexion server){ this.server = server; }

    void answerCommand()
    {
        server.sendResponse(makeAnswer());
    }

    abstract String makeAnswer();

}
