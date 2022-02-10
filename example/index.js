/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './App';
import { name as appName } from './app.json';


import { configure } from 'react-native-flybits-concierge-react-native';


configure()
AppRegistry.registerComponent(appName, () => App);
