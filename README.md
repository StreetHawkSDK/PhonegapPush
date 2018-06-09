# StreetHawk's Push Plugin

## Introduction
The repository hosts Phonegap plugin of StreetHawk SDK's Push module. The plugin is required if you wish to send push notifications to your application.

## Integration Steps
Add Push plugin by running the below command.
```
$ cd <APPLICATION_DIRECTORY>
$ cordova plugin add streethawkpush
```
Note that Push plugin requires StreetHawk Analytics plugin which can be added by running the below mentioned command

```
cordova plugin add streethawkanalytics  --variable APP_KEY=<YOUR_APPLICATIONS_APP_KEY> --variable URL_SCHEME=<URL_SCHEME_OF_APP>
```
Replace YOUR_APPLICATIONS_APP_KEY with app_key registered with StreetHawk for your application and URL_SCHEME_OF_APP with deeplinking scheme of your application.

## Documentation
Click [here](https://dashboard.streethawk.com/docs/sdks/phonegap/push/) for detailed documentation of StreetHawk's Push plugin.

## Other StreetHawk Plugins
* [Growth](https://github.com/StreetHawkSDK/PhonegapGrowth) for Viral and organic growth
* [Beacon](https://github.com/StreetHawkSDK/PhonegapBeacon) for running beacon based campaigns
* [Geofence](https://github.com/StreetHawkSDK/PhonegapGeofence) for running geofence based campaigns 
* [Location](https://github.com/StreetHawkSDK/PhonegapLocations) for running campaigns based on user's location
* [Feeds](https://github.com/StreetHawkSDK/PhonegapFeeds) for sending feeds in your application

[Streethawk](http://www.streethawk.com) 
