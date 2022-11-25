package m.vita.module.http.cookie;

import java.util.ArrayList;
import java.util.List;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.exception.ParseException;
import m.vita.module.http.header.BasicHeaderElement;
import m.vita.module.http.header.BasicNameValuePair;
import m.vita.module.http.header.HeaderElement;
import m.vita.module.http.header.NameValuePair;
import m.vita.module.http.header.ParserCursor;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.CharArrayBuffer;
import m.vita.module.http.util.HTTP;

@Immutable
public class NetscapeDraftHeaderParser {

    public final static NetscapeDraftHeaderParser DEFAULT = new NetscapeDraftHeaderParser();

    public NetscapeDraftHeaderParser() {
        super();
    }

    public HeaderElement parseHeader(
            final CharArrayBuffer buffer,
            final ParserCursor cursor) throws ParseException {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(cursor, "Parser cursor");
        final NameValuePair nvp = parseNameValuePair(buffer, cursor);
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        while (!cursor.atEnd()) {
            final NameValuePair param = parseNameValuePair(buffer, cursor);
            params.add(param);
        }
        return new BasicHeaderElement(
                nvp.getName(),
                nvp.getValue(), params.toArray(new NameValuePair[params.size()]));
    }

    private NameValuePair parseNameValuePair(
            final CharArrayBuffer buffer, final ParserCursor cursor) {
        boolean terminated = false;

        int pos = cursor.getPos();
        final int indexFrom = cursor.getPos();
        final int indexTo = cursor.getUpperBound();

        // Find name
        String name = null;
        while (pos < indexTo) {
            final char ch = buffer.charAt(pos);
            if (ch == '=') {
                break;
            }
            if (ch == ';') {
                terminated = true;
                break;
            }
            pos++;
        }

        if (pos == indexTo) {
            terminated = true;
            name = buffer.substringTrimmed(indexFrom, indexTo);
        } else {
            name = buffer.substringTrimmed(indexFrom, pos);
            pos++;
        }

        if (terminated) {
            cursor.updatePos(pos);
            return new BasicNameValuePair(name, null);
        }

        // Find value
        String value = null;
        int i1 = pos;

        while (pos < indexTo) {
            final char ch = buffer.charAt(pos);
            if (ch == ';') {
                terminated = true;
                break;
            }
            pos++;
        }

        int i2 = pos;
        // Trim leading white spaces
        while (i1 < i2 && (HTTP.isWhitespace(buffer.charAt(i1)))) {
            i1++;
        }
        // Trim trailing white spaces
        while ((i2 > i1) && (HTTP.isWhitespace(buffer.charAt(i2 - 1)))) {
            i2--;
        }
        value = buffer.substring(i1, i2);
        if (terminated) {
            pos++;
        }
        cursor.updatePos(pos);
        return new BasicNameValuePair(name, value);
    }

}
