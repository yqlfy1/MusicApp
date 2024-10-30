package com.app.music.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.app.music.model.MusicEntity;
import com.app.music.service.MusicService;

/**
 * @description: MusicUtils
 * @author:marker
 */
public class MusicUtils {

    public static  ArrayList<MusicEntity> musicList = new ArrayList<>();
    /**
     *  加载歌曲
     * @param context
     * @return
     */
    public static List<MusicEntity> loadMusicData(Context context) {
        if(musicList.size() == 0){
            // 获取ContentResolver对象
            Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, MediaStore.Audio.AudioColumns.IS_MUSIC);
            int id = 0;
            while (cursor.moveToNext()) {
                String song = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String[] songInfo = song.split("-");
                String singer = "未知歌手";
                if (songInfo.length > 1) {
                    singer = song.split("-")[1];
                }
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                id++;
                String sid = String.valueOf(id);
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                System.out.println(path);
//                String path = "http://localhost:8080/music/在无风时 - 毛不易.mp3";
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                String time = sdf.format(new Date(duration));
                // 获取专辑图片主要是通过album_id进行查询
                String album_id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String albumArt = MusicUtils.getAlbumArt(context, album_id);
                // 将一行当中的数据封装到对象当中
                MusicEntity bean = new MusicEntity(sid, song, singer, album, time, path, albumArt);
                musicList.add(bean);
                MusicService musicService = new MusicService();
                if(!musicService.checkMusicIsExits(context,song)){
                    musicService.saveMusic(context, bean);
                 }

            }
        }
        return musicList;

    }

    /**
     * 得到专辑
     * @param context
     * @param album_id
     * @return
     */
    public static String getAlbumArt(Context context, String album_id) {
        String[] projection = new String[]{"album_art"};
        Cursor cur = context.getContentResolver().query(
                Uri.parse(ItFxqConstants.URIAlBUMS + "/" + album_id),
                projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        cur = null;
        return album_art;
    }


}
