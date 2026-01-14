package org.example.whiskr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.retainedComponent
import io.github.vinceglb.filekit.core.FileKit
import org.example.whiskr.di.AndroidApplicationComponentDI
import org.example.whiskr.di.create

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

        FileKit.init(this)

        setContent {
            RootContent(root)
        }
    }
}
