package m.vita.module.http.header;

import java.io.IOException;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.connect.HttpConnection;
import m.vita.module.http.exception.HttpException;

public interface HttpClientConnection extends HttpConnection {

    /**
     * Checks if response data is available from the connection. May wait for
     * the specified time until some data becomes available. Note that some
     * implementations may completely ignore the timeout parameter.
     *
     * @param timeout the maximum time in milliseconds to wait for data
     * @return true if data is available; false if there was no data available
     *         even after waiting for <code>timeout</code> milliseconds.
     * @throws IOException if an error happens on the connection
     */
    boolean isResponseAvailable(int timeout)
            throws IOException;

    /**
     * Sends the request line and all headers over the connection.
     * @param request the request whose headers to send.
     * @throws HttpException in case of HTTP protocol violation
     * @throws IOException in case of an I/O error
     */
    void sendRequestHeader(HttpRequest request)
            throws HttpException, IOException;

    /**
     * Sends the request entity over the connection.
     * @param request the request whose entity to send.
     * @throws HttpException in case of HTTP protocol violation
     * @throws IOException in case of an I/O error
     */
    void sendRequestEntity(HttpEntityEnclosingRequest request)
            throws HttpException, IOException;

    /**
     * Receives the request line and headers of the next response available from
     * this connection. The caller should examine the HttpResponse object to
     * find out if it should try to receive a response entity as well.
     *
     * @return a new HttpResponse object with status line and headers
     *         initialized.
     * @throws HttpException in case of HTTP protocol violation
     * @throws IOException in case of an I/O error
     */
    HttpResponse receiveResponseHeader()
            throws HttpException, IOException;

    /**
     * Receives the next response entity available from this connection and
     * attaches it to an existing HttpResponse object.
     *
     * @param response the response to attach the entity to
     * @throws HttpException in case of HTTP protocol violation
     * @throws IOException in case of an I/O error
     */
    void receiveResponseEntity(HttpResponse response)
            throws HttpException, IOException;

    /**
     * Writes out all pending buffered data over the open connection.
     *
     * @throws IOException in case of an I/O error
     */
    void flush() throws IOException;

}
