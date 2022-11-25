package m.vita.module.http.cookie;

import java.util.Date;

import m.vita.module.http.annotation.NotThreadSafe;

@NotThreadSafe
public class BasicClientCookie2 extends BasicClientCookie implements SetCookie2 {

    private String commentURL;
    private int[] ports;
    private boolean discard;

    /**
     * Default Constructor taking a name and a value. The value may be null.
     *
     * @param name The name.
     * @param value The value.
     */
    public BasicClientCookie2(final String name, final String value) {
        super(name, value);
    }

    @Override
    public int[] getPorts() {
        return this.ports;
    }

    public void setPorts(final int[] ports) {
        this.ports = ports;
    }

    @Override
    public String getCommentURL() {
        return this.commentURL;
    }

    public void setCommentURL(final String commentURL) {
        this.commentURL = commentURL;
    }

    public void setDiscard(final boolean discard) {
        this.discard = discard;
    }

    @Override
    public boolean isPersistent() {
        return !this.discard && super.isPersistent();
    }

    @Override
    public boolean isExpired(final Date date) {
        return this.discard || super.isExpired(date);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        final BasicClientCookie2 clone = (BasicClientCookie2) super.clone();
        if (this.ports != null) {
            clone.ports = this.ports.clone();
        }
        return clone;
    }

}

