package org.example.whiskr

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

class AndroidDatabaseFactory(
    private val context: Context
) : DatabaseFactory {
    override fun create(): AppDatabase {
        val dbFile = context.getDatabasePath("whiskr.db")

        return Room.databaseBuilder<AppDatabase>(
            context = context,
            name = dbFile.absolutePath
        )
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}