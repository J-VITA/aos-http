package m.vita.module.http.concurrent.cancellable;

import m.vita.module.http.concurrent.Cancellable;

public interface HttpExecutionAware {

    boolean isAborted();

    /**
     * Sets {@link Cancellable} for the ongoing operation.
     */
    void setCancellable(Cancellable cancellable);

}

