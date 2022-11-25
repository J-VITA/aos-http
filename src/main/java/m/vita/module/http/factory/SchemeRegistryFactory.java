package m.vita.module.http.factory;

import m.vita.module.http.annotation.ThreadSafe;
import m.vita.module.http.client.Scheme;

@ThreadSafe
public final class SchemeRegistryFactory {

    /**
     * Initializes default scheme registry based on JSSE defaults. System properties will
     * not be taken into consideration.
     */
    public static SchemeRegistry createDefault() {
        final SchemeRegistry registry = new SchemeRegistry();
        registry.register(
                new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        registry.register(
                new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
        return registry;
    }

    /**
     * Initializes default scheme registry using system properties as described in
     * <a href="http://download.oracle.com/javase/1,5.0/docs/guide/security/jsse/JSSERefGuide.html">
     * "JavaTM Secure Socket Extension (JSSE) Reference Guide for the JavaTM 2 Platform
     * Standard Edition 5</a>
     * <p>
     * The following system properties are taken into account by this method:
     * <ul>
     *  <li>ssl.TrustManagerFactory.algorithm</li>
     *  <li>javax.net.ssl.trustStoreType</li>
     *  <li>javax.net.ssl.trustStore</li>
     *  <li>javax.net.ssl.trustStoreProvider</li>
     *  <li>javax.net.ssl.trustStorePassword</li>
     *  <li>java.home</li>
     *  <li>ssl.KeyManagerFactory.algorithm</li>
     *  <li>javax.net.ssl.keyStoreType</li>
     *  <li>javax.net.ssl.keyStore</li>
     *  <li>javax.net.ssl.keyStoreProvider</li>
     *  <li>javax.net.ssl.keyStorePassword</li>
     * </ul>
     * <p>
     *
     * @since 4.2
     */
    public static SchemeRegistry createSystemDefault() {
        final SchemeRegistry registry = new SchemeRegistry();
        registry.register(
                new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        registry.register(
                new Scheme("https", 443, SSLSocketFactory.getSystemSocketFactory()));
        return registry;
    }
}

