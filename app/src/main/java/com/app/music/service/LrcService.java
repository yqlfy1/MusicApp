package com.app.music.service;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.app.music.model.LrcEntity;
import com.app.music.util.ItFxqConstants;

/**
 * description: LrcService 歌词处理类
 */
public class LrcService {
	//List集合存放歌词内容对象
	private List<LrcEntity> lrcList;
	//声明一个歌词内容对象
	private LrcEntity mLrcEntity;

	public LrcService() {
		mLrcEntity = new LrcEntity();
		lrcList = new ArrayList<LrcEntity>();
	}

	public List<LrcEntity> getLrcList() {
		return lrcList;
	}
	
	/**
	 * 读取歌词
	 * @param path

	 */
	public String loadLrc(String path) {
		//定义一个StringBuffer对象，用来存放歌词内容
		StringBuffer sb = new StringBuffer();
		File f = new File(path.replace(".mp3", ".lrc"));
		
		try {
			//创建一个文件输入流对象
			FileInputStream fis = new FileInputStream(f);
			InputStreamReader isr = new InputStreamReader(fis, ItFxqConstants.CHARSET);
			BufferedReader br = new BufferedReader(isr);
			String str = "";
			while((str = br.readLine()) != null) {
				//替换字符
				str = str.replace("[", "");
				str = str.replace("]", "@");
				//分离@字符
				String lrcData[] = str.split("@");
				if(lrcData.length > 1) {
				//String   str2 = new String(lrcData[1].getBytes("ISO-8859-1"),"GB2312");

					System.out.println("歌词是.................."+lrcData[1]);
					mLrcEntity.setLrcStr(lrcData[1]);
					//处理歌词取得歌曲的时间
					int lrcTime = time2Str(lrcData[0]);
					mLrcEntity.setLrcTime(lrcTime);
					//添加进列表数组
					lrcList.add(mLrcEntity);
					//新创建歌词内容对象
					mLrcEntity = new LrcEntity();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			sb.append("没有歌词文件,去下载！...");
		} catch (IOException e) {
			e.printStackTrace();
			sb.append("没有读取到歌词！");
		}catch (Exception e){
			sb.append("没有读取到歌词！");
		}
		return sb.toString();
	}
	/**
	 * 处理 歌词时间 	 * 处理 歌词时间
	 * [00:01.20] 张靓颖
	 * [00:02.33] 您好 你好 您好好
	 * [00:03.25] 歌词制作 测试者
	 * @param timeStr
	 * @return
	 */
	public int time2Str(String timeStr) {
		timeStr = timeStr.replace(":", ".");
		timeStr = timeStr.replace(".", "@");
		//将时间分隔成字符串数组
		String timeData[] = timeStr.split("@");
		//分离出分、秒并转换为整型
		int mi = Integer.parseInt(timeData[0]);
		int ss = Integer.parseInt(timeData[1]);
		int mm = Integer.parseInt(timeData[2]);
		//计算上一行与下一行的时间转换为毫秒数
		int currentTime = (mi * 60 + ss) * 1000 + mm * 10;
		return currentTime;
	}

}