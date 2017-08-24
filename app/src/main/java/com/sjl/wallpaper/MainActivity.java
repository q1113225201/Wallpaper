package com.sjl.wallpaper;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.sjl.wallpaper.service.CameraWallpaperService;
import com.sjl.wallpaper.service.VideoWallpaperService;
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
        //选择视频壁纸
        findViewById(R.id.btnVideoWallpaper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,new ComponentName(context, VideoWallpaperService.class));
                startActivity(intent);
            }
        });
        //选择相机壁纸
        findViewById(R.id.btnCameraWallpaper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermisstionUtil.requestPermissions(context, PermisstionUtil.CAMERA, 100, "正在请求拍照权限", new PermisstionUtil.OnPermissionResult() {
                    @Override
                    public void granted(int requestCode) {
                        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,new ComponentName(context, CameraWallpaperService.class));
                        startActivity(intent);
                    }

                    @Override
                    public void denied(int requestCode) {
                        ToastUtil.showToast(context, "拍照权限被禁止");
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermisstionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
