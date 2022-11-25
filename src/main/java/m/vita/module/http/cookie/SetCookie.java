package m.vita.module.http.cookie;

import java.util.Date;

public interface SetCookie extends Cookie {

    void setValue(String value);

    /**
     * If a user agent (web browser) presents this cookie to a user, the
     * cookie's purpose will be described using this comment.
     *
     * @param comment
     *
     * @see #getComment()
     */
    void setComment(String comment);

    /**
     * Sets expiration date.
     * <p><strong>Note:</strong> the object returned by this method is considered
     * immutable. Changing it (e.g. using setTime()) could result in undefined
     * behaviour. Do so at your peril.</p>
     *
     * @param expiryDate the {@link Date} after which this cookie is no longer valid.
     *
     * @see Cookie#getExpiryDate
     *
     */
    void setExpiryDate (Date expiryDate);

    /**
     * Sets the domain attribute.
     *
     * @param domain The value of the domain attribute
     *
     * @see Cookie#getDomain
     */
    void setDomain(String domain);

    /**
     * Sets the path attribute.
     *
     * @param path The value of the path attribute
     *
     * @see Cookie#getPath
     *
     */
    void setPath(String path);

    /**
     * Sets the secure attribute of the cookie.
     * <p>
     * When <tt>true</tt> the cookie should only be sent
     * using a secure protocol (https).  This should only be set when
     * the cookie's originating server used a secure protocol to set the
     * cookie's value.
     *
     * @param secure The value of the secure attribute
     *
     * @see #isSecure()
     */
    void setSecure (boolean secure);

    /**
     * Sets the version of the cookie specification to which this
     * cookie conforms.
     *
     * @param version the version of the cookie.
     *
     * @see Cookie#getVersion
     */
    void setVersion(int version);

}

