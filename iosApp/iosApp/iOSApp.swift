import SwiftUI
import FirebaseCore
import ComposeApp

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        FirebaseApp.configure()

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
