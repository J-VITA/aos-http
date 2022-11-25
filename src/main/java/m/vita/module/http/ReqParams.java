package m.vita.module.http;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import m.vita.module.http.builder.URLEncodedUtils;
import m.vita.module.http.entity.UrlEncodedFormEntity;
import m.vita.module.http.handler.ResponseHandlerInterface;
import m.vita.module.http.header.BasicNameValuePair;
import m.vita.module.http.json.JEBJsonStreamerEntity;
import m.vita.module.http.method.HttpEntityEnclosingRequestBase;
import m.vita.module.http.util.HTTP;

public class ReqParams implements Parcelable {

    public final static String APPLICATION_OCTET_STREAM =
            "application/octet-stream";

    public final static String APPLICATION_JSON =
            "application/json";

    protected final static String LOG_TAG = "RequestParams";
    protected final ConcurrentHashMap<String, String> urlParams = new ConcurrentHashMap<String, String>();
    protected final ConcurrentHashMap<String, RequestParams.StreamWrapper> streamParams = new ConcurrentHashMap<String, RequestParams.StreamWrapper>();
    protected final ConcurrentHashMap<String, RequestParams.FileWrapper> fileParams = new ConcurrentHashMap<String, RequestParams.FileWrapper>();
    protected final ConcurrentHashMap<String, List<RequestParams.FileWrapper>> fileArrayParams = new ConcurrentHashMap<String, List<RequestParams.FileWrapper>>();
    protected final ConcurrentHashMap<String, Object> urlParamsWithObjects = new ConcurrentHashMap<String, Object>();
    protected boolean isRepeatable;
    protected boolean forceMultipartEntity = false;
    protected boolean useJsonStreamer;
    protected String elapsedFieldInJsonStreamer = "_elapsed";
    protected boolean autoCloseInputStreams;
    protected String contentEncoding = HTTP.UTF_8;

    protected ReqParams(Parcel in) {
        isRepeatable = in.readByte() != 0;
        forceMultipartEntity = in.readByte() != 0;
        useJsonStreamer = in.readByte() != 0;
        elapsedFieldInJsonStreamer = in.readString();
        autoCloseInputStreams = in.readByte() != 0;
        contentEncoding = in.readString();
    }

    /**
     * Constructs a new empty {@code RequestParams} instance.
     */
    public ReqParams() {
        this((Map<String, String>) null);
    }

