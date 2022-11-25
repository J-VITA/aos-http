package m.vita.module.http.method;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import m.vita.module.http.concurrent.Cancellable;
import m.vita.module.http.concurrent.cancellable.HttpExecutionAware;
import m.vita.module.http.connect.ClientConnectionRequest;
import m.vita.module.http.header.ConnectionReleaseTrigger;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.message.AbstractHttpMessage;
import m.vita.module.http.util.CloneUtils;

public abstract class AbstractExecutionAwareRequest extends AbstractHttpMessage implements
        HttpExecutionAware, AbortableHttpRequest, Cloneable, HttpRequest {

    private final AtomicBoolean aborted;
    private final AtomicReference<Cancellable> cancellableRef;

    protected AbstractExecutionAwareRequest() {
        super();
        this.aborted = new AtomicBoolean(false);
        this.cancellableRef = new AtomicReference<Cancellable>(null);
    }

    @Deprecated
    public void setConnectionRequest(final ClientConnectionRequest connRequest) {
        setCancellable(new Cancellable() {

            public boolean cancel() {
                connRequest.abortRequest();
                return true;
            }

        });
    }

    @Deprecated
    public void setReleaseTrigger(final ConnectionReleaseTrigger releaseTrigger) {
        setCancellable(new Cancellable() {

            public boolean cancel() {
                try {
                    releaseTrigger.abortConnection();
                    return true;
                } catch (final IOException ex) {
                    return false;
                }
            }

        });
    }

    public void abort() {
        if (this.aborted.compareAndSet(false, true)) {
            final Cancellable cancellable = this.cancellableRef.getAndSet(null);
            if (cancellable != null) {
                cancellable.cancel();
            }
        }
    }

    public boolean isAborted() {
        return this.aborted.get();
    }

    /**
     * @since 4.2
     */
    public void setCancellable(final Cancellable cancellable) {
        if (!this.aborted.get()) {
            this.cancellableRef.set(cancellable);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        final AbstractExecutionAwareRequest clone = (AbstractExecutionAwareRequest) super.clone();
        clone.headergroup = CloneUtils.cloneObject(this.headergroup);
        clone.params = CloneUtils.cloneObject(this.params);
        return clone;
    }

    /**
     * @since 4.2
     */
    public void completed() {
        this.cancellableRef.set(null);
    }

    /**
     * Resets internal state of the request making it reusable.
     *
     * @since 4.2
     */
    public void reset() {
        final Cancellable cancellable = this.cancellableRef.getAndSet(null);
        if (cancellable != null) {
            cancellable.cancel();
        }
        this.aborted.set(false);
    }

}
