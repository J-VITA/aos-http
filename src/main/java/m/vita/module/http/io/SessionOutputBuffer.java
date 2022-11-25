package m.vita.module.http.io;

import java.io.IOException;

import m.vita.module.http.util.CharArrayBuffer;

public interface SessionOutputBuffer {

    /**
     * Writes <code>len</code> bytes from the specified byte array
     * starting at offset <code>off</code> to this session buffer.
     * <p>
     * If <code>off</code> is negative, or <code>len</code> is negative, or
     * <code>off+len</code> is greater than the length of the array
     * <code>b</code>, then an <tt>IndexOutOfBoundsException</tt> is thrown.
     *
     * @param      b     the data.
     * @param      off   the start offset in the data.
     * @param      len   the number of bytes to write.
     * @exception IOException  if an I/O error occurs.
     */
    void write(byte[] b, int off, int len) throws IOException;

    /**
     * Writes <code>b.length</code> bytes from the specified byte array
     * to this session buffer.
     *
     * @param      b   the data.
     * @exception  IOException  if an I/O error occurs.
     */
    void write(byte[] b) throws IOException;

    /**
     * Writes the specified byte to this session buffer.
     *
     * @param      b   the <code>byte</code>.
     * @exception  IOException  if an I/O error occurs.
     */
    void write(int b) throws IOException;

    /**
     * Writes characters from the specified string followed by a line delimiter
     * to this session buffer.
     * <p>
     * The choice of a char encoding and line delimiter sequence is up to the
     * specific implementations of this interface.
     *
     * @param      s   the line.
     * @exception  IOException  if an I/O error occurs.
     */
    void writeLine(String s) throws IOException;

    /**
     * Writes characters from the specified char array followed by a line
     * delimiter to this session buffer.
     * <p>
     * The choice of a char encoding and line delimiter sequence is up to the
     * specific implementations of this interface.
     *
     * @param      buffer   the buffer containing chars of the line.
     * @exception  IOException  if an I/O error occurs.
     */
    void writeLine(CharArrayBuffer buffer) throws IOException;

    /**
     * Flushes this session buffer and forces any buffered output bytes
     * to be written out. The general contract of <code>flush</code> is
     * that calling it is an indication that, if any bytes previously
     * written have been buffered by the implementation of the output
     * stream, such bytes should immediately be written to their
     * intended destination.
     *
     * @exception  IOException  if an I/O error occurs.
     */
    void flush() throws IOException;

    /**
     * Returns {@link HttpTransportMetrics} for this session buffer.
     *
     * @return transport metrics.
     */
    HttpTransportMetrics getMetrics();

}
