package com.dup.beauty.mvp.ui.activity;

import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.view.ViewGroup;

import com.dup.beauty.R;
import com.dup.beauty.app.BaseActivity;
import com.dup.beauty.mvp.presenter.contract.ILoginRegisterPresenter;
import com.dup.beauty.mvp.presenter.impl.LoginRegisterPresenter;
import com.dup.beauty.util.T;
import com.dup.beauty.mvp.view.ILoginRegisterView;
import com.jaeger.library.StatusBarUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登陆注册 界面
 * <ul>
 * <li>{@link ILoginRegisterPresenter}</li>
 * <li>{@link LoginRegisterPresenter}</li>
 * <li>{@link ILoginRegisterView}</li>
 * </ul>
 */
public class LoginRegisterActivity extends BaseActivity implements ILoginRegisterView {
    @BindView(R.id.toolbar)
    public ViewGroup toolbar;
    @BindView(R.id.login_register_login_layout)
    public ViewGroup loginLayout;
    @BindView(R.id.login_register_register_layout)
    public ViewGroup registerLayout;

    @BindView(R.id.login_email_et)
    public TextInputEditText loginNameEt;
    @BindView(R.id.login_pwd_et)
    public TextInputEditText loginPwdEt;
    @BindView(R.id.register_email_et)
    public TextInputEditText registerEmailEt;
    @BindView(R.id.register_account_et)
    public TextInputEditText registerAccountEt;
    @BindView(R.id.register_pwd_et)
    public TextInputEditText registerPwdEt;
    @BindView(R.id.register_confirm_pwd_et)
    public TextInputEditText registerConfirmEt;

    @Inject
    public LoginRegisterPresenter mPresenter;


    /**
     * 记录是注册还是登陆,1:注册 0:登陆
     */
    private int mType = 1;

    @Override
    protected boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_register;
    }

    @Override
    protected void bindViewToPresenters() {
        super.bindViewToPresenters();
        mPresenter.attachView(this);
    }

    @Override
    protected void unBindPresentersView() {
        super.unBindPresentersView();
        mPresenter.clearRef();
    }

    @Override
    protected void initDI() {
        super.initDI();
        mActivityComponent.inject(this);
    }

    @Override
    protected void initView() {
        super.initView();
        ButterKnife.bind(this);
        StatusBarUtil.setTransparentForImageView(this, toolbar);
    }

    @Override
    protected void initData() {
        super.initData();
        mType = getIntent().getIntExtra("TYPE", 1);
        if (mType == 1) {
            //注册
            loginLayout.setVisibility(View.GONE);
            registerLayout.setVisibility(View.VISIBLE);
        } else if (mType == 0) {
            //登陆
            loginLayout.setVisibility(View.VISIBLE);
            registerLayout.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.toolbar_back)
    public void backPress(View view) {
        finish();
    }

    @OnClick({R.id.form_finish_button, R.id.login_register_qq_authorize, R.id.login_register_weibo_authorize})
    public void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.form_finish_button:
                if (mType == 0) {
                    //登陆
                    mPresenter.formCommit(loginNameEt.getText().toString(), "", loginPwdEt.getText().toString(), "", 0);
                } else {
                    //注册
                    mPresenter.formCommit(registerEmailEt.getText().toString(), registerAccountEt.getText().toString(),
                            registerPwdEt.getText().toString(), registerConfirmEt.getText().toString(), 1);
                }
                break;
            case R.id.login_register_qq_authorize:
                break;
            case R.id.login_register_weibo_authorize:
                break;
        }
    }

    @Override
    public void onSendFormResult(String message, boolean isSuccess) {
        if (isSuccess) {
            T.s(this, message);
            finish();
        } else {
            T.e(this, message);
        }
    }
}
