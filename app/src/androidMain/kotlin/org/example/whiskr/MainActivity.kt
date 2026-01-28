package org.example.whiskr

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.retainedComponent
import io.github.vinceglb.filekit.core.FileKit
import org.example.whiskr.di.AndroidApplicationModule
import org.example.whiskr.di.create
import org.example.whiskr.root.RootComponent
import org.example.whiskr.share.AndroidImageShareManager

class MainActivity : ComponentActivity() {

    private var rootComponent: RootComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val databaseFactory = AndroidDatabaseFactory(applicationContext)
        val shareService = AndroidShareService(applicationContext)
        val imageShareManager = AndroidImageShareManager(applicationContext)
        val initialDeepLink = intent?.data?.toString()

        val appComponent =
            AndroidApplicationModule::class.create(applicationContext, databaseFactory, shareService, imageShareManager)

        val root = retainedComponent { componentContext ->
            appComponent.rootComponentFactory(componentContext, initialDeepLink)
        }

        rootComponent = root

        FileKit.init(this)

        setContent {
            RootContent(root)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val newLink = intent.data?.toString()
        if (newLink != null) {
            rootComponent?.onDeepLink(newLink)
        }
    }
}
