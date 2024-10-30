package com.app.music.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.app.music.R;
import com.app.music.activity.LrcActivity;
import com.app.music.activity.MyLikeActivity;
import com.app.music.model.MyLikeMusicEntity;
import com.app.music.service.MusicService;
import com.app.music.util.CommonUtils;

public class MyLikeMusicAdapter extends RecyclerView.Adapter<MyLikeMusicAdapter.MusicViewHolder>{

    Context context;
    List<MyLikeMusicEntity> mDatas;

    OnItemClickListener onItemClickListener;

    private MyLikeMusicEntity musicBean;



    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        public void OnItemClick(View view, int position);
    }
    public MyLikeMusicAdapter(Context context, List<MyLikeMusicEntity> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mylike_music,parent,false);
        MusicViewHolder holder = new MusicViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, final int position) {
         musicBean = mDatas.get(position);
        holder.idTv.setText(musicBean.getId());
        holder.songTv.setText(musicBean.getSong());
        holder.singerTv.setText(musicBean.getSinger());
        holder.albumTv.setText(musicBean.getAlbum());
        holder.timeTv.setText(musicBean.getDuration());


        // 删除我的喜好
        holder.item_mylike_delIV.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MusicService musicService = new MusicService();
                MyLikeActivity myLikeContext = (MyLikeActivity)context;
                boolean isResult = musicService.delMyLikeMusic(context, musicBean);
                if(isResult){
                    myLikeContext.getMyLikeList().remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
                }
            }

        });
        //播放
        holder.item_mylike_playIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                MyLikeMusicEntity myLikeMusicEntity = (MyLikeMusicEntity)mDatas.get(position);
                bundle.putString("song",myLikeMusicEntity.getSong());
                CommonUtils.navigateTo(context, LrcActivity.class,bundle);
            }
        });



    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MusicViewHolder extends RecyclerView.ViewHolder{
        TextView idTv,songTv,singerTv,albumTv,timeTv;
        ImageView item_mylike_playIV,item_mylike_delIV;
        public MusicViewHolder(View itemView) {
            super(itemView);
            idTv = itemView.findViewById(R.id.item_mylike_music_num);
            songTv = itemView.findViewById(R.id.item_mylike_music_song);
            singerTv = itemView.findViewById(R.id.item_mylike_music_singer);
            albumTv = itemView.findViewById(R.id.item_mylike_music_album);
            timeTv = itemView.findViewById(R.id.item_mylike_music_durtion);
            item_mylike_playIV = itemView.findViewById(R.id.item_mylike_play);
            item_mylike_delIV = itemView.findViewById(R.id.item_mylike_del);
        }
    }
}
