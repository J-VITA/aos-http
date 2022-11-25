package m.vita.module.http.connect;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import m.vita.module.http.annotation.ThreadSafe;
import m.vita.module.http.client.ClientConnectionOperator;
import m.vita.module.http.client.OperatedClientConnection;
import m.vita.module.http.client.Scheme;
import m.vita.module.http.exception.ConnectTimeoutException;
import m.vita.module.http.factory.SchemeLayeredSocketFactory;
import m.vita.module.http.factory.SchemeRegistry;
import m.vita.module.http.factory.SchemeSocketFactory;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpHost;
import m.vita.module.http.header.HttpParams;
import m.vita.module.http.client.protocol.ClientContext;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.Asserts;
import m.vita.module.http.util.HttpClientAndroidLog;

@ThreadSafe
public class DefaultClientConnectionOperator implements ClientConnectionOperator {

    public HttpClientAndroidLog log = new HttpClientAndroidLog(getClass());

    /** The scheme registry for looking up socket factories. */
    protected final SchemeRegistry schemeRegistry; // @ThreadSafe

    /** the custom-configured DNS lookup mechanism. */
    protected final DnsResolver dnsResolver;

    /**
     * Creates a new client connection operator for the given scheme registry.
     *
     * @param schemes   the scheme registry
     *
     * @since 4.2
     */
    public DefaultClientConnectionOperator(final SchemeRegistry schemes) {
        Args.notNull(schemes, "Scheme registry");
        this.schemeRegistry = schemes;
        this.dnsResolver = new SystemDefaultDnsResolver();
    }

    /**
     * Creates a new client connection operator for the given scheme registry
     * and the given custom DNS lookup mechanism.
     *
     * @param schemes
     *            the scheme registry
     * @param dnsResolver
     *            the custom DNS lookup mechanism
     */
    public DefaultClientConnectionOperator(final SchemeRegistry schemes,final DnsResolver dnsResolver) {
        Args.notNull(schemes, "Scheme registry");

        Args.notNull(dnsResolver, "DNS resolver");

        this.schemeRegistry = schemes;
        this.dnsResolver = dnsResolver;
    }

    public OperatedClientConnection createConnection() {
        return new DefaultClientConnection();
    }

    private SchemeRegistry getSchemeRegistry(final HttpContext context) {
        SchemeRegistry reg = (SchemeRegistry) context.getAttribute(
                ClientContext.SCHEME_REGISTRY);
        if (reg == null) {
            reg = this.schemeRegistry;
        }
        return reg;
    }

    public void openConnection(
            final OperatedClientConnection conn,
            final HttpHost target,
            final InetAddress local,
            final HttpContext context,
            final HttpParams params) throws IOException {
        Args.notNull(conn, "Connection");
        Args.notNull(target, "Target host");
        Args.notNull(params, "HTTP parameters");
        Asserts.check(!conn.isOpen(), "Connection must not be open");

        final SchemeRegistry registry = getSchemeRegistry(context);
        final Scheme schm = registry.getScheme(target.getSchemeName());
        final SchemeSocketFactory sf = schm.getSchemeSocketFactory();

        final InetAddress[] addresses = resolveHostname(target.getHostName());
        final int port = schm.resolvePort(target.getPort());
        for (int i = 0; i < addresses.length; i++) {
            final InetAddress address = addresses[i];
            final boolean last = i == addresses.length - 1;

            Socket sock = sf.createSocket(params);
            conn.opening(sock, target);

            final InetSocketAddress remoteAddress = new HttpInetSocketAddress(target, address, port);
            InetSocketAddress localAddress = null;
            if (local != null) {
                localAddress = new InetSocketAddress(local, 0);
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("Connecting to " + remoteAddress);
            }
            try {
                final Socket connsock = sf.connectSocket(sock, remoteAddress, localAddress, params);
                if (sock != connsock) {
                    sock = connsock;
                    conn.opening(sock, target);
                }
                prepareSocket(sock, context, params);
                conn.openCompleted(sf.isSecure(sock), params);
                return;
            } catch (final ConnectException ex) {
                if (last) {
                    throw ex;
                }
            } catch (final ConnectTimeoutException ex) {
                if (last) {
                    throw ex;
                }
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("Connect to " + remoteAddress + " timed out. " +
                        "Connection will be retried using another IP address");
            }
        }
    }

    public void updateSecureConnection(
            final OperatedClientConnection conn,
            final HttpHost target,
            final HttpContext context,
            final HttpParams params) throws IOException {
        Args.notNull(conn, "Connection");
        Args.notNull(target, "Target host");
        Args.notNull(params, "Parameters");
        Asserts.check(conn.isOpen(), "Connection must be open");

        final SchemeRegistry registry = getSchemeRegistry(context);
        final Scheme schm = registry.getScheme(target.getSchemeName());
        Asserts.check(schm.getSchemeSocketFactory() instanceof SchemeLayeredSocketFactory,
                "Socket factory must implement SchemeLayeredSocketFactory");
        final SchemeLayeredSocketFactory lsf = (SchemeLayeredSocketFactory) schm.getSchemeSocketFactory();
        final Socket sock = lsf.createLayeredSocket(
                conn.getSocket(), target.getHostName(), schm.resolvePort(target.getPort()), params);
        prepareSocket(sock, context, params);
        conn.update(sock, target, lsf.isSecure(sock), params);
    }

    /**
     * Performs standard initializations on a newly created socket.
     *
     * @param sock      the socket to prepare
     * @param context   the context for the connection
     * @param params    the parameters from which to prepare the socket
     *
     * @throws IOException      in case of an IO problem
     */
    protected void prepareSocket(
            final Socket sock,
            final HttpContext context,
            final HttpParams params) throws IOException {
        sock.setTcpNoDelay(HttpConnectionParams.getTcpNoDelay(params));
        sock.setSoTimeout(HttpConnectionParams.getSoTimeout(params));

        final int linger = HttpConnectionParams.getLinger(params);
        if (linger >= 0) {
            sock.setSoLinger(linger > 0, linger);
        }
    }

    /**
     * Resolves the given host name to an array of corresponding IP addresses, based on the
     * configured name service on the provided DNS resolver. If one wasn't provided, the system
     * configuration is used.
     *
     * @param host host name to resolve
     * @return array of IP addresses
     * @exception UnknownHostException  if no IP address for the host could be determined.
     *
     * @see DnsResolver
     * @see SystemDefaultDnsResolver
     *
     * @since 4.1
     */
    protected InetAddress[] resolveHostname(final String host) throws UnknownHostException {
        return dnsResolver.resolve(host);
    }

}

