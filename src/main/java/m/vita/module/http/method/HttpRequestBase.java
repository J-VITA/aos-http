package m.vita.module.http.method;

import java.net.URI;

import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.config.RequestConfig;
import m.vita.module.http.header.HttpUriRequest;
import m.vita.module.http.header.RequestLine;
import m.vita.module.http.message.BasicRequestLine;
import m.vita.module.http.params.HttpProtocolParams;
import m.vita.module.http.util.ProtocolVersion;

@NotThreadSafe
public abstract class HttpRequestBase extends AbstractExecutionAwareRequest
        implements HttpUriRequest, Configurable {

    private ProtocolVersion version;
    private URI uri;
    private RequestConfig config;

    public abstract String getMethod();

    /**
     * @since 4.3
     */
    public void setProtocolVersion(final ProtocolVersion version) {
        this.version = version;
    }

    public ProtocolVersion getProtocolVersion() {
        return version != null ? version : HttpProtocolParams.getVersion(getParams());
    }

    /**
     * Returns the original request URI.
     * <p>
     * Please note URI remains unchanged in the course of request execution and
     * is not updated if the request is redirected to another location.
     */
    public URI getURI() {
        return this.uri;
    }

    public RequestLine getRequestLine() {
        final String method = getMethod();
        final ProtocolVersion ver = getProtocolVersion();
        final URI uri = getURI();
        String uritext = null;
        if (uri != null) {
            uritext = uri.toASCIIString();
        }
        if (uritext == null || uritext.length() == 0) {
            uritext = "/";
        }
        return new BasicRequestLine(method, uritext, ver);
    }


    public RequestConfig getConfig() {
        return config;
    }

    public void setConfig(final RequestConfig config) {
        this.config = config;
    }

    public void setURI(final URI uri) {
        this.uri = uri;
    }

    /**
     * @since 4.2
     */
    public void started() {
    }

    /**
     * A convenience method to simplify migration from HttpClient 3.1 API. This method is
     * equivalent to {@link #reset()}.
     *
     * @since 4.2
     */
    public void releaseConnection() {
        reset();
    }

    @Override
    public String toString() {
        return getMethod() + " " + getURI() + " " + getProtocolVersion();
    }

}
