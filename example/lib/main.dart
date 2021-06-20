import 'package:flutter/material.dart';
import 'package:flutter_applovin_max/flutter_applovin_banner.dart';

import 'package:flutter_applovin_max/flutter_applovin_max.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    FlutterApplovinMax.init("ab3865a22eecc00d", "fb6af5b81cf3911b", "", "");
    super.initState();
  }

  listener(AppLovinAdListener event) {
    print(event);
    if (event == AppLovinAdListener.onUserRewarded) {
      print('👍get reward');
    }
  }

  bool isRewardedVideoAvailable = false;
  bool isFullAvailable = false;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: [
              RaisedButton(
                onPressed: () async {
                  var result = await FlutterApplovinMax.showRewardVideo(
                      (AppLovinAdListener event) => listener(event));
                  print('Show Reward Video $result');
                },
                child: Text('Show Reward Video'),
              ),
              RaisedButton(
                onPressed: () async {
                  var result = await FlutterApplovinMax.ShowFullAds(
                      (AppLovinAdListener event) => listener(event));
                  print('Show Full Ads $result');
                },
                child: Text('Show Full Ads'),
              ),
              BannerView("76ec3eef04a11522", AdsSdk.bannerAdsize.banner),
              BannerView("17405179f4c61d84", AdsSdk.bannerAdsize.mrec),
            ],
          ),
        ),
      ),
    );
  }
}
