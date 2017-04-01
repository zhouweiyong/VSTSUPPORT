package com.vst.vstsupport.validation;

import android.content.Context;
import android.widget.Toast;

import com.vstecs.android.funframework.validation.ValidationExecutor;

import java.util.regex.Pattern;


/**
 * @author: 周维勇
 * @类 说 明:
 * @version 1.0
 * @创建时间：2014年12月29日 下午2:34:40
 * 
 */
public class PhoneNumberValidation extends ValidationExecutor {

	@Override
	public boolean doValidate(Context context, String text) {
		String regex = "^(13[0-9]|14[57]|15[0-9]|17[012356789]|18[0-9])\\d{8}$";
		boolean result = Pattern.compile(regex).matcher(text).find();
		int len = text.length();
		if (len >0) {
			if (!result) {
				Toast.makeText(context, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
				return false;
			}
		} else {
			Toast.makeText(context, "请输入手机号", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

}
