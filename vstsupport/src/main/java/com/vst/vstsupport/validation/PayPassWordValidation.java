package com.vst.vstsupport.validation;

import android.content.Context;
import android.widget.Toast;

import com.vstecs.android.funframework.validation.ValidationExecutor;

import java.util.regex.Pattern;


public class PayPassWordValidation extends ValidationExecutor {

	@Override
	public boolean doValidate(Context context, String text) {
		String regex = "^[a-zA-Z0-9]{8,16}$";
//		String regex = "^[^\\s\\u4e00-\\u9fa5]{8,16}$";
		boolean result = Pattern.compile(regex).matcher(text).find();
		int len = text.length();
		if (len>0) {
//			if (!result) {
//				Toast.makeText(context,"登陆密码为8-16位", Toast.LENGTH_SHORT).show();
//				return false;
//			}
			if (len<8 || len > 16) {
				Toast.makeText(context,"请输入8-16位的交易密码", Toast.LENGTH_SHORT).show();
				return false;
			}
		}else{
			Toast.makeText(context,"请输入交易密码", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}

}
