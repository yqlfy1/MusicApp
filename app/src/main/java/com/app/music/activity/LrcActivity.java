package com.app.music.activity;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.app.music.R;
import com.app.music.model.LrcEntity;
import com.app.music.model.MusicEntity;
import com.app.music.service.LrcService;
import com.app.music.service.MusicService;
import com.app.music.util.CommonUtils;
import com.app.music.view.LrcView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 *   描述:LrcActivity 歌词处理类

 */
public class LrcActivity extends AppCompatActivity implements View.OnClickListener {
    //媒体对象
    private MediaPlayer mediaPlayer;
    //歌词处理
    private LrcService mLrcService;
    //存放歌词列表对象
    private List<LrcEntity> lrcList = new ArrayList<LrcEntity>();
    //歌词检索值
    private int index = 0;
    //记录当前正在播放的音乐的位置
    private int currentPlayPosition = -1;
    //记录暂停音乐时进度条的位置
    private int currentPausePositionInSong = 0;

    //进度条
    private SeekBar seekBar;
    //定时器
    private Timer timer;
    //防止进度条与定时器冲突
    private boolean isSeekBarChanging;

    private LrcView mLrcView;
    private Handler mHandler;
    private int currentTime;
    private int duration;

    private List<MusicEntity> musicEntityList;
    private MusicEntity mLocalMusicBean;
    private Animation operatingAnim ;
    //文本组件
    private TextView lrc_music_currenttime,lrc_music_totaltime,
            lrc_music_bottom_tv_song;
    //歌曲播放按钮  我的喜欢按钮
    private ImageView lrc_music_bottom_iv_play,lrc_music_mylike_iv;
    private CircleImageView infoOperatingIV ;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lrc);

        //显示返回按钮
        if(NavUtils.getParentActivityName(LrcActivity.this)!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String song =  getIntent().getStringExtra("song");
        //查询所有的歌曲
        MusicService musicService  = new MusicService();
        mLocalMusicBean =  musicService.queryMusicBySong(this,song);
        mHandler = new Handler();
        initView();
        initLrc();
        goToPlayMusic(mLocalMusicBean);

        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.roraterepeat);
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        operatingAnim.setInterpolator(linearInterpolator);

        if (operatingAnim != null) {
            infoOperatingIV.startAnimation(operatingAnim);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        if (operatingAnim != null && infoOperatingIV != null && operatingAnim.hasStarted()) {
            infoOperatingIV.clearAnimation();
            infoOperatingIV.startAnimation(operatingAnim);
        }
    }

    private void initView() {
        mediaPlayer = new MediaPlayer();
        infoOperatingIV = findViewById(R.id.infoOperating);
         //歌手
        lrc_music_bottom_tv_song = findViewById(R.id.lrc_music_bottom_tv_song);
        //时间
        lrc_music_currenttime = findViewById(R.id.lrc_music_currenttime);
        lrc_music_totaltime = findViewById(R.id.lrc_music_totaltime);
        //监听滚动条事件
        seekBar = (SeekBar) findViewById(R.id.lrc_playSeekBar);
        mLrcView = findViewById(R.id.lrcShowView);
        lrc_music_bottom_iv_play = findViewById(R.id.lrc_music_bottom_iv_play);
        lrc_music_mylike_iv = findViewById(R.id.lrc_music_mylike_iv);
        lrc_music_mylike_iv.setOnClickListener(this);
        //绑定播放事件
        lrc_music_bottom_iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    //暂停音乐
                    pauseMusic();
                }else {
                    //开始播放音乐
                    playMusic();
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new MySeekBar());
    }

    /*进度条处理*/
    public class MySeekBar implements SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }

        /*滚动时,应当暂停后台定时器*/
        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = true;
        }

        /*滑动结束后，重新设置值*/
        public void onStopTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = false;
            mediaPlayer.seekTo(seekBar.getProgress());
        }
    }
    /*
     * 点击播放按钮播放音乐，或者暂停从新播放
     * */
    private void playMusic() {
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.roraterepeat);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (operatingAnim != null) {
            infoOperatingIV.startAnimation(operatingAnim);
        }
        /* 播放音乐的函数*/
        if (mediaPlayer!=null&&!mediaPlayer.isPlaying()) {
            if (currentPausePositionInSong == 0) {
                try {
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    mediaPlayer.seekTo(currentPausePositionInSong);
                    seekBar.setMax(mediaPlayer.getDuration());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
            //从暂停到播放
                mediaPlayer.seekTo(currentPausePositionInSong);
                mediaPlayer.start();
            }
            lrc_music_bottom_iv_play.setImageResource(R.mipmap.icon_pause);
        }

        //监听播放时回调函数
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!isSeekBarChanging){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lrc_music_currenttime.setText(CommonUtils.formatTime(mediaPlayer.getCurrentPosition()));

                        }
                    });

                }
            }
        },0,50);

    }

    private void stopMusic() {
        /* 停止音乐的函数*/
        if (mediaPlayer!=null) {
            currentPausePositionInSong = 0;
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.stop();
            lrc_music_bottom_iv_play.setImageResource(R.mipmap.icon_play);
            if(timer != null) {
                timer.cancel();
                timer = null;
            }

        }

    }

    public void goToPlayMusic(MusicEntity musicBean) {
        stopMusic();
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(musicBean.getPath());
            playMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pauseMusic() {

        infoOperatingIV.clearAnimation();
        /* 暂停音乐的函数*/
        if (mediaPlayer!=null&&mediaPlayer.isPlaying()) {
            currentPausePositionInSong = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            lrc_music_bottom_iv_play.setImageResource(R.mipmap.icon_play);
        }
    }



    /**
     * 初始化歌词配置
     */
    public void initLrc() {
        mLrcService = new LrcService();
        //设置歌曲名称
        lrc_music_bottom_tv_song.setText(mLocalMusicBean.getSong());
        //设置歌曲的总时间
        lrc_music_totaltime.setText("/"+mLocalMusicBean.getDuration());

        //读取歌词文件
        mLrcService.loadLrc(mLocalMusicBean.getPath());
        //传回处理后的歌词文件
        lrcList = mLrcService.getLrcList();
        mLrcView.setmLrcList(lrcList);
        //切换带动画显示歌词
        mLrcView.setAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.alpha_z));
        mHandler.post(mRunnable);
    }

    Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            mLrcView.setIndex(lrcIndex());
            mLrcView.invalidate();
            mHandler.postDelayed(mRunnable, 100);
        }
    };

    /**
     * 根据时间获取歌词显示的索引值
     */
    public int lrcIndex() {
        if (mediaPlayer.isPlaying()) {
            currentTime = mediaPlayer.getCurrentPosition();
            duration = mediaPlayer.getDuration();
        }
        if (currentTime < duration) {
            for (int i = 0; i < lrcList.size(); i++) {
                if (i < lrcList.size() - 1) {
                    if (currentTime < lrcList.get(i).getLrcTime() && i == 0) {
                        index = i;
                    }
                    if (currentTime > lrcList.get(i).getLrcTime()
                            && currentTime < lrcList.get(i + 1).getLrcTime()) {
                        index = i;
                    }
                }
                if (i == lrcList.size() - 1
                        && currentTime > lrcList.get(i).getLrcTime()) {
                    index = i;
                }
            }
        }
        return index;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lrc_music_mylike_iv:
                //加入到我的收藏里面
                MusicService musicService = new MusicService();
                boolean isExits = musicService.checkMyMusicIsExits(this, mLocalMusicBean.getSong());
                if(isExits){
                    Toast.makeText(this,"该歌曲已被添加到喜好",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        musicService.saveMyLikeMusic(this, mLocalMusicBean);
                        Toast.makeText(this,"添加喜好歌曲成功",Toast.LENGTH_SHORT).show();
                    }catch(Exception e){
                        Toast.makeText(this,"添加喜好歌曲失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }

                break;

        }
    }
}