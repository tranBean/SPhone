package com.itheima.mobileguard.services;

import java.util.List;

import com.itheima.mobileguard.activities.EnterPwdActivity;
import com.itheima.mobileguard.db.dao.AppLockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.ViewDebug.FlagToString;

public class WatchDogService extends Service {

	private ActivityManager activityManager;
	private AppLockDao dao;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	//ÁÙÊ±Í£Ö¹±£»¤µÄ°üÃû
	private String tempStopProtectPackageName;
	
	private class WatchDogReceiver extends BroadcastReceiver{

		

		@Override
		public void onReceive(Context context, Intent intent) {
			
			if(intent.getAction().equals("com.itheima.mobileguard.stopprotect")){
				//»ñÈ¡µ½Í£Ö¹±£»¤µÄ¶ÔÏó
				
				tempStopProtectPackageName = intent.getStringExtra("packageName");
			}else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
				tempStopProtectPackageName = null;
				// ÈÃ¹·ÐÝÏ¢
				falg = false;
			}else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
				//ÈÃ¹·¼ÌÐø¸É»î
				if(falg == false){
					startWatDog();
				}
			}
			
			
		}
		
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		
		dao = new AppLockDao(this);
		
		appLockInfos = dao.findAll();
		
		//×¢²á¹ã²¥½ÓÊÜÕß
		
		
		receiver = new WatchDogReceiver();
		
		IntentFilter filter = new IntentFilter();
		//Í£Ö¹±£»¤
		filter.addAction("com.itheima.mobileguard.stopprotect");
		
		//×¢²áÒ»¸öËøÆÁµÄ¹ã²¥
		/**
		 * µ±ÆÁÄ»Ëø×¡µÄÊ±ºò¡£¹·¾ÍÐÝÏ¢
		 * ÆÁÄ»½âËøµÄÊ±ºò¡£ÈÃ¹·»î¹ýÀ´
		 */
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		
		filter.addAction(Intent.ACTION_SCREEN_ON);
		
		
		registerReceiver(receiver, filter);
		
		
		
		//»ñÈ¡µ½½ø³Ì¹ÜÀíÆ÷
		
		activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		
		
		
		//1 Ê×ÏÈÐèÒª»ñÈ¡µ½µ±Ç°µÄÈÎÎñÕ»
		
		//2È¡ÈÎÎñÕ»×îÉÏÃæµÄÈÎÎñ
		
		startWatDog();
	}
	//±ê¼Çµ±Ç°µÄ¿´ÃÈ¹·ÊÇ·ñÍ£ÏÂÀ´
	private boolean falg = false;
	private List<String> appLockInfos;
	private WatchDogReceiver receiver;
	
	private void startWatDog() {
		
		new Thread(){
			public void run() {
				falg = true;
				while (falg) {
					//ÓÉÓÚÕâ¸ö¹·Ò»Ö±ÔÚºóÌ¨ÔËÐÐ¡£ÎªÁË±ÜÃâ³ÌÐò×èÈû¡£
					//»ñÈ¡µ½µ±Ç°ÕýÔÚÔËÐÐµÄÈÎÎñÕ»
					List<RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
					//»ñÈ¡µ½×îÉÏÃæµÄ½ø³Ì
					RunningTaskInfo taskInfo = tasks.get(0);
					//»ñÈ¡µ½×î¶¥¶ËÓ¦ÓÃ³ÌÐòµÄ°üÃû
					String packageName = taskInfo.topActivity.getPackageName();
					
					System.out.println(packageName);
					//ÈÃ¹·ÐÝÏ¢Ò»»á
					SystemClock.sleep(30);
					//Ö±½Ó´ÓÊý¾Ý¿âÀïÃæ²éÕÒµ±Ç°µÄÊý¾Ý
					//Õâ¸ö¿ÉÒÔÓÅ»¯¡£¸Ä³É´ÓÄÚ´æµ±ÖÐÑ°ÕÒ
					if(appLockInfos.contains(packageName)){
//					if(dao.find(packageName)){
//						System.out.println("ÔÚ³ÌÐòËøÊý¾Ý¿âÀïÃæ");
						
						//ËµÃ÷ÐèÒªÁÙÊ±È¡Ïû±£»¤
						//ÊÇÒòÎªÓÃ»§ÊäÈëÁËÕýÈ·µÄÃÜÂë
						if(packageName.equals(tempStopProtectPackageName)){
							
						}else{
							Intent intent = new Intent(WatchDogService.this,EnterPwdActivity.class);
							/**
							 * ÐèÒª×¢Òâ£ºÈç¹ûÊÇÔÚ·þÎñÀïÃæÍùactivity½çÃæÌøµÄ»°¡£ÐèÒªÉèÖÃflag
							 */
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							//Í£Ö¹±£»¤µÄ¶ÔÏó
							intent.putExtra("packageName", packageName);
							
							startActivity(intent);
						}
						
						
						
					}
					
					
					
				}
				
				
			};
		}.start();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		falg = false;
		unregisterReceiver(receiver);
		receiver = null;
	}

}
