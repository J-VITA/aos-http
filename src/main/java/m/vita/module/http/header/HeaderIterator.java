package m.vita.module.http.header;

import java.util.Iterator;

public interface HeaderIterator extends Iterator<Object> {
    /**
     * Indicates whether there is another header in this iteration.
     *
     * @return  <code>true</code> if there is another header,
     *          <code>false</code> otherwise
     */
    boolean hasNext();

    /**
     * Obtains the next header from this iteration.
     * This method should only be called while {@link #hasNext hasNext}
     * is true.
     *
     * @return  the next header in this iteration
     */
    Header nextHeader();

}
