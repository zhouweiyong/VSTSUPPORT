package com.vst.vstsupport.validation;

import android.content.Context;
import android.widget.Toast;

import com.vstecs.android.funframework.validation.ValidationExecutor;

import java.util.regex.Pattern;


/**
 * @author: 周维勇
 * @类 说 明:
 * @version 1.0
 * @创建时间：2014年12月29日 下午3:38:29
 * 
 */
public class NewPassWordValidation extends ValidationExecutor {

	@Override
	public boolean doValidate(Context context, String text) {
		String regex = "^[a-zA-Z0-9]{6,20}$";
//		String regex = "^[^\\s\\u4e00-\\u9fa5]{8,16}$";
		boolean result = Pattern.compile(regex).matcher(text).find();
		int len = text.length();
		if (len>0) {
//			if (!result) {
//				Toast.makeText(context,"登陆密码为8-16位", Toast.LENGTH_SHORT).show();
//				return false;
//			}
			if (len<8 || len > 16) {
				Toast.makeText(context,"请输入6-20位新密码", Toast.LENGTH_SHORT).show();
				return false;
			}
		}else{
			Toast.makeText(context,"请输入新密码", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}

}
