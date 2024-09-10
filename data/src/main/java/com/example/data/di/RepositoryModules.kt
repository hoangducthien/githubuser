package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.common.AppDataBase
import com.example.data.user.UserRepositoryImpl
import com.example.data.user.UserService
import com.example.domain.user.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModules {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDataBase {
        return Room.databaseBuilder(
            context,
            AppDataBase::class.java, "github-user-db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserRepository(userService: UserService, db: AppDataBase): UserRepository {
        return UserRepositoryImpl(userService, db, Dispatchers.IO)
    }

}