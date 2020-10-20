package org.ridlo.resto.db

import androidx.room.*
import io.reactivex.Single

@Dao
interface FoodDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cart: Food)

    @Update
    fun update(cart: Food)

    @Query("SELECT * from food WHERE foodId = :key")
    fun get(key: Long): Food?

    @Query("DELETE FROM food")
    fun clear()

    @Query("SELECT * FROM food ORDER BY foodId DESC ")
    fun getLocation(): Food?

    @Query("SELECT * FROM food")
    fun getAllLocation(): Single<List<Food>>

    @Delete
    fun deleteID(cart: Food)

    //Delete one item by id
    @Query("DELETE FROM food WHERE foodId = :itemId")
    fun deleteByItemId(itemId: Long)
}