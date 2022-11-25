package m.vita.module.http.connect;

import java.io.IOException;
import java.io.InputStream;

public interface EofSensorWatcher {

    /**
     * Indicates that EOF is detected.
     *
     * @param wrapped   the underlying stream which has reached EOF
     *
     * @return  <code>true</code> if <code>wrapped</code> should be closed,
     *          <code>false</code> if it should be left alone
     *
     * @throws IOException
     *         in case of an IO problem, for example if the watcher itself
     *         closes the underlying stream. The caller will leave the
     *         wrapped stream alone, as if <code>false</code> was returned.
     */
    boolean eofDetected(InputStream wrapped)
            throws IOException;

    /**
     * Indicates that the {@link EofSensorInputStream stream} is closed.
     * This method will be called only if EOF was <i>not</i> detected
     * before closing. Otherwise, {@link #eofDetected eofDetected} is called.
     *
     * @param wrapped   the underlying stream which has not reached EOF
     *
     * @return  <code>true</code> if <code>wrapped</code> should be closed,
     *          <code>false</code> if it should be left alone
     *
     * @throws IOException
     *         in case of an IO problem, for example if the watcher itself
     *         closes the underlying stream. The caller will leave the
     *         wrapped stream alone, as if <code>false</code> was returned.
     */
    boolean streamClosed(InputStream wrapped)
            throws IOException;

    /**
     * Indicates that the {@link EofSensorInputStream stream} is aborted.
     * This method will be called only if EOF was <i>not</i> detected
     * before aborting. Otherwise, {@link #eofDetected eofDetected} is called.
     * <p/>
     * This method will also be invoked when an input operation causes an
     * IOException to be thrown to make sure the input stream gets shut down.
     *
     * @param wrapped   the underlying stream which has not reached EOF
     *
     * @return  <code>true</code> if <code>wrapped</code> should be closed,
     *          <code>false</code> if it should be left alone
     *
     * @throws IOException
     *         in case of an IO problem, for example if the watcher itself
     *         closes the underlying stream. The caller will leave the
     *         wrapped stream alone, as if <code>false</code> was returned.
     */
    boolean streamAbort(InputStream wrapped)
            throws IOException;

}
