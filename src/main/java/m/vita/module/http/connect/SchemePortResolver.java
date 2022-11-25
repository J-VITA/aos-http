package m.vita.module.http.connect;


import m.vita.module.http.exception.UnsupportedSchemeException;
import m.vita.module.http.header.HttpHost;

/**
 * Strategy for default port resolution for protocol schemes.
 *
 * @since 4.3
 */
public interface SchemePortResolver {

    /**
     * Returns the actual port for the host based on the protocol scheme.
     */
    int resolve(HttpHost host) throws UnsupportedSchemeException;

}