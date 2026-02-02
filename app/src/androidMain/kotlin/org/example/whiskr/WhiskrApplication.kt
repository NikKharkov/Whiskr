package org.example.whiskr

import android.app.Application
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.stripe.android.PaymentConfiguration
import org.example.whiskr.util.STRIPE_PUBLISHABLE_KEY

class WhiskrApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        PaymentConfiguration.init(
            context = this,
            publishableKey = STRIPE_PUBLISHABLE_KEY
        )

        NotifierManager.initialize(
            configuration = NotificationPlatformConfiguration.Android(
                notificationIconResId = R.drawable.ic_notification,
                showPushNotification = true
            )
        )
    }
}