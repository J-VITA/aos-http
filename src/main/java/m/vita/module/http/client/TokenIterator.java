package m.vita.module.http.client;

import java.util.Iterator;

public interface TokenIterator extends Iterator<Object> {

    /**
     * Indicates whether there is another token in this iteration.
     *
     * @return  <code>true</code> if there is another token,
     *          <code>false</code> otherwise
     */
    boolean hasNext();

    /**
     * Obtains the next token from this iteration.
     * This method should only be called while {@link #hasNext hasNext}
     * is true.
     *
     * @return  the next token in this iteration
     */
    String nextToken();

}
