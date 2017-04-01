package com.vst.vstsupport.validation;

import android.content.Context;
import android.widget.Toast;

import com.vstecs.android.funframework.validation.ValidationExecutor;

import java.util.regex.Pattern;


/**
 * @author: 周维勇
 * @类 说 明:
 * @version 1.0
 * @创建时间：2014年12月29日 下午3:37:45
 * 
 */
public class UserNameValidation extends ValidationExecutor {

	@Override
	public boolean doValidate(Context context, String text) {
		String regex = "^[A-Za-z][A-Za-z0-9_.]{5,19}$";
		boolean result = Pattern.compile(regex).matcher(text).find();
		int len = text.length();
		if (len>=6 && len<=20) {
			if (!result) {
				Toast.makeText(context, "用户名格式错误", Toast.LENGTH_SHORT).show();
				return false;
			}
		}else{
			Toast.makeText(context, "请输入用户名", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
}
