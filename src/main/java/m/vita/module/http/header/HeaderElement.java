package m.vita.module.http.header;

public interface HeaderElement {

    /**
     * Returns header element name.
     *
     * @return header element name
     */
    String getName();

    /**
     * Returns header element value.
     *
     * @return header element value
     */
    String getValue();

    /**
     * Returns an array of name / value pairs.
     *
     * @return array of name / value pairs
     */
    NameValuePair[] getParameters();

    /**
     * Returns the first parameter with the given name.
     *
     * @param name parameter name
     *
     * @return name / value pair
     */
    NameValuePair getParameterByName(String name);

    /**
     * Returns the total count of parameters.
     *
     * @return parameter count
     */
    int getParameterCount();

    /**
     * Returns parameter with the given index.
     *
     * @param index index
     * @return name / value pair
     */
    NameValuePair getParameter(int index);

}