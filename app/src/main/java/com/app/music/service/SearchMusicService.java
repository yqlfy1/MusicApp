package com.app.music.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.app.music.model.MusicEntity;
import com.app.music.util.DBUtils;
import com.app.music.util.ItFxqConstants;

/**
 * @description: SearchMusicService
 * @author:marker
 * @copyright:www.itfxq.cn
 * @email:2579692606@qq.com
 * @createTime 2020/12/31 17:09
 */
public class SearchMusicService {

    /**
     * 查询所有的歌曲
     */
    public List<MusicEntity> queryAllMusicListByKeyWord(Context context,String keyWord){
        List<MusicEntity> list = new ArrayList<>();
        SQLiteDatabase db = DBUtils.getDbHelper(context);
        String username = DBUtils.getLoginUsername();
        String sql = "select * from "+ItFxqConstants.SONG_TABLE+" where song like ? or singer like ? ";
        Cursor cursor = db.rawQuery(sql, new String[]{"%"+keyWord+"%","%"+keyWord+"%"});
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                MusicEntity myMusic = new MusicEntity();
                Long id = cursor.getLong(cursor.getColumnIndex("id"));
                String song = cursor.getString(cursor.getColumnIndex("song"));
                String singer = cursor.getString(cursor.getColumnIndex("singer"));
                String album = cursor.getString(cursor.getColumnIndex("album"));
                String duration = cursor.getString(cursor.getColumnIndex("duration"));
                String path = cursor.getString(cursor.getColumnIndex("path"));
                String albumArt = cursor.getString(cursor.getColumnIndex("albumArt"));
                myMusic.setId(id+"");
                myMusic.setSong(song);
                myMusic.setSinger(singer);
                myMusic.setAlbum(album);
                myMusic.setDuration(duration);
                myMusic.setPath(path);
                myMusic.setAlbumArt(albumArt);
                list.add(myMusic);
            }
            cursor.close();

        }else{
            cursor.close();

        }
        return list;
    }



}
