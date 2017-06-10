package com.itheima.mobileguard.activities;

import java.lang.reflect.Method;
import java.util.List;

import com.itheima.mobileguard.R;

import android.app.Activity;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
/**
 * ============================================================
 * 
 * 版 权 ： 黑马程序员教育集团 版权所有 (c) 2015
 * 
 * 作 者 : 马伟奇
 * 
 * 版 本 ： 1.0
 * 
 * 创建日期 ： 2015-3-7 下午5:02:37
 * 
 * 描 述 ：
 * 
 *      缓存清理
 * 修订历史 ：
 * 
 * ============================================================
 **/
public class CleanCacheActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initUI();
	}

	private void initUI() {
		setContentView(R.layout.activity_clean_cache);
		PackageManager packageManager = getPackageManager();
		/**
		 * 接收2个参数
		 * 第一个参数接收一个包名
		 * 第二个参数接收aidl的对象
		 */
//		  * @hide
//		     */
//		    public abstract void getPackageSizeInfo(String packageName,
//		            IPackageStatsObserver observer);
//		packageManager.getPackageSizeInfo();
		
		//安装到手机上面所有的应用程序
		List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
		
		for (PackageInfo packageInfo : installedPackages) {
			getCacheSize(packageInfo);
		}
		
		
	}

	private void getCacheSize(PackageInfo packageInfo) {
		try {
			Class<?> clazz = getClassLoader().loadClass("packageManager");
			//通过反射获取到当前的方法
			Method method = clazz.getDeclaredMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
