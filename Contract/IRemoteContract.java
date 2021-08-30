package Contract;

import Server.publications.Publication;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Interface que encapsula as operações remotas realizadas entre o servidor e cliente.
 */
public interface IRemoteContract extends Remote {

    LoginStatus login(String userName, String password) throws RemoteException;

    MessageStatus register(String name, String email, String password, String affiliation) throws RemoteException;

    MessageStatus addPublication(String authors, String title, String publishYear, String magazine,
                                 String volume, String number, String pages, String citations,
                                 String id) throws RemoteException;

    ArrayList<Publication> getPublicationByCitations() throws RemoteException;

    ArrayList<Publication> getPublicationByPublishYear() throws RemoteException;

    ArrayList<Publication> getCandidatePublications() throws RemoteException;

    MessageStatus addCandidatePublication(ArrayList<Integer> publicationsIndexes) throws RemoteException;

    AuthorStatistics getAuthorStatistics() throws RemoteException;

    MessageStatus deletePublication(String publicationId) throws RemoteException;

    void logout() throws RemoteException;

    void saveData() throws IOException;

    void loadData() throws IOException, ClassNotFoundException;
}
