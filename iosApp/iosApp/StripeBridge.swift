import SwiftUI
import StripePaymentSheet
import ComposeApp

class StripeBridge {
    
    static func initialize() {
        StripeEntryPoint.shared.register { clientSecret, onResultKotlin in
            
            guard let rootVC = UIApplication.shared.keyWindow?.rootViewController else {
                print("Stripe Error: No Root VC")
                onResultKotlin(false)
                return
            }
            
            var configuration = PaymentSheet.Configuration()
            configuration.merchantDisplayName = "Whiskr"
            
            let paymentSheet = PaymentSheet(
                paymentIntentClientSecret: clientSecret,
                configuration: configuration
            )
            
            paymentSheet.present(from: rootVC) { result in
                switch result {
                case .completed:
                    print("Stripe: Completed")
                    onResultKotlin(true)
                case .canceled:
                    print("Stripe: Canceled")
                    onResultKotlin(false)
                case .failed(let error):
                    print("Stripe: Failed \(error.localizedDescription)")
                    onResultKotlin(false)
                }
            }
        }
    }
}
