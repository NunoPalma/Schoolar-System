package Server;

import Server.publications.PublicationsManager;


/**
 * Classe que gere managers fulcráis para o funcionamento do servidor.
 */
public class ServerManager {

    private final Users mUsers;
    private final PublicationsManager mPublicationsManager;

    public ServerManager() {
        mUsers = new Users();
        mPublicationsManager = new PublicationsManager();
    }

    public Users getUsers() {
        return mUsers;
    }

    public PublicationsManager getPublicationsManager() {
        return mPublicationsManager;
    }
}
