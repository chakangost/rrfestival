package kr.rrcoporation.rrfestival.festival.transaction;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {

    @FormUrlEncoded
    @POST("v1/members/login/username")
    Observable<Void> testapi(
            @Field("test") String test1,
            @Field("test") String test2
    );

    @GET("v1/members/username/validate")
    Observable<Void> testapi2(
            @Query("test") String test
    );
}
