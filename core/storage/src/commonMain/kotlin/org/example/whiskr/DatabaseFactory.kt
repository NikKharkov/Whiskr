package org.example.whiskr

import me.tatarka.inject.annotations.Inject

interface DatabaseFactory {
    fun create(): AppDatabase
}