package m.vita.module.http.method;

import java.net.URI;

import m.vita.module.http.HttpEntity;
import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HttpEntityEnclosingRequest;
import m.vita.module.http.header.HttpParams;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.header.HttpUriRequest;
import m.vita.module.http.header.RequestLine;
import m.vita.module.http.message.AbstractHttpMessage;
import m.vita.module.http.message.BasicRequestLine;
import m.vita.module.http.util.HTTP;
import m.vita.module.http.util.ProtocolVersion;
import m.vita.module.http.config.RequestConfig;

@NotThreadSafe
public class HttpRequestWrapper extends AbstractHttpMessage implements HttpUriRequest {

    private final HttpRequest original;
    private final String method;
    private ProtocolVersion version;
    private URI uri;

    private HttpRequestWrapper(final HttpRequest request) {
        super();
        this.original = request;
        this.version = this.original.getRequestLine().getProtocolVersion();
        this.method = this.original.getRequestLine().getMethod();
        if (request instanceof HttpUriRequest) {
            this.uri = ((HttpUriRequest) request).getURI();
        } else {
            this.uri = null;
        }
        setHeaders(request.getAllHeaders());
    }

    public ProtocolVersion getProtocolVersion() {
        return this.version != null ? this.version : this.original.getProtocolVersion();
    }

    public void setProtocolVersion(final ProtocolVersion version) {
        this.version = version;
    }

    public URI getURI() {
        return this.uri;
    }

    public void setURI(final URI uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void abort() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public boolean isAborted() {
        return false;
    }

    public RequestLine getRequestLine() {
        String requestUri = null;
        if (this.uri != null) {
            requestUri = this.uri.toASCIIString();
        } else {
            requestUri = this.original.getRequestLine().getUri();
        }
        if (requestUri == null || requestUri.length() == 0) {
            requestUri = "/";
        }
        return new BasicRequestLine(this.method, requestUri, getProtocolVersion());
    }

    public HttpRequest getOriginal() {
        return this.original;
    }

    @Override
    public String toString() {
        return getRequestLine() + " " + this.headergroup;
    }

    static class HttpEntityEnclosingRequestWrapper extends HttpRequestWrapper
            implements HttpEntityEnclosingRequest {

        private HttpEntity entity;

        public HttpEntityEnclosingRequestWrapper(final HttpEntityEnclosingRequest request) {
            super(request);
            this.entity = request.getEntity();
        }

        public HttpEntity getEntity() {
            return this.entity;
        }

        public void setEntity(final HttpEntity entity) {
            this.entity = entity;
        }

        public boolean expectContinue() {
            final Header expect = getFirstHeader(HTTP.EXPECT_DIRECTIVE);
            return expect != null && HTTP.EXPECT_CONTINUE.equalsIgnoreCase(expect.getValue());
        }

    }

    public static HttpRequestWrapper wrap(final HttpRequest request) {
        if (request == null) {
            return null;
        }
        if (request instanceof HttpEntityEnclosingRequest) {
            return new HttpEntityEnclosingRequestWrapper((HttpEntityEnclosingRequest) request);
        } else {
            return new HttpRequestWrapper(request);
        }
    }

    /**
     * @deprecated (4.3) use
     *   {@link RequestConfig}.
     */
    @Override
    @Deprecated
    public HttpParams getParams() {
        if (this.params == null) {
            this.params = original.getParams().copy();
        }
        return this.params;
    }

}
