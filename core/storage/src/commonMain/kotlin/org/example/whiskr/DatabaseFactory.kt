package org.example.whiskr

interface DatabaseFactory {
    fun create(): AppDatabase
}