package m.vita.module.http.handler;

import java.io.IOException;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.exception.ClientProtocolException;

public interface ResponseHandler<T> {

    /**
     * Processes an {@link HttpResponse} and returns some value
     * corresponding to that response.
     *
     * @param response The response to process
     * @return A value determined by the response
     *
     * @throws ClientProtocolException in case of an http protocol error
     * @throws IOException in case of a problem or the connection was aborted
     */
    T handleResponse(HttpResponse response) throws ClientProtocolException, IOException;

}