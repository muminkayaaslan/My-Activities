package com.aslansoft.myactivities.Data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Database(entities = [ActivityEntity::class], version = 1)
abstract class ActivityDatabase : RoomDatabase() {
    abstract fun getDao():ActivityDao
}

@Dao
interface ActivityDao {
    @Insert
    suspend fun insert(item: ActivityEntity)
    @Delete
    suspend fun delete(item: ActivityEntity)
    @Query("SELECT * FROM activityentity")
    fun getAll(): Flow<List<ActivityEntity>>
    @Query("DELETE FROM activityentity")
    suspend fun deleteAll()
}
@Entity(tableName = "activityentity")
data class ActivityEntity(
    val note: String,
    val date: String,
    val type: String,
    val enabled: Boolean,
    @PrimaryKey(autoGenerate = true)  val id: Int? = null
)