package com.odin.sms.demo;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;

/**
 * 验证成功后和重新验证的Activity
 */
public class ValidationSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation_success);

        ImmersionBar.with(this).barColor(R.color.colorBlue).statusBarDarkFont(true).transparentNavigationBar().init();
    }

    public void reValidation(View view) {
        finish();
    }
}
