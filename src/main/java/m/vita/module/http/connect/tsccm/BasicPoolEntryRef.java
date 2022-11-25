package m.vita.module.http.connect.tsccm;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import m.vita.module.http.header.HttpRoute;
import m.vita.module.http.util.Args;

public class BasicPoolEntryRef extends WeakReference<BasicPoolEntry> {

    /** The planned route of the entry. */
    private final HttpRoute route; // HttpRoute is @Immutable


    /**
     * Creates a new reference to a pool entry.
     *
     * @param entry   the pool entry, must not be <code>null</code>
     * @param queue   the reference queue, or <code>null</code>
     */
    public BasicPoolEntryRef(final BasicPoolEntry entry,
                             final ReferenceQueue<Object> queue) {
        super(entry, queue);
        Args.notNull(entry, "Pool entry");
        route = entry.getPlannedRoute();
    }


    /**
     * Obtain the planned route for the referenced entry.
     * The planned route is still available, even if the entry is gone.
     *
     * @return      the planned route
     */
    public final HttpRoute getRoute() {
        return this.route;
    }

} // class BasicPoolEntryRef

