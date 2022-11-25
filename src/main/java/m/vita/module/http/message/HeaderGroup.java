package m.vita.module.http.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HeaderIterator;
import m.vita.module.http.util.CharArrayBuffer;

@NotThreadSafe
public class HeaderGroup implements Cloneable, Serializable {

    private static final long serialVersionUID = 2608834160639271617L;

    /** The list of headers for this group, in the order in which they were added */
    private final List<Header> headers;

    /**
     * Constructor for HeaderGroup.
     */
    public HeaderGroup() {
        this.headers = new ArrayList<Header>(16);
    }

    /**
     * Removes any contained headers.
     */
    public void clear() {
        headers.clear();
    }

    /**
     * Adds the given header to the group.  The order in which this header was
     * added is preserved.
     *
     * @param header the header to add
     */
    public void addHeader(final Header header) {
        if (header == null) {
            return;
        }
        headers.add(header);
    }

    /**
     * Removes the given header.
     *
     * @param header the header to remove
     */
    public void removeHeader(final Header header) {
        if (header == null) {
            return;
        }
        headers.remove(header);
    }

    /**
     * Replaces the first occurence of the header with the same name. If no header with
     * the same name is found the given header is added to the end of the list.
     *
     * @param header the new header that should replace the first header with the same
     * name if present in the list.
     */
    public void updateHeader(final Header header) {
        if (header == null) {
            return;
        }
        // HTTPCORE-361 : we don't use the for-each syntax, i.e.
        //     for (Header header : headers)
        // as that creates an Iterator that needs to be garbage-collected
        for (int i = 0; i < this.headers.size(); i++) {
            final Header current = this.headers.get(i);
            if (current.getName().equalsIgnoreCase(header.getName())) {
                this.headers.set(i, header);
                return;
            }
        }
        this.headers.add(header);
    }

    /**
     * Sets all of the headers contained within this group overriding any
     * existing headers. The headers are added in the order in which they appear
     * in the array.
     *
     * @param headers the headers to set
     */
    public void setHeaders(final Header[] headers) {
        clear();
        if (headers == null) {
            return;
        }
        Collections.addAll(this.headers, headers);
    }

    /**
     * Gets a header representing all of the header values with the given name.
     * If more that one header with the given name exists the values will be
     * combined with a "," as per RFC 2616.
     *
     * <p>Header name comparison is case insensitive.
     *
     * @param name the name of the header(s) to get
     * @return a header with a condensed value or <code>null</code> if no
     * headers by the given name are present
     */
    public Header getCondensedHeader(final String name) {
        final Header[] hdrs = getHeaders(name);

        if (hdrs.length == 0) {
            return null;
        } else if (hdrs.length == 1) {
            return hdrs[0];
        } else {
            final CharArrayBuffer valueBuffer = new CharArrayBuffer(128);
            valueBuffer.append(hdrs[0].getValue());
            for (int i = 1; i < hdrs.length; i++) {
                valueBuffer.append(", ");
                valueBuffer.append(hdrs[i].getValue());
            }

            return new BasicHeader(name.toLowerCase(Locale.ENGLISH), valueBuffer.toString());
        }
    }

    /**
     * Gets all of the headers with the given name.  The returned array
     * maintains the relative order in which the headers were added.
     *
     * <p>Header name comparison is case insensitive.
     *
     * @param name the name of the header(s) to get
     *
     * @return an array of length >= 0
     */
    public Header[] getHeaders(final String name) {
        final List<Header> headersFound = new ArrayList<Header>();
        // HTTPCORE-361 : we don't use the for-each syntax, i.e.
        //     for (Header header : headers)
        // as that creates an Iterator that needs to be garbage-collected
        for (int i = 0; i < this.headers.size(); i++) {
            final Header header = this.headers.get(i);
            if (header.getName().equalsIgnoreCase(name)) {
                headersFound.add(header);
            }
        }

        return headersFound.toArray(new Header[headersFound.size()]);
    }

    /**
     * Gets the first header with the given name.
     *
     * <p>Header name comparison is case insensitive.
     *
     * @param name the name of the header to get
     * @return the first header or <code>null</code>
     */
    public Header getFirstHeader(final String name) {
        // HTTPCORE-361 : we don't use the for-each syntax, i.e.
        //     for (Header header : headers)
        // as that creates an Iterator that needs to be garbage-collected
        for (int i = 0; i < this.headers.size(); i++) {
            final Header header = this.headers.get(i);
            if (header.getName().equalsIgnoreCase(name)) {
                return header;
            }
        }
        return null;
    }

    /**
     * Gets the last header with the given name.
     *
     * <p>Header name comparison is case insensitive.
     *
     * @param name the name of the header to get
     * @return the last header or <code>null</code>
     */
    public Header getLastHeader(final String name) {
        // start at the end of the list and work backwards
        for (int i = headers.size() - 1; i >= 0; i--) {
            final Header header = headers.get(i);
            if (header.getName().equalsIgnoreCase(name)) {
                return header;
            }
        }

        return null;
    }

    /**
     * Gets all of the headers contained within this group.
     *
     * @return an array of length >= 0
     */
    public Header[] getAllHeaders() {
        return headers.toArray(new Header[headers.size()]);
    }

    /**
     * Tests if headers with the given name are contained within this group.
     *
     * <p>Header name comparison is case insensitive.
     *
     * @param name the header name to test for
     * @return <code>true</code> if at least one header with the name is
     * contained, <code>false</code> otherwise
     */
    public boolean containsHeader(final String name) {
        // HTTPCORE-361 : we don't use the for-each syntax, i.e.
        //     for (Header header : headers)
        // as that creates an Iterator that needs to be garbage-collected
        for (int i = 0; i < this.headers.size(); i++) {
            final Header header = this.headers.get(i);
            if (header.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns an iterator over this group of headers.
     *
     * @return iterator over this group of headers.
     *
     * @since 4.0
     */
    public HeaderIterator iterator() {
        return new BasicListHeaderIterator(this.headers, null);
    }

    /**
     * Returns an iterator over the headers with a given name in this group.
     *
     * @param name      the name of the headers over which to iterate, or
     *                  <code>null</code> for all headers
     *
     * @return iterator over some headers in this group.
     *
     * @since 4.0
     */
    public HeaderIterator iterator(final String name) {
        return new BasicListHeaderIterator(this.headers, name);
    }

    /**
     * Returns a copy of this object
     *
     * @return copy of this object
     *
     * @since 4.0
     */
    public HeaderGroup copy() {
        final HeaderGroup clone = new HeaderGroup();
        clone.headers.addAll(this.headers);
        return clone;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return this.headers.toString();
    }

}
