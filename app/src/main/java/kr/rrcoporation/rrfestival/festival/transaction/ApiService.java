package kr.rrcoporation.rrfestival.festival.transaction;

import kr.rrcoporation.rrfestival.festival.model.FestivalResult;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {

    @FormUrlEncoded
    @POST("test")
    Observable<Void> testapi(
            @Field("test") String test1,
            @Field("test") String test2
    );

    @GET("openapi/service/rest/KorService/areaBasedList")
    Observable<FestivalResult> fetchFestivalData(
            @Query("ServiceKey") String ServiceKey,
            @Query("areaCode") String areaCode,
            @Query("MobileOS") String MobileOS,
            @Query("MobileApp") String MobileApp,
            @Query("_type") String _type,
            @Query("numOfRows") String numOfRows,
            @Query("cat1") String cat1,
            @Query("cat2") String cat2
    );
}
