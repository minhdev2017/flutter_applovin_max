package dev.iori.flutter_applovin_max;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;


import io.flutter.Log;

public class RewardedVideo implements MaxRewardedAdListener {
    private MaxRewardedAd RewardedAd;
    private int           retryAttempt;

    public void Init(String unitId) {
        RewardedAd = MaxRewardedAd.getInstance(unitId, FlutterApplovinMaxPlugin.getInstance().activity );
        RewardedAd.setListener( this );
        RewardedAd.loadAd();
    }

    public void Show() {
        try {
            if (RewardedAd != null && RewardedAd.isReady() && FlutterApplovinMaxPlugin.getInstance().activity != null)
                RewardedAd.showAd();
        } catch (Exception e) {
            Log.e("AppLovin", e.toString());
        }
    }

    public boolean IsLoaded() {
        return RewardedAd.isReady();
    }

    public void Loaded() {
        RewardedAd.loadAd();
    }

    @Override
    public void onAdLoaded(MaxAd ad) {
        retryAttempt = 0;
        FlutterApplovinMaxPlugin.getInstance().Callback("AdLoaded");

    }


    @Override
    public void onAdDisplayed(MaxAd ad) {
        FlutterApplovinMaxPlugin.getInstance().Callback("AdDisplayed");

    }

    @Override
    public void onAdHidden(MaxAd ad) {
        FlutterApplovinMaxPlugin.getInstance().Callback("AdHidden");
        RewardedAd.loadAd();
    }

    @Override
    public void onAdClicked(MaxAd ad) {
        FlutterApplovinMaxPlugin.getInstance().Callback("AdClicked");
    }

    @Override
    public void onAdRevenuePaid(MaxAd ad) {

    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError error) {
        Log.e("AppLovin", "AdLoadFailed sdk error " + error.getAdLoadFailureInfo());
        FlutterApplovinMaxPlugin.getInstance().Callback("AdLoadFailed");
    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
        Log.e("AppLovin", "onAdDisplayFailed sdk error " + error.getAdLoadFailureInfo());
        FlutterApplovinMaxPlugin.getInstance().Callback("AdFailedToDisplay");
    }



    @Override
    public void onRewardedVideoStarted(MaxAd ad) {
        FlutterApplovinMaxPlugin.getInstance().Callback("RewardedVideoStarted");

    }

    @Override
    public void onRewardedVideoCompleted(MaxAd ad) {
        FlutterApplovinMaxPlugin.getInstance().Callback("RewardedVideoCompleted");

    }

    @Override
    public void onUserRewarded(MaxAd ad, MaxReward reward) {
        Log.d("onUserRewarded","reward" + reward);
        FlutterApplovinMaxPlugin.getInstance().Callback("UserRewarded");

    }
}
