package m.vita.module.http.client;

import java.io.IOException;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.exception.HttpException;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpHost;
import m.vita.module.http.header.HttpRequest;

public interface RequestDirector {


    /**
     * Executes a request.
     * <br/><b>Note:</b>
     * For the time being, a new director is instantiated for each request.
     * This is the same behavior as for <code>HttpMethodDirector</code>
     * in HttpClient 3.
     *
     * @param target    the target host for the request.
     *                  Implementations may accept <code>null</code>
     *                  if they can still determine a route, for example
     *                  to a default target or by inspecting the request.
     * @param request   the request to execute
     * @param context   the context for executing the request
     *
     * @return  the final response to the request.
     *          This is never an intermediate response with status code 1xx.
     *
     * @throws HttpException            in case of a problem
     * @throws IOException              in case of an IO problem
     *                                     or if the connection was aborted
     */
    HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context)
            throws HttpException, IOException;

}
