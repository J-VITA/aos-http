package m.vita.module.http.client.impl.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import m.vita.module.http.HttpEntity;
import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.config.HttpEntityWrapper;
import m.vita.module.http.exception.ProtocolException;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HttpEntityEnclosingRequest;
import m.vita.module.http.util.HTTP;

@NotThreadSafe // e.g. [gs]etEntity()
public class EntityEnclosingRequestWrapper extends RequestWrapper
        implements HttpEntityEnclosingRequest {

    private HttpEntity entity;
    private boolean consumed;

    public EntityEnclosingRequestWrapper(final HttpEntityEnclosingRequest request)
            throws ProtocolException {
        super(request);
        setEntity(request.getEntity());
    }

    public HttpEntity getEntity() {
        return this.entity;
    }

    public void setEntity(final HttpEntity entity) {
        this.entity = entity != null ? new EntityWrapper(entity) : null;
        this.consumed = false;
    }

    public boolean expectContinue() {
        final Header expect = getFirstHeader(HTTP.EXPECT_DIRECTIVE);
        return expect != null && HTTP.EXPECT_CONTINUE.equalsIgnoreCase(expect.getValue());
    }

    @Override
    public boolean isRepeatable() {
        return this.entity == null || this.entity.isRepeatable() || !this.consumed;
    }

    class EntityWrapper extends HttpEntityWrapper {

        EntityWrapper(final HttpEntity entity) {
            super(entity);
        }

        @Override
        public void consumeContent() throws IOException {
            consumed = true;
            super.consumeContent();
        }

        @Override
        public InputStream getContent() throws IOException {
            consumed = true;
            return super.getContent();
        }

        @Override
        public void writeTo(final OutputStream outstream) throws IOException {
            consumed = true;
            super.writeTo(outstream);
        }

    }

}
