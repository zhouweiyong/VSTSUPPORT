package com.vst.vstsupport.control.message.jpush;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.vst.vstsupport.VstApplication;
import com.vst.vstsupport.control.message.activity.CommonMsgDetailActivity;
import com.vst.vstsupport.control.message.activity.InventoryMsgIMDetailActivity;
import com.vst.vstsupport.control.message.activity.LimitMsgIMDetailActivity;
import com.vst.vstsupport.control.message.activity.WLimitMsgIMDetailActivity;
import com.vst.vstsupport.mode.bean.InventoryBean;
import com.vst.vstsupport.mode.bean.LimitBean;
import com.vst.vstsupport.mode.bean.ReceiveMessageContent;
import com.vst.vstsupport.mode.bean.WLimitBean;
import com.vstecs.android.funframework.eventbus.EventCenter;
import com.vstecs.android.funframework.net.xokhttp.utils.L;
import com.vstecs.android.funframework.utils.AppManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MessageReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MessageReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MessageReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MessageReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            //收到推送的处理
            receiveMsgAction(bundle);

            Log.d(TAG, "[MessageReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MessageReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MessageReceiver] 用户点击打开了通知");

            //打开自定义的Activity
//            Intent i = new Intent(context, MainActivity.class);
//            i.putExtras(bundle);
//            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

//            context.startActivity(i);
            //用户点击通知栏的处理
            messageAtion(context, bundle);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MessageReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MessageReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MessageReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    private void receiveMsgAction(Bundle bundle) {
        try {
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            JSONObject obj = new JSONObject(extra);
            String content = obj.getString("objectId");
            Gson gson = new Gson();
            ReceiveMessageContent re = gson.fromJson(content, ReceiveMessageContent.class);
            EventBus.getDefault().post(new EventCenter<ReceiveMessageContent>(1013, re));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //commentsType	true	String	评论类型（1：应收 2:库存项目类型问询 3:库存流量类型问询）
    private void messageAtion(Context context, Bundle bundle) {
        if (!AppManager.isAppRunning("com.vst.vstsupport", context)) {
            L.i("应用已经关闭");
            ComponentName componentName = new ComponentName("com.vst.vstsupport", "com.vst.vstsupport.control.main.activity.WelcomeAct");
            Intent intent = new Intent();
            intent.setComponent(componentName);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            return;
        }
        if (VstApplication.getInstance().getUserBean() == null) return;
        try {
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            JSONObject obj = new JSONObject(extra);
            String content = obj.getString("objectId");
            Gson gson = new Gson();
            ReceiveMessageContent re = gson.fromJson(content, ReceiveMessageContent.class);
            Intent intent = new Intent();
            switch (re.getCommentsType()) {
                case 1:

                    if (VstApplication.getInstance().getUserBean().getIsvstuser() == 1) {
                        intent.setClass(context, WLimitMsgIMDetailActivity.class);
                        WLimitBean wLimitBean = new WLimitBean();
                        wLimitBean.personId = re.getPersonId();
                        wLimitBean.customerId = re.getCustomerName();
                        wLimitBean.brandId = re.getUnionSecdCode();
                        intent.putExtra("WLimitBean", wLimitBean);
                    } else {
                        intent.setClass(context, LimitMsgIMDetailActivity.class);
                        LimitBean limitBean = new LimitBean();
                        limitBean.customerName = re.getCustomerName();
                        limitBean.secendLevelLine = re.getUnionSecdCode();
                        limitBean.personId = re.getPersonId();
                        intent.putExtra("LimitBean", limitBean);
                    }
                    intent.putExtra("title", re.getSourceAccount());
                    intent.putExtra("flag", "5");
                    break;
                case 2:
                    intent.setClass(context, InventoryMsgIMDetailActivity.class);
                    InventoryBean inventoryBean = new InventoryBean();
                    inventoryBean.projectSerial = re.getProductName();
                    inventoryBean.secendLevelLine = re.getUnionSecdCode();
//                    inventoryBean.projectName = re.getCustomerName();
                    inventoryBean.type = "项目";
                    inventoryBean.businessDepartment = re.getPersonId();
                    intent.putExtra("InventoryBean", inventoryBean);
                    intent.putExtra("title", re.getSourceAccount());
                    break;
                case 3:
                    intent.setClass(context, InventoryMsgIMDetailActivity.class);
                    InventoryBean inventoryBean2 = new InventoryBean();
                    inventoryBean2.projectSerial = re.getProductName();
                    inventoryBean2.secendLevelLine = re.getUnionSecdCode();
//                    inventoryBean2.projectName = re.getCustomerName();
                    inventoryBean2.type = "流量";
                    inventoryBean2.businessDepartment = re.getPersonId();
                    intent.putExtra("InventoryBean", inventoryBean2);
                    intent.putExtra("title", re.getSourceAccount());
                    break;
                case 4://超期跟进
                    intent.setClass(context, CommonMsgDetailActivity.class);
                    intent.putExtra("flag", 1);
                    break;
                case 5://即将到期
                    intent.setClass(context, CommonMsgDetailActivity.class);
                    intent.putExtra("flag", 0);
                    break;
                case 6://总库存
                    intent.setClass(context, CommonMsgDetailActivity.class);
                    intent.putExtra("flag", 2);
                    break;
                case 7://90天库存
                    intent.setClass(context, CommonMsgDetailActivity.class);
                    intent.putExtra("flag", 3);
                    break;
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (null != extraJson && extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			context.sendBroadcast(msgIntent);
//		}
    }
}
