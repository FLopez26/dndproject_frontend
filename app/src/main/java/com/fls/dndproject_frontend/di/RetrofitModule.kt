package com.fls.dndproject_frontend.di

import com.fls.dndproject_frontend.data.source.remote.UserServiceClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<UserServiceClient> {get<Retrofit>().create(UserServiceClient::class.java)}
}