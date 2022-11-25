package m.vita.module.http.resolve;

import java.io.IOException;
import java.net.InetAddress;

public interface HostNameResolver {

    /**
     * Resolves given hostname to its IP address
     *
     * @param hostname the hostname.
     * @return IP address.
     * @throws IOException
     */
    InetAddress resolve (String hostname) throws IOException;

}