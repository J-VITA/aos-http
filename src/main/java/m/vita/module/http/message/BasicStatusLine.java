package m.vita.module.http.message;

import java.io.Serializable;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.ProtocolVersion;
import m.vita.module.http.util.StatusLine;

@Immutable
public class BasicStatusLine implements StatusLine, Cloneable, Serializable {

    private static final long serialVersionUID = -2443303766890459269L;

    // ----------------------------------------------------- Instance Variables

    /** The protocol version. */
    private final ProtocolVersion protoVersion;

    /** The status code. */
    private final int statusCode;

    /** The reason phrase. */
    private final String reasonPhrase;

    // ----------------------------------------------------------- Constructors
    /**
     * Creates a new status line with the given version, status, and reason.
     *
     * @param version           the protocol version of the response
     * @param statusCode        the status code of the response
     * @param reasonPhrase      the reason phrase to the status code, or
     *                          <code>null</code>
     */
    public BasicStatusLine(final ProtocolVersion version, final int statusCode,
                           final String reasonPhrase) {
        super();
        this.protoVersion = Args.notNull(version, "Version");
        this.statusCode = Args.notNegative(statusCode, "Status code");
        this.reasonPhrase = reasonPhrase;
    }

    // --------------------------------------------------------- Public Methods

    public int getStatusCode() {
        return this.statusCode;
    }

    public ProtocolVersion getProtocolVersion() {
        return this.protoVersion;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    @Override
    public String toString() {
        // no need for non-default formatting in toString()
        return BasicLineFormatter.INSTANCE.formatStatusLine(null, this).toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
