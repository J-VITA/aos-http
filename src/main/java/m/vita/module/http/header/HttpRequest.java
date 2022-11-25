package m.vita.module.http.header;

import m.vita.module.http.HttpMessage;

public interface HttpRequest extends HttpMessage {

    /**
     * Returns the request line of this request.
     * @return the request line.
     */
    RequestLine getRequestLine();

}