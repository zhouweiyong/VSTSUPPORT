package com.vst.vstsupport.validation;

import android.content.Context;
import android.widget.Toast;

import com.vstecs.android.funframework.validation.ValidationExecutor;

import java.util.regex.Pattern;


/**
 * @version 1.0
 * @author: zwy
 * @类 说 明:
 * @创建时间：2015年5月25日 下午2:33:28
 */
public class EmailValidation extends ValidationExecutor {

    @Override
    public boolean doValidate(Context context, String text) {
        String regex = "^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$";
        boolean result = Pattern.compile(regex).matcher(text).find();
        if (!result) {
            Toast.makeText(context, "请输入正确的邮箱账号", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
