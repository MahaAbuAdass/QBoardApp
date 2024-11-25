package com.example.slaughterhousescreen.network

import com.example.slaughterhousescreen.data.CurrentQ
import com.example.slaughterhousescreen.data.CurrentTicket
import com.example.slaughterhousescreen.data.ImagesResponse
import com.example.slaughterhousescreen.data.ScrollMessages
import com.example.slaughterhousescreen.data.TimeResponse
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


    @GET("api/GetCurrentQAnd")
    suspend fun getCurrentQ(
        @Query("BranchCode") BranchCode: String
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


    @GET("api/Get_Current_Time")
    suspend fun getCurrentTime(
    ) : TimeResponse

}