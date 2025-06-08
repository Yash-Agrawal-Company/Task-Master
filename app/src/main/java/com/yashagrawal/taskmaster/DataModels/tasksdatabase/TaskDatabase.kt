package com.yashagrawal.taskmaster.DataModels.tasksdatabase


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yashagrawal.taskmaster.DataModels.userdatabase.UserDao
import com.yashagrawal.taskmaster.DataModels.userdatabase.UserDataModel

@Database(
    entities = [UserDataModel::class], // Add more like TaskModel::class later
    version = 1,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_master_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
