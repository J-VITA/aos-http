package m.vita.module.http.client.protocol;

import java.io.IOException;
import java.net.InetAddress;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.connect.HttpConnection;
import m.vita.module.http.exception.HttpException;
import m.vita.module.http.exception.ProtocolException;
import m.vita.module.http.execute.HttpCoreContext;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpHost;
import m.vita.module.http.header.HttpInetConnection;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.interceptor.HttpRequestInterceptor;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.HTTP;
import m.vita.module.http.util.HttpVersion;
import m.vita.module.http.util.ProtocolVersion;

@Immutable
public class RequestTargetHost implements HttpRequestInterceptor {

    public RequestTargetHost() {
        super();
    }

    public void process(final HttpRequest request, final HttpContext context)
            throws HttpException, IOException {
        Args.notNull(request, "HTTP request");

        final HttpCoreContext corecontext = HttpCoreContext.adapt(context);

        final ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
        final String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("CONNECT") && ver.lessEquals(HttpVersion.HTTP_1_0)) {
            return;
        }

        if (!request.containsHeader(HTTP.TARGET_HOST)) {
            HttpHost targethost = corecontext.getTargetHost();
            if (targethost == null) {
                final HttpConnection conn = corecontext.getConnection();
                if (conn instanceof HttpInetConnection) {
                    // Populate the context with a default HTTP host based on the
                    // inet address of the target host
                    final InetAddress address = ((HttpInetConnection) conn).getRemoteAddress();
                    final int port = ((HttpInetConnection) conn).getRemotePort();
                    if (address != null) {
                        targethost = new HttpHost(address.getHostName(), port);
                    }
                }
                if (targethost == null) {
                    if (ver.lessEquals(HttpVersion.HTTP_1_0)) {
                        return;
                    } else {
                        throw new ProtocolException("Target host missing");
                    }
                }
            }
            request.addHeader(HTTP.TARGET_HOST, targethost.toHostString());
        }
    }

}
