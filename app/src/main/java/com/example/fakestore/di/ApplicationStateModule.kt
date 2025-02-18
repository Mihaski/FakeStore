package com.example.fakestore.di

import com.example.fakestore.mvi.ApplicationState
import com.example.fakestore.mvi.Store
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationStateModule {

    @Provides
    @Singleton
    fun providesApplicationStateStore(): Store<ApplicationState> =
        Store(ApplicationState())
}