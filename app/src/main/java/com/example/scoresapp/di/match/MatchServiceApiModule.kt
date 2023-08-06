package com.example.scoresapp.di.match

import com.example.scoresapp.api.FakeMatchServiceApi
import com.example.scoresapp.api.MatchServiceApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MatchServiceApiModule {

    @Singleton
    @Binds
    abstract fun bindMatchServiceApi(
        fakeMatchServiceApi: FakeMatchServiceApi
    ): MatchServiceApi

}