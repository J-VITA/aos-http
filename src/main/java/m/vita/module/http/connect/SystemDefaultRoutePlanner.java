package m.vita.module.http.connect;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.exception.HttpException;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpHost;
import m.vita.module.http.header.HttpRequest;

@Immutable
public class SystemDefaultRoutePlanner extends DefaultRoutePlanner {

    private final ProxySelector proxySelector;

    public SystemDefaultRoutePlanner(
            final SchemePortResolver schemePortResolver,
            final ProxySelector proxySelector) {
        super(schemePortResolver);
        this.proxySelector = proxySelector != null ? proxySelector : ProxySelector.getDefault();
    }

    public SystemDefaultRoutePlanner(final ProxySelector proxySelector) {
        this(null, proxySelector);
    }

    @Override
    protected HttpHost determineProxy(
            final HttpHost    target,
            final HttpRequest request,
            final HttpContext context) throws HttpException {
        final URI targetURI;
        try {
            targetURI = new URI(target.toURI());
        } catch (final URISyntaxException ex) {
            throw new HttpException("Cannot convert host to URI: " + target, ex);
        }
        final List<Proxy> proxies = this.proxySelector.select(targetURI);
        final Proxy p = chooseProxy(proxies);
        HttpHost result = null;
        if (p.type() == Proxy.Type.HTTP) {
            // convert the socket address to an HttpHost
            if (!(p.address() instanceof InetSocketAddress)) {
                throw new HttpException("Unable to handle non-Inet proxy address: " + p.address());
            }
            final InetSocketAddress isa = (InetSocketAddress) p.address();
            // assume default scheme (http)
            result = new HttpHost(getHost(isa), isa.getPort());
        }

        return result;
    }

    private String getHost(final InetSocketAddress isa) {

        //@@@ Will this work with literal IPv6 addresses, or do we
        //@@@ need to wrap these in [] for the string representation?
        //@@@ Having it in this method at least allows for easy workarounds.
        return isa.isUnresolved() ?
                isa.getHostName() : isa.getAddress().getHostAddress();

    }

    private Proxy chooseProxy(final List<Proxy> proxies) {
        Proxy result = null;
        // check the list for one we can use
        for (int i=0; (result == null) && (i < proxies.size()); i++) {
            final Proxy p = proxies.get(i);
            switch (p.type()) {

                case DIRECT:
                case HTTP:
                    result = p;
                    break;

                case SOCKS:
                    // SOCKS hosts are not handled on the route level.
                    // The socket may make use of the SOCKS host though.
                    break;
            }
        }
        if (result == null) {
            //@@@ log as warning or info that only a socks proxy is available?
            // result can only be null if all proxies are socks proxies
            // socks proxies are not handled on the route planning level
            result = Proxy.NO_PROXY;
        }
        return result;
    }

}
