package dev.iori.flutter_applovin_max;

import android.app.Activity;

import android.content.Context;

import androidx.annotation.NonNull;

import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinSdk;
import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.common.StandardMethodCodec;
import io.flutter.plugin.platform.PlatformViewRegistry;


public class FlutterApplovinMaxPlugin  implements FlutterPlugin, MethodCallHandler, ActivityAware {
    private static FlutterApplovinMaxPlugin instance;
    private static RewardedVideo instanceReward;
    private static InterstitialAd instanceInter;
    private static Context context;
    private static MethodChannel channel;
    public static Activity activity;


    public static FlutterApplovinMaxPlugin getInstance() {
        return instance;
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        this.RegistrarBanner(flutterPluginBinding.getPlatformViewRegistry(),flutterPluginBinding.getBinaryMessenger());
        this.onAttachedToEngine(flutterPluginBinding.getApplicationContext(), flutterPluginBinding.getBinaryMessenger());
    }

    public static void registerWith(Registrar registrar) {
        if (instance == null) {
            instance = new FlutterApplovinMaxPlugin();
        }
        instance.RegistrarBanner(registrar.platformViewRegistry(), registrar.messenger());
        instance.onAttachedToEngine(registrar.context(), registrar.messenger());
    }


    public void onAttachedToEngine(Context applicationContext, BinaryMessenger messenger) {
        if (channel != null) {
            return;
        }
        instance = new FlutterApplovinMaxPlugin();
        Log.i("AppLovin Plugin", "onAttachedToEngine");
        this.context = applicationContext;
        channel = new MethodChannel(messenger, "flutter_applovin_max", StandardMethodCodec.INSTANCE);
        channel.setMethodCallHandler(this);
    }
    public void RegistrarBanner(PlatformViewRegistry registry, BinaryMessenger messenger){
        registry.registerViewFactory("/Banner", new BannerFactory(messenger));
    }

    public FlutterApplovinMaxPlugin() {
    }

    AppLovinSdk sdk;

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        try {
            switch (call.method) {
                case "Init":
                    sdk = AppLovinSdk.getInstance(context);
                    sdk.setMediationProvider( AppLovinMediationProvider.MAX );
                    sdk.getSettings().setMuted( true );
                    instanceReward.Init(call.argument("reward").toString());
                    instanceInter.Init(call.argument("full").toString());
                    result.success(Boolean.TRUE);
                    break;
                    case "ShowRewardVideo":
                        if(instanceReward.IsLoaded()) {
                            instanceReward.Show();
                            result.success(Boolean.TRUE);
                        }else{
                            instanceReward.Loaded();
                            result.success(Boolean.FALSE);
                        }
                    break;
                case "ShowFullAds":
                    if(instanceInter.IsLoaded()) {
                        instanceInter.Show();
                        result.success(Boolean.TRUE);
                    }else{
                        instanceInter.Loaded();
                        result.success(Boolean.FALSE);

                    }
                    break;
                default:
                    result.notImplemented();
            }
        } catch (Exception err) {
            Log.e("Method error", err.toString());
            result.notImplemented();
        }
    }

    static public void Callback(final String method) {
        if (instance.context != null && instance.channel != null && instance.activity != null) {
            instance.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    instance.channel.invokeMethod(method, null);
                }
            });
        } else {
            Log.e("AppLovin", "instance method channel not created");
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        try {
            this.context = null;
            this.channel.setMethodCallHandler(null);
            this.channel = null;
        }catch (Exception err) {
            Log.e("Method error", err.toString());
        }
    }

    @Override
    public void onAttachedToActivity(ActivityPluginBinding binding) {
        this.activity = binding.getActivity();
        instance.instanceReward = new RewardedVideo();
        instance.instanceInter= new InterstitialAd();
        Log.i("AppLovin Plugin", "Instances created");
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
    }

    @Override
    public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
        this.activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {
    }
}
