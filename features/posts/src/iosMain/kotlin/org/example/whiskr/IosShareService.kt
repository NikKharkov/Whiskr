package org.example.whiskr

import org.example.whiskr.domain.ShareService
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.popoverPresentationController

class IosShareService : ShareService {

    override fun share(text: String) {
        val items = listOf(text)
        val activityController = UIActivityViewController(activityItems = items, applicationActivities = null)

        val window = UIApplication.sharedApplication.keyWindow
        val rootViewController = window?.rootViewController

        var topController = rootViewController
        while (topController?.presentedViewController != null) {
            topController = topController.presentedViewController
        }

        activityController.popoverPresentationController?.sourceView = topController?.view

        topController?.presentViewController(
            viewControllerToPresent = activityController,
            animated = true,
            completion = null
        )
    }
}