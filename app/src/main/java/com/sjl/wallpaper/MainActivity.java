package com.sjl.wallpaper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.sjl.wallpaper.util.PermisstionUtil;
import com.sjl.wallpaper.util.ToastUtil;

public class MainActivity extends Activity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        context = this;
        findViewById(R.id.btnChooseWallpaper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseWallpaper();
            }
        });
    }

    /**
     * 选择壁纸
     */
    private void chooseWallpaper() {
        PermisstionUtil.requestPermissions(context, PermisstionUtil.CAMERA, 100, "正在请求拍照权限", new PermisstionUtil.OnPermissionResult() {
            @Override
            public void granted(int requestCode) {
                Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
                startActivity(Intent.createChooser(intent, "请选择壁纸"));
            }

            @Override
            public void denied(int requestCode) {
                ToastUtil.showToast(context, "拍照权限被禁止");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermisstionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
