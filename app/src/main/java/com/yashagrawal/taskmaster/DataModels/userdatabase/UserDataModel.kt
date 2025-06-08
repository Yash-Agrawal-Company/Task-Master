package com.yashagrawal.taskmaster.DataModels.userdatabase

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.yashagrawal.taskmaster.DataModels.tasksdatabase.TaskModel
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "users_list")
data class UserDataModel(
    @ColumnInfo(name = "user_id")
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val password: String,
)

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserDataModel)
}