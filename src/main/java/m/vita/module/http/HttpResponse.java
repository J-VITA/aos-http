package m.vita.module.http;

import java.util.Locale;

import m.vita.module.http.util.EntityUtils;
import m.vita.module.http.util.HttpStatus;
import m.vita.module.http.util.ProtocolVersion;
import m.vita.module.http.util.StatusLine;

public interface HttpResponse extends HttpMessage {

    /**
     * Obtains the status line of this response.
     * The status line can be set using one of the
     * {@link #setStatusLine setStatusLine} methods,
     * or it can be initialized in a constructor.
     *
     * @return  the status line, or <code>null</code> if not yet set
     */
    StatusLine getStatusLine();

    /**
     * Sets the status line of this response.
     *
     * @param statusline the status line of this response
     */
    void setStatusLine(StatusLine statusline);

    /**
     * Sets the status line of this response.
     * The reason phrase will be determined based on the current
     * {@link #getLocale locale}.
     *
     * @param ver       the HTTP version
     * @param code      the status code
     */
    void setStatusLine(ProtocolVersion ver, int code);

    /**
     * Sets the status line of this response with a reason phrase.
     *
     * @param ver       the HTTP version
     * @param code      the status code
     * @param reason    the reason phrase, or <code>null</code> to omit
     */
    void setStatusLine(ProtocolVersion ver, int code, String reason);

    /**
     * Updates the status line of this response with a new status code.
     *
     * @param code the HTTP status code.
     *
     * @throws IllegalStateException
     *          if the status line has not be set
     *
     * @see HttpStatus
     * @see #setStatusLine(StatusLine)
     * @see #setStatusLine(ProtocolVersion,int)
     */
    void setStatusCode(int code)
            throws IllegalStateException;

    /**
     * Updates the status line of this response with a new reason phrase.
     *
     * @param reason    the new reason phrase as a single-line string, or
     *                  <code>null</code> to unset the reason phrase
     *
     * @throws IllegalStateException
     *          if the status line has not be set
     *
     * @see #setStatusLine(StatusLine)
     * @see #setStatusLine(ProtocolVersion,int)
     */
    void setReasonPhrase(String reason)
            throws IllegalStateException;

    /**
     * Obtains the message entity of this response, if any.
     * The entity is provided by calling {@link #setEntity setEntity}.
     *
     * @return  the response entity, or
     *          <code>null</code> if there is none
     */
    HttpEntity getEntity();

    /**
     * Associates a response entity with this response.
     * <p/>
     * Please note that if an entity has already been set for this response and it depends on
     * an input stream ({@link HttpEntity#isStreaming()} returns <code>true</code>),
     * it must be fully consumed in order to ensure release of resources.
     *
     * @param entity    the entity to associate with this response, or
     *                  <code>null</code> to unset
     *
     * @see HttpEntity#isStreaming()
     * @see EntityUtils#updateEntity(HttpResponse, HttpEntity)
     */
    void setEntity(HttpEntity entity);

    /**
     * Obtains the locale of this response.
     * The locale is used to determine the reason phrase
     * for the {@link #setStatusCode status code}.
     * It can be changed using {@link #setLocale setLocale}.
     *
     * @return  the locale of this response, never <code>null</code>
     */
    Locale getLocale();

    /**
     * Changes the locale of this response.
     *
     * @param loc       the new locale
     */
    void setLocale(Locale loc);

}