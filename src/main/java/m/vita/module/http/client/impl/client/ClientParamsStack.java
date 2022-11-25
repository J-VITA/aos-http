package m.vita.module.http.client.impl.client;

import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.header.AbstractHttpParams;
import m.vita.module.http.header.HttpParams;
import m.vita.module.http.util.Args;

@NotThreadSafe
public class ClientParamsStack extends AbstractHttpParams {

    /** The application parameter collection, or <code>null</code>. */
    protected final HttpParams applicationParams;

    /** The client parameter collection, or <code>null</code>. */
    protected final HttpParams clientParams;

    /** The request parameter collection, or <code>null</code>. */
    protected final HttpParams requestParams;

    /** The override parameter collection, or <code>null</code>. */
    protected final HttpParams overrideParams;


    /**
     * Creates a new parameter stack from elements.
     * The arguments will be stored as-is, there is no copying to
     * prevent modification.
     *
     * @param aparams   application parameters, or <code>null</code>
     * @param cparams   client parameters, or <code>null</code>
     * @param rparams   request parameters, or <code>null</code>
     * @param oparams   override parameters, or <code>null</code>
     */
    public ClientParamsStack(final HttpParams aparams, final HttpParams cparams,
                             final HttpParams rparams, final HttpParams oparams) {
        applicationParams = aparams;
        clientParams      = cparams;
        requestParams     = rparams;
        overrideParams    = oparams;
    }


    /**
     * Creates a copy of a parameter stack.
     * The new stack will have the exact same entries as the argument stack.
     * There is no copying of parameters.
     *
     * @param stack     the stack to copy
     */
    public ClientParamsStack(final ClientParamsStack stack) {
        this(stack.getApplicationParams(),
                stack.getClientParams(),
                stack.getRequestParams(),
                stack.getOverrideParams());
    }


    /**
     * Creates a modified copy of a parameter stack.
     * The new stack will contain the explicitly passed elements.
     * For elements where the explicit argument is <code>null</code>,
     * the corresponding element from the argument stack is used.
     * There is no copying of parameters.
     *
     * @param stack     the stack to modify
     * @param aparams   application parameters, or <code>null</code>
     * @param cparams   client parameters, or <code>null</code>
     * @param rparams   request parameters, or <code>null</code>
     * @param oparams   override parameters, or <code>null</code>
     */
    public ClientParamsStack(final ClientParamsStack stack,
                             final HttpParams aparams, final HttpParams cparams,
                             final HttpParams rparams, final HttpParams oparams) {
        this((aparams != null) ? aparams : stack.getApplicationParams(),
                (cparams != null) ? cparams : stack.getClientParams(),
                (rparams != null) ? rparams : stack.getRequestParams(),
                (oparams != null) ? oparams : stack.getOverrideParams());
    }


    /**
     * Obtains the application parameters of this stack.
     *
     * @return  the application parameters, or <code>null</code>
     */
    public final HttpParams getApplicationParams() {
        return applicationParams;
    }

    /**
     * Obtains the client parameters of this stack.
     *
     * @return  the client parameters, or <code>null</code>
     */
    public final HttpParams getClientParams() {
        return clientParams;
    }

    /**
     * Obtains the request parameters of this stack.
     *
     * @return  the request parameters, or <code>null</code>
     */
    public final HttpParams getRequestParams() {
        return requestParams;
    }

    /**
     * Obtains the override parameters of this stack.
     *
     * @return  the override parameters, or <code>null</code>
     */
    public final HttpParams getOverrideParams() {
        return overrideParams;
    }


    /**
     * Obtains a parameter from this stack.
     * See class comment for search order.
     *
     * @param name      the name of the parameter to obtain
     *
     * @return  the highest-priority value for that parameter, or
     *          <code>null</code> if it is not set anywhere in this stack
     */
    public Object getParameter(final String name) {
        Args.notNull(name, "Parameter name");

        Object result = null;

        if (overrideParams != null) {
            result = overrideParams.getParameter(name);
        }
        if ((result == null) && (requestParams != null)) {
            result = requestParams.getParameter(name);
        }
        if ((result == null) && (clientParams != null)) {
            result = clientParams.getParameter(name);
        }
        if ((result == null) && (applicationParams != null)) {
            result = applicationParams.getParameter(name);
        }
        return result;
    }

    /**
     * Does <i>not</i> set a parameter.
     * Parameter stacks are read-only. It is possible, though discouraged,
     * to access and modify specific stack entries.
     * Derived classes may change this behavior.
     *
     * @param name      ignored
     * @param value     ignored
     *
     * @return  nothing
     *
     * @throws UnsupportedOperationException    always
     */
    public HttpParams setParameter(final String name, final Object value)
            throws UnsupportedOperationException {

        throw new UnsupportedOperationException
                ("Setting parameters in a stack is not supported.");
    }


    /**
     * Does <i>not</i> remove a parameter.
     * Parameter stacks are read-only. It is possible, though discouraged,
     * to access and modify specific stack entries.
     * Derived classes may change this behavior.
     *
     * @param name      ignored
     *
     * @return  nothing
     *
     * @throws UnsupportedOperationException    always
     */
    public boolean removeParameter(final String name) {
        throw new UnsupportedOperationException
                ("Removing parameters in a stack is not supported.");
    }


    /**
     * Does <i>not</i> copy parameters.
     * Parameter stacks are lightweight objects, expected to be instantiated
     * as needed and to be used only in a very specific context. On top of
     * that, they are read-only. The typical copy operation to prevent
     * accidental modification of parameters passed by the application to
     * a framework object is therefore pointless and disabled.
     * Create a new stack if you really need a copy.
     * <br/>
     * Derived classes may change this behavior.
     *
     * @return <code>this</code> parameter stack
     */
    public HttpParams copy() {
        return this;
    }


}
