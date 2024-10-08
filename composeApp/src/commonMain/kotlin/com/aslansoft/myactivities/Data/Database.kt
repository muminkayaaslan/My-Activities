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

    @Query("UPDATE activityentity SET enabled= :newEnableValue WHERE id = :id")
    suspend fun updateEnable(id: Int?, newEnableValue: Boolean)

    @Query ("SELECT note FROM activityentity Where type = :type AND date = :date ")
    suspend fun getReminderNoteByTypeAndDate(type: String,date: String): String?


    @Query("Select * FROM activityentity WHERE SUBSTR(date,1,10) = :date AND type = :type ORDER BY id ASC")
    suspend fun getReminderbyLocalDate(date: String,type: String): List<ActivityEntity?>

    @Query("SELECT * FROM activityentity WHERE date > :currentDate ORDER BY date ASC LIMIT 1")
    suspend fun getNextReminder(currentDate: String): ActivityEntity?


}
@Entity(tableName = "activityentity")
data class ActivityEntity(
    val note: String,
    val date: String,
    val type: String,
    val enabled: Boolean,
    @PrimaryKey(autoGenerate = true)  val id: Int? = null
)