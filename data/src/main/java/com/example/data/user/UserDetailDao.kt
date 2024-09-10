package com.example.data.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.user.UserDetail

@Dao
interface UserDetailDao {
    @Query("SELECT * FROM UserDetailEntity WHERE login=:userName")
    fun getUserDetail(userName: String): UserDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserDetail(userDetail: UserDetailEntity)
}