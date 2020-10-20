package org.ridlo.resto.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.ridlo.resto.CartActivity
import org.ridlo.resto.fragment.MenuFragment


@Database(entities = [Food::class], version = 3, exportSchema = false)
abstract class FoodCartDatabase : RoomDatabase() {
    abstract val cartFoodDatabaseDAO : FoodDAO

    companion object {

        @Volatile
        private var INSTANCE: FoodCartDatabase? = null

        fun getInstance(context: Context): FoodCartDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FoodCartDatabase::class.java,
                        "food_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}