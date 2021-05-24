#import "FlutterApplovinMaxPlugin.h"
#import <AppLovinSDK/AppLovinSDK.h>


@interface FlutterApplovinMaxPlugin()<MARewardedAdDelegate, MAAdViewAdDelegate>
@property (nonatomic, strong) MARewardedAd *rewardedAd;
@property (nonatomic, strong) MAInterstitialAd *interstitialAd;
@property (nonatomic, assign) NSInteger retryAttempt;
@property (nonatomic,  weak) ALSdk *sdk;
@end
@implementation FlutterApplovinMaxPlugin

FlutterMethodChannel* globalMethodChannel;

- (void)initApplovin:(FlutterMethodCall*)call{
    NSString* rewardId = call.arguments[@"reward"];
    NSString* fullId = call.arguments[@"full"];
    self.sdk = [ALSdk shared];
    self.sdk.mediationProvider = @"max";
    self.sdk.settings.muted = true;
    [self.sdk initializeSdkWithCompletionHandler:^(ALSdkConfiguration *configuration) {
        // AppLovin SDK is initialized, start loading ads
    }];
    self.rewardedAd = [MARewardedAd sharedWithAdUnitIdentifier: rewardId];
    self.rewardedAd.delegate = self;
    
    self.interstitialAd = [[MAInterstitialAd alloc] initWithAdUnitIdentifier: fullId];
    self.interstitialAd.delegate = self;
    
    // Load the first ad
    [self.rewardedAd loadAd];
    [self.interstitialAd loadAd];
}


+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    FlutterMethodChannel* channel = [FlutterMethodChannel
                                     methodChannelWithName:@"flutter_applovin_max"
                                     binaryMessenger:[registrar messenger]];
    FlutterApplovinMaxPlugin* instance = [[FlutterApplovinMaxPlugin alloc] init];
    [registrar addMethodCallDelegate:instance channel:channel];
    globalMethodChannel = channel;
}



- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    if ([@"Init" isEqualToString: call.method]) {
        [self initApplovin:call];
        result([NSNumber numberWithBool:YES]);

    } else if([@"ShowRewardVideo" isEqualToString: call.method]){
        if ( [self.rewardedAd isReady] ) {
            [self.rewardedAd showAd];
            result([NSNumber numberWithBool:YES]);
        }else{
            [self.rewardedAd loadAd];
            result([NSNumber numberWithBool:NO]);
        }
    } else if([@"ShowFullAds" isEqualToString: call.method]){
        if ( [self.interstitialAd isReady] ) {
            [self.interstitialAd showAd];
            result([NSNumber numberWithBool:YES]);
        }else{
            [self.interstitialAd loadAd];
            result([NSNumber numberWithBool:NO]);
        }
    } else {
        result(FlutterMethodNotImplemented);
    }
    
}

- (void)didClickAd:(nonnull MAAd *)ad {
    [globalMethodChannel invokeMethod:@"AdClicked" arguments: nil];
}

- (void)didDisplayAd:(nonnull MAAd *)ad {
    
    [globalMethodChannel invokeMethod:@"AdDisplayed" arguments: nil];
}

- (void)didFailToDisplayAd:(nonnull MAAd *)ad withErrorCode:(NSInteger)errorCode {
    [globalMethodChannel invokeMethod:@"AdFailedToDisplay" arguments: nil];
    [self.rewardedAd loadAd];
}

- (void)didFailToLoadAdForAdUnitIdentifier:(nonnull NSString *)adUnitIdentifier withErrorCode:(NSInteger)errorCode {
    if ( [self.rewardedAd isReady] == false) {
        [self.rewardedAd loadAd];
    }
    if ( [self.interstitialAd isReady] == false) {
        [self.interstitialAd loadAd];
    }
    
    
}

- (void)didHideAd:(nonnull MAAd *)ad {
    [globalMethodChannel invokeMethod:@"AdHidden" arguments: nil];
    if ( [self.rewardedAd isReady] == false) {
        [self.rewardedAd loadAd];
    }
    if ( [self.interstitialAd isReady] == false) {
        [self.interstitialAd loadAd];
    }
}

- (void)didLoadAd:(nonnull MAAd *)ad {
    [globalMethodChannel invokeMethod:@"AdLoaded" arguments: nil];
}

- (void)didCompleteRewardedVideoForAd:(nonnull MAAd *)ad {
    [globalMethodChannel invokeMethod:@"RewardedVideoCompleted" arguments: nil];
}

- (void)didRewardUserForAd:(nonnull MAAd *)ad withReward:(nonnull MAReward *)reward {
    [globalMethodChannel invokeMethod:@"UserRewarded" arguments: nil];}

- (void)didStartRewardedVideoForAd:(nonnull MAAd *)ad {
    [globalMethodChannel invokeMethod:@"RewardedVideoStarted" arguments: nil];}
- (void)didExpandAd:(MAAd *)ad
{
    
}

- (void)didCollapseAd:(MAAd *)ad
{
   
}
@end
