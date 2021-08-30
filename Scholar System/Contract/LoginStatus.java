package Contract;

import java.io.Serializable;

/**
 * Classe que representa a resposta a um pedido de autênticação.
 */
public class LoginStatus implements Serializable {
    private final LOGIN_STATUS LOGIN_STATUS;
    private final String MESSAGE;
    private final String USER_NAME;

    public LoginStatus(LOGIN_STATUS loginStatus, String message, String userName) {
        LOGIN_STATUS = loginStatus;
        MESSAGE = message;
        USER_NAME = userName;
    }

    public LOGIN_STATUS getLoginStatus() {
        return LOGIN_STATUS;
    }

    public String getMessage() {
        return MESSAGE;
    }

    public String getUserName() {
        return USER_NAME;
    }
}
