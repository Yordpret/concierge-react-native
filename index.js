// main index.js

import { requireNativeComponent } from 'react-native';
import { NativeModules  } from 'react-native';

const { RNFlybitsConcierge } = NativeModules;

//const FlybitsConcierge2 = requireNativeComponent('RNFlybitsConcierge', null);

const FlybitsConcierge = requireNativeComponent('RNFlybitsConcierge', null);

export function connect() {
    RNFlybitsConcierge.connectToFlybits()
}

export function configure() {
    // RNFlybitsConcierge.configureFlybits()
}

export function sendBatteryContext() {
    RNFlybitsConcierge.sendContext()
}

export default FlybitsConcierge;