package m.vita.module.http.client.execchain;

import java.io.IOException;
import java.util.Locale;

import m.vita.module.http.HttpEntity;
import m.vita.module.http.HttpResponse;
import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.execute.CloseableHttpResponse;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HeaderIterator;
import m.vita.module.http.header.HttpParams;
import m.vita.module.http.util.ProtocolVersion;
import m.vita.module.http.util.StatusLine;

@NotThreadSafe
class HttpResponseProxy implements CloseableHttpResponse {

    private final HttpResponse original;
    private final ConnectionHolder connHolder;

    public HttpResponseProxy(final HttpResponse original, final ConnectionHolder connHolder) {
        this.original = original;
        this.connHolder = connHolder;
        ResponseEntityProxy.enchance(original, connHolder);
    }

    public void close() throws IOException {
        if (this.connHolder != null) {
            this.connHolder.abortConnection();
        }
    }

    public StatusLine getStatusLine() {
        return original.getStatusLine();
    }

    public void setStatusLine(final StatusLine statusline) {
        original.setStatusLine(statusline);
    }

    public void setStatusLine(final ProtocolVersion ver, final int code) {
        original.setStatusLine(ver, code);
    }

    public void setStatusLine(final ProtocolVersion ver, final int code, final String reason) {
        original.setStatusLine(ver, code, reason);
    }

    public void setStatusCode(final int code) throws IllegalStateException {
        original.setStatusCode(code);
    }

    public void setReasonPhrase(final String reason) throws IllegalStateException {
        original.setReasonPhrase(reason);
    }

    public HttpEntity getEntity() {
        return original.getEntity();
    }

    public void setEntity(final HttpEntity entity) {
        original.setEntity(entity);
    }

    public Locale getLocale() {
        return original.getLocale();
    }

    public void setLocale(final Locale loc) {
        original.setLocale(loc);
    }

    public ProtocolVersion getProtocolVersion() {
        return original.getProtocolVersion();
    }

    public boolean containsHeader(final String name) {
        return original.containsHeader(name);
    }

    public Header[] getHeaders(final String name) {
        return original.getHeaders(name);
    }

    public Header getFirstHeader(final String name) {
        return original.getFirstHeader(name);
    }

    public Header getLastHeader(final String name) {
        return original.getLastHeader(name);
    }

    public Header[] getAllHeaders() {
        return original.getAllHeaders();
    }

    public void addHeader(final Header header) {
        original.addHeader(header);
    }

    public void addHeader(final String name, final String value) {
        original.addHeader(name, value);
    }

    public void setHeader(final Header header) {
        original.setHeader(header);
    }

    public void setHeader(final String name, final String value) {
        original.setHeader(name, value);
    }

    public void setHeaders(final Header[] headers) {
        original.setHeaders(headers);
    }

    public void removeHeader(final Header header) {
        original.removeHeader(header);
    }

    public void removeHeaders(final String name) {
        original.removeHeaders(name);
    }

    public HeaderIterator headerIterator() {
        return original.headerIterator();
    }

    public HeaderIterator headerIterator(final String name) {
        return original.headerIterator(name);
    }

    @Deprecated
    public HttpParams getParams() {
        return original.getParams();
    }

    @Deprecated
    public void setParams(final HttpParams params) {
        original.setParams(params);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HttpResponseProxy{");
        sb.append(original);
        sb.append('}');
        return sb.toString();
    }

}
