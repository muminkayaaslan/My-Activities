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

    @Query("DELETE FROM activityentity WHERE id = :id")
    suspend fun deleteById(id:Int)

    @Query("SELECT * FROM activityentity")
    fun getAll(): Flow<List<ActivityEntity>>

    @Query("DELETE FROM activityentity")
    suspend fun deleteAll()

    @Query("UPDATE activityentity SET enabled= :newEnableValue WHERE id = :id")
    suspend fun updateEnable(id: Int?, newEnableValue: Boolean)
}
@Entity(tableName = "activityentity")
data class ActivityEntity(
    val note: String,
    val date: String,
    val type: String,
    val enabled: Boolean,
    @PrimaryKey(autoGenerate = true)  val id: Int? = null
)