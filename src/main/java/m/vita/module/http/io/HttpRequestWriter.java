package m.vita.module.http.io;

import java.io.IOException;

import m.vita.module.http.header.HttpParams;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.message.LineFormatter;

public class HttpRequestWriter extends AbstractMessageWriter<HttpRequest> {

    public HttpRequestWriter(final SessionOutputBuffer buffer,
                             final LineFormatter formatter,
                             final HttpParams params) {
        super(buffer, formatter, params);
    }

    @Override
    protected void writeHeadLine(final HttpRequest message) throws IOException {
        lineFormatter.formatRequestLine(this.lineBuf, message.getRequestLine());
        this.sessionBuffer.writeLine(this.lineBuf);
    }

}
