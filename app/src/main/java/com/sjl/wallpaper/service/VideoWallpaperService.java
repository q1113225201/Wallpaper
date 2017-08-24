package com.sjl.wallpaper.service;

import android.media.MediaPlayer;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.sjl.wallpaper.R;

public class VideoWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new VideoEngine();
    }
    class VideoEngine extends Engine{
        MediaPlayer mediaPlayer;

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            initMediaPlayer(holder);
        }

        /**
         * 初始化MediaPlayer
         * @param surfaceHolder
         */
        private void initMediaPlayer(SurfaceHolder surfaceHolder) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.video);
            mediaPlayer.setSurface(surfaceHolder.getSurface());
            mediaPlayer.setLooping(true);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if(visible){
                //可见时播放
                mediaPlayer.start();
            }else{
                //不可见时暂停
                mediaPlayer.pause();
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            //停止释放MediaPlayer
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
