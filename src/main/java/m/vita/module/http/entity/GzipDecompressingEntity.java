package m.vita.module.http.entity;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import m.vita.module.http.HttpEntity;
import m.vita.module.http.header.Header;

public class GzipDecompressingEntity extends DecompressingEntity {

    /**
     * Creates a new {@link GzipDecompressingEntity} which will wrap the specified
     * {@link HttpEntity}.
     *
     * @param entity
     *            the non-null {@link HttpEntity} to be wrapped
     */
    public GzipDecompressingEntity(final HttpEntity entity) {
        super(entity);
    }

    @Override
    InputStream decorate(final InputStream wrapped) throws IOException {
        return new GZIPInputStream(wrapped);
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

        /* length of ungzipped content is not known */
        return -1;
    }

}
