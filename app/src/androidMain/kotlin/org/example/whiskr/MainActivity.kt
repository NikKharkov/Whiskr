package org.example.whiskr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.retainedComponent
import com.russhwolf.settings.SharedPreferencesSettings
import org.example.whiskr.di.AndroidApplicationComponentDI
import org.example.whiskr.di.create
import org.example.whiskr.util.WHISKR_PREFERENCES

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val databaseFactory = AndroidDatabaseFactory(context = applicationContext)

        val appComponent =
            AndroidApplicationComponentDI::class.create(applicationContext, databaseFactory)

        val root = retainedComponent { componentContext ->
            appComponent.rootComponentFactory(componentContext)
        }

        setContent {
            RootContent(root)
        }
    }
}
