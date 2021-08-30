package Server;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String mName, mEmail, mPassword, mAffiliation;

    public User(String name, String email, String password, String affiliation) {
        mName = name;
        mEmail = email;
        mPassword = password;
        mAffiliation = affiliation;
    }

    public String getName() {
        return mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getAffiliation() {
        return mAffiliation;
    }
}
