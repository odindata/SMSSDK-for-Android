package com.odin.sms.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.odin.sms.OdinSMS;
import com.odin.sms.OdinSMSEventHandler;
import com.odin.sms.demo.utils.Utils;

/**
 * 请求和验证验证码的Activity
 */
public class MainActivity extends AppCompatActivity {

    private EditText mEtPhoneNum;
    private EditText mEtVerificationCode;
    private Button mBtnGmEtVerificationCode;

    /**
     * 短信sid，请求成功后返回的id，用于后续验证
     */
    private String strSmsSid;

    private CountDownTimer mTimer;
    private OdinSMSEventHandler odinSMSEventHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        ImmersionBar.with(this).barColor(R.color.colorWhite).statusBarDarkFont(true).transparentNavigationBar().init();

        initSendSMSTimer();
        registerSMSEventHandler();
    }

    private void initViews() {
        mEtPhoneNum = findViewById(R.id.et_phone_num);
        mEtVerificationCode = findViewById(R.id.et_verification_code);
        mBtnGmEtVerificationCode = findViewById(R.id.btn_get_verification);
        mBtnGmEtVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //请求验证码
                getVerificationCode();
            }
        });
        Button mBtnSubmitVerificationCode = findViewById(R.id.btn_submit_verification);
        mBtnSubmitVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //验证验证码
                submitVerificationCode();
            }
        });
    }

    /**
     * 获取验证码
     */
    private void getVerificationCode() {
        //短信模板ID，在奥丁官网开发者中心创建并申请短信模板，申请通过后，将短信模板ID填入，默认为空
        String smsTplId = null;
        String phoneNum = mEtPhoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum) || !Utils.isMobilePhoneNum(phoneNum)) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        //请求发送验证码
        OdinSMS.getSMSVerificationCode(phoneNum, smsTplId);
        mTimer.start();
        mEtVerificationCode.requestFocus();
    }

    /**
     * 验证验证码
     */
    private void submitVerificationCode() {
        String phoneNum = mEtPhoneNum.getText().toString().trim();
        String verificationCode = mEtVerificationCode.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum) || !Utils.isMobilePhoneNum(phoneNum)) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(verificationCode)) {
            Toast.makeText(this, "请获取验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        //请求验证验证码
        OdinSMS.submitSMSValidationCode(phoneNum, strSmsSid, verificationCode);
    }

    private void initSendSMSTimer() {
        mTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isFinishing()) {
                    int remainTime = (int) (millisUntilFinished / 1000L);
                    if (remainTime == 0) {
                        onFinish();
                        return;
                    }
                    if (mBtnGmEtVerificationCode != null) {
                        mBtnGmEtVerificationCode.setClickable(false);
                        String text = String.format(getString(R.string.str_re_get_verification_code), remainTime);
                        mBtnGmEtVerificationCode.setText(text);
                    }
                } else {
                    if (mTimer != null) {
                        mTimer.cancel();
                        mTimer = null;
                    }
                }
            }

            @Override
            public void onFinish() {
                if (mBtnGmEtVerificationCode != null) {
                    mBtnGmEtVerificationCode.setClickable(true);
                    mBtnGmEtVerificationCode.setText(getString(R.string.str_get_verification_code));
                }
            }
        };
    }

    /**
     * 注册回调事件
     */
    private void registerSMSEventHandler() {
        odinSMSEventHandler = new OdinSMSEventHandler() {
            /**
             * 请求发送验证码的回调事件
             * @param resultCode 结果码
             * @param smsSid     短信sid
             * @param msg        消息
             */
            @Override
            public void afterGetValidationCodeEvent(final int resultCode, final String smsSid, final String msg) {
                strSmsSid = smsSid;
                if (resultCode == OdinSMSEventHandler.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "发送验证码失败：" + msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "发送验证码成功", Toast.LENGTH_SHORT).show();
                }
            }

            /**
             * 请求验证验证码的回调事件
             * @param resultCode 结果码
             * @param msg        消息
             */
            @Override
            public void afterSubmitValidationCodeEvent(final int resultCode, final String msg) {
                if (resultCode == OdinSMSEventHandler.RESULT_SUCCESS) {
                    Toast.makeText(MainActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, ValidationSuccessActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                }
            }
        };
        OdinSMS.registerSMSEventHandler(odinSMSEventHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
        if (odinSMSEventHandler != null) {
            OdinSMS.unRegisterSMSEventHandler(odinSMSEventHandler);
        }
    }
}
