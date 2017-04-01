package com.vst.vstsupport.control.main.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vst.vstsupport.R;
import com.vst.vstsupport.VstApplication;
import com.vst.vstsupport.control.arrears.fragment.ArrearsFragment;
import com.vst.vstsupport.control.base.BaseAct;
import com.vst.vstsupport.control.base.BaseFra;
import com.vst.vstsupport.control.base.BaseFra.FragmentKeyDown;
import com.vst.vstsupport.control.inventory.fragment.InventoryFragment;
import com.vst.vstsupport.control.message.fragment.MessageFragment;
import com.vst.vstsupport.control.setting.fragment.SettingFragment;
import com.vst.vstsupport.control.setting.version.VersionManager;
import com.vst.vstsupport.mode.event.MainPageEvent;
import com.vstecs.android.funframework.eventbus.EventCenter;
import com.vstecs.android.funframework.netstatus.NetUtils;
import com.vstecs.android.uiframework.view.jazzyviewpage.JazzyViewPager;

import java.util.HashSet;
import java.util.Set;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseAct implements ViewPager.OnPageChangeListener {

    private JazzyViewPager pager;
    private LinearLayout page_indicator;
    private Set<Integer> initFrags;
    private MainPagerAdapter pagerAdapter;
    private boolean isHideInventory;
    private TextView tv_num_am;
    /**
     * 是否退出应用
     */
    private boolean isExit;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Object instantiateItem = pagerAdapter.instantiateItem(pager, pager.getCurrentItem());
        if (instantiateItem instanceof FragmentKeyDown) {
            BaseFra.FragmentKeyDown iFraKeyDown = (FragmentKeyDown) instantiateItem;
            if (iFraKeyDown.onFraKeyDown(keyCode, event))
                return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                showToastMsg("再按一次退出应用");
                isExit = true;
                mHandler.sendEmptyMessageDelayed(0, 1000);
                return true;
            } else {
                // sendBroadcast(new Intent(Constant.ACTION_EXIT_SYSTEM));
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    ;

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {
        if (eventCenter.getEventCode() == 1011) {
            MainPageEvent event = (MainPageEvent) eventCenter.getData();
            pager.setCurrentItem(event.getPageIndex(), false);
        } else if (eventCenter.getEventCode() == 1012) {
            Integer num = (Integer) eventCenter.getData();
            if (num > 0) {
                tv_num_am.setVisibility(View.VISIBLE);
                tv_num_am.setText(String.valueOf(num));
            }else {
                tv_num_am.setVisibility(View.GONE);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected View getContainerTargetView() {
        return null;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }


    @Override
    public void setRootView() {
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        VersionManager versionManager = new VersionManager(this);
        versionManager.checkVersion(true, false,new Handler());
        tv_num_am = (TextView) findViewById(R.id.tv_num_am);
        isHideInventory = VstApplication.getInstance().getUserBean().getIsHideInventory();

        pager = (JazzyViewPager) findViewById(R.id.pager);
        pager.setFadeEnabled(false);
        pager.setPagingEnabled(false);
        pager.setOutlineEnabled(false);
        pager.setTransitionEffect(JazzyViewPager.TransitionEffect.Standard);

        pager.setOffscreenPageLimit(4);

        pager.setOnPageChangeListener(this);
        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        // pagerAdapter = new MainPagerAdapter();
        pager.setAdapter(pagerAdapter);

        page_indicator = (LinearLayout) findViewById(R.id.page_indicator);
        if (isHideInventory) {
            page_indicator.removeViewAt(2);
        }
        int tabCount = page_indicator.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            page_indicator.getChildAt(i).setTag(i);
            page_indicator.getChildAt(i).setOnClickListener(this);
        }

        initFrags = new HashSet<Integer>();
        initFrags.add(0);
        onPageSelected(0);
    }

    /**
     * viewpager监听
     **************************/
    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int item) {
        final int tabCount = page_indicator.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final View child = page_indicator.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);
        }

//        if (!initFrags.contains(item)) {
//            initFrags.add(item);
//            Object frag = pagerAdapter.instantiateItem(pager, item);
//            if (frag instanceof BaseFra.OnInitShowListener) {
//                ((BaseFra.OnInitShowListener) frag).onInitShow();
//            }
//        } else {
//            if (item == 4) {
//                Object frag = pagerAdapter.instantiateItem(pager, item);
//                if (frag instanceof BaseFra.OnInitShowListener) {
//                    ((BaseFra.OnInitShowListener) frag).onInitShow();
//                }
//            }
//        }

    }

    /**
     * 只针对tab
     */
    @Override
    public void widgetClick(View v) {
        int index = (Integer) v.getTag();
        if (index != pager.getCurrentItem()) {
            pager.setCurrentItem(index, false);
        }
        super.widgetClick(v);
    }


    /**
     * viewpager监听 end
     **************************/

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private class MainPagerAdapter extends FragmentStatePagerAdapter {

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment frag = null;
            switch (position) {
                case 0:
                    frag = new MessageFragment();
                    break;
                case 1:
                    frag = new ArrearsFragment();
                    break;
                case 2:
                    if (isHideInventory) {
                        frag = new SettingFragment();
                    } else {
                        frag = new InventoryFragment();
                    }
                    break;
                case 3:
                    frag = new SettingFragment();
                    break;
            }
            return frag;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object object = super.instantiateItem(container, position);
            pager.setObjectForPosition(object, position);
            return object;
        }

        @Override
        public int getCount() {
            return isHideInventory ? 3 : 4;
        }

    }


}
