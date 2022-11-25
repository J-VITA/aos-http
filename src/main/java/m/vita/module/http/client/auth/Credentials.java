package m.vita.module.http.client.auth;

import java.security.Principal;

public interface Credentials {

    Principal getUserPrincipal();

    String getPassword();

}
