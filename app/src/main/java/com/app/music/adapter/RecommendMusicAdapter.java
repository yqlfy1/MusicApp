package com.app.music.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.app.music.R;
import com.app.music.activity.LrcActivity;
import com.app.music.model.MusicEntity;
import com.app.music.util.CommonUtils;

public class RecommendMusicAdapter extends RecyclerView.Adapter<RecommendMusicAdapter.MusicViewHolder>{

    Context context;
    List<MusicEntity> mDatas;

    OnItemClickListener onItemClickListener;

    private MusicEntity musicBean;



    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        public void OnItemClick(View view, int position);
    }
    public RecommendMusicAdapter(Context context, List<MusicEntity> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommend_music,parent,false);
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

        //播放
        holder.item_recommend_playIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                MusicEntity musicEntity = (MusicEntity)mDatas.get(position);
                bundle.putString("song",musicEntity.getSong());
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
        ImageView item_recommend_playIV;
        public MusicViewHolder(View itemView) {
            super(itemView);
            idTv = itemView.findViewById(R.id.item_recommend_music_num);
            songTv = itemView.findViewById(R.id.item_recommend_music_song);
            singerTv = itemView.findViewById(R.id.item_recommend_music_singer);
            albumTv = itemView.findViewById(R.id.item_recommend_music_album);
            timeTv = itemView.findViewById(R.id.item_recommend_music_durtion);
            item_recommend_playIV = itemView.findViewById(R.id.item_recommend_play);
        }
    }
}
