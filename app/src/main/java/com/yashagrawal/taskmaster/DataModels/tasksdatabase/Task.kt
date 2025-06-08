package com.yashagrawal.taskmaster.DataModels.tasksdatabase

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "tasks_list")
data class TaskModel(
    @ColumnInfo(name = "task_id")
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val timestamp: Long
)

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskModel)

    @Delete
    suspend fun deleteTask(task: TaskModel)

//    @Query("SELECT * FROM Task ORDER BY timestamp DESC")
//    fun getAllTasks(): Flow<List<Task>>
}
