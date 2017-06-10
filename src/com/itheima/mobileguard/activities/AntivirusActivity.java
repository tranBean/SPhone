package com.itheima.mobileguard.activities;

import java.util.List;

import org.w3c.dom.Text;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.db.dao.AntivirusDao;
import com.itheima.mobileguard.domain.AppInfo;
import com.itheima.mobileguard.engine.AppInfoParser;
import com.itheima.mobileguard.utils.Md5Utils;

import android.R.bool;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

public class AntivirusActivity extends Activity {
	// ɨ�迪ʼ
	protected static final int BEGING = 1;
	// ɨ����
	protected static final int SCANING = 2;
	// ɨ�����
	protected static final int FINISH = 3;
	private Message message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initUI();

		initData();
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case BEGING:
				tv_init_virus.setText("��ʼ���˺�����");
				break;

			case SCANING:
				// ����ɨ���У�
				TextView child = new TextView(AntivirusActivity.this);

				ScanInfo scanInfo = (ScanInfo) msg.obj;
				// ���Ϊtrue��ʾ�в���
				if (scanInfo.desc) {
					child.setTextColor(Color.RED);

					child.setText(scanInfo.appName + "�в���");

				} else {

					child.setTextColor(Color.BLACK);
//					// Ϊfalse��ʾû�в���
					child.setText(scanInfo.appName + "ɨ�谲ȫ");

					
				}

				ll_content.addView(child,0);
				//�Զ�����
				scrollView.post(new Runnable() {
					
					@Override
					public void run() {
						//һֱ��������й���
						scrollView.fullScroll(scrollView.FOCUS_DOWN);
						
					}
				});
				
				
				System.out.println(scanInfo.appName + "ɨ�谲ȫ");
				break;
			case FINISH:
				// ��ɨ�������ʱ��ֹͣ����
				iv_scanning.clearAnimation();
				break;
			}

		};
	};
	
	private TextView tv_init_virus;
	private ProgressBar pb;
	private ImageView iv_scanning;
	private LinearLayout ll_content;
	private ScrollView scrollView;

	private void initData() {

		new Thread() {

			public void run() {

				message = Message.obtain();

				message.what = BEGING;

				PackageManager packageManager = getPackageManager();
				// ��ȡ�����а�װ��Ӧ�ó���
				List<PackageInfo> installedPackages = packageManager
						.getInstalledPackages(0);
				// �����ֻ����氲װ�˶��ٸ�Ӧ�ó���
				int size = installedPackages.size();
				// ���ý����������ֵ
				pb.setMax(size);

				int progress = 0;

				for (PackageInfo packageInfo : installedPackages) {

					ScanInfo scanInfo = new ScanInfo();

					// ��ȡ����ǰ�ֻ������app������
					String appName = packageInfo.applicationInfo.loadLabel(
							packageManager).toString();

					scanInfo.appName = appName;

					String packageName = packageInfo.applicationInfo.packageName;

					scanInfo.packageName = packageName;

					// ������Ҫ��ȡ��ÿ��Ӧ�ó����Ŀ¼

					String sourceDir = packageInfo.applicationInfo.sourceDir;
					// ��ȡ���ļ���md5
					String md5 = Md5Utils.getFileMd5(sourceDir);
					// �жϵ�ǰ���ļ��Ƿ��ǲ������ݿ�����
					String desc = AntivirusDao.checkFileVirus(md5);

					System.out.println("-------------------------");
					
					System.out.println(appName);
					
					System.out.println(md5);
					
					
//					03-06 07:37:32.505: I/System.out(23660): ����
//					03-06 07:37:32.505: I/System.out(23660): 51dc6ba54cbfbcff299eb72e79e03668

//					["md5":"51dc6ba54cbfbcff299eb72e79e03668","desc":"�ȳ没���Ͽ�ж��","desc":"�ȳ没���Ͽ�ж��","desc":"�ȳ没���Ͽ�ж��"]
					
					
//					B7DA3864FD19C0B2390C9719E812E649
					// �����ǰ��������Ϣ����null˵��û�в���
					if (desc == null) {
						scanInfo.desc = false;
					} else {
						scanInfo.desc = true;
					}
					progress++;
					
					SystemClock.sleep(100);

					pb.setProgress(progress);

					message = Message.obtain();

					message.what = SCANING;

					message.obj = scanInfo;

					handler.sendMessage(message);

				}

				message = Message.obtain();

				message.what = FINISH;

				handler.sendMessage(message);
			};
		}.start();

	}

	static class ScanInfo {
		boolean desc;
		String appName;
		String packageName;
	}

	private void initUI() {
		setContentView(R.layout.activity_antivirusa);
		iv_scanning = (ImageView) findViewById(R.id.iv_scanning);

		tv_init_virus = (TextView) findViewById(R.id.tv_init_virus);

		pb = (ProgressBar) findViewById(R.id.progressBar1);

		ll_content = (LinearLayout) findViewById(R.id.ll_content);
		
		scrollView = (ScrollView) findViewById(R.id.scrollView);

		/**
		 * ��һ��������ʾ��ʼ�ĽǶ� �ڶ���������ʾ�����ĽǶ� ������������ʾ�����Լ� ��ʼ����ת����
		 */
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		// ���ö�����ʱ��
		rotateAnimation.setDuration(5000);
		// ���ö�������ѭ��
		rotateAnimation.setRepeatCount(Animation.INFINITE);
		// ��ʼ����
		iv_scanning.startAnimation(rotateAnimation);
	}
}