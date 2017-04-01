package com.vstecs.android.funframework.net.xokhttp.params.type;

import android.text.TextUtils;

public class StringTypeParam extends CommonTypeParam<String>{

	public StringTypeParam(String key, String value, String defaultValue) {
		super(key, value, defaultValue);
	}
	
	@Override
	protected String getValue(String v, String defV) {
		if (TextUtils.isEmpty(v)) {
			v = defV;
			if(TextUtils.isEmpty(v))
				return null;
		}
		return v;
	}

}
