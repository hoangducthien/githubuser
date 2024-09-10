package com.example.data.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM UserEntity WHERE id >= :since ORDER BY id ASC LIMIT :limit")
    fun getUsers(limit: Int, since: Int): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<UserEntity>)

}