package com.iceboy.destinedchat.ui.activity;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.presenter.impl.RegisterPresenterImpl;
import com.iceboy.destinedchat.view.RegisterView;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity implements RegisterView {

    private RegisterPresenterImpl mRegisterPresenter;

    @BindView(R.id.cv_add)
    CardView mCvAdd;

    @BindView(R.id.username_et)
    EditText mUserNameEt;

    @BindView(R.id.password_et)
    EditText mPasswordEt;

    @BindView(R.id.repeat_password_et)
    EditText mRepeatPasswordEt;

    @BindView(R.id.register_btn)
    Button mLoginBtn;

    @BindView(R.id.login_fab)
    FloatingActionButton mLoginFab;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected void init() {
        ShowEnterAnimation();
        mRegisterPresenter = new RegisterPresenterImpl(this);
        mRepeatPasswordEt.setOnEditorActionListener(mOnEditorActionListener);
    }

    @OnClick({R.id.register_btn, R.id.login_fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_btn:
                register();
                break;
            case R.id.login_fab:
                animateRevealClose();
                break;
        }
    }

    /**
     * 显示进入动画
     */
    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                mCvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    /**
     * 显示动画
     */
    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(mCvAdd, mCvAdd.getWidth() / 2,
                0, mLoginFab.getWidth() / 2, mCvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mCvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    /**
     * 关闭动画
     */
    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(mCvAdd, mCvAdd.getWidth() / 2,
                0, mCvAdd.getHeight(), mLoginFab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                mLoginFab.setImageResource(R.drawable.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    /**
     * 注册用户名
     */
    private void register() {
        hideKeyBoard();
        String userName = mUserNameEt.getText().toString().trim();
        String password = mPasswordEt.getText().toString().trim();
        String confirmPassword = mRepeatPasswordEt.getText().toString().trim();
        mRegisterPresenter.register(userName, password, confirmPassword);
    }

    @Override
    public void onStartRegister() {
        showProgress(getString(R.string.registering));
    }

    @Override
    public void onRegisterError() {
        hideProgress();
        toast(getString(R.string.register_failed));
    }

    @Override
    public void onResisterUserExist() {
        hideProgress();
        toast(getString(R.string.register_failed_user_exist));
    }

    @Override
    public void onRegisterSuccess() {
        hideProgress();
        toast(getString(R.string.register_success));
        startActivity(LoginActivity.class);
    }

    @Override
    public void onUserNameError() {
        mUserNameEt.setError(getString(R.string.user_name_error));

    }

    @Override
    public void onPasswordError() {
        mPasswordEt.setError(getString(R.string.user_password_error));

    }

    @Override
    public void onRepeatPasswordError() {
        mRepeatPasswordEt.setError(getString(R.string.user_password_confirm_error));
    }

    /**
     * 监听软键盘的完成事件
     */
    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                register();
                return true;
            }
            return false;
        }
    };
}
