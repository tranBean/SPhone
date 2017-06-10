package com.itheima.mobileguard.activities;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.utils.SmsUtils;
import com.itheima.mobileguard.utils.SmsUtils.BackUpCallBackSms;
import com.itheima.mobileguard.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
/**
 * ============================================================
 * 
 * 版 权 ： 黑马程序员教育集团 版权所有 (c) 2015
 * 
 * 作 者 : 马伟奇
 * 
 * 版 本 ： 1.0
 * 
 * 创建日期 ： 2015-3-3 上午9:10:18
 * 
 * 描 述 ：
 * 
 *        高级工具
 * 修订历史 ：
 * 
 * ============================================================
 **/
public class AtoolsActivity extends Activity {
	
	private Button button;
	private ProgressDialog pd;
	@ViewInject(R.id.progressBar1)
	private ProgressBar progressBar1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
		ViewUtils.inject(this);
		
	}
	
	
	public void numberAddressQuery(View view){
		Intent intent = new Intent(this,NumberAddressQueryActivity.class);
		startActivity(intent);
	}
	/**
	 * 程序锁
	 * @param view
	 */
	public void appLock(View view){
		Intent intent = new Intent(this,AppLockActivity.class);
		startActivity(intent);
	}
	
	
	/**
	 * 备份短信
	 * @param view
	 */
	public void backUpsms(View view){
		//初始化一个进度条的对话框
		pd = new ProgressDialog(AtoolsActivity.this);
		pd.setTitle("提示");
		pd.setMessage("稍安勿躁。正在备份。你等着吧。。");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.show();
		new Thread(){
			public void run() {
				boolean result = SmsUtils.backUp(AtoolsActivity.this,new BackUpCallBackSms() {
					
					@Override
					public void onBackUpSms(int process) {
						pd.setProgress(process);
						progressBar1.setProgress(process);
						
					}
					
					@Override
					public void befor(int count) {
						pd.setMax(count);
						progressBar1.setMax(count);
					}
				});
				if(result){
					//安全弹吐司的方法
					UIUtils.showToast(AtoolsActivity.this, "备份成功");
				}else{
					UIUtils.showToast(AtoolsActivity.this, "备份失败");
				}
				pd.dismiss();
			};
		}.start();
		
		
	}
	

}
