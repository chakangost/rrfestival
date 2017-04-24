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
            @Query("areaCode") String areaCode,
            @Query("MobileOS") String MobileOS,
            @Query("MobileApp") String MobileApp,
            @Query("_type") String _type,
            @Query("numOfRows") String numOfRows,
            @Query("cat1") String cat1,
            @Query("cat2") String cat2
    );

    //기본상세정보
    @GET("openapi/service/rest/KorService/detailCommon")
    Observable<FestivalResult> getFestivalCommonInformation(
            @Query("contentTypeId") String contentTypeId, //@NotNull
            @Query("contentId") String contentId
    );
    //자세한 정보(주최 주관 행사시작 종료일 등)
    @GET("openapi/service/rest/KorService/detailIntro")
    Observable<FestivalResult> getFestivalDetailInformation(
            @Query("contentTypeId") String contentTypeId, //@NotNull
            @Query("contentId") String contentId
    );
    //행사 요약(반복정보)
    @GET("openapi/service/rest/KorService/detailInfo")
    Observable<FestivalResult> getFestivalSummaryInformation(
            @Query("contentTypeId") String contentTypeId, //@NotNull
            @Query("contentId") String contentId
    );
    //추가 이미지.
    @GET("openapi/service/rest/KorService/detailImage")
    Observable<FestivalResult> getFestivalImageInformation(
            @Query("contentTypeId") String contentTypeId, //@NotNull
            @Query("contentId") String contentId
    );
}
