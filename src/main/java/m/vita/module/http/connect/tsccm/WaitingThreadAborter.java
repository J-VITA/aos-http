package m.vita.module.http.connect.tsccm;

public class WaitingThreadAborter {

    private WaitingThread waitingThread;
    private boolean aborted;

    /**
     * If a waiting thread has been set, interrupts it.
     */
    public void abort() {
        aborted = true;

        if (waitingThread != null) {
            waitingThread.interrupt();
        }

    }

    /**
     * Sets the waiting thread.  If this has already been aborted,
     * the waiting thread is immediately interrupted.
     *
     * @param waitingThread The thread to interrupt when aborting.
     */
    public void setWaitingThread(final WaitingThread waitingThread) {
        this.waitingThread = waitingThread;
        if (aborted) {
            waitingThread.interrupt();
        }
    }

}
