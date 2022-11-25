package m.vita.module.http.message;

import java.io.Serializable;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.exception.ParseException;
import m.vita.module.http.header.BasicHeaderValueParser;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HeaderElement;
import m.vita.module.http.util.Args;

@Immutable
public class BasicHeader implements Header, Cloneable, Serializable {

    private static final long serialVersionUID = -5427236326487562174L;

    private final String name;
    private final String value;

    /**
     * Constructor with name and value
     *
     * @param name the header name
     * @param value the header value
     */
    public BasicHeader(final String name, final String value) {
        super();
        this.name = Args.notNull(name, "Name");
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        // no need for non-default formatting in toString()
        return BasicLineFormatter.INSTANCE.formatHeader(null, this).toString();
    }

    public HeaderElement[] getElements() throws ParseException {
        if (this.value != null) {
            // result intentionally not cached, it's probably not used again
            return BasicHeaderValueParser.parseElements(this.value, null);
        } else {
            return new HeaderElement[] {};
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
