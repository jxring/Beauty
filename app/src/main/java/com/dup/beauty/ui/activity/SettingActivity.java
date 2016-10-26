package com.dup.beauty.ui.activity;

import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dup.beauty.R;
import com.dup.beauty.app.BaseActivity;
import com.dup.beauty.presenter.contract.ISettingPresenter;
import com.dup.beauty.presenter.impl.SettingPresenter;
import com.dup.beauty.util.T;
import com.dup.beauty.view.ISettingView;
import com.dup.changeskin.SkinManager;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * menu设置 界面
 * <ul>
 * <li>{@link ISettingView}</li>
 * <li>{@link ISettingPresenter}</li>
 * <li>{@link SettingPresenter}</li>
 * </ul>
 */
public class SettingActivity extends BaseActivity implements ISettingView {
    private ISettingPresenter mPresenter;

    @OnClick({R.id.setting_clear_img, R.id.setting_clear_net})
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.setting_clear_img:
                new MaterialDialog.Builder(this).title(R.string.clear_cache_img)
                        .content(R.string.clear_cache_img_warning).negativeText(R.string.cancel)
                        .positiveText(R.string.done).onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (which == DialogAction.POSITIVE) {
                            mPresenter.clearImageCache();
                        }
                    }
                }).show();
                break;
            case R.id.setting_clear_net:
                new MaterialDialog.Builder(this).title(R.string.clear_cache_net)
                        .content(R.string.clear_cache_net_warning).negativeText(R.string.cancel)
                        .positiveText(R.string.done).onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (which == DialogAction.POSITIVE) {
                            mPresenter.clearNetCache();
                        }
                    }
                }).show();
                break;
        }
    }

    @Override
    protected boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void bindPresenters() {
        super.bindPresenters();
        this.mPresenter = new SettingPresenter(this, this);
    }

    @Override
    protected void initData() {
        super.initData();
        StatusBarUtil.setColor(this, SkinManager.getInstance().getResourceManager().getColor("status_bar_bg"));
        ButterKnife.bind(SettingActivity.this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImageClearedEvent(String s) {
        if ("IMAGE_CACHE_CLEAR".equals(s)) {
            T.s(this, R.string.image_cache_cleared);
        } else if ("NET_CACHE_CLEAR".equals(s)) {
            T.s(this, R.string.net_cache_cleared);
        }
    }

}
