package com.example.restaurantmanagement.di

import android.content.Context
import com.example.restaurantmanagement.Util.DataTempMngr
import com.example.restaurantmanagement.domain.network.NetworkBuilder
import com.example.restaurantmanagement.domain.network.NetworkService
import com.example.restaurantmanagement.domain.repository.NetworkRepository
import com.example.restaurantmanagement.domain.repository.NetworkRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideNetworkService(@ApplicationContext context: Context): NetworkService {
        return NetworkBuilder.create(
            DataTempMngr.API_BASE_URL,
            context.cacheDir,
            (10 * 1024 * 1024).toLong()
        )
    }

    @Provides
    @Singleton
    fun provideTaskRepository(
        networkService: NetworkService,
    ): NetworkRepository {
        return NetworkRepositoryImpl(
            networkService
        )
    }
}