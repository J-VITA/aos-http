package m.vita.module.http.io;

import java.io.IOException;

import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.message.LineFormatter;
import m.vita.module.http.message.BasicLineFormatter;

@NotThreadSafe
public class DefaultHttpRequestWriter extends AbstractMessageWriter<HttpRequest> {

    /**
     * Creates an instance of DefaultHttpRequestWriter.
     *
     * @param buffer the session output buffer.
     * @param formatter the line formatter If <code>null</code>
     *   {@link BasicLineFormatter#INSTANCE}
     *   will be used.
     */
    public DefaultHttpRequestWriter(
            final SessionOutputBuffer buffer,
            final LineFormatter formatter) {
        super(buffer, formatter);
    }

    public DefaultHttpRequestWriter(final SessionOutputBuffer buffer) {
        this(buffer, null);
    }

    @Override
    protected void writeHeadLine(final HttpRequest message) throws IOException {
        lineFormatter.formatRequestLine(this.lineBuf, message.getRequestLine());
        this.sessionBuffer.writeLine(this.lineBuf);
    }

}