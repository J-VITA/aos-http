package m.vita.module.http.header;

import m.vita.module.http.util.CharArrayBuffer;

public interface HeaderValueFormatter { /**
 * Formats an array of header elements.
 *
 * @param buffer    the buffer to append to, or
 *                  <code>null</code> to create a new buffer
 * @param elems     the header elements to format
 * @param quote     <code>true</code> to always format with quoted values,
 *                  <code>false</code> to use quotes only when necessary
 *
 * @return  a buffer with the formatted header elements.
 *          If the <code>buffer</code> argument was not <code>null</code>,
 *          that buffer will be used and returned.
 */
CharArrayBuffer formatElements(CharArrayBuffer buffer,
                               HeaderElement[] elems,
                               boolean quote);

    /**
     * Formats one header element.
     *
     * @param buffer    the buffer to append to, or
     *                  <code>null</code> to create a new buffer
     * @param elem      the header element to format
     * @param quote     <code>true</code> to always format with quoted values,
     *                  <code>false</code> to use quotes only when necessary
     *
     * @return  a buffer with the formatted header element.
     *          If the <code>buffer</code> argument was not <code>null</code>,
     *          that buffer will be used and returned.
     */
    CharArrayBuffer formatHeaderElement(CharArrayBuffer buffer,
                                        HeaderElement elem,
                                        boolean quote);

    /**
     * Formats the parameters of a header element.
     * That's a list of name-value pairs, to be separated by semicolons.
     * This method will <i>not</i> generate a leading semicolon.
     *
     * @param buffer    the buffer to append to, or
     *                  <code>null</code> to create a new buffer
     * @param nvps      the parameters (name-value pairs) to format
     * @param quote     <code>true</code> to always format with quoted values,
     *                  <code>false</code> to use quotes only when necessary
     *
     * @return  a buffer with the formatted parameters.
     *          If the <code>buffer</code> argument was not <code>null</code>,
     *          that buffer will be used and returned.
     */
    CharArrayBuffer formatParameters(CharArrayBuffer buffer,
                                     NameValuePair[] nvps,
                                     boolean quote);

    /**
     * Formats one name-value pair, where the value is optional.
     *
     * @param buffer    the buffer to append to, or
     *                  <code>null</code> to create a new buffer
     * @param nvp       the name-value pair to format
     * @param quote     <code>true</code> to always format with a quoted value,
     *                  <code>false</code> to use quotes only when necessary
     *
     * @return  a buffer with the formatted name-value pair.
     *          If the <code>buffer</code> argument was not <code>null</code>,
     *          that buffer will be used and returned.
     */
    CharArrayBuffer formatNameValuePair(CharArrayBuffer buffer,
                                        NameValuePair nvp,
                                        boolean quote);


}
