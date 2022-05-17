package messagelogix.com.k12campusalerts.utils;

import android.util.Log;

import java.io.IOException;
import java.util.Locale;


import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by Ahmed Daou on 1/19/2016.
 */
public class LoggingInterceptor implements Interceptor {
    private static final String LOG_TAG = LoggingInterceptor.class.getSimpleName();
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        String requestBody = bodyToString(request);
        int bodyLength = requestBody.length();
        if (requestBody.length() > 2000){
            requestBody = "<BODY-TOO-LARGE>";
        }
        Log.v(LOG_TAG, String.format(
                "=======================%nSending request %s on %s%nBody: %s%nLength: %s%n%n%s%n=======================",
                request.url(), chain.connection(),
                requestBody, bodyLength,
                request.headers()));
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        Log.v(LOG_TAG, String.format(Locale.US,
                "=======================%nReceived response for %s in %.1fms%n%s%n=======================",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));
        return response;
    }

    private String bodyToString(final Request request) {

        try {
            Request copy = request.newBuilder().build();
            Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (Exception e) {
            return "Can't parse body: " + e.getMessage();
        }
    }
}