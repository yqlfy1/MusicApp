package com.app.music;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class SongTools {

    //自动从媒体库中取得歌曲
    public List<Song> findSongs(Context context){
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        List<Song> songs = new ArrayList<Song>();
        while(cursor.moveToNext()){

            @SuppressLint("Range") String name = cursor.getString(
                    cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            @SuppressLint("Range") String artist = cursor.getString(
                    cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            @SuppressLint("Range") String album = cursor.getString(
                    cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            @SuppressLint("Range") Long duration = cursor.getLong(
                    cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            @SuppressLint("Range") int album_id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
//            Bitmap front = getAlbumBitmap(context, album_id);
            @SuppressLint("Range") String file_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

            if (file_name.endsWith(".mp3")){
                Song song = new Song();
                song.setName(name);
                song.setAlbum(album);
                song.setDuration(duration);
                song.setArtist(artist);
//                song.setFront(front);
                song.setPath(path);
                songs.add(song);
            }
        }
        cursor.close();
        return songs;
    }


    //将毫秒转换为分：秒的形式
    public String getCurTime(int duration){
        String time = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;

        time += min + ":";
        if(sec < 10){
            time += "0";
        }
        time += sec;
        return time;
    }
}
