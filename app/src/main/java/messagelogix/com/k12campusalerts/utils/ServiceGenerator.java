package messagelogix.com.k12campusalerts.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by Ahmed Daou on 1/12/2016.
 *
 */
public class ServiceGenerator {

    public static final String API_BASE_URL = "https://k-12alerts.com/api/admin/v1/";//"https://www.k12alerts.com/api/admin/v1/";
    public static final String API_KEY = "22O0h2Ji1uQNi5419Qb1f6yoU861YY4";
    public static final String APP_ID = "873452dbe7fb1874f80eab6b488f718c";

    private static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new LoggingInterceptor())
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .protocols(Arrays.asList(Protocol.HTTP_1_1))
            .build();

    private static OkHttpClient clientNoProtocol = new OkHttpClient.Builder()
            .addInterceptor(new LoggingInterceptor())
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public static HashMap<String, String> getApiMap(){
        HashMap<String, String> params = new HashMap<>();
        params.put("api_key", API_KEY);
        params.put("app_id", APP_ID);
        return params;
    }
}
