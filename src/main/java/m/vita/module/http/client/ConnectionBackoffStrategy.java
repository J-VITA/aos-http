package m.vita.module.http.client;

import m.vita.module.http.HttpResponse;

public interface ConnectionBackoffStrategy {

    /**
     * Determines whether seeing the given <code>Throwable</code> as
     * a result of request execution should result in a backoff
     * signal.
     * @param t the <code>Throwable</code> that happened
     * @return <code>true</code> if a backoff signal should be
     *   given
     */
    boolean shouldBackoff(Throwable t);

    /**
     * Determines whether receiving the given {@link HttpResponse} as
     * a result of request execution should result in a backoff
     * signal. Implementations MUST restrict themselves to examining
     * the response header and MUST NOT consume any of the response
     * body, if any.
     * @param resp the <code>HttpResponse</code> that was received
     * @return <code>true</code> if a backoff signal should be
     *   given
     */
    boolean shouldBackoff(HttpResponse resp);
}
