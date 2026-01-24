import SwiftUI
import StripeCore
import FirebaseCore
import ComposeApp

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        
        FirebaseApp.configure()
        StripeAPI.defaultPublishableKey = "pk_test_51SqLqAD9IFbFeS4YHydYpaVu3ot9Xspu6MYQCiQcbO4Zh5ELSQyc9Dim0aWLtZ0JHMPF7ZLX3VcgaB9TY7h1Pbq100onTcH6mb"
        StripeBridge.initialize()
        
        return true
    }
}

@main
struct iOSApp: App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    @StateObject var holder = LifecycleHolder()
    
    @Environment(\.scenePhase) var scenePhase
    
    var body: some Scene {
        WindowGroup {
            ContentView(component: holder.root)
                .onOpenURL { url in
                    KmpHelper.shared.handleDeepLink(url: url.absoluteString)
                }
                .onChange(of: scenePhase) { newPhase in
                    switch newPhase {
                    case .background:
                        KmpHelper.shared.stop()
                    case .inactive:
                        KmpHelper.shared.pause()
                    case .active:
                        KmpHelper.shared.resume()
                    @unknown default:
                        break
                    }
                }
        }
    }
}
