package com.kadmiv.newsapi.repo.rest;

import com.kadmiv.newsapi.repo.model.NewsList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NewsAPI {

    /*
    q
    Keywords or phrases to search for.

    sources
    A comma-seperated string of identifiers (maximum 20) for the news sources or blogs you want headlines from. Use the /sources endpoint to locate these programmatically or look at the sources index.

    from
    A date and optional time for the oldest article allowed. This should be in ISO 8601 format (e.g. 2019-02-18 or 2019-02-18T09:35:05) Default: the oldest according to your plan.

    to
    A date and optional time for the newest article allowed. This should be in ISO 8601 format (e.g. 2019-02-18 or 2019-02-18T09:35:05) Default: the newest according to your plan.

    pageSize
    int
    The number of results to return per page. 20 is the default, 100 is the maximum.

    page
    int
    Use this to page through the results.
     */

    String BASE_URL = "https://newsapi.org/v2/";

    String COUNTRY = "country";
    String QUERY = "q";
    String API = "apiKey";
    String PAGE = "page";
    String PAGE_SIZE = "pageSize";

    @GET("top-headlines")
    Call<NewsList> getTopNews(@Query(COUNTRY) String country,
                              @Query(PAGE_SIZE) int size,
                              @Query(API) String apiKey
    );

    @GET("everything")
    Call<NewsList> getQueryNews(@Query(QUERY) String query,
                                @Query(PAGE) int page,
                                @Query(PAGE_SIZE) int size,
                                @Query(API) String apiKey
    );

    class Factory {

        private static NewsAPI api;

        public static NewsAPI getInstance() {
            if (api == null) {
                // Init REST API
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                api = retrofit.create(NewsAPI.class);
                return api;
            } else {
                return api;
            }
        }
    }
}
