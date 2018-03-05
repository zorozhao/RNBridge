package com.rnbridge.base;

import android.app.Application;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactInstanceManagerBuilder;
import com.facebook.react.bridge.JavaScriptExecutorFactory;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.devsupport.RedBoxHandler;
import com.facebook.react.uimanager.UIImplementationProvider;
import com.rnbridge.BuildConfig;
import com.rnbridge.communication.CommPackage;
import com.rnbridge.constants.FileConstant;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by zhaochong on 2018/2/1.
 */

public abstract class BaseReactNativeHost extends ReactNativeHost {

    private final Application mApplication;
    private @Nullable ReactInstanceManager mReactInstanceManager;

    protected BaseReactNativeHost(Application application) {
        super(application);
        mApplication = application;
    }

    /**
     * Get the current {@link ReactInstanceManager} instance, or create one.
     */
    public ReactInstanceManager getReactInstanceManager() {
        if (mReactInstanceManager == null) {
            mReactInstanceManager = createReactInstanceManager();
        }
        return mReactInstanceManager;
    }

    /**
     * Get whether this holder contains a {@link ReactInstanceManager} instance, or not. I.e. if
     * {@link #getReactInstanceManager()} has been called at least once since this object was created
     * or {@link #clear()} was called.
     */
    public boolean hasInstance() {
        return mReactInstanceManager != null;
    }

    /**
     * Destroy the current instance and release the internal reference to it, allowing it to be GCed.
     */
    public void clear() {
        if (mReactInstanceManager != null) {
            mReactInstanceManager.destroy();
            mReactInstanceManager = null;
        }
    }

    protected ReactInstanceManager createReactInstanceManager() {
        ReactInstanceManagerBuilder builder = ReactInstanceManager.builder()
                .setApplication(mApplication)
                .setJSMainModulePath(getJSMainModuleName())
                .setUseDeveloperSupport(getUseDeveloperSupport())
                .setRedBoxHandler(getRedBoxHandler())
                .setJavaScriptExecutorFactory(getJavaScriptExecutorFactory())
                .setUIImplementationProvider(getUIImplementationProvider())
                .setInitialLifecycleState(LifecycleState.BEFORE_CREATE);

        for (ReactPackage reactPackage : getPackages()) {
            builder.addPackage(reactPackage);
        }

        String jsBundleFile = getJSBundleFile();
        if (jsBundleFile != null) {
            builder.setJSBundleFile(jsBundleFile);
        } else {
            builder.setBundleAssetName(Assertions.assertNotNull(getBundleAssetName()));
        }
        return builder.build();
    }

    /**
     * Get the {@link RedBoxHandler} to send RedBox-related callbacks to.
     */
    protected @Nullable RedBoxHandler getRedBoxHandler() {
        return null;
    }

    /**
     * Get the {@link JavaScriptExecutorFactory}.  Override this to use a custom
     * Executor.
     */
    protected @Nullable JavaScriptExecutorFactory getJavaScriptExecutorFactory() {
        return null;
    }


    /**
     * Get the {@link UIImplementationProvider} to use. Override this method if you want to use a
     * custom UI implementation.
     *
     * Note: this is very advanced functionality, in 99% of cases you don't need to override this.
     */
    protected UIImplementationProvider getUIImplementationProvider() {
        return new UIImplementationProvider();
    }

    /**
     * Returns the name of the main module. Determines the URL used to fetch the JS bundle
     * from the packager server. It is only used when dev support is enabled.
     * This is the first file to be executed once the {@link ReactInstanceManager} is created.
     * e.g. "index.android"
     */
    protected String getJSMainModuleName() {
        return "index.android";
    }

    /**
     * Returns a custom path of the bundle file. This is used in cases the bundle should be loaded
     * from a custom path. By default it is loaded from Android assets, from a path specified
     * by {@link getBundleAssetName}.
     * e.g. "file://sdcard/myapp_cache/index.android.bundle"
     */
    protected @Nullable String getJSBundleFile() {
        return null;
    }

    /**
     * Returns the name of the bundle in assets. If this is null, and no file path is specified for
     * the bundle, the app will only work with {@code getUseDeveloperSupport} enabled and will
     * always try to load the JS bundle from the packager server.
     * e.g. "index.android.bundle"
     */
    protected @Nullable String getBundleAssetName() {
        return "index.android.bundle";
    }

    /**
     * Returns whether dev mode should be enabled. This enables e.g. the dev menu.
     */
    public abstract boolean getUseDeveloperSupport();

    /**
     * Returns a list of {@link ReactPackage} used by the app.
     * You'll most likely want to return at least the {@code MainReactPackage}.
     * If your app uses additional views or modules besides the default ones,
     * you'll want to include more packages here.
     */
    protected abstract List<ReactPackage> getPackages();
}
