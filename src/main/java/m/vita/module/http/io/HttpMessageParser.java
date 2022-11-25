package m.vita.module.http.io;

import java.io.IOException;

import m.vita.module.http.HttpMessage;
import m.vita.module.http.exception.HttpException;

public interface HttpMessageParser<T extends HttpMessage> {

    /**
     * Generates an instance of {@link HttpMessage} from the underlying data
     * source.
     *
     * @return HTTP message
     * @throws IOException in case of an I/O error
     * @throws HttpException in case of HTTP protocol violation
     */
    T parse()
            throws IOException, HttpException;

}
