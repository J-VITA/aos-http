package m.vita.module.http.entity;

import java.io.IOException;
import java.io.InputStream;

import m.vita.module.http.HttpEntity;
import m.vita.module.http.header.Header;

public class DeflateDecompressingEntity extends DecompressingEntity {

    /**
     * Creates a new {@link DeflateDecompressingEntity} which will wrap the specified
     * {@link HttpEntity}.
     *
     * @param entity
     *            a non-null {@link HttpEntity} to be wrapped
     */
    public DeflateDecompressingEntity(final HttpEntity entity) {
        super(entity);
    }

    /**
     * Returns the non-null InputStream that should be returned to by all requests to
     * {@link #getContent()}.
     *
     * @return a non-null InputStream
     * @throws IOException if there was a problem
     */
    @Override
    InputStream decorate(final InputStream wrapped) throws IOException {
        return new DeflateInputStream(wrapped);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Header getContentEncoding() {

        /* This HttpEntityWrapper has dealt with the Content-Encoding. */
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getContentLength() {

        /* Length of inflated content is unknown. */
        return -1;
    }

}
