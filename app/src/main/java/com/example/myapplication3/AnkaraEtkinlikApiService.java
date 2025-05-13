package com.example.myapplication3;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface AnkaraEtkinlikApiService {
    // Etkinlikleri almak için GET isteği
    @Headers("X-Etkinlik-Token: 2057eaeed269909d9e49371465bdcbfb")
    @GET("api/v2/events")
    Call<AnkaraEventResponse> getEtkinlikler(
            @Query("city_ids") int cityId,
            @Query("take") int take
    );
}
