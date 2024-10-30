package com.app.music.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.app.music.model.MusicEntity;
import com.app.music.model.MyLikeMusicEntity;
import com.app.music.util.DBUtils;
import com.app.music.util.ItFxqConstants;

/**
 * @description: MusicService
 * @author:zhoulei
 * @createTime
 */
public class MusicService {

    //查询所有的歌曲
    public List<MusicEntity> queryAllMusicList(Context context){
        List<MusicEntity> list = new ArrayList<>();
        SQLiteDatabase db = DBUtils.getDbHelper(context);
        String username = DBUtils.getLoginUsername();
        String sql = "select * from "+ItFxqConstants.SONG_TABLE;
        Cursor cursor = db.rawQuery(sql, new String[]{});
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

    //通过歌名找歌曲
    public MusicEntity queryMusicBySong(Context context,String song){
        MusicEntity musicEntity = new MusicEntity();
        SQLiteDatabase db = DBUtils.getDbHelper(context);
        String username = DBUtils.getLoginUsername();
        String sql = "select * from "+ItFxqConstants.SONG_TABLE+" where song=?";
        Cursor cursor = db.rawQuery(sql, new String[]{song});
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){

                Long id = cursor.getLong(cursor.getColumnIndex("id"));
                String song1 = cursor.getString(cursor.getColumnIndex("song"));
                String singer = cursor.getString(cursor.getColumnIndex("singer"));
                String album = cursor.getString(cursor.getColumnIndex("album"));
                String duration = cursor.getString(cursor.getColumnIndex("duration"));
                String path = cursor.getString(cursor.getColumnIndex("path"));
                String albumArt = cursor.getString(cursor.getColumnIndex("albumArt"));
                musicEntity.setId(id+"");
                musicEntity.setSong(song1);
                musicEntity.setSinger(singer);
                musicEntity.setAlbum(album);
                musicEntity.setDuration(duration);
                musicEntity.setPath(path);
                musicEntity.setAlbumArt(albumArt);

            }
            cursor.close();

        }else{
            cursor.close();

        }
        return musicEntity;
    }


    //保存到数据库
    public boolean saveMusic(Context context, MusicEntity musicEntity){
        SQLiteDatabase dbHelper = DBUtils.getDbHelper(context);
        ContentValues values = new ContentValues();
        values.put("song",musicEntity.getSong());
        values.put("singer",musicEntity.getSinger());
        values.put("album",musicEntity.getAlbum());
        values.put("duration",musicEntity.getDuration());
        values.put("path",musicEntity.getPath());
        values.put("albumArt",musicEntity.getAlbumArt());
        try {
            long result = dbHelper.insert(ItFxqConstants.SONG_TABLE,null,values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            dbHelper.close();
        }
    }

    /**
     * 检查歌曲是否存在
     */
    public static boolean checkMusicIsExits(Context context,String song){
        SQLiteDatabase db = DBUtils.getDbHelper(context);
        String sql = "select * from "+ItFxqConstants.SONG_TABLE+" where song=?";
        Cursor cursor = db.rawQuery(sql, new String[]{song});
        if(cursor.getCount()>0){
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }

    /**
     * 加入我的喜好表
     */
    public boolean saveMyLikeMusic(Context context, MusicEntity myLikeMusicEntity){
        SQLiteDatabase dbHelper = DBUtils.getDbHelper(context);
        ContentValues values = new ContentValues();
        values.put("song",myLikeMusicEntity.getSong());
        values.put("singer",myLikeMusicEntity.getSinger());
        values.put("album",myLikeMusicEntity.getAlbum());
        values.put("duration",myLikeMusicEntity.getDuration());
        values.put("path",myLikeMusicEntity.getPath());
        values.put("albumArt",myLikeMusicEntity.getAlbumArt());
        values.put("username",DBUtils.getLoginUsername());
        try {
            long result = dbHelper.insert(ItFxqConstants.MYLIKE_TABLE,null,values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            dbHelper.close();
        }
    }

    /**
     * 检查歌曲是否在 我的喜好表里面 存在
     */
    public  boolean checkMyMusicIsExits(Context context,String song){
        SQLiteDatabase db = DBUtils.getDbHelper(context);
        String username = DBUtils.getLoginUsername();
        String sql = "select * from "+ItFxqConstants.MYLIKE_TABLE+" where song=? and username=?";
        Cursor cursor = db.rawQuery(sql, new String[]{song,username});
        if(cursor.getCount()>0){
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }

    /**
     * 查询我喜好的歌曲
     */
    public List<MyLikeMusicEntity> queryAllMyLikeMusicList(Context context){
        List<MyLikeMusicEntity> list = new ArrayList<>();
        SQLiteDatabase db = DBUtils.getDbHelper(context);
        String username = DBUtils.getLoginUsername();
        String sql = "select * from "+ItFxqConstants.MYLIKE_TABLE+" where username='"+username+"'";
        Cursor cursor = db.rawQuery(sql, new String[]{});
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                MyLikeMusicEntity myMusic = new MyLikeMusicEntity();
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

    //删除我喜好的音乐
    public boolean delMyLikeMusic(Context context, MyLikeMusicEntity myLikeMusicEntity) {
        SQLiteDatabase db = DBUtils.getDbHelper(context);

        String sql = "delete from "+ItFxqConstants.MYLIKE_TABLE+" where song=? and username=?";
        try{
            db.execSQL(sql,new Object[]{myLikeMusicEntity.getSong(),DBUtils.getLoginUsername()});
            return true;
        }catch (Exception e){
            return false;
        }finally {
            db.close();
        }

    }

    public List<MusicEntity> queryRandomMusicList(Context context) {

        //查询所有音乐
        List<MusicEntity> musicList = queryAllMusicList(context);

        //取出所有的ids (11 26 30 89 90 10 90)
        List<Long> ids = musicList.stream().map(music -> {
            return Long.valueOf(music.getId());
        }).collect(Collectors.toList());
        //目标数组
        List<Long> resultIds = new ArrayList();
        for(int i=0;i<5;i++) {
            Long target = ids.get(new Random().nextInt(ids.size()));
            resultIds.add(target);
            ids.remove(target);
        }

        /**
         * 11 88 99 10 123  11
         * 88 99 10
         */
        List<MusicEntity> musicEntitys = new ArrayList<>();
        musicList.forEach(musicEntity->{
            resultIds.forEach(id->{
                if(Long.valueOf(musicEntity.getId()) == id){
                    musicEntitys.add(musicEntity);
                }
            });
        });
        return musicEntitys;
    }


}
