import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'flutter_applovin_max.dart';

enum AdsSdkBannerAdsize {
  banner,
  mrec,
  leader,
}

class BannerPx {
  final double width;
  final double height;

  BannerPx(this.width, this.height);
}

class BannerView extends StatelessWidget {
  final Function? onError;
  final Map<AdsSdkBannerAdsize, String> sizes = {
    AdsSdkBannerAdsize.banner: 'BANNER',
    AdsSdkBannerAdsize.leader: 'LEADER',
    AdsSdkBannerAdsize.mrec: 'MREC'
  };
  final Map<AdsSdkBannerAdsize, BannerPx> sizesNum = {
    AdsSdkBannerAdsize.banner: new BannerPx(350, 50),
    AdsSdkBannerAdsize.leader: new BannerPx(double.infinity, 90),
    AdsSdkBannerAdsize.mrec: new BannerPx(300, 250)
  };
  final AdsSdkBannerAdsize size;
  final String banner_id;
  final String theme;
  BannerView(this.banner_id, this.size, {Key? key, this.theme = 'dark', this.onError}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      width: this.sizesNum[this.size]!.width,
      height: this.sizesNum[this.size]!.height,
      decoration: BoxDecoration(color: Theme.of(context).scaffoldBackgroundColor, border: Border.all()),
      child: AndroidView(
        viewType: '/Banner',
        key: UniqueKey(),
        creationParams: {'Size': this.sizes[this.size], 'id': this.banner_id, 'theme': this.theme},
        creationParamsCodec: StandardMessageCodec(),
        onPlatformViewCreated: (int id) {
          final channel = MethodChannel('flutter_applovin_banner_$id');
          channel.setMethodCallHandler((MethodCall call) {
            if (FlutterApplovinMax.isBannerLoadFailed(call) && this.onError != null) {
              this.onError!();
            }
            return Future.value();
          });
        },
      ),
    );
  }
}
