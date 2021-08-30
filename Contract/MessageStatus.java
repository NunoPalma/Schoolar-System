package Contract;

import java.io.Serializable;

/**
 * Classe que representa a resposta a um pedido de operações remotas.
 */
public class MessageStatus implements Serializable {
    private MESSAGE_STATUS status;
    private String message;

    public MessageStatus(MESSAGE_STATUS status, String message) {
        this.status = status;
        this.message = message;
    }

    public MESSAGE_STATUS getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
