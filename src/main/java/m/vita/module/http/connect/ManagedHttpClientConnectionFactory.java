package m.vita.module.http.connect;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.concurrent.atomic.AtomicLong;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.config.ConnectionConfig;
import m.vita.module.http.factory.HttpConnectionFactory;
import m.vita.module.http.factory.HttpMessageWriterFactory;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.header.HttpRoute;
import m.vita.module.http.header.ManagedHttpClientConnection;
import m.vita.module.http.io.DefaultHttpRequestWriterFactory;
import m.vita.module.http.io.DefaultHttpResponseParserFactory;
import m.vita.module.http.io.HttpMessageParserFactory;
import m.vita.module.http.util.HttpClientAndroidLog;

@Immutable
public class ManagedHttpClientConnectionFactory
        implements HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> {

    private static final AtomicLong COUNTER = new AtomicLong();

    public static final ManagedHttpClientConnectionFactory INSTANCE = new ManagedHttpClientConnectionFactory();

    public HttpClientAndroidLog log = new HttpClientAndroidLog(DefaultManagedHttpClientConnection.class);
    public HttpClientAndroidLog headerlog = new HttpClientAndroidLog("m.vita.module.http.header.Header");
    public HttpClientAndroidLog wirelog = new HttpClientAndroidLog("m.vita.module.http.connect.Wire");

    private final HttpMessageWriterFactory<HttpRequest> requestWriterFactory;
    private final HttpMessageParserFactory<HttpResponse> responseParserFactory;

    public ManagedHttpClientConnectionFactory(
            final HttpMessageWriterFactory<HttpRequest> requestWriterFactory,
            final HttpMessageParserFactory<HttpResponse> responseParserFactory) {
        super();
        this.requestWriterFactory = requestWriterFactory != null ? requestWriterFactory :
                DefaultHttpRequestWriterFactory.INSTANCE;
        this.responseParserFactory = responseParserFactory != null ? responseParserFactory :
                DefaultHttpResponseParserFactory.INSTANCE;
    }

    public ManagedHttpClientConnectionFactory(
            final HttpMessageParserFactory<HttpResponse> responseParserFactory) {
        this(null, responseParserFactory);
    }

    public ManagedHttpClientConnectionFactory() {
        this(null, null);
    }

    public ManagedHttpClientConnection create(final HttpRoute route, final ConnectionConfig config) {
        final ConnectionConfig cconfig = config != null ? config : ConnectionConfig.DEFAULT;
        CharsetDecoder chardecoder = null;
        CharsetEncoder charencoder = null;
        final Charset charset = cconfig.getCharset();
        final CodingErrorAction malformedInputAction = cconfig.getMalformedInputAction() != null ?
                cconfig.getMalformedInputAction() : CodingErrorAction.REPORT;
        final CodingErrorAction unmappableInputAction = cconfig.getUnmappableInputAction() != null ?
                cconfig.getUnmappableInputAction() : CodingErrorAction.REPORT;
        if (charset != null) {
            chardecoder = charset.newDecoder();
            chardecoder.onMalformedInput(malformedInputAction);
            chardecoder.onUnmappableCharacter(unmappableInputAction);
            charencoder = charset.newEncoder();
            charencoder.onMalformedInput(malformedInputAction);
            charencoder.onUnmappableCharacter(unmappableInputAction);
        }
        final String id = "http-outgoing-" + Long.toString(COUNTER.getAndIncrement());
        return new LoggingManagedHttpClientConnection(
                id,
                log,
                headerlog,
                wirelog,
                cconfig.getBufferSize(),
                cconfig.getFragmentSizeHint(),
                chardecoder,
                charencoder,
                cconfig.getMessageConstraints(),
                null,
                null,
                requestWriterFactory,
                responseParserFactory);
    }

}