    /**
     * Constructs a new RequestParams instance containing the key/value string params from the
     * specified map.
     *
     * @param source the source key/value string map to add.
     */
    public ReqParams(Map<String, String> source) {
        if (source != null) {
            for (Map.Entry<String, String> entry : source.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Constructs a new RequestParams instance and populate it with a single initial key/value
     * string param.
     *
     * @param key   the key name for the intial param.
     * @param value the value string for the initial param.
     */
    public ReqParams(final String key, final String value) {
        this(new HashMap<String, String>() {{
            put(key, value);
        }});
    }

    /**
     * Constructs a new RequestParams instance and populate it with multiple initial key/value
     * string param.
     *
     * @param keysAndValues a sequence of keys and values. Objects are automatically converted to
     *                      Strings (including the value {@code null}).
     * @throws IllegalArgumentException if the number of arguments isn't even.
     */
    public ReqParams(Object... keysAndValues) {
        int len = keysAndValues.length;
        if (len % 2 != 0)
            throw new IllegalArgumentException("Supplied arguments must be even");
        for (int i = 0; i < len; i += 2) {
            String key = String.valueOf(keysAndValues[i]);
            String val = String.valueOf(keysAndValues[i + 1]);
            put(key, val);
        }
    }

    /**
     * Sets content encoding for return value of {@link #getParamString()} and {@link
     * #createFormEntity()} <p>&nbsp;</p> Default encoding is "UTF-8"
     *
     * @param encoding String constant from {@link HTTP}
     */
    public void setContentEncoding(final String encoding) {
        if (encoding != null) {
            this.contentEncoding = encoding;
        } else {
            JEBAsyncHttpClient.log.d(LOG_TAG, "setContentEncoding called with null attribute");
        }
    }

    /**
     * If set to true will force Content-Type header to `multipart/form-data`
     * even if there are not Files or Streams to be send
     * <p>&nbsp;</p>
     * Default value is false
     *
     * @param force boolean, should declare content-type multipart/form-data even without files or streams present
     */
    public void setForceMultipartEntityContentType(boolean force) {
        this.forceMultipartEntity = force;
    }

    /**
     * Adds a key/value string pair to the request.
     *
     * @param key   the key name for the new param.
     * @param value the value string for the new param.
     */
    public void put(String key, String value) {
        if (key != null && value != null) {
            urlParams.put(key, value);
        }
    }

    /**
     * Adds files array to the request.
     *
     * @param key   the key name for the new param.
     * @param files the files array to add.
     * @throws FileNotFoundException if one of passed files is not found at time of assembling the requestparams into request
     */
    public void put(String key, File files[]) throws FileNotFoundException {
        put(key, files, null, null);
    }

    /**
     * Adds files array to the request with both custom provided file content-type and files name
     *
     * @param key            the key name for the new param.
     * @param files          the files array to add.
     * @param contentType    the content type of the file, eg. application/json
     * @param customFileName file name to use instead of real file name
     * @throws FileNotFoundException throws if wrong File argument was passed
     */
    public void put(String key, File files[], String contentType, String customFileName) throws FileNotFoundException {

        if (key != null) {
            List<RequestParams.FileWrapper> fileWrappers = new ArrayList<RequestParams.FileWrapper>();
            for (File file : files) {
                if (file == null || !file.exists()) {
                    throw new FileNotFoundException();
                }
                fileWrappers.add(new RequestParams.FileWrapper(file, contentType, customFileName));
            }
            fileArrayParams.put(key, fileWrappers);
        }
    }

    /**
     * Adds a file to the request.
     *
     * @param key  the key name for the new param.
     * @param file the file to add.
     * @throws FileNotFoundException throws if wrong File argument was passed
     */
    public void put(String key, File file) throws FileNotFoundException {
        put(key, file, null, null);
    }

    /**
     * Adds a file to the request with custom provided file name
     *
     * @param key            the key name for the new param.
     * @param file           the file to add.
     * @param customFileName file name to use instead of real file name
     * @throws FileNotFoundException throws if wrong File argument was passed
     */
    public void put(String key, String customFileName, File file) throws FileNotFoundException {
        put(key, file, null, customFileName);
    }

    /**
     * Adds a file to the request with custom provided file content-type
     *
     * @param key         the key name for the new param.
     * @param file        the file to add.
     * @param contentType the content type of the file, eg. application/json
     * @throws FileNotFoundException throws if wrong File argument was passed
     */
    public void put(String key, File file, String contentType) throws FileNotFoundException {
        put(key, file, contentType, null);
    }

    /**
     * Adds a file to the request with both custom provided file content-type and file name
     *
     * @param key            the key name for the new param.
     * @param file           the file to add.
     * @param contentType    the content type of the file, eg. application/json
     * @param customFileName file name to use instead of real file name
     * @throws FileNotFoundException throws if wrong File argument was passed
     */
    public void put(String key, File file, String contentType, String customFileName) throws FileNotFoundException {
        if (file == null || !file.exists()) {
            throw new FileNotFoundException();
        }
        if (key != null) {
            fileParams.put(key, new RequestParams.FileWrapper(file, contentType, customFileName));
        }
    }

    /**
     * Adds an input stream to the request.
     *
     * @param key    the key name for the new param.
     * @param stream the input stream to add.
     */
    public void put(String key, InputStream stream) {
        put(key, stream, null);
    }

    /**
     * Adds an input stream to the request.
     *
     * @param key    the key name for the new param.
     * @param stream the input stream to add.
     * @param name   the name of the stream.
     */
    public void put(String key, InputStream stream, String name) {
        put(key, stream, name, null);
    }

    /**
     * Adds an input stream to the request.
     *
     * @param key         the key name for the new param.
     * @param stream      the input stream to add.
     * @param name        the name of the stream.
     * @param contentType the content type of the file, eg. application/json
     */
    public void put(String key, InputStream stream, String name, String contentType) {
        put(key, stream, name, contentType, autoCloseInputStreams);
    }

    /**
     * Adds an input stream to the request.
     *
     * @param key         the key name for the new param.
     * @param stream      the input stream to add.
     * @param name        the name of the stream.
     * @param contentType the content type of the file, eg. application/json
     * @param autoClose   close input stream automatically on successful upload
     */
    public void put(String key, InputStream stream, String name, String contentType, boolean autoClose) {
        if (key != null && stream != null) {
            streamParams.put(key, RequestParams.StreamWrapper.newInstance(stream, name, contentType, autoClose));
        }
    }

    /**
     * Adds param with non-string value (e.g. Map, List, Set).
     *
     * @param key   the key name for the new param.
     * @param value the non-string value object for the new param.
     */
    public void put(String key, Object value) {
        if (key != null && value != null) {
            urlParamsWithObjects.put(key, value);
        }
    }

    /**
     * Adds a int value to the request.
     *
     * @param key   the key name for the new param.
     * @param value the value int for the new param.
     */
    public void put(String key, int value) {
        if (key != null) {
            urlParams.put(key, String.valueOf(value));
        }
    }

    /**
     * Adds a long value to the request.
     *
     * @param key   the key name for the new param.
     * @param value the value long for the new param.
     */
    public void put(String key, long value) {
        if (key != null) {
            urlParams.put(key, String.valueOf(value));
        }
    }

    /**
     * Adds string value to param which can have more than one value.
     *
     * @param key   the key name for the param, either existing or new.
     * @param value the value string for the new param.
     */
    public void add(String key, String value) {
        if (key != null && value != null) {
            Object params = urlParamsWithObjects.get(key);
            if (params == null) {
                // Backward compatible, which will result in "k=v1&k=v2&k=v3"
                params = new HashSet<String>();
                this.put(key, params);
            }
            if (params instanceof List) {
                ((List<Object>) params).add(value);
            } else if (params instanceof Set) {
                ((Set<Object>) params).add(value);
            }
        }
    }

    /**
     * Removes a parameter from the request.
     *
     * @param key the key name for the parameter to remove.
     */
    public void remove(String key) {
        urlParams.remove(key);
        streamParams.remove(key);
        fileParams.remove(key);
        urlParamsWithObjects.remove(key);
        fileArrayParams.remove(key);
    }

    /**
     * Check if a parameter is defined.
     *
     * @param key the key name for the parameter to check existence.
     * @return Boolean
     */
    public boolean has(String key) {
        return urlParams.get(key) != null ||
                streamParams.get(key) != null ||
                fileParams.get(key) != null ||
                urlParamsWithObjects.get(key) != null ||
                fileArrayParams.get(key) != null;
    }

    public void setHttpEntityIsRepeatable(boolean flag) {
        this.isRepeatable = flag;
    }

    public void setUseJsonStreamer(boolean flag) {
        this.useJsonStreamer = flag;
    }

    /**
     * Sets an additional field when upload a JSON object through the streamer
     * to hold the time, in milliseconds, it took to upload the payload. By
     * default, this field is set to "_elapsed".
     * <p>&nbsp;</p>
     * To disable this feature, call this method with null as the field value.
     *
     * @param value field name to add elapsed time, or null to disable
     */
    public void setElapsedFieldInJsonStreamer(String value) {
        this.elapsedFieldInJsonStreamer = value;
    }

    /**
     * Set global flag which determines whether to automatically close input streams on successful
     * upload.
     *
     * @param flag boolean whether to automatically close input streams
     */
    public void setAutoCloseInputStreams(boolean flag) {
        autoCloseInputStreams = flag;
    }

    /**
     * Returns an HttpEntity containing all request parameters.
     *
     * @param progressHandler HttpResponseHandler for reporting progress on entity submit
     * @return HttpEntity resulting HttpEntity to be included along with {@link
     * HttpEntityEnclosingRequestBase}
     * @throws IOException if one of the streams cannot be read
     */
    public HttpEntity getEntity(ResponseHandlerInterface progressHandler) throws IOException {
        if (useJsonStreamer) {
            return createJsonStreamerEntity(progressHandler);
        } else if (!forceMultipartEntity && streamParams.isEmpty() && fileParams.isEmpty() && fileArrayParams.isEmpty()) {
            return createFormEntity();
        } else {
            return createMultipartEntity(progressHandler);
        }
    }

    private HttpEntity createJsonStreamerEntity(ResponseHandlerInterface progressHandler) throws IOException {
        JEBJsonStreamerEntity entity = new JEBJsonStreamerEntity(
                progressHandler,
                !fileParams.isEmpty() || !streamParams.isEmpty(),
                elapsedFieldInJsonStreamer);

        // Add string params
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            entity.addPart(entry.getKey(), entry.getValue());
        }

        // Add non-string params
        for (ConcurrentHashMap.Entry<String, Object> entry : urlParamsWithObjects.entrySet()) {
            entity.addPart(entry.getKey(), entry.getValue());
        }

        // Add file params
        for (ConcurrentHashMap.Entry<String, RequestParams.FileWrapper> entry : fileParams.entrySet()) {
            entity.addPart(entry.getKey(), entry.getValue());
        }

        // Add stream params
        for (ConcurrentHashMap.Entry<String, RequestParams.StreamWrapper> entry : streamParams.entrySet()) {
            RequestParams.StreamWrapper stream = entry.getValue();
            if (stream.inputStream != null) {
                entity.addPart(entry.getKey(),
                        RequestParams.StreamWrapper.newInstance(
                                stream.inputStream,
                                stream.name,
                                stream.contentType,
                                stream.autoClose)
                );
            }
        }

        return entity;
    }

    private HttpEntity createFormEntity() {
        try {
            return new UrlEncodedFormEntity(getParamsList(), contentEncoding);
        } catch (UnsupportedEncodingException e) {
            JEBAsyncHttpClient.log.e(LOG_TAG, "createFormEntity failed", e);
            return null; // Can happen, if the 'contentEncoding' won't be HTTP.UTF_8
        }
    }

    private HttpEntity createMultipartEntity(ResponseHandlerInterface progressHandler) throws IOException {
        JEBAMultipartEntity entity = new JEBAMultipartEntity(progressHandler);
        entity.setIsRepeatable(isRepeatable);

        // Add string params
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            entity.addPartWithCharset(entry.getKey(), entry.getValue(), contentEncoding);
        }

        // Add non-string params
        List<BasicNameValuePair> params = getParamsList(null, urlParamsWithObjects);
        for (BasicNameValuePair kv : params) {
            entity.addPartWithCharset(kv.getName(), kv.getValue(), contentEncoding);
        }

        // Add stream params
        for (ConcurrentHashMap.Entry<String, RequestParams.StreamWrapper> entry : streamParams.entrySet()) {
            RequestParams.StreamWrapper stream = entry.getValue();
            if (stream.inputStream != null) {
                entity.addPart(entry.getKey(), stream.name, stream.inputStream,
                        stream.contentType);
            }
        }

        // Add file params
        for (ConcurrentHashMap.Entry<String, RequestParams.FileWrapper> entry : fileParams.entrySet()) {
            RequestParams.FileWrapper fileWrapper = entry.getValue();
            entity.addPart(entry.getKey(), fileWrapper.file, fileWrapper.contentType, fileWrapper.customFileName);
        }

        // Add file collection
        for (ConcurrentHashMap.Entry<String, List<RequestParams.FileWrapper>> entry : fileArrayParams.entrySet()) {
            List<RequestParams.FileWrapper> fileWrapper = entry.getValue();
            for (RequestParams.FileWrapper fw : fileWrapper) {
                entity.addPart(entry.getKey(), fw.file, fw.contentType, fw.customFileName);
            }
        }

        return entity;
    }

    protected List<BasicNameValuePair> getParamsList() {
        List<BasicNameValuePair> lparams = new LinkedList<BasicNameValuePair>();

        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            lparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        lparams.addAll(getParamsList(null, urlParamsWithObjects));

        return lparams;
    }

    private List<BasicNameValuePair> getParamsList(String key, Object value) {
        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        if (value instanceof Map) {
            Map map = (Map) value;
            List list = new ArrayList<Object>(map.keySet());
            // Ensure consistent ordering in query string
            if (list.size() > 0 && list.get(0) instanceof Comparable) {
                Collections.sort(list);
            }
            for (Object nestedKey : list) {
                if (nestedKey instanceof String) {
                    Object nestedValue = map.get(nestedKey);
                    if (nestedValue != null) {
                        params.addAll(getParamsList(key == null ? (String) nestedKey : String.format(Locale.US, "%s[%s]", key, nestedKey),
                                nestedValue));
                    }
                }
            }
        } else if (value instanceof List) {
            List list = (List) value;
            int listSize = list.size();
            for (int nestedValueIndex = 0; nestedValueIndex < listSize; nestedValueIndex++) {
                params.addAll(getParamsList(String.format(Locale.US, "%s[%d]", key, nestedValueIndex), list.get(nestedValueIndex)));
            }
        } else if (value instanceof Object[]) {
            Object[] array = (Object[]) value;
            int arrayLength = array.length;
            for (int nestedValueIndex = 0; nestedValueIndex < arrayLength; nestedValueIndex++) {
                params.addAll(getParamsList(String.format(Locale.US, "%s[%d]", key, nestedValueIndex), array[nestedValueIndex]));
            }
        } else if (value instanceof Set) {
            Set set = (Set) value;
            for (Object nestedValue : set) {
                params.addAll(getParamsList(key, nestedValue));
            }
        } else {
            params.add(new BasicNameValuePair(key, value.toString()));
        }
        return params;
    }

    protected String getParamString() {
        return URLEncodedUtils.format(getParamsList(), contentEncoding);
    }

    public static class FileWrapper implements Serializable {
        public final File file;
        public final String contentType;
        public final String customFileName;

        public FileWrapper(File file, String contentType, String customFileName) {
            this.file = file;
            this.contentType = contentType;
            this.customFileName = customFileName;
        }
    }

    public static class StreamWrapper {
        public final InputStream inputStream;
        public final String name;
        public final String contentType;
        public final boolean autoClose;

        public StreamWrapper(InputStream inputStream, String name, String contentType, boolean autoClose) {
            this.inputStream = inputStream;
            this.name = name;
            this.contentType = contentType;
            this.autoClose = autoClose;
        }

        static RequestParams.StreamWrapper newInstance(InputStream inputStream, String name, String contentType, boolean autoClose) {
            return new RequestParams.StreamWrapper(
                    inputStream,
                    name,
                    contentType == null ? APPLICATION_OCTET_STREAM : contentType,
                    autoClose);
        }
    }

    public static final Creator<ReqParams> CREATOR = new Creator<ReqParams>() {
        @Override
        public ReqParams createFromParcel(Parcel in) {
            return new ReqParams(in);
        }

        @Override
        public ReqParams[] newArray(int size) {
            return new ReqParams[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isRepeatable ? 1 : 0));
        dest.writeByte((byte) (forceMultipartEntity ? 1 : 0));
        dest.writeByte((byte) (useJsonStreamer ? 1 : 0));
        dest.writeString(elapsedFieldInJsonStreamer);
        dest.writeByte((byte) (autoCloseInputStreams ? 1 : 0));
        dest.writeString(contentEncoding);
    }
}
