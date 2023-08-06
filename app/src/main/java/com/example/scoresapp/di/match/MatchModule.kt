package com.example.scoresapp.di.match

import android.content.Context
import com.example.scoresapp.api.FakeMatchServiceApi
import com.example.scoresapp.api.MatchServiceApi
import com.example.scoresapp.repository.MatchRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object MatchModule {

    @ActivityRetainedScoped
    @Provides
    fun provideFakeMatchServiceApi(
        @ApplicationContext context: Context,
        moshi: Moshi
    ): FakeMatchServiceApi {
        return FakeMatchServiceApi(
            context,
            moshi
        )
    }


    @ActivityRetainedScoped
    @Provides
    fun provideMatchRepository(serviceApi: MatchServiceApi): MatchRepository {
        return MatchRepository(serviceApi)
    }


}