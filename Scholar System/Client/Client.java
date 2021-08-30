package Client;

import Contract.*;
import Server.publications.Publication;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    public static void main(String args[]) throws IOException, NotBoundException, InterruptedException {
        initProcess();

    }

    private static void initProcess() throws IOException, NotBoundException, InterruptedException {
        ClientManager clientManager = new ClientManager();

        while (true) {
            printMenu();
            String choice = evaluateChoice();

            switch (choice) {
                case "1":
                    register(clientManager);
                    break;
                case "2":
                    authenticate(clientManager);
                    break;
                case "3":
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }

    private static void proceedToAuthenticatedView(ClientManager clientManager, String userName) throws IOException, NotBoundException, InterruptedException {

        while (true) {
            printAuthenticatedMenu(userName);
            String choice = evaluateChoice();
            switch (choice) {
                case "1":
                    ArrayList<Publication> publicationsByPublishYear;
                    try {
                        publicationsByPublishYear = clientManager.getRemoteObject().getPublicationByPublishYear();
                    } catch (RemoteException re) {
                        clientManager.reconnect();
                        publicationsByPublishYear = clientManager.getRemoteObject().getPublicationByPublishYear();
                    }
                    printPublicationList(publicationsByPublishYear);
                    break;
                case "2":
                    ArrayList<Publication> publicationsByCitations;
                    try {
                        publicationsByCitations = clientManager.getRemoteObject().getPublicationByCitations();
                    } catch (RemoteException re) {
                        clientManager.reconnect();
                        publicationsByCitations = clientManager.getRemoteObject().getPublicationByCitations();
                    }
                    printPublicationList(publicationsByCitations);
                    break;
                case "3":
                    proceedToAddNewPublication(clientManager);
                    break;
                case "4":
                    processCandidatePublicationsRequest(clientManager);
                    break;
                case "5":
                    removePublication(clientManager);
                    break;
                case "6":
                    showAuthorStatistics(clientManager);
                    break;
                case "7":
                    try {
                        clientManager.getRemoteObject().logout();
                    } catch (RemoteException re) {
                        clientManager.reconnect();
                        clientManager.getRemoteObject().logout();
                    }
                    initProcess();
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }

    }

    private static void processCandidatePublicationsRequest(ClientManager clientManager) throws RemoteException, MalformedURLException, NotBoundException, InterruptedException {
        ArrayList<Publication> publicationsList;

        try {
            publicationsList = clientManager.getRemoteObject().getCandidatePublications();
        } catch (RemoteException re) {
            clientManager.reconnect();
            publicationsList = clientManager.getRemoteObject().getCandidatePublications();
        }

        if (publicationsList.isEmpty()) {
            System.out.println("There are no candidate publications at this moment.");
            return;
        }

        System.out.println("Suggested Publications:");
        System.out.println("---------->Publications<----------\n");
        for(int i = 0; i <= publicationsList.size() - 1; i++)
            System.out.println(i + "-> " + publicationsList.get(i).toString());

        System.out.println("\n");


        System.out.println("Which ones would you like to add? (Separated by \";\" Invalid inputs will be ignored.)");
        Scanner scanner = new Scanner(System.in);

        String indexes = scanner.nextLine();
        ArrayList<Integer> requestedIndexes = new ArrayList<>();

        for (String value: indexes.split(";")) {
            try {
                Integer index = Integer.valueOf(value);
                if (index >= publicationsList.size())
                    continue;
                requestedIndexes.add(index);
            } catch (NumberFormatException nfe)  {

            }
        }

        MessageStatus messageStatus;

        try {
            messageStatus = clientManager.getRemoteObject().addCandidatePublication(requestedIndexes);
        } catch (RemoteException re) {
            clientManager.reconnect();
            messageStatus = clientManager.getRemoteObject().addCandidatePublication(requestedIndexes);
        }

        System.out.println(messageStatus.getMessage());
    }

    private static void showAuthorStatistics(ClientManager clientManager) throws RemoteException, MalformedURLException, NotBoundException, InterruptedException {
        AuthorStatistics authorStatistics;
        try {
            authorStatistics = clientManager.getRemoteObject().getAuthorStatistics();

        } catch (RemoteException re) {
            clientManager.reconnect();

            authorStatistics = clientManager.getRemoteObject().getAuthorStatistics();

        }

        System.out.println(authorStatistics.toString());

    }

    private static void removePublication(ClientManager clientManager) throws RemoteException, InterruptedException, MalformedURLException, NotBoundException {
        System.out.println("Input the publication's DOI:");
        Scanner scanner = new Scanner(System.in);

        try {
            if (!printPublicationList(clientManager.getRemoteObject().getPublicationByPublishYear()))
                return;
        } catch (ConnectException ce) {
            clientManager.reconnect();

            printPublicationList(clientManager.getRemoteObject().getPublicationByPublishYear());
        }

        String message = scanner.nextLine();
        MessageStatus messageStatus;

        try {
            messageStatus = clientManager.getRemoteObject().deletePublication(message);
        } catch (RemoteException re) {
            clientManager.reconnect();
            messageStatus = clientManager.getRemoteObject().deletePublication(message);
        }

        System.out.println(messageStatus.getMessage());
    }

    private static boolean printPublicationList(ArrayList<Publication> publications) {
        if (publications.isEmpty()) {
            System.out.println("You have no publications.");
            return false;
        }
        else {
            for (Publication publication : publications)
                System.out.println(publication.toString());
        }
        return true;
    }

    private static void proceedToAddNewPublication(ClientManager clientManager) throws RemoteException, MalformedURLException, NotBoundException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Introduza o nome dos autores. Separando por \";\". Por exemplo \"Apelido1, Nome1; ...; ApelidoN, NomeN\"");

        String authors = scanner.nextLine();

        System.out.println("Introduza o título:");

        String title = scanner.nextLine();

        System.out.println("Introduza o ano de publicação:");

        String publishYear = scanner.nextLine();

        System.out.println("Introduza a revista:");

        String magazine = scanner.nextLine();

        System.out.println("Introduza o volume:");

        String volume = scanner.nextLine();

        System.out.println("Introduza o número:");

        String number = scanner.nextLine();

        System.out.println("Introduza as páginas. Por exemplo “Página de Início–Página de fim:");

        String pages = scanner.nextLine();

        System.out.println("Introduza o número de citações:");

        String citations = scanner.nextLine();

        System.out.println("Introduza o identificador único:");

        String id = scanner.nextLine();

        MessageStatus messageStatus;
        try {
            messageStatus = clientManager.getRemoteObject().addPublication(authors, title, publishYear, magazine, volume, number, pages, citations, id);
        } catch (RemoteException re) {
            clientManager.reconnect();
            messageStatus = clientManager.getRemoteObject().addPublication(authors, title, publishYear, magazine, volume, number, pages, citations, id);
        }

        System.out.println(messageStatus.getMessage());
    }

    private static void printAuthenticatedMenu(String userName) {
        System.out.printf("\n========== %s Menu ==========%n", userName);
        System.out.println("1-List Publication by year (Newest first)");
        System.out.println("2-List Publication by citations (Most cited first)");
        System.out.println("3-Add Publication");
        System.out.println("4-Look for author Publications in database");
        System.out.println("5-Remove Publication");
        System.out.println("6-Show author Statistics");
        System.out.println("7-Logout");
        System.out.println("====================");
        System.out.println("Option:");
    }

    private static String evaluateChoice() {
        return new Scanner(System.in).nextLine();
    }

    private static void printMenu() {
        System.out.println("==========Scholar System==========");
        System.out.println("1-Register new author");
        System.out.println("2-Login");
        System.out.println("3-Exit");
        System.out.println("Option?");

    }

    static void authenticate(ClientManager clientManager) throws IOException, NotBoundException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=====================================\nIntroduza Login (Email):");

        String email = scanner.nextLine();

        System.out.println("Introduza Password:");

        String password = scanner.nextLine();

        LoginStatus loginStatus;

        try {
            loginStatus = clientManager.getRemoteObject().login(email, password);
        } catch (ConnectException ce) {
            clientManager.reconnect();

            loginStatus = clientManager.getRemoteObject().login(email, password);
        }

        System.out.println("\n" + loginStatus.getMessage() + "\n");

        if (loginStatus.getLoginStatus() == LOGIN_STATUS.SUCCESS)
            proceedToAuthenticatedView(clientManager, loginStatus.getUserName());

    }

    static void register(ClientManager clientManager) throws IOException, NotBoundException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=====================================\nIntroduza o seu nome:");

        String name = scanner.nextLine();

        System.out.println("Introduza o seu email:");

        String email = scanner.nextLine();

        System.out.println("Introduza Password:");

        String password = scanner.nextLine();

        System.out.println("Introduza a sua a afiliação:");

        String affiliation = scanner.nextLine();

        try {
            MessageStatus messageStatus = clientManager.getRemoteObject().register(name, email, password, affiliation);

            System.out.println(messageStatus.getMessage() + "\n");
        } catch (ConnectException ce) {
            clientManager.reconnect();

            MessageStatus messageStatus = clientManager.getRemoteObject().register(name, email, password, affiliation);

            System.out.println(messageStatus.getMessage() + "\n");
        }
    }
}