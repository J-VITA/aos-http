package m.vita.module.http.connect.tsccm;

import java.util.Date;
import java.util.concurrent.locks.Condition;

import m.vita.module.http.util.Args;


public class WaitingThread {

    /** The condition on which the thread is waiting. */
    private final Condition cond;

    /** The route specific pool on which the thread is waiting. */
    //@@@ replace with generic pool interface
    private final RouteSpecificPool pool;

    /** The thread that is waiting for an entry. */
    private Thread waiter;

    /** True if this was interrupted. */
    private boolean aborted;


    /**
     * Creates a new entry for a waiting thread.
     *
     * @param cond      the condition for which to wait
     * @param pool      the pool on which the thread will be waiting,
     *                  or <code>null</code>
     */
    public WaitingThread(final Condition cond, final RouteSpecificPool pool) {

        Args.notNull(cond, "Condition");

        this.cond = cond;
        this.pool = pool;
    }


    /**
     * Obtains the condition.
     *
     * @return  the condition on which to wait, never <code>null</code>
     */
    public final Condition getCondition() {
        // not synchronized
        return this.cond;
    }


    /**
     * Obtains the pool, if there is one.
     *
     * @return  the pool on which a thread is or was waiting,
     *          or <code>null</code>
     */
    public final RouteSpecificPool getPool() {
        // not synchronized
        return this.pool;
    }


    /**
     * Obtains the thread, if there is one.
     *
     * @return  the thread which is waiting, or <code>null</code>
     */
    public final Thread getThread() {
        // not synchronized
        return this.waiter;
    }


    /**
     * Blocks the calling thread.
     * This method returns when the thread is notified or interrupted,
     * if a timeout occurrs, or if there is a spurious wakeup.
     * <br/>
     * This method assumes external synchronization.
     *
     * @param deadline  when to time out, or <code>null</code> for no timeout
     *
     * @return  <code>true</code> if the condition was satisfied,
     *          <code>false</code> in case of a timeout.
     *          Typically, a call to {@link #wakeup} is used to indicate
     *          that the condition was satisfied. Since the condition is
     *          accessible outside, this cannot be guaranteed though.
     *
     * @throws InterruptedException     if the waiting thread was interrupted
     *
     * @see #wakeup
     */
    public boolean await(final Date deadline)
            throws InterruptedException {

        // This is only a sanity check. We cannot synchronize here,
        // the lock would not be released on calling cond.await() below.
        if (this.waiter != null) {
            throw new IllegalStateException
                    ("A thread is already waiting on this object." +
                            "\ncaller: " + Thread.currentThread() +
                            "\nwaiter: " + this.waiter);
        }

        if (aborted) {
            throw new InterruptedException("Operation interrupted");
        }

        this.waiter = Thread.currentThread();

        boolean success = false;
        try {
            if (deadline != null) {
                success = this.cond.awaitUntil(deadline);
            } else {
                this.cond.await();
                success = true;
            }
            if (aborted) {
                throw new InterruptedException("Operation interrupted");
            }
        } finally {
            this.waiter = null;
        }
        return success;

    } // await


    /**
     * Wakes up the waiting thread.
     * <br/>
     * This method assumes external synchronization.
     */
    public void wakeup() {

        // If external synchronization and pooling works properly,
        // this cannot happen. Just a sanity check.
        if (this.waiter == null) {
            throw new IllegalStateException
                    ("Nobody waiting on this object.");
        }

        // One condition might be shared by several WaitingThread instances.
        // It probably isn't, but just in case: wake all, not just one.
        this.cond.signalAll();
    }

    public void interrupt() {
        aborted = true;
        this.cond.signalAll();
    }


} // class WaitingThread
