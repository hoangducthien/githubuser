package com.example.data.common

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.user.UserDao
import com.example.data.user.UserDetailDao
import com.example.data.user.UserDetailEntity
import com.example.data.user.UserEntity

@Database(entities = [UserEntity::class, UserDetailEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userDetailDao(): UserDetailDao
}