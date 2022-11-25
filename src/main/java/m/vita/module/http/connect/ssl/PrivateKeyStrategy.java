package m.vita.module.http.connect.ssl;

import java.net.Socket;
import java.util.Map;

public interface PrivateKeyStrategy {

    /**
     * Determines what key material to use for SSL authentication.
     */
    String chooseAlias(Map<String, PrivateKeyDetails> aliases, Socket socket);

}