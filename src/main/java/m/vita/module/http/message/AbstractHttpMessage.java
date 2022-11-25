package m.vita.module.http.message;

import m.vita.module.http.HttpMessage;
import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.header.BasicHttpParams;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HeaderIterator;
import m.vita.module.http.header.HttpParams;
import m.vita.module.http.util.Args;

@NotThreadSafe
public abstract class AbstractHttpMessage implements HttpMessage {

    protected HeaderGroup headergroup;

    @Deprecated
    protected HttpParams params;

    /**
     * @deprecated (4.3) use {@link AbstractHttpMessage#AbstractHttpMessage()}
     */
    @Deprecated
    protected AbstractHttpMessage(final HttpParams params) {
        super();
        this.headergroup = new HeaderGroup();
        this.params = params;
    }

    protected AbstractHttpMessage() {
        this(null);
    }

    // non-javadoc, see interface HttpMessage
    public boolean containsHeader(final String name) {
        return this.headergroup.containsHeader(name);
    }

    // non-javadoc, see interface HttpMessage
    public Header[] getHeaders(final String name) {
        return this.headergroup.getHeaders(name);
    }

    // non-javadoc, see interface HttpMessage
    public Header getFirstHeader(final String name) {
        return this.headergroup.getFirstHeader(name);
    }

    // non-javadoc, see interface HttpMessage
    public Header getLastHeader(final String name) {
        return this.headergroup.getLastHeader(name);
    }

    // non-javadoc, see interface HttpMessage
    public Header[] getAllHeaders() {
        return this.headergroup.getAllHeaders();
    }

    // non-javadoc, see interface HttpMessage
    public void addHeader(final Header header) {
        this.headergroup.addHeader(header);
    }

    // non-javadoc, see interface HttpMessage
    public void addHeader(final String name, final String value) {
        Args.notNull(name, "Header name");
        this.headergroup.addHeader(new BasicHeader(name, value));
    }

    // non-javadoc, see interface HttpMessage
    public void setHeader(final Header header) {
        this.headergroup.updateHeader(header);
    }

    // non-javadoc, see interface HttpMessage
    public void setHeader(final String name, final String value) {
        Args.notNull(name, "Header name");
        this.headergroup.updateHeader(new BasicHeader(name, value));
    }

    // non-javadoc, see interface HttpMessage
    public void setHeaders(final Header[] headers) {
        this.headergroup.setHeaders(headers);
    }

    // non-javadoc, see interface HttpMessage
    public void removeHeader(final Header header) {
        this.headergroup.removeHeader(header);
    }

    // non-javadoc, see interface HttpMessage
    public void removeHeaders(final String name) {
        if (name == null) {
            return;
        }
        for (final HeaderIterator i = this.headergroup.iterator(); i.hasNext(); ) {
            final Header header = i.nextHeader();
            if (name.equalsIgnoreCase(header.getName())) {
                i.remove();
            }
        }
    }

    // non-javadoc, see interface HttpMessage
    public HeaderIterator headerIterator() {
        return this.headergroup.iterator();
    }

    // non-javadoc, see interface HttpMessage
    public HeaderIterator headerIterator(final String name) {
        return this.headergroup.iterator(name);
    }

    /**
     * @deprecated (4.3) use constructor parameters of configuration API provided by HttpClient
     */
    @Deprecated
    public HttpParams getParams() {
        if (this.params == null) {
            this.params = new BasicHttpParams();
        }
        return this.params;
    }

    /**
     * @deprecated (4.3) use constructor parameters of configuration API provided by HttpClient
     */
    @Deprecated
    public void setParams(final HttpParams params) {
        this.params = Args.notNull(params, "HTTP parameters");
    }
}
