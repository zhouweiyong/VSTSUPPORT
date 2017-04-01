package com.vstecs.android.funframework.net.xokhttp.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * ��ȡ�����ļ�������
 * @version 1.1
 */
public final class FileUtils {

    public static String getMimeType(String fileUrl) throws IOException, MalformedURLException {
        String type = null;
        URL u = new URL(fileUrl);
        URLConnection uc = null;
        uc = u.openConnection();
        type = uc.getContentType();
        return type;
    }
}