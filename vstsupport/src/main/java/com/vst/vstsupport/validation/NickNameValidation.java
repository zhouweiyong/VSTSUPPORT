package com.vst.vstsupport.validation;

import android.content.Context;
import android.widget.Toast;

import com.vstecs.android.funframework.validation.ValidationExecutor;

import java.util.regex.Pattern;


/**
 * @author: zwy
 * @类 说 明:
 * @version 1.0
 * @创建时间：2015年5月25日 下午2:33:28
 * 
 */
public class NickNameValidation extends ValidationExecutor {

	@Override
	public boolean doValidate(Context context, String text) {
		String regex = "^[\u4e00-\u9fa5A-Za-z][\u4e00-\u9fa5A-Za-z0-9_]{1,7}$";
		boolean result = Pattern.compile(regex).matcher(text).find();
		int len = text.length();
		if (len >= 1 && len <= 8) {
			if (!result) {
				Toast.makeText(context, "昵称格式错误", Toast.LENGTH_SHORT).show();
				return false;
			}
		} else {
			Toast.makeText(context, "请填写1-8字昵称", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

}
