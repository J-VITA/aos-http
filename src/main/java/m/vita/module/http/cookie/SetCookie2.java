package m.vita.module.http.cookie;

public interface SetCookie2 extends SetCookie {

    /**
     * If a user agent (web browser) presents this cookie to a user, the
     * cookie's purpose will be described by the information at this URL.
     */
    void setCommentURL(String commentURL);

    /**
     * Sets the Port attribute. It restricts the ports to which a cookie
     * may be returned in a Cookie request header.
     */
    void setPorts(int[] ports);

    /**
     * Set the Discard attribute.
     *
     * Note: <tt>Discard</tt> attribute overrides <tt>Max-age</tt>.
     *
     * @see #isPersistent()
     */
    void setDiscard(boolean discard);

}

