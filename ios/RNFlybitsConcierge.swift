//
//  RNFlybitsConcierge.swift
//  FlybitsConcierge
//
//  Created by MikeH on 2022-01-28.
//  Copyright © 2022 Facebook. All rights reserved.
//

import Foundation
import React
import FlybitsConciergeSDK
import FlybitsContextSDK
import FlybitsPushSDK

/**

When ever you want to display the Concierge.
 ```
import FlybitsConcierge from 'react-native-flybits-concierge-react-native';

<SafeAreaView style={styles.container}>
         <FlybitsConcierge style={{flex:1, width: "100%"}}/>
</SafeAreaView>
 ```
 */
@objc(RNFlybitsConcierge)
class ConciergeViewManager: RCTViewManager {

    public override static func requiresMainQueueSetup() -> Bool {
      return true
    }

    public override func view() -> UIView! {
        let container = UIView(frame: .zero)

        // Concierge needs to be hosted by a ViewController. This host will properly pass ViewController
        // life-cycle events to it. You need to ensure if this method is called multiple times that you remove
        // any existing Concierge before creating a new. This will ensure you don't leak memory.
        if let delegate = UIApplication.shared.delegate, let root = delegate.window??.rootViewController {

            let fb = FlybitsConciergeManager.conciergeViewController(for: "", using: "", customPhysicalDeviceId: nil, display: [.displayMode: DisplayMode.embedded], callback: nil)

            root.addChild(fb)

            container.addSubview(fb.view)

            fb.view.translatesAutoresizingMaskIntoConstraints = false

            fb.view.leadingAnchor.constraint(equalTo: container.leadingAnchor).isActive = true
            fb.view.topAnchor.constraint(equalTo: container.safeAreaLayoutGuide.topAnchor).isActive = true
            fb.view.bottomAnchor.constraint(equalTo: container.bottomAnchor).isActive = true
            fb.view.trailingAnchor.constraint(equalTo: container.trailingAnchor).isActive = true

            fb.didMove(toParent: root)
        }

        container.clipsToBounds = true

      return container
    }

    /**
     Method connects to Flybits servers and fetches a Jwt token for Authentication.
     ```
         <Button title='Connect' styles={{ flex: 1 }} onPress={() => {
               connect()
          }} />
     ```
     */
    @objc
    func connectToFlybits() {

        FlybitsConciergeManager.shared.connectFlybitsManager(to: "C1A63879-DEC8-4EF0-B3D6-A89AE8E5FFEE", with: AnonymousIDP(), customerId: nil, physicalDeviceId: nil) { error in
            guard error == nil else {
                print(error)
                return
            }
            print("Success")
        }
    }
    /**
        This method should be called at the earilest point in your programs application.
     ```
        configure()
        AppRegistry.registerComponent(appName, () => App);
     ```
     */
    @objc
    func configureFlybits() {

        loadCustomTheme()
        FlybitsConciergeManager.configure()
        FlybitsManager.enableLogging()
        FlybitsManager.environment = FlybitsManager.Environment.other("https://api.mc-sg.flybits.com")
    }

    func loadCustomTheme(fileName: String = "ConciergeTheme", bundle: Bundle = Bundle.main) {
        ConciergeViewManager.readThemeDataFromFile(fileName: fileName, bundle: bundle)
        //self.customTheme = CustomTheme(defaultTheme: defaultTheme, customThemeData: customThemeData)
    }

    static func readThemeDataFromFile(fileName: String, bundle: Bundle) {
        guard let path = bundle.path(forResource: fileName, ofType: "json") else {
            return
        }
        print("Found \(path)")
        return //URL(fileURLWithPath: path).readJsonObject()
    }

    /**
     Method uploads context to a reserved plugin. This method concept should be called when ever the app should let Flybits' know
     if a user's context has been changed.
     ```
     <Button title='SendBatteryContext' styles={{ flex: 1 }} onPress={() => {
            sendBatteryContext()
      }}/>
     ```
     */
    @objc
    func sendContext(_ percent: Int) {
        let batteryCtx = ContextData(pluginId: "ctx.sdk.battery", values: ["percentage": percent])
        ContextManager.sendContextData([batteryCtx]) { error in
            guard error == nil else {
                print(error)
                return
            }

            print("Context Upload successfully")
        }
    }

    @objc
    func setToken() {
        
    }
}

@objc(PushHandler)
class PushHandler: NSObject {
    @objc
    static func setToken(_ data: Data) {
        PushManager.shared.configuration.apnsToken = data
    }
}


/*

 export default class App extends Component<{}> {
   render() {
     return (
       <SafeAreaView style={styles.container}>
         <Button title='Connect' styles={{ flex: 1 }} onPress={() => {
           connect()
         }} />
         <Button title='SendBatteryContext' styles={{ flex: 1 }} onPress={() => {
           sendBatteryContext()
         } }/>

         <FlybitsConcierge style={{flex:1, width: "100%"}}/>
       </SafeAreaView>
     );
   }
 }
 */
