package ru.sulatskov.composeapplication.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.sulatskov.composeapplication.common.AppConst
import ru.sulatskov.composeapplication.model.network.ApiInterface
import javax.inject.Singleton

@Module(includes = [OkHttpClientModule::class])
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit) = retrofit.create(ApiInterface::class.java)

    @Provides
    @Singleton
    fun provideRetrofitInterface(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConst.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

}