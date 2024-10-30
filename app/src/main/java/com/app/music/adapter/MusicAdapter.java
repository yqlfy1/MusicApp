package com.app.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.app.music.R;
import com.app.music.model.MusicEntity;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder>{

    Context context;
    List<MusicEntity> mDatas;

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        public void OnItemClick(View view,int position);
    }
    public MusicAdapter(Context context, List<MusicEntity> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_music,parent,false);
        MusicViewHolder holder = new MusicViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, final int position) {
        MusicEntity musicBean = mDatas.get(position);
        if(Integer.parseInt(musicBean.getId()) < 10){
            holder.idTv.setText("0"+musicBean.getId());
        }else{
            holder.idTv.setText(musicBean.getId());
        }
        holder.songTv.setText(musicBean.getSong());
        holder.singerTv.setText(musicBean.getSinger());
        holder.albumTv.setText(musicBean.getAlbum());
        holder.timeTv.setText(musicBean.getDuration());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MusicViewHolder extends RecyclerView.ViewHolder{
        TextView idTv,songTv,singerTv,albumTv,timeTv;
        public MusicViewHolder(View itemView) {
            super(itemView);
            idTv = itemView.findViewById(R.id.item_music_num);
            songTv = itemView.findViewById(R.id.item_music_song);
            singerTv = itemView.findViewById(R.id.item_music_singer);
            albumTv = itemView.findViewById(R.id.item_music_album);
            timeTv = itemView.findViewById(R.id.item_music_durtion);
        }
    }
}
