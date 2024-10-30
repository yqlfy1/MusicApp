package com.app.music.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import com.app.music.R;
import com.app.music.adapter.MyLikeMusicAdapter;
import com.app.music.model.MyLikeMusicEntity;
import com.app.music.service.MusicService;

public class MyLikeActivity extends AppCompatActivity {

    //歌曲列表
    RecyclerView myLikemusicRv;
    MyLikeMusicAdapter mMyLikeMusicAdapter;
    //存储歌曲数据
    List<MyLikeMusicEntity> mMyLikeMusicDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_like);
        initView();
        initBaseData();

        //创建适配器对象
        mMyLikeMusicAdapter = new MyLikeMusicAdapter(this, mMyLikeMusicDatas);
        myLikemusicRv.setAdapter(mMyLikeMusicAdapter);
        //数据源变化，提示适配器更新
        mMyLikeMusicAdapter.notifyDataSetChanged();
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myLikemusicRv.setLayoutManager(layoutManager);
    }

    private void initView(){
        myLikemusicRv = findViewById(R.id.mylike_music_rv);
    }
    //加载初始数据
    private void initBaseData(){
        mMyLikeMusicDatas = new ArrayList<>();
        //加载音乐数据
        MusicService musicService = new MusicService();
        mMyLikeMusicDatas = musicService.queryAllMyLikeMusicList(this);
    }


    public List getMyLikeList() {
        return this.mMyLikeMusicDatas;
    }
}