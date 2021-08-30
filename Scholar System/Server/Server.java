package Server;

import Contract.IRemoteContract;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class Server {


    public static void main(String args[]) throws IOException, AlreadyBoundException, ClassNotFoundException {
        final Integer PORT = 1248;
        IRemoteContract stub = new RemoteContract();
        stub.loadData();

        try {

            LocateRegistry.createRegistry(PORT);
            Naming.bind("rmi://127.0.0.1:1248/scholarsystem", stub);

            System.out.println("Server started on port " + PORT);

            Thread thread = new Thread(() -> {
                try {
                    checkForServerShutdown(stub);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            thread.start();
        } catch (RemoteException re) {
            stub.saveData();
        }
    }

    /**
     * CTRL+D fecha o servidor.
     * @param stub
     * @throws IOException
     * @throws NoClassDefFoundError
     */
    private static void checkForServerShutdown(IRemoteContract stub) throws IOException, NoClassDefFoundError {
        Scanner scanner = new Scanner(System.in);

        while (true) {
           try {
               scanner.nextLine();
           } catch (NoSuchElementException nsee) {
               stub.saveData();
               System.exit(0);
           }
        }
    }
}