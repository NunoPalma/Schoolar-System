package Server.publications;

import Server.User;

import java.io.*;
import java.util.HashMap;

public class PublicationsManager {
    HashMap<String, Publication> mPublications;
    HashMap<String, HashMap<String, Publication>> mUserPublications;

    public PublicationsManager() {
        mPublications = new HashMap<>();
        mUserPublications = new HashMap<>();
    }

    public boolean isPublicationsRegistered(String id) {
        return mPublications.containsKey(id);
    }

    public boolean isPublicationsRegisteredByUser(String email, String id) {
        return mUserPublications.get(email).containsKey(id);
    }

    public void addPublication(User user, Publication publication) {
        mPublications.putIfAbsent(publication.getId(), publication);
        mUserPublications.computeIfAbsent(user.getEmail(), k -> new HashMap<>());
        mUserPublications.get(user.getEmail()).putIfAbsent(publication.getId(), publication);
    }

    public HashMap<String, Publication> getPublicationsByUser(String email) {
        return mUserPublications.get(email);
    }

    public HashMap<String, Publication> getPublications() {
        return mPublications;
    }

    public void save() throws IOException {
        FileOutputStream fout = new FileOutputStream("publications.data");
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(mPublications);

        oos.close();

        fout = new FileOutputStream("user_publications.data");
        oos = new ObjectOutputStream(fout);
        oos.writeObject(mUserPublications);

        oos.close();
    }

    public void load() throws IOException, ClassNotFoundException {
        File publicationsFile = new File("publications.data");
        File userPublicationsFile = new File("user_publications.data");

        if (publicationsFile.createNewFile())
            System.out.println("Creating new data store file.");

        if (userPublicationsFile.createNewFile())
            System.out.println("Creating new data store file.");

        FileInputStream fileInputStream = new FileInputStream(publicationsFile);

        if (fileInputStream.available() > 0) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            try {
                mPublications = (HashMap<String, Publication>) objectInputStream.readObject();
            } catch (EOFException eofe) {

            }

            objectInputStream.close();
        }

        FileInputStream userPublicationsFileInputStream = new FileInputStream(userPublicationsFile);

        if (userPublicationsFileInputStream.available() > 0) {
            ObjectInputStream objectInputStream = new ObjectInputStream(userPublicationsFileInputStream);

            mUserPublications = (HashMap<String, HashMap<String, Publication>>) objectInputStream.readObject();

            objectInputStream.close();
        }
    }
}
