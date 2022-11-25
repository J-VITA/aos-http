package m.vita.module.http.client;

import m.vita.module.http.header.HttpContext;

public interface UserTokenHandler {

    /**
     * The token object returned by this method is expected to uniquely
     * identify the current user if the context is user specific or to be
     * <code>null</code> if it is not.
     *
     * @param context the execution context
     *
     * @return user token that uniquely identifies the user or
     * <code>null</null> if the context is not user specific.
     */
    Object getUserToken(HttpContext context);

}
