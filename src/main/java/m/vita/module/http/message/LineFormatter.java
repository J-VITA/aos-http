package m.vita.module.http.message;

import m.vita.module.http.header.Header;
import m.vita.module.http.header.RequestLine;
import m.vita.module.http.util.CharArrayBuffer;
import m.vita.module.http.util.ProtocolVersion;
import m.vita.module.http.util.StatusLine;
import m.vita.module.http.exception.ParseException;

public interface LineFormatter {

    /**
     * Formats a protocol version.
     * This method does <i>not</i> follow the general contract for
     * <code>buffer</code> arguments.
     * It does <i>not</i> clear the argument buffer, but appends instead.
     * The returned buffer can always be modified by the caller.
     * Because of these differing conventions, it is not named
     * <code>formatProtocolVersion</code>.
     *
     * @param buffer    a buffer to which to append, or <code>null</code>
     * @param version   the protocol version to format
     *
     * @return  a buffer with the formatted protocol version appended.
     *          The caller is allowed to modify the result buffer.
     *          If the <code>buffer</code> argument is not <code>null</code>,
     *          the returned buffer is the argument buffer.
     */
    CharArrayBuffer appendProtocolVersion(CharArrayBuffer buffer,
                                          ProtocolVersion version);

    /**
     * Formats a request line.
     *
     * @param buffer    a buffer available for formatting, or
     *                  <code>null</code>.
     *                  The buffer will be cleared before use.
     * @param reqline   the request line to format
     *
     * @return  the formatted request line
     */
    CharArrayBuffer formatRequestLine(CharArrayBuffer buffer,
                                      RequestLine reqline);

    /**
     * Formats a status line.
     *
     * @param buffer    a buffer available for formatting, or
     *                  <code>null</code>.
     *                  The buffer will be cleared before use.
     * @param statline  the status line to format
     *
     * @return  the formatted status line
     *
     * @throws ParseException        in case of a parse error
     */
    CharArrayBuffer formatStatusLine(CharArrayBuffer buffer,
                                     StatusLine statline);

    /**
     * Formats a header.
     * Due to header continuation, the result may be multiple lines.
     * In order to generate well-formed HTTP, the lines in the result
     * must be separated by the HTTP line break sequence CR-LF.
     * There is <i>no</i> trailing CR-LF in the result.
     * <br/>
     * See the class comment for details about the buffer argument.
     *
     * @param buffer    a buffer available for formatting, or
     *                  <code>null</code>.
     *                  The buffer will be cleared before use.
     * @param header    the header to format
     *
     * @return  a buffer holding the formatted header, never <code>null</code>.
     *          The returned buffer may be different from the argument buffer.
     *
     * @throws ParseException        in case of a parse error
     */
    CharArrayBuffer formatHeader(CharArrayBuffer buffer,
                                 Header header);

}
