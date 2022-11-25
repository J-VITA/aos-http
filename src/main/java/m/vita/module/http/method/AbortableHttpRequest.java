package m.vita.module.http.method;

import java.io.IOException;

import m.vita.module.http.client.impl.client.HttpClient;
import m.vita.module.http.connect.ClientConnectionRequest;
import m.vita.module.http.header.ConnectionReleaseTrigger;
import m.vita.module.http.header.ClientConnectionManager;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpHost;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.header.HttpUriRequest;
import m.vita.module.http.header.ManagedClientConnection;

public interface AbortableHttpRequest {

    /**
     * Sets the {@link ClientConnectionRequest}
     * callback that can be used to abort a long-lived request for a connection.
     * If the request is already aborted, throws an {@link IOException}.
     *
     * @see ClientConnectionManager
     */
    void setConnectionRequest(ClientConnectionRequest connRequest) throws IOException;

    /**
     * Sets the {@link ConnectionReleaseTrigger} callback that can
     * be used to abort an active connection.
     * Typically, this will be the
     *   {@link ManagedClientConnection} itself.
     * If the request is already aborted, throws an {@link IOException}.
     */
    void setReleaseTrigger(ConnectionReleaseTrigger releaseTrigger) throws IOException;

    /**
     * Aborts this http request. Any active execution of this method should
     * return immediately. If the request has not started, it will abort after
     * the next execution. Aborting this request will cause all subsequent
     * executions with this request to fail.
     *
     * @see HttpClient#execute(HttpUriRequest)
     * @see HttpClient#execute(HttpHost,
     *      HttpRequest)
     * @see HttpClient#execute(HttpUriRequest,
     *      HttpContext)
     * @see HttpHost ,
     *      HttpContext)
     */
    void abort();

}

