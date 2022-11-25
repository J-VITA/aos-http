package m.vita.module.http.connect;

public interface HttpConnectionMetrics {
    /**
     * Returns the number of requests transferred over the connection,
     * 0 if not available.
     */
    long getRequestCount();

    /**
     * Returns the number of responses transferred over the connection,
     * 0 if not available.
     */
    long getResponseCount();

    /**
     * Returns the number of bytes transferred over the connection,
     * 0 if not available.
     */
    long getSentBytesCount();

    /**
     * Returns the number of bytes transferred over the connection,
     * 0 if not available.
     */
    long getReceivedBytesCount();

    /**
     * Return the value for the specified metric.
     *
     *@param metricName the name of the metric to query.
     *
     *@return the object representing the metric requested,
     *        <code>null</code> if the metric cannot not found.
     */
    Object getMetric(String metricName);

    /**
     * Resets the counts
     *
     */
    void reset();

}
