package kr.co.thiscat.stadiumampsetting.server;

import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

public class ServerInterceptor implements Interceptor {
    private final String TAG = "AAAA";

    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final String SEPARATOR = "   ";

    private boolean isLogEnable = true;

    @Override
    public Response intercept(Chain chain) throws IOException {
        // Request
        Request request = chain.request();
        RequestBody requestBody = request.body();
        log(String.format("=> %s, %s", request.method(), request.url()));

        Charset charset = UTF8;
        if (requestBody != null)
        {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            MediaType contentType = requestBody.contentType();
            if (contentType != null)
            {
                charset = contentType.charset(UTF8);
            }

            log(SEPARATOR + buffer.readString(charset));
        }

        // Request Header
        Headers headers = request.headers();
        for (int ii = 0, count = headers.size(); ii < count; ii++)
        {
            String name = headers.name(ii);
            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name))
            {
                log(SEPARATOR + name + " : " + headers.value(ii));
            }
        }

        // Response
        Response response;
        try
        {
            response = chain.proceed(request);
        }
        catch (Exception e)
        {
            log("<= FAILED = ", e);
            throw e;
        }

        ResponseBody responseBody = response.body();
        log("<= " + response.code());

        // Response Header
        headers = response.headers();
        for (int ii = 0, count = headers.size(); ii < count; ii++)
        {
            String name = headers.name(ii);
            if ("x-request-id".equalsIgnoreCase(name))
            {
                log(SEPARATOR + name + " : " + headers.value(ii));
            }
        }

        if (HttpHeaders.hasBody(response))
        {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();

            MediaType contentType = responseBody.contentType();
            if (contentType != null)
            {
                charset = contentType.charset(UTF8);
            }

            if (responseBody.contentLength() != 0)
            {
                log(SEPARATOR + buffer.clone().readString(charset));
            }
        }

        return response;
    }

    private void log(String message)
    {
        if(isLogEnable)
        {
            Log.d(TAG, message);
        }
    }
    private void log(String message, Throwable e)
    {
        if(isLogEnable)
        {
            Log.d(TAG, message, e);
        }
    }
}
