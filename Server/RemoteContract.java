package Server;

import Contract.*;
import Server.publications.CitationsSorter;
import Server.publications.Publication;
import Server.publications.PublicationYearSorter;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class RemoteContract extends UnicastRemoteObject implements IRemoteContract {
    private ServerManager serverManager;

    protected RemoteContract() throws RemoteException {
        serverManager = new ServerManager();
    }

    protected RemoteContract(int port) throws RemoteException {
        super(port);
    }

    protected RemoteContract(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    /**
     * Autêntica um utilizador.
     */
    @Override
    public LoginStatus login(String email, String password) throws RemoteException {
        if (!serverManager.getUsers().userExists(email))
            return new LoginStatus(LOGIN_STATUS.NOT_REGISTERED, String.format("%s is not registered.", email), null);
        else if (serverManager.getUsers().isPasswordValid(email, password)) {
            User user = serverManager.getUsers().mUsers.get(email);
            serverManager.getUsers().setCurrentUser(user);
            return new LoginStatus(LOGIN_STATUS.SUCCESS, "Login successful.", serverManager.getUsers().mUsers.get(email).getName());
        }

        return new LoginStatus(LOGIN_STATUS.FAILED, "Incorrect username or password.", null);
    }

    /**
     * Regista um novo utilizador.
     */
    @Override
    public MessageStatus register(String name, String email, String password, String affiliation) throws RemoteException {
        if (serverManager.getUsers().userExists(email))
            return new MessageStatus(MESSAGE_STATUS.ALREADY_REGISTERED, String.format("%s is already registered.", email));

        User user = new User(name, email, password, affiliation);
        serverManager.getUsers().mUsers.put(email, user);

        return new MessageStatus(MESSAGE_STATUS.SUCCESS, "Successfully registered.");
    }

    @Override
    public MessageStatus addPublication(String authors, String title, String publishYear, String magazine,
                                        String volume, String number, String pages, String citations,
                                        String id) throws RemoteException {
        if (serverManager.getPublicationsManager().isPublicationsRegistered(id))
            return new MessageStatus(MESSAGE_STATUS.ALREADY_REGISTERED,
                    String.format("Book with the identifier %s is already registered.", id));

        ArrayList<String> authorsList = new ArrayList<>(Arrays.asList(authors.split(";")));
        List<String> pagesList = Arrays.asList(pages.split("-"));
        Integer startingPage = Integer.valueOf(pagesList.get(0));
        Integer endPage = Integer.valueOf(pagesList.get(1));
        Publication publication = new Publication(authorsList, Integer.valueOf(publishYear), title,
                magazine, Integer.valueOf(volume), Integer.valueOf(number), startingPage, endPage,
                Integer.valueOf(citations), id);

        serverManager.getPublicationsManager().addPublication(serverManager.getUsers().mCurrentUser, publication);

        return new MessageStatus(MESSAGE_STATUS.SUCCESS, "Successfully registered.");
    }

    /**
     * @return Lista de publicações do autor ordenadas pelo número de citações.
     * @throws RemoteException
     */
    @Override
    public ArrayList<Publication> getPublicationByCitations() throws RemoteException {
        ArrayList<Publication> requestList = getPublicationsByUser();

        requestList.sort(new CitationsSorter());

        return requestList;
    }

    /**
     * @return Lista de publicações do autor ordenadas pelo ano de publicação.
     * @throws RemoteException
     */
    @Override
    public ArrayList<Publication> getPublicationByPublishYear() throws RemoteException {
        ArrayList<Publication> requestList = getPublicationsByUser();

        requestList.sort(new PublicationYearSorter());

        return requestList;
    }

    /**
     * @return Lista de publicações do autor.
     */
    private ArrayList<Publication> getPublicationsByUser() {
        ArrayList<Publication> requestList = new ArrayList<>();

        if (serverManager.getPublicationsManager()
                .getPublicationsByUser(serverManager.getUsers().mCurrentUser.getEmail()) == null)
            return requestList;

        for (Map.Entry<String, Publication> entry :
                serverManager.getPublicationsManager()
                        .getPublicationsByUser(serverManager.getUsers().mCurrentUser.getEmail()).entrySet()) {
            requestList.add(entry.getValue());
        }

        return requestList;
    }

    /**
     * @return Lista que contém todas as publicações registadas no servidor, que podem pertencer ao utlizador que .
     * se encontra autênticado.
     * @throws RemoteException
     */
    @Override
    public ArrayList<Publication> getCandidatePublications() throws RemoteException {
        ArrayList<Publication> requestList = new ArrayList<>();

        for (Map.Entry<String, Publication> entry : serverManager.getPublicationsManager().getPublications().entrySet())
            if (entry.getValue().getAuthors().contains(serverManager.getUsers().mCurrentUser.getName()))
                requestList.add(entry.getValue());

        return requestList;
    }

    /**
     * Método que processa um conjunto de publicações candidatas e tenta adicioná-las à lista de publicações do autor.
     * @param publicationsIndexes
     * @return
     * @throws RemoteException
     */
    @Override
    public MessageStatus addCandidatePublication(ArrayList<Integer> publicationsIndexes) throws RemoteException {
        ArrayList<Publication> candidatePublications = getCandidatePublications();

        if (publicationsIndexes.isEmpty())
            return new MessageStatus(MESSAGE_STATUS.SUCCESS, "No candidates received.");

        StringBuilder message = new StringBuilder();

        for (Integer index : publicationsIndexes) {
            if (serverManager.getPublicationsManager().isPublicationsRegisteredByUser(serverManager.getUsers().mCurrentUser.getEmail(),
                    candidatePublications.get(index).getId()))
                message.append(String.format("Publication %d already registered\n", index));
            else if (!serverManager.getPublicationsManager().getPublications().get(candidatePublications.get(index).getId())
                    .getAuthors().contains(serverManager.getUsers().mCurrentUser.getName()))
                message.append(String.format("Publication %d not registered under the current author.\n", index));
            else {
                serverManager.getPublicationsManager().addPublication(serverManager.getUsers().mCurrentUser, candidatePublications.get(index));
                message.append(String.format("Publication %d added.\n", index));
            }
        }

        return new MessageStatus(MESSAGE_STATUS.SUCCESS, message.toString());
    }

    /**
     * Processa as estatisticas do autor e envia o resultado da operação.
     * @return {@link AuthorStatistics}
     * @throws RemoteException
     */
    @Override
    public AuthorStatistics getAuthorStatistics() throws RemoteException {
        ArrayList<Publication> requestList = getPublicationsByUser();
        requestList.sort(new CitationsSorter());
        Collections.reverse(requestList);
        Integer totalCitations = 0;
        Integer h = requestList.size();
        Integer i = 0;
        boolean foundHIndex = false;

        for (Publication publication : requestList) {
            totalCitations += publication.getAmountOfCitations();

            if (publication.getAmountOfCitations() >= 10)
                i += 1;

            if (publication.getAmountOfCitations() >= h && !foundHIndex)
                foundHIndex = true;
            else if (!foundHIndex)
                h -= 1;
        }

        return new AuthorStatistics(totalCitations, h, i);
    }

    /**
     * Apaga uma publicação e envia o resultado da operação.
     * @param publicationId identificador da publicação
     * @return {@link MessageStatus}
     * @throws RemoteException
     */
    @Override
    public MessageStatus deletePublication(String publicationId) throws RemoteException {
        if (serverManager.getPublicationsManager()
                .getPublicationsByUser(serverManager.getUsers().mCurrentUser.getEmail()) //null
                .get(publicationId) == null)
            return new MessageStatus(MESSAGE_STATUS.NO_SUCH_PUBLICATION,
                    String.format("The publication with id %s isn't registered under this author.", publicationId));
        else {
            serverManager.getPublicationsManager().getPublicationsByUser(serverManager.getUsers().mCurrentUser.getEmail())
                    .remove(publicationId);
            serverManager.getPublicationsManager().getPublications().remove(publicationId);
            return new MessageStatus(MESSAGE_STATUS.SUCCESS, "Publication deleted.");
        }
    }

    /**
     * Remove o utilizador que estava autênticado.
     * @throws RemoteException
     */
    @Override
    public void logout() throws RemoteException {
        serverManager.getUsers().mCurrentUser = null;
    }

    /**
     * Guarda todos os dados relevantes em ficheiros.
     * @throws IOException
     */
    @Override
    public void saveData() throws IOException {
        serverManager.getUsers().save();
        serverManager.getPublicationsManager().save();
    }

    /**
     * Lê os dados que estão guardados em ficheiros.
     * @throws IOException
     */
    @Override
    public void loadData() throws IOException, ClassNotFoundException {
        serverManager.getUsers().load();
        serverManager.getPublicationsManager().load();
    }
}
