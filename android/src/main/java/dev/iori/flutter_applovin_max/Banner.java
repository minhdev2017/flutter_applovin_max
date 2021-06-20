package dev.iori.flutter_applovin_max;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinSdkUtils;

import java.util.HashMap;
import io.flutter.Log;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;

public class Banner implements PlatformView, MaxAdViewAdListener {

    private MaxAdView adView;
    private Activity activity;
    private MaxAdFormat size;
    private final MethodChannel channel;

    final HashMap<String, MaxAdFormat> sizes = new HashMap<String, MaxAdFormat>() {
        {
            put("BANNER", MaxAdFormat.BANNER);
            put("MREC", MaxAdFormat.MREC);
            put("LEADER", MaxAdFormat.LEADER);
        }
    };

    public Banner(Context context,int id, HashMap args, BinaryMessenger messenger) {
        this.activity = FlutterApplovinMaxPlugin.getInstance().activity;
        channel = new MethodChannel(messenger,
                "flutter_applovin_banner_" + id);
        try {
            try {
                this.size = this.sizes.get(args.get("Size"));
            } catch (Exception e) {
                this.size = MaxAdFormat.BANNER;
            }
            adView = new MaxAdView( args.get("id").toString(), this.size, activity );
            adView.setListener( this );

            // Stretch to the width of the screen for banners to be fully functional
            int width = ViewGroup.LayoutParams.MATCH_PARENT;

            // Banner height on phones and tablets is 50 and 90, respectively
            //int heightPx = activity.getResources().getDimensionPixelSize( R.dimen.banner_height );

            adView.setLayoutParams( new FrameLayout.LayoutParams( width, this.dpToPx(context, this.size.getSize().getHeight()) ) );
            if(args.get("theme").toString() == "dark") {
                // Set background or background color for banners to be fully functional
                adView.setBackgroundColor(activity.getResources().getColor(R.color.black));
            }else{
                adView.setBackgroundColor(activity.getResources().getColor(R.color.white));
            }
            // Load the ad
            adView.loadAd();
        } catch (Exception e) {
            Log.e("AppLovin", "AdLoadFailed sdk error " + e.getMessage());
            new MaxAdView( "", activity );
        }

    }
    int dpToPx(Context context, int dp) {
        return AppLovinSdkUtils.dpToPx(context, dp);
    }

    @Override
    public View getView() {
        return this.adView;
    }

    @Override
    public void dispose() {
        this.adView.destroy();
    }

    // MAX Ad Listener
    @Override
    public void onAdLoaded(final MaxAd maxAd) {
        channel.invokeMethod("AdLoaded", null);
    }


    @Override
    public void onAdClicked(final MaxAd maxAd) {}


    @Override
    public void onAdLoadFailed(String adUnitId, MaxError error) {
        Log.e("AppLovin", "AdLoadFailed sdk error " + error.getAdLoadFailureInfo());
        channel.invokeMethod("AdLoadFailed", null);
    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError error) {

    }

    @Override
    public void onAdExpanded(final MaxAd maxAd) {}

    @Override
    public void onAdCollapsed(final MaxAd maxAd) {}

    @Override
    public void onAdDisplayed(final MaxAd maxAd) { /* DO NOT USE - THIS IS RESERVED FOR FULLSCREEN ADS ONLY AND WILL BE REMOVED IN A FUTURE SDK RELEASE */ }

    @Override
    public void onAdHidden(final MaxAd maxAd) { /* DO NOT USE - THIS IS RESERVED FOR FULLSCREEN ADS ONLY AND WILL BE REMOVED IN A FUTURE SDK RELEASE */ }
}
