package Client;

import Contract.IRemoteContract;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class ClientManager {
    private static IRemoteContract mRemoteObject;

    public ClientManager() throws MalformedURLException, NotBoundException, RemoteException, InterruptedException {
        reconnect();
    }

    public IRemoteContract getRemoteObject() {
        return mRemoteObject;
    }

    /**
     * Tenta estabelecer uma conexão com o servidor.
     * @throws MalformedURLException
     * @throws NotBoundException
     * @throws RemoteException
     * @throws InterruptedException
     */
    public void reconnect() throws MalformedURLException, NotBoundException, RemoteException, InterruptedException {
        int attempts = 0;

        while (attempts < 10) {
            try {
                mRemoteObject = (IRemoteContract) Naming.lookup("rmi://127.0.0.1:1248/scholarsystem");
                break;
            } catch (ConnectException ce) {
                System.out.printf("\nSERVER OFF. Let's try again in 3 seconds (%d Attempts!!!)%n", ++attempts);
                Thread.sleep(3000);
            }
        }

        if (attempts == 10) {
            System.out.println("Exiting...");
            System.exit(0);
        }
    }

}
