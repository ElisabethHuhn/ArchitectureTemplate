package com.huhn.fetchrewards.datasource.localdatasource.dbModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DBUser(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "listId") val listId: Int,
    @ColumnInfo(name = "name") val name: String,
)
