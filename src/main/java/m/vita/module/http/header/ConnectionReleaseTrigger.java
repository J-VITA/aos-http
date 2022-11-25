package m.vita.module.http.header;

import java.io.IOException;

public interface ConnectionReleaseTrigger {
    /**
     * Releases the connection with the option of keep-alive. This is a
     * "graceful" release and may cause IO operations for consuming the
     * remainder of a response entity. Use
     * {@link #abortConnection abortConnection} for a hard release. The
     * connection may be reused as specified by the duration.
     *
     * @throws IOException
     *             in case of an IO problem. The connection will be released
     *             anyway.
     */
    void releaseConnection()
            throws IOException;

    /**
     * Releases the connection without the option of keep-alive.
     * This is a "hard" release that implies a shutdown of the connection.
     * Use {@link #releaseConnection()} for a graceful release.
     *
     * @throws IOException      in case of an IO problem.
     *         The connection will be released anyway.
     */
    void abortConnection()
            throws IOException;

}
