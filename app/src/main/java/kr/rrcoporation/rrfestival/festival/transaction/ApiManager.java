package kr.rrcoporation.rrfestival.festival.transaction;

import android.content.Context;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private String BASE_URL = "";
    private static ApiManager instance;
    public static ApiService apiService;
    private ApiManager() {}

//    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            Request original = chain.request();
//            HttpUrl originalHttpUrl = original.url();
//
//            HttpUrl url = originalHttpUrl.newBuilder()
//                    .build();
//
//            Request.Builder requestBuilder = original.newBuilder()
//                    .addHeader("User-agent", appVersion)
//                    .addHeader("token", accessToken)
//                    .url(url);
//
//            Request request = requestBuilder.build();
//            return chain.proceed(request);
//        }
//    });

    public static void init(Context context) {
        if (instance != null) {
            throw new IllegalStateException("Access token is already set");
        }
        instance = new ApiManager();
        instance.init_(context);
    }

    public void init_(Context context) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()))
//                .client(httpClient.build())
                .build();
        apiService = retrofit.create(ApiService.class);
    }
}
