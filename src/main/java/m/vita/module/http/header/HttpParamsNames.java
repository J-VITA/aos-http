package m.vita.module.http.header;

import java.util.Set;

public interface HttpParamsNames {

    /**
     * Returns the current set of names;
     * in the case of stacked parameters, returns the names
     * from all the participating HttpParams instances.
     *
     * Changes to the underlying HttpParams are not reflected
     * in the set - it is a snapshot.
     *
     * @return the names, as a Set<String>
     */
    Set<String> getNames();

}
