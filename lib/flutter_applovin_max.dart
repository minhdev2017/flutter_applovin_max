library applovin;

import 'dart:async';

import 'package:flutter/services.dart';

enum AppLovinAdListener {
  adLoaded,
  adLoadFailed,
  adDisplayed,
  adHidden,
  adClicked,
  onAdDisplayFailed,
  onRewardedVideoStarted,
  onRewardedVideoCompleted,
  onUserRewarded
}

typedef AppLovinListener(AppLovinAdListener listener);

class FlutterApplovinMax {
  static final MethodChannel _channel = MethodChannel('flutter_applovin_max');
  static final Map<String, AppLovinAdListener> appLovinAdListener = {
    'AdLoaded': AppLovinAdListener.adLoaded,
    'AdLoadFailed': AppLovinAdListener.adLoadFailed,
    'AdDisplayed': AppLovinAdListener.adDisplayed,
    'AdHidden': AppLovinAdListener.adHidden,
    'AdClicked': AppLovinAdListener.adClicked,
    'AdFailedToDisplay': AppLovinAdListener.onAdDisplayFailed,
    'RewardedVideoStarted': AppLovinAdListener.onRewardedVideoStarted,
    'RewardedVideoCompleted': AppLovinAdListener.onRewardedVideoCompleted,
    'UserRewarded': AppLovinAdListener.onUserRewarded,
  };

  static Future<void> init(String reward, String full, String banner, String native) async {
    try {
      await _channel.invokeMethod('Init', {'reward': reward, 'full': full, 'banner': banner, 'native': native});
    } catch (e) {
      print(e.toString());
    }
  }

  static Future<bool> showRewardVideo(AppLovinListener listener) async {
    try {
      _channel.setMethodCallHandler((MethodCall call) async => handleMethod(call, listener));
      return await _channel.invokeMethod('ShowRewardVideo');
    } catch (e) {
      print(e.toString());
      return false;
    }
  }

  static Future<bool> ShowFullAds(AppLovinListener listener) async {
    try {
      _channel.setMethodCallHandler((MethodCall call) async => handleMethod(call, listener));
      return await _channel.invokeMethod('ShowFullAds');
    } catch (e) {
      print(e.toString());
      return false;
    }
  }

  static bool isBannerLoadFailed(
    MethodCall call,
  ) {
    return appLovinAdListener[call.method] == AppLovinAdListener.adLoadFailed;
  }

  static Future<void> handleMethod(MethodCall call, AppLovinListener listener) async {
    listener(appLovinAdListener[call.method]!);
  }
}
