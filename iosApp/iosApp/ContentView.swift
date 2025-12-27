import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    
    let component: RootComponent
    
    func makeUIViewController(context: Context) -> UIViewController {
            return MainViewControllerKt.MainViewController(root: component)
        }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    
    let component: RootComponent
    
    var body: some View {
        ComposeView(component: component)
            .ignoresSafeArea()
    }
}



