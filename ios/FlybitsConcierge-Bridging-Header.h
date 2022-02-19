//
//  Use this file to import your target's public headers that you would like to expose to Swift.
//

#import <React/RCTViewManager.h>
#import <React/RCTBridgeModule.h>

#import <NSObject+ExtraMethods.h>

@interface RCT_EXTERN_MODULE(RNFlybitsConcierge, RCTViewManager)

@end

@interface RCT_EXTERN_MODULE(FlybitsModule, NSObject)
RCT_EXTERN_METHOD(connectToFlybits)
RCT_EXTERN_METHOD(configureFlybits)
RCT_EXTERN_METHOD(sendBattery)
RCT_EXTERN_METHOD(disconnectToFlybits)
@end

