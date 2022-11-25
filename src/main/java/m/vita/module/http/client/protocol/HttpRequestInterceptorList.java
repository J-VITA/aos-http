package m.vita.module.http.client.protocol;

import java.util.List;

import m.vita.module.http.interceptor.HttpRequestInterceptor;

public interface HttpRequestInterceptorList {

    /**
     * Appends a request interceptor to this list.
     *
     * @param interceptor the request interceptor to add
     */
    void addRequestInterceptor(HttpRequestInterceptor interceptor);

    /**
     * Inserts a request interceptor at the specified index.
     *
     * @param interceptor the request interceptor to add
     * @param index     the index to insert the interceptor at
     */
    void addRequestInterceptor(HttpRequestInterceptor interceptor, int index);

    /**
     * Obtains the current size of this list.
     *
     * @return  the number of request interceptors in this list
     */
    int getRequestInterceptorCount();

    /**
     * Obtains a request interceptor from this list.
     *
     * @param index     the index of the interceptor to obtain,
     *                  0 for first
     *
     * @return  the interceptor at the given index, or
     *          <code>null</code> if the index is out of range
     */
    HttpRequestInterceptor getRequestInterceptor(int index);

    /**
     * Removes all request interceptors from this list.
     */
    void clearRequestInterceptors();

    /**
     * Removes all request interceptor of the specified class
     *
     * @param clazz  the class of the instances to be removed.
     */
    void removeRequestInterceptorByClass(Class<? extends HttpRequestInterceptor> clazz);

    /**
     * Sets the request interceptors in this list.
     * This list will be cleared and re-initialized to contain
     * all request interceptors from the argument list.
     * If the argument list includes elements that are not request
     * interceptors, the behavior is implementation dependent.
     *
     * @param list the list of request interceptors
     */
    void setInterceptors(List<?> list);

}

