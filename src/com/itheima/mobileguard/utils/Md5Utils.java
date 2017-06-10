package com.itheima.mobileguard.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.R.integer;

public class Md5Utils {
	/**
	 * md5加密的算法
	 * @param text
	 * @return
	 */
	public static String encode(String text){
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(text.getBytes());
			StringBuilder sb  =new StringBuilder();
			for(byte b : result){
				int number = b&0xff; // 加盐 +1 ;
				String hex = Integer.toHexString(number);
				if(hex.length()==1){
					sb.append("0"+hex);
				}else{
					sb.append(hex);
				}
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			//can't reach
			return "";
		}
	}
    /**
     * 获取到文件的MD5(病毒特征码)
     * @param sourceDir
     * @return
     * 主动防御
     */
	public static String getFileMd5(String sourceDir) {
		
		File file = new File(sourceDir);
		
		try {
			FileInputStream fis = new FileInputStream(file);
			
			byte[] buffer =  new byte[1024];
			
			int len = -1;
			//获取到数字摘要
			MessageDigest messageDigest = MessageDigest.getInstance("md5");
			
			while((len = fis.read(buffer))!= -1){
				
				messageDigest.update(buffer, 0, len);
				
			}
			byte[] result = messageDigest.digest();
			
			StringBuffer sb = new StringBuffer();
			
			for(byte b : result){
				int number = b&0xff; // 加盐 +1 ;
				String hex = Integer.toHexString(number);
				if(hex.length()==1){
					sb.append("0"+hex);
				}else{
					sb.append(hex);
				}
			}
			return sb.toString();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	
}
