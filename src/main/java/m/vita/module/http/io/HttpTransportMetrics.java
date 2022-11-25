package m.vita.module.http.io;

public interface HttpTransportMetrics {

    /**
     * Returns the number of bytes transferred.
     */
    long getBytesTransferred();

    /**
     * Resets the counts
     */
    void reset();

}
