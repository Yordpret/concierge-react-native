// main index.js

import { requireNativeComponent } from 'react-native';
import { NativeModules  } from 'react-native';

const { FlybitsModule } = NativeModules;

//const FlybitsConcierge2 = requireNativeComponent('RNFlybitsConcierge', null);

const FlybitsConcierge = requireNativeComponent('RNFlybitsConcierge', null);

export function connect() {
    FlybitsModule.connectToFlybits()
}

export function configure() {
    FlybitsModule.configureFlybits()
}

export function sendBatteryContext() {
    FlybitsModule.sendBattery()
}

export default FlybitsConcierge;
