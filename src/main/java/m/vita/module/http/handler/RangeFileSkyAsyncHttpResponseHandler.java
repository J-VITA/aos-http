package m.vita.module.http.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


import m.vita.module.http.JEBAsyncHttpClient;
import m.vita.module.http.HttpEntity;
import m.vita.module.http.HttpResponse;
import m.vita.module.http.exception.HttpResponseException;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HttpUriRequest;
import m.vita.module.http.util.HttpStatus;
import m.vita.module.http.util.StatusLine;

public abstract class RangeFileJEBAsyncHttpResponseHandler extends FileJEBAsyncHttpResponseHandler {
    private static final String LOG_TAG = "RangeFileAsyncHttpRH";

    private long current = 0;
    private boolean append = false;

    /**
     * Obtains new RangeFileJEBAsyncHttpResponseHandler and stores response in passed file
     *
     * @param file File to store response within, must not be null
     */
    public RangeFileJEBAsyncHttpResponseHandler(File file) {
        super(file);
    }

    @Override
    public void sendResponseMessage(HttpResponse response) throws IOException {
        if (!Thread.currentThread().isInterrupted()) {
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() == HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE) {
                //already finished
                if (!Thread.currentThread().isInterrupted())
                    sendSuccessMessage(status.getStatusCode(), response.getAllHeaders(), null);
            } else if (status.getStatusCode() >= 300) {
                if (!Thread.currentThread().isInterrupted())
                    sendFailureMessage(status.getStatusCode(), response.getAllHeaders(), null, new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()));
            } else {
                if (!Thread.currentThread().isInterrupted()) {
                    Header header = response.getFirstHeader(JEBAsyncHttpClient.HEADER_CONTENT_RANGE);
                    if (header == null) {
                        append = false;
                        current = 0;
                    } else {
                        JEBAsyncHttpClient.log.v(LOG_TAG, JEBAsyncHttpClient.HEADER_CONTENT_RANGE + ": " + header.getValue());
                    }
                    sendSuccessMessage(status.getStatusCode(), response.getAllHeaders(), getResponseData(response.getEntity()));
                }
            }
        }
    }

    @Override
    protected byte[] getResponseData(HttpEntity entity) throws IOException {
        if (entity != null) {
            InputStream instream = entity.getContent();
            long contentLength = entity.getContentLength() + current;
            FileOutputStream buffer = new FileOutputStream(getTargetFile(), append);
            if (instream != null) {
                try {
                    byte[] tmp = new byte[BUFFER_SIZE];
                    int l;
                    while (current < contentLength && (l = instream.read(tmp)) != -1 && !Thread.currentThread().isInterrupted()) {
                        current += l;
                        buffer.write(tmp, 0, l);
                        sendProgressMessage(current, contentLength);
                    }
                } finally {
                    instream.close();
                    buffer.flush();
                    buffer.close();
                }
            }
        }
        return null;
    }

    public void updateRequestHeaders(HttpUriRequest uriRequest) {
        if (file.exists() && file.canWrite())
            current = file.length();
        if (current > 0) {
            append = true;
            uriRequest.setHeader("Range", "bytes=" + current + "-");
        }
    }

}
