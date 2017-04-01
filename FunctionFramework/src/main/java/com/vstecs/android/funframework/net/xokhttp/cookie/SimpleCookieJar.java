package com.vstecs.android.funframework.net.xokhttp.cookie;

import com.vstecs.android.funframework.net.okhttp.Cookie;
import com.vstecs.android.funframework.net.okhttp.CookieJar;
import com.vstecs.android.funframework.net.okhttp.HttpUrl;

import java.util.ArrayList;
import java.util.List;


public final class SimpleCookieJar implements CookieJar
{
    private final List<Cookie> allCookies = new ArrayList<>();

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies)
    {
        allCookies.addAll(cookies);
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url)
    {
        List<Cookie> result = new ArrayList<>();
        for (Cookie cookie : allCookies)
        {
            if (cookie.matches(url))
            {
                result.add(cookie);
            }
        }
        return result;
    }
}
