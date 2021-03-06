package com.dup.beauty.mvp.presenter.impl;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.dup.beauty.R;
import com.dup.beauty.app.Constant;
import com.dup.beauty.mvp.model.util.SPUtil;
import com.dup.beauty.mvp.model.util.UserUtil;
import com.dup.beauty.mvp.presenter.contract.ISplashPresenter;
import com.dup.beauty.mvp.view.ISplashView;
import com.dup.beauty.util.NetUtil;
import com.dup.beauty.util.T;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by DP on 2016/10/14.
 */
public class SplashPresenter extends BasePresenter<ISplashView> implements ISplashPresenter {


    @Inject
    public SplashPresenter(Context context) {
        super(context);
    }

    @Override
    public void checkNetStateAndNetMode() {
        int networkType = NetUtil.getNetworkType();
        boolean onlyWifi = SPUtil.getBoolean(SPUtil.KEY_NET_MODE, false);
        boolean isFirstTimeUse = SPUtil.getBoolean(SPUtil.KEY_FIRST_TIME_USE, true);
        getView().onGetNetState(networkType, onlyWifi, isFirstTimeUse);
        SPUtil.setInfo(SPUtil.KEY_FIRST_TIME_USE, false);
    }

    @Override
    public void startDelaySplash() {
        Observable.timer(Constant.SPLASH_DELAY, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        getView().onSplash();
                    }
                });
    }

    @Override
    public void autoLogin() {
        UserUtil.getInstance().autoLogin(getContext(), new UserUtil.OnResultListener() {
            @Override
            public void onResult(String message, boolean isSuccess) {
                if (!isSuccess) {
                    T.e(getContext(), R.string.login_failed);
                }
            }
        });
    }

    @Override
    public void cancelLogin() {
        UserUtil.getInstance().removeResultListener();
    }

    @Override
    public String getAppVersion() {
        PackageManager manager;
        PackageInfo info = null;
        manager = getContext().getPackageManager();

        try {
            info = manager.getPackageInfo(getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionName;
    }
}
