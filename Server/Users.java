package Server;

import java.io.*;
import java.util.HashMap;

public class Users implements Serializable {

    HashMap<String, User> mUsers = new HashMap<>(); // Utilizadores que estão "registados" no servidor e podem aceder às suas funcionalidades
    public User mCurrentUser;


    Users() {
    }

    /**
     * @return true se o utilizador estiver "registado" no servidor, false caso contrário
     */
    public boolean userExists(String user) {
        return mUsers.containsKey(user);
    }

    /**
     * Processa a validação das credênciais do utilizador.
     *
     * @param user     Utilizador cuja password vai ser validada
     * @param password Password a ser validada
     * @return true se as credênciais forem válidas, false caso contrário
     */
    public boolean isPasswordValid(String user, String password) {
        return userExists(user) && mUsers.get(user).getPassword().equals(password);
    }

    /**
     * Altera o utilizador que está autênticado.
     * @param user Utilizador que está autênticado num dado momento.
     */
    public void setCurrentUser(User user) {
        mCurrentUser = user;
    }

    public void save() throws IOException {
        FileOutputStream fout = new FileOutputStream("users.data");
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(mUsers);

        oos.close();

        fout = new FileOutputStream("current_user.data");
        oos = new ObjectOutputStream(fout);
        oos.writeObject(mCurrentUser);

        oos.close();
    }

    public void load() throws IOException, ClassNotFoundException {
        File userFile = new File("users.data");
        File currentUser = new File("current_user.data");

        if (userFile.createNewFile())
            System.out.println("Creating new data store file.");

        if (currentUser.createNewFile())
            System.out.println("Creating new data store file.");
        
        FileInputStream fileInputStream = new FileInputStream(userFile);

        if (fileInputStream.available() == 0)
            return;

        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        mUsers = (HashMap<String, User>) objectInputStream.readObject();

        objectInputStream.close();

        fileInputStream = new FileInputStream(currentUser);

        objectInputStream = new ObjectInputStream(fileInputStream);

        if (fileInputStream.available() == 0)
            return;

        mCurrentUser = (User) objectInputStream.readObject();

        objectInputStream.close();
    }
}
