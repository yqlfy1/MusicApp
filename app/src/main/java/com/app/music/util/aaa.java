//package cn.itfxq.music.util;
//
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.os.AsyncTask;
//
//import java.io.BufferedInputStream;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.URL;
//import java.net.URLConnection;
//
//public class aaa {
//
//    public static void getaa(){
//        //从服务器播放音频文件试试这个
//        try {
//
//            MediaPlayer player = new MediaPlayer();
//
//            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//
//            player.setDataSource("http://xty/MRESC/images/test/xy.mp3"
//
//            );
//
//            player.prepare();
//
//            player.start();
//
//        } catch (Exception e) {
//
//// TODO: handle exception
//
//          }
//
//
//
//
//}
////如果你想下载.mp3文件格式服务器,那么试试这个..
//    private class DownloadFile extends AsyncTask {
//        @Override
//        protected Object doInBackground(Object[] objects) {
//
//            int count;
//
//            try {
//
//                URL url = new URL("url of your .mp3 file");
//
//                URLConnection conexion = url.openConnection();
//
//                conexion.connect();
//
//// this will be useful so that you can show a tipical 0-100% progress bar
//
//                int lenghtOfFile = conexion.getContentLength();
//
//// downlod the file
//
//                InputStream input = new BufferedInputStream(url.openStream());
//
//                OutputStream output = new FileOutputStream("/sdcard/somewhere/nameofthefile.mp3");
//
//                byte data[] = new byte[1024];
//
//                long total = 0;
//
//                while ((count = input.read(data)) != -1) {
//
//                    total += count;
//
//// publishing the progress....
//
//                    publishProgress((int)(total*100/lenghtOfFile));
//
//                    output.write(data, 0, count);
//
//                }
//
//                output.flush();
//
//                output.close();
//
//                input.close();
//
//            } catch (Exception e) {}
//
//            return null;
//
//        }
//
//
//}