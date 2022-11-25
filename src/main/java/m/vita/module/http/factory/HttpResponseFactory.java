package m.vita.module.http.factory;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.util.ProtocolVersion;
import m.vita.module.http.util.StatusLine;

public interface HttpResponseFactory {

    /**
     * Creates a new response from status line elements.
     *
     * @param ver       the protocol version
     * @param status    the status code
     * @param context   the context from which to determine the locale
     *                  for looking up a reason phrase to the status code, or
     *                  <code>null</code> to use the default locale
     *
     * @return  the new response with an initialized status line
     */
    HttpResponse newHttpResponse(ProtocolVersion ver, int status,
                                 HttpContext context);

    /**
     * Creates a new response from a status line.
     *
     * @param statusline the status line
     * @param context    the context from which to determine the locale
     *                   for looking up a reason phrase if the status code
     *                   is updated, or
     *                   <code>null</code> to use the default locale
     *
     * @return  the new response with the argument status line
     */
    HttpResponse newHttpResponse(StatusLine statusline,
                                 HttpContext context);

}
