import SwiftUI
import ComposeApp

class LifecycleHolder: ObservableObject {
    
    let root: RootComponent = KmpHelper.shared.root
    
    init() {
        KmpHelper.shared.resume()
    }
    
    deinit {
        KmpHelper.shared.destroy()
    }
}
