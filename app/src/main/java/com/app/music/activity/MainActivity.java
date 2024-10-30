package com.app.music.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.music.R;
import com.app.music.adapter.MusicAdapter;
import com.app.music.model.MusicEntity;
import com.app.music.util.CommonUtils;
import com.app.music.util.MusicUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * MainActivity --音乐首页类
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //文本组件
    TextView singerTv, songTv;
    //歌曲列表
    RecyclerView musicRv;

    //存储歌曲数据
    List<MusicEntity> mMusicDatas;
    private MusicAdapter adapter;

    //记录当前正在播放音乐的位置
    int currnetPlayPos = -1;
    //记录暂停音乐时位置
    int currentPausePos = 0;
    MediaPlayer mediaPlayer;
    //进度条
    private SeekBar seekBar;
    private Timer timer;
    //防止进度条与定时器冲突
    private boolean isSeekBarChanging;
    //当前时间 和总的时间
    private TextView music_currenttime, music_totaltime, music_bottom_songTv;
    //图片组件
    ImageView nextIv, playIv, lastIv, albumIv,
            fun_likeIv, imageview_playstyle;

    PopupWindow popupWindow;//定义popupWindow
    Button myMenuBtn, createplaylist, mylist;//我的菜单

    // 用于判断当前的播放顺序，0->单曲循环,1->顺序播放,2->随机播放
    private int play_style = 0;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 在setCOntentView前面
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        initBase();
        initView();
        initEvent();
        //创建适配器对象
        adapter = new MusicAdapter(this, mMusicDatas);
        musicRv.setAdapter(adapter);
        //数据源变化，提示适配器更新
        adapter.notifyDataSetChanged();
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        musicRv.setLayoutManager(layoutManager);
        //设置每一项的点击事件
        setEventListener();


    }

    private void setEventListener() {
        /* 设置每一项的点击事件*/
        adapter.setOnItemClickListener(new MusicAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                currnetPlayPos = position;
                MusicEntity musicBean = mMusicDatas.get(position);
                goToplayMusic(musicBean);
            }
        });

        //绑定点击 歌词
        music_bottom_songTv.setOnClickListener(v -> {
            stopMusic();
            Bundle bundle = new Bundle();
            MusicEntity musicBean = mMusicDatas.get(currnetPlayPos);
            bundle.putString("song", musicBean.getSong());
            CommonUtils.navigateTo(MainActivity.this, LrcActivity.class, bundle);
        });
    }

    //菜单按钮的单机事件
    public void OnMenu(View v) {

        myMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMusic();
                CommonUtils.navigateTo(MainActivity.this, MyActivity.class);
            }
        });


    }

    public void goToplayMusic(MusicEntity musicBean) {

        songTv.setText(musicBean.getSong());
        music_totaltime.setText("/" + musicBean.getDuration());
        stopMusic();
        //重置多媒体播放器
        mediaPlayer.reset();
        //设置新的播放路径


        try {
            mediaPlayer.setDataSource(musicBean.getPath());
            mediaPlayer.setLooping(play_style==0);
            playMusic();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * 播放音乐
     *
     */
    private void playMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            if (currentPausePos == 0) {
                try {
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    mediaPlayer.seekTo(currentPausePos);
                    seekBar.setMax(mediaPlayer.getDuration());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //从暂停到播放
                mediaPlayer.seekTo(currentPausePos);
                mediaPlayer.start();
            }
            playIv.setImageResource(R.mipmap.icon_pause);
        }

        //监听播放时回调函数
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isSeekBarChanging) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            music_currenttime.setText(CommonUtils.formatTime(mediaPlayer.getCurrentPosition()));
                        }
                    });

                }
            }
        }, 0, 50);

        mediaPlayer.setOnCompletionListener(mp ->{
            if (play_style==1){
                if (currnetPlayPos == mMusicDatas.size() - 1) {
                    currnetPlayPos = 0;
                    MusicEntity nextBean = mMusicDatas.get(currnetPlayPos);
                    goToplayMusic(nextBean);
                    return;
                }
                currnetPlayPos = currnetPlayPos + 1;
                MusicEntity nextBean = mMusicDatas.get(currnetPlayPos);
                goToplayMusic(nextBean);
            }else if (play_style==2){
                int random = new Random().nextInt(mMusicDatas.size());
                if (random==currnetPlayPos){
                    random++;
                    if (random == mMusicDatas.size() - 1){
                        random = 0;
                    }
                }
                currnetPlayPos = random;
                MusicEntity nextBean = mMusicDatas.get(currnetPlayPos);
                goToplayMusic(nextBean);
            }

        });
    }

    private void pauseMusic() {
        /* 暂停音乐的函数*/
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            currentPausePos = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            playIv.setImageResource(R.mipmap.icon_play);
        }
    }

    private void stopMusic() {
        /* 停止音乐的函数*/
        if (mediaPlayer != null) {
            currentPausePos = 0;
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.stop();
            playIv.setImageResource(R.mipmap.icon_play);
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }

    }

    private void initBase() {

        mediaPlayer = new MediaPlayer();
        mMusicDatas = new ArrayList<>();
        //加载音乐数据
        loadMusicData();
    }

    private void initView() {

        /* 初始化控件的函数*/
        nextIv = findViewById(R.id.music_bottom_iv_next);
        playIv = findViewById(R.id.music_bottom_iv_play);
        lastIv = findViewById(R.id.music_bottom_iv_last);
        albumIv = findViewById(R.id.music_bottom_iv_icon);
        singerTv = findViewById(R.id.music_bottom_tv_singer);
        songTv = findViewById(R.id.music_bottom_tv_song);
        createplaylist = findViewById(R.id.buttom_create_playlist);
        mylist = findViewById(R.id.buttom_mylist);
        musicRv = findViewById(R.id.music_rv);
        imageview_playstyle = (ImageView) this.findViewById(R.id.play_style);

        //我的喜好
        fun_likeIv = findViewById(R.id.fun_like);


        //时间
        music_currenttime = findViewById(R.id.music_currenttime);
        music_totaltime = findViewById(R.id.music_totaltime);
        //监听滚动条事件
        seekBar = (SeekBar) findViewById(R.id.playSeekBar);
        //查看歌词
        music_bottom_songTv = findViewById(R.id.music_bottom_tv_song);


    }

    public void initEvent() {
        nextIv.setOnClickListener(this);
        lastIv.setOnClickListener(this);
        playIv.setOnClickListener(this);
        fun_likeIv.setOnClickListener(this);
        createplaylist.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new MySeekBar());
        mylist.setOnClickListener(this);

        mylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到 CreateListActivity
                Intent intent = new Intent(MainActivity.this, CreateListActivity.class);
                startActivity(intent);
            }
        });

        createplaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("创建新歌单");

                // 设置对话框内容
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_create_playlist, null);
                final EditText editText = dialogView.findViewById(R.id.editTextPlaylistName);
                builder.setView(dialogView);

                // 设置确定按钮
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String playlistName = editText.getText().toString().trim();
                        if (!playlistName.isEmpty()) {
                            Log.d("11223",playlistName);
                            createNewPlaylist(playlistName);
                        }
                    }
                });

                // 设置取消按钮
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // 取消时关闭对话框
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        imageview_playstyle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                play_style++;
                if (play_style > 2) {
                    play_style = 0;
                }

                switch (play_style) {
                    case 0:
                        imageview_playstyle.setImageResource(R.mipmap.cicle);
                        Toast.makeText(MainActivity.this, "单曲循环",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        imageview_playstyle.setImageResource(R.mipmap.ordered);
                        Toast.makeText(MainActivity.this, "顺序播放",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        imageview_playstyle.setImageResource(R.mipmap.unordered);
                        Toast.makeText(MainActivity.this, "随机播放",
                                Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });
    }

    public void createNewPlaylist(String name) {
        Log.d("112233",name);
        // 创建Intent对象，用于跳转到CreateListActivity
        Intent intent = new Intent(MainActivity.this, CreateListActivity.class);

        // 将歌单名称作为额外信息传递给CreateListActivity
        intent.putExtra("PLAYLIST_NAME", name);

        // 设置Intent标志，确保CreateListActivity不会每次都重新创建
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // 启动CreateListActivity
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.music_bottom_iv_last:
                if (currnetPlayPos == 0) {
                    currnetPlayPos = mMusicDatas.size() - 1;
                    MusicEntity lastBean = mMusicDatas.get(currnetPlayPos);
                    goToplayMusic(lastBean);
                    return;
                }
                currnetPlayPos = currnetPlayPos - 1;
                MusicEntity lastBean = mMusicDatas.get(currnetPlayPos);
                goToplayMusic(lastBean);
                break;
            case R.id.music_bottom_iv_next:
                if (currnetPlayPos == mMusicDatas.size() - 1) {
                    currnetPlayPos = 0;
                    MusicEntity nextBean = mMusicDatas.get(currnetPlayPos);
                    goToplayMusic(nextBean);
                    return;
                }
                currnetPlayPos = currnetPlayPos + 1;
                MusicEntity nextBean = mMusicDatas.get(currnetPlayPos);
                goToplayMusic(nextBean);
                break;
            case R.id.music_bottom_iv_play:
                if (currnetPlayPos == -1) {
                    Toast.makeText(this, "请选择要播放的音乐", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mediaPlayer.isPlaying()) {
                    pauseMusic();
                } else {
                    playMusic();
                }
                break;
            case R.id.fun_like:
                stopMusic();
                CommonUtils.navigateTo(MainActivity.this, MyLikeActivity.class);


        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }


    private void loadMusicData() {
        mMusicDatas = MusicUtils.loadMusicData(this);
    }


}
