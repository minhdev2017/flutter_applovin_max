package dev.iori.flutter_applovin_max;

import android.content.Context;

import java.util.HashMap;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class BannerFactory extends PlatformViewFactory {
    private final BinaryMessenger messenger;
    public BannerFactory(BinaryMessenger messenger) {
        super(StandardMessageCodec.INSTANCE);
        this.messenger = messenger;
    }

    @Override
    public PlatformView create(Context context, int viewId, Object args) {
        return new Banner(context, viewId, (HashMap) args, messenger);
    }
}
