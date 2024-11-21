package com.example.slaughterhousescreen.network

import com.example.slaughterhousescreen.data.CurrentQ
import com.example.slaughterhousescreen.data.CurrentTicket
import com.example.slaughterhousescreen.data.ImagesResponse
import com.example.slaughterhousescreen.data.ScrollMessages
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {


    @GET("api/AndriodGetScroll")
    suspend fun getScrollMessages(
        @Query("branchcode") branchcode: String
    ): ScrollMessages


    @GET("api/AndriodGetCurrentQ")
    suspend fun getCurrentQ(
        @Query("branchcode") branchcode: String
    ) : List<CurrentQ>

    @GET("api/AndriodGetCurrentCalled")
    suspend fun getCurrentTicket(
        @Query("branchcode") branchcode: String
      //  @Query("QBoardNo") QBoardNo: String

    ) : CurrentTicket

    @GET("api/AndriodGetImages")
    suspend fun getImages(
        @Query("BaseURL") baseURL: String
    ) : ImagesResponse




}