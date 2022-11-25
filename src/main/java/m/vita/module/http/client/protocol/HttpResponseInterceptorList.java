package m.vita.module.http.client.protocol;

import java.util.List;

import m.vita.module.http.interceptor.HttpResponseInterceptor;

public interface HttpResponseInterceptorList {

    /**
     * Appends a response interceptor to this list.
     *
     * @param interceptor the response interceptor to add
     */
    void addResponseInterceptor(HttpResponseInterceptor interceptor);

    /**
     * Inserts a response interceptor at the specified index.
     *
     * @param interceptor the response interceptor to add
     * @param index     the index to insert the interceptor at
     */
    void addResponseInterceptor(HttpResponseInterceptor interceptor, int index);

    /**
     * Obtains the current size of this list.
     *
     * @return  the number of response interceptors in this list
     */
    int getResponseInterceptorCount();

    /**
     * Obtains a response interceptor from this list.
     *
     * @param index     the index of the interceptor to obtain,
     *                  0 for first
     *
     * @return  the interceptor at the given index, or
     *          <code>null</code> if the index is out of range
     */
    HttpResponseInterceptor getResponseInterceptor(int index);

    /**
     * Removes all response interceptors from this list.
     */
    void clearResponseInterceptors();

    /**
     * Removes all response interceptor of the specified class
     *
     * @param clazz  the class of the instances to be removed.
     */
    void removeResponseInterceptorByClass(Class<? extends HttpResponseInterceptor> clazz);

    /**
     * Sets the response interceptors in this list.
     * This list will be cleared and re-initialized to contain
     * all response interceptors from the argument list.
     * If the argument list includes elements that are not response
     * interceptors, the behavior is implementation dependent.
     *
     * @param list the list of response interceptors
     */
    void setInterceptors(List<?> list);

}

