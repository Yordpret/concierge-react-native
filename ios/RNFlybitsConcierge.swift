//
//  RNFlybitsConcierge.swift
//  FlybitsConcierge
//
//  Created by MikeH on 2022-01-28.
//  Copyright © 2022 Facebook. All rights reserved.
//

import Foundation
import React
import FlybitsConcierge
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
            
            
            let fb = Concierge.viewController(.none, params: [], options: [])

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


    @objc
    func setToken() {
        
    }
}

@objc(FlybitsModule)
class FlybitsModuleSwift: NSObject {

    @objc static func requiresMainQueueSetup() -> Bool {
        return false
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
        FlybitsManager.configure(configuration: FlybitsConfiguration.Builder()
                                    .setProjectId("C1A63879-DEC8-4EF0-B3D6-A89AE8E5FFEE")
                                    .setGateWayUrl("https://api.mc-sg.flybits.com")
                                    .build())

        FlybitsManager.enableLogging()
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
    func sendBattery() {
        let batteryCtx = ContextData(pluginId: "ctx.sdk.battery", values: ["percentage": 10])
        ContextManager.sendContextData([batteryCtx]) { error in
            guard error == nil else {
                print(error)
                return
            }
            print("Context Upload successfully")
        }
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
        Concierge.connect(with: AnonymousConciergeIDP()){ error in
            guard error == nil else {
                print(error)
                return
            }
            print("Success")
        }
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
