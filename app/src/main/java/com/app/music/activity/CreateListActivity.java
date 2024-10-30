package com.app.music.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.app.music.R;
import com.app.music.adapter.MyListAdapter;

public class CreateListActivity extends AppCompatActivity {
    private ListView listView;
    private MyListAdapter adapter;
    private static String[] data; // 静态变量，用于存储所有歌单名称的数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        listView = findViewById(R.id.listView);

        if (data == null) {
            data = new String[]{}; // 初始为空数组
        }

        // 从Intent中获取传递的数据
        String playlistName = getIntent().getStringExtra("PLAYLIST_NAME");
        if (playlistName != null && !playlistName.isEmpty()) {
            // 将新数据添加到数组中
            String[] newData = new String[data.length + 1];
            System.arraycopy(data, 0, newData, 0, data.length);
            newData[data.length] = playlistName;
            data = newData; // 更新全局数组
            Log.d("12138", "Updated data: " + String.join(", ", data));
        }

        // 创建适配器并设置给ListView
        adapter = new MyListAdapter(data);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存数据到Bundle，以便在活动重建时恢复
        outState.putStringArray("PLAYLIST_DATA", data);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // 从Bundle中恢复数据
        data = savedInstanceState.getStringArray("PLAYLIST_DATA");
    }
}