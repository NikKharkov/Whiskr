package org.example.whiskr

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.retainedComponent
import com.mmk.kmpnotifier.extensions.onCreateOrOnNewIntent
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.permission.permissionUtil
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

        var initialDeepLink = intent?.data?.toString()
        if (initialDeepLink == null) {
            initialDeepLink = intent?.extras?.getString("click_action")
                ?: intent?.extras?.getString("link")
        }

        val appComponent =
            AndroidApplicationModule::class.create(applicationContext, databaseFactory, shareService, imageShareManager)

        val root = retainedComponent { componentContext ->
            appComponent.rootComponentFactory(componentContext, initialDeepLink)
        }

        rootComponent = root

        FileKit.init(this)
        val permissionUtil by permissionUtil()
        permissionUtil.askNotificationPermission()
        NotifierManager.onCreateOrOnNewIntent(intent)
        setContent {
            RootContent(root)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        NotifierManager.onCreateOrOnNewIntent(intent)
        var link = intent.data?.toString()
        if (link == null) {
            link = intent.extras?.getString("click_action")
                ?: intent.extras?.getString("link")
        }

        if (link != null) {
            rootComponent?.onDeepLink(link)
        }
    }
}
