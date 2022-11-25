package m.vita.module.http.connect;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import m.vita.module.http.annotation.ThreadSafe;
import m.vita.module.http.header.HttpRoute;
import m.vita.module.http.header.ManagedHttpClientConnection;
import m.vita.module.http.pool.AbstractConnPool;
import m.vita.module.http.pool.ConnFactory;
import m.vita.module.http.util.HttpClientAndroidLog;

@ThreadSafe
class CPool extends AbstractConnPool<HttpRoute, ManagedHttpClientConnection, CPoolEntry> {

    private static final AtomicLong COUNTER = new AtomicLong();

    public HttpClientAndroidLog log = new HttpClientAndroidLog(CPool.class);
    private final long timeToLive;
    private final TimeUnit tunit;

    public CPool(
            final ConnFactory<HttpRoute, ManagedHttpClientConnection> connFactory,
            final int defaultMaxPerRoute, final int maxTotal,
            final long timeToLive, final TimeUnit tunit) {
        super(connFactory, defaultMaxPerRoute, maxTotal);
        this.timeToLive = timeToLive;
        this.tunit = tunit;
    }

    @Override
    protected CPoolEntry createEntry(final HttpRoute route, final ManagedHttpClientConnection conn) {
        final String id = Long.toString(COUNTER.getAndIncrement());
        return new CPoolEntry(this.log, id, route, conn, this.timeToLive, this.tunit);
    }

}
