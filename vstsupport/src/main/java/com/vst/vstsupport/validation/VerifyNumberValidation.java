package com.vst.vstsupport.validation;

import android.content.Context;
import android.widget.Toast;

import com.vstecs.android.funframework.validation.ValidationExecutor;

import java.util.regex.Pattern;


/**
 * @author: 周维勇
 * @类   说   明:	
 * @version 1.0
 * @创建时间：2014年12月29日 下午3:13:02
 */
public class VerifyNumberValidation extends ValidationExecutor {

	@Override
	public boolean doValidate(Context context, String text) {
		String regex = "^[0-9]{6}$";
		boolean result = Pattern.compile(regex).matcher(text).find();
		int len = text.length();
		if (len>0) {
			if (!result) {
				Toast.makeText(context,"验证码不匹配", Toast.LENGTH_SHORT).show();
				return false;
			}
		}else{
			Toast.makeText(context,"请输入验证码", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}

}
