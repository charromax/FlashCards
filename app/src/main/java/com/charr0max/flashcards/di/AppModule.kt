package com.charr0max.flashcards.di

import android.app.Application
import com.charr0max.flashcards.data.remote.AIService
import com.charr0max.flashcards.data.repository.GeminiRepositoryImpl
import com.charr0max.flashcards.data.speech.SpeechRecognizerHelper
import com.charr0max.flashcards.domain.repository.GeminiRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // ✅ Ver logs en Logcat
            .build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(json: Json, okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder().baseUrl("https://generativelanguage.googleapis.com/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideGeminiService(retrofit: Retrofit): AIService =
        retrofit.create(AIService::class.java)

    @Provides
    @Singleton
    fun provideGeminiRepository(service: AIService): GeminiRepository =
        GeminiRepositoryImpl(service)

    @Provides
    @Singleton
    fun provideSpeechRecognizerHelper(application: Application): SpeechRecognizerHelper {
        return SpeechRecognizerHelper(application)
    }
}