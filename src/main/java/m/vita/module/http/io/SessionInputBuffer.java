package m.vita.module.http.io;

import java.io.IOException;

import m.vita.module.http.util.CharArrayBuffer;

public interface SessionInputBuffer {

    /**
     * Reads up to <code>len</code> bytes of data from the session buffer into
     * an array of bytes.  An attempt is made to read as many as
     * <code>len</code> bytes, but a smaller number may be read, possibly
     * zero. The number of bytes actually read is returned as an integer.
     *
     * <p> This method blocks until input data is available, end of file is
     * detected, or an exception is thrown.
     *
     * <p> If <code>off</code> is negative, or <code>len</code> is negative, or
     * <code>off+len</code> is greater than the length of the array
     * <code>b</code>, then an <code>IndexOutOfBoundsException</code> is
     * thrown.
     *
     * @param      b     the buffer into which the data is read.
     * @param      off   the start offset in array <code>b</code>
     *                   at which the data is written.
     * @param      len   the maximum number of bytes to read.
     * @return     the total number of bytes read into the buffer, or
     *             <code>-1</code> if there is no more data because the end of
     *             the stream has been reached.
     * @exception IOException  if an I/O error occurs.
     */
    int read(byte[] b, int off, int len) throws IOException;

    /**
     * Reads some number of bytes from the session buffer and stores them into
     * the buffer array <code>b</code>. The number of bytes actually read is
     * returned as an integer.  This method blocks until input data is
     * available, end of file is detected, or an exception is thrown.
     *
     * @param      b   the buffer into which the data is read.
     * @return     the total number of bytes read into the buffer, or
     *             <code>-1</code> is there is no more data because the end of
     *             the stream has been reached.
     * @exception  IOException  if an I/O error occurs.
     */
    int read(byte[] b) throws IOException;

    /**
     * Reads the next byte of data from this session buffer. The value byte is
     * returned as an <code>int</code> in the range <code>0</code> to
     * <code>255</code>. If no byte is available because the end of the stream
     * has been reached, the value <code>-1</code> is returned. This method
     * blocks until input data is available, the end of the stream is detected,
     * or an exception is thrown.
     *
     * @return     the next byte of data, or <code>-1</code> if the end of the
     *             stream is reached.
     * @exception  IOException  if an I/O error occurs.
     */
    int read() throws IOException;

    /**
     * Reads a complete line of characters up to a line delimiter from this
     * session buffer into the given line buffer. The number of chars actually
     * read is returned as an integer. The line delimiter itself is discarded.
     * If no char is available because the end of the stream has been reached,
     * the value <code>-1</code> is returned. This method blocks until input
     * data is available, end of file is detected, or an exception is thrown.
     * <p>
     * The choice of a char encoding and line delimiter sequence is up to the
     * specific implementations of this interface.
     *
     * @param      buffer   the line buffer.
     * @return     one line of characters
     * @exception  IOException  if an I/O error occurs.
     */
    int readLine(CharArrayBuffer buffer) throws IOException;

    /**
     * Reads a complete line of characters up to a line delimiter from this
     * session buffer. The line delimiter itself is discarded. If no char is
     * available because the end of the stream has been reached,
     * <code>null</code> is returned. This method blocks until input data is
     * available, end of file is detected, or an exception is thrown.
     * <p>
     * The choice of a char encoding and line delimiter sequence is up to the
     * specific implementations of this interface.
     *
     * @return HTTP line as a string
     * @exception  IOException  if an I/O error occurs.
     */
    String readLine() throws IOException;

    /** Blocks until some data becomes available in the session buffer or the
     * given timeout period in milliseconds elapses. If the timeout value is
     * <code>0</code> this method blocks indefinitely.
     *
     * @param timeout in milliseconds.
     * @return <code>true</code> if some data is available in the session
     *   buffer or <code>false</code> otherwise.
     * @exception  IOException  if an I/O error occurs.
     *
     * @deprecated (4.3) do not use. This function should be provided at the
     *   connection level
     */
    @Deprecated
    boolean isDataAvailable(int timeout) throws IOException;

    /**
     * Returns {@link HttpTransportMetrics} for this session buffer.
     *
     * @return transport metrics.
     */
    HttpTransportMetrics getMetrics();

}
