package org.example.whiskr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.retainedComponent
import org.example.whiskr.di.AndroidApplicationComponentDI
import org.example.whiskr.di.create

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val appComponent = AndroidApplicationComponentDI::class.create(applicationContext)

        val root =
            retainedComponent { componentContext ->
                appComponent.rootComponentFactory(componentContext)
            }

        setContent {
            RootContent(root)
        }
    }
}
