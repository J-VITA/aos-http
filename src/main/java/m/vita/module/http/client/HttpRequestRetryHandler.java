package m.vita.module.http.client;

import java.io.IOException;

import m.vita.module.http.header.HttpContext;

public interface HttpRequestRetryHandler {

    /**
     * Determines if a method should be retried after an IOException
     * occurs during execution.
     *
     * @param exception the exception that occurred
     * @param executionCount the number of times this method has been
     * unsuccessfully executed
     * @param context the context for the request execution
     *
     * @return <code>true</code> if the method should be retried, <code>false</code>
     * otherwise
     */
    boolean retryRequest(IOException exception, int executionCount, HttpContext context);

}
