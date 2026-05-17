// Copyright (c) 2026 Serhan Ensar. All rights reserved.
package com.serhanensar.homeagentmobilek.data.remote

import com.serhanensar.homeagentmobilek.data.model.SystemStatus
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HomeAgentService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): retrofit2.Response<Unit>

    @GET("api/status")
    suspend fun getStatus(
        @Query("api_key") apiKey: String
    ): SystemStatus

    @GET("api/info")
    suspend fun getInfo(
        @Query("api_key") apiKey: String
    ): retrofit2.Response<Unit> // Yapı spesifikasyonda detaylandırılmadığı için Unit bırakıldı

    @POST("api/assistant")
    suspend fun askAssistant(
        @Query("api_key") apiKey: String,
        @retrofit2.http.Body request: com.serhanensar.homeagentmobilek.data.model.AssistantRequest
    ): com.serhanensar.homeagentmobilek.data.model.AssistantResponse
}
