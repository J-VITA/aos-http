package m.vita.module.http.client.protocol;

import java.io.IOException;

import m.vita.module.http.HttpEntity;
import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.exception.HttpException;
import m.vita.module.http.exception.ProtocolException;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpEntityEnclosingRequest;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.interceptor.HttpRequestInterceptor;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.HTTP;
import m.vita.module.http.util.HttpVersion;
import m.vita.module.http.util.ProtocolVersion;

@Immutable
public class RequestContent implements HttpRequestInterceptor {

    private final boolean overwrite;

    /**
     * Default constructor. The <code>Content-Length</code> or <code>Transfer-Encoding</code>
     * will cause the interceptor to throw {@link ProtocolException} if already present in the
     * response message.
     */
    public RequestContent() {
        this(false);
    }

    /**
     * Constructor that can be used to fine-tune behavior of this interceptor.
     *
     * @param overwrite If set to <code>true</code> the <code>Content-Length</code> and
     * <code>Transfer-Encoding</code> headers will be created or updated if already present.
     * If set to <code>false</code> the <code>Content-Length</code> and
     * <code>Transfer-Encoding</code> headers will cause the interceptor to throw
     * {@link ProtocolException} if already present in the response message.
     *
     * @since 4.2
     */
    public RequestContent(final boolean overwrite) {
        super();
        this.overwrite = overwrite;
    }

    public void process(final HttpRequest request, final HttpContext context)
            throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        if (request instanceof HttpEntityEnclosingRequest) {
            if (this.overwrite) {
                request.removeHeaders(HTTP.TRANSFER_ENCODING);
                request.removeHeaders(HTTP.CONTENT_LEN);
            } else {
                if (request.containsHeader(HTTP.TRANSFER_ENCODING)) {
                    throw new ProtocolException("Transfer-encoding header already present");
                }
                if (request.containsHeader(HTTP.CONTENT_LEN)) {
                    throw new ProtocolException("Content-Length header already present");
                }
            }
            final ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
            final HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
            if (entity == null) {
                request.addHeader(HTTP.CONTENT_LEN, "0");
                return;
            }
            // Must specify a transfer encoding or a content length
            if (entity.isChunked() || entity.getContentLength() < 0) {
                if (ver.lessEquals(HttpVersion.HTTP_1_0)) {
                    throw new ProtocolException(
                            "Chunked transfer encoding not allowed for " + ver);
                }
                request.addHeader(HTTP.TRANSFER_ENCODING, HTTP.CHUNK_CODING);
            } else {
                request.addHeader(HTTP.CONTENT_LEN, Long.toString(entity.getContentLength()));
            }
            // Specify a content type if known
            if (entity.getContentType() != null && !request.containsHeader(
                    HTTP.CONTENT_TYPE )) {
                request.addHeader(entity.getContentType());
            }
            // Specify a content encoding if known
            if (entity.getContentEncoding() != null && !request.containsHeader(
                    HTTP.CONTENT_ENCODING)) {
                request.addHeader(entity.getContentEncoding());
            }
        }
    }

}
