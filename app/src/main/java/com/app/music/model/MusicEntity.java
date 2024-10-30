package com.app.music.model;

public class MusicEntity {
    //歌曲id
    private String id;
    //歌曲名称
    private String song;
    //歌手名称
    private String singer;
    //专辑名称
    private String album;
    //歌曲时长
    private String duration;
    //歌曲路径
    private String path;
    //专辑地址
    private String albumArt;
    private int vip=0;
    public MusicEntity() {
    }

    public MusicEntity(String id, String song, String singer, String album, String duration, String path, String albumArt) {
        this.id = id;
        this.song = song;
        this.singer = singer;
        this.album = album;
        this.duration = duration;
        this.path = path;
        this.albumArt = albumArt;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "MusicEntity{" +
                "id='" + id + '\'' +
                ", song='" + song + '\'' +
                ", singer='" + singer + '\'' +
                ", album='" + album + '\'' +
                ", duration='" + duration + '\'' +
                ", path='" + path + '\'' +
                ", albumArt='" + albumArt + '\'' +
                '}';
    }
}
