package m.vita.module.http.connect;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * DNS resolver that uses the default OS implementation for resolving host names.
 *
 */
public class SystemDefaultDnsResolver implements DnsResolver {

    public static final SystemDefaultDnsResolver INSTANCE = new SystemDefaultDnsResolver();

    public InetAddress[] resolve(final String host) throws UnknownHostException {
        return InetAddress.getAllByName(host);
    }

}
