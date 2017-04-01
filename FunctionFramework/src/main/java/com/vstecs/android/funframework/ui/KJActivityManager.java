package com.vstecs.android.funframework.ui;

import android.app.Activity;
import android.content.Context;

import com.vstecs.android.funframework.ui.activity.IKJActivity;

import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出<br>
 * <b>创建时间</b> 2014-2-28
 * @version 1.1
 */
final public class KJActivityManager {
	private static Stack<IKJActivity> activityStack;
	private Activity mCurrentAct;

	private KJActivityManager() {}

	public static KJActivityManager create() {
		return ManagerHolder.instance;
	}

	/**
	 * 获取当前Activity栈中元素个数
	 */
	public int getCount() {
		return activityStack.size();
	}

	/**
	 * 添加Activity到栈
	 */
	public void addActivity(IKJActivity activity) {
		if (activityStack == null) {
			activityStack = new Stack<IKJActivity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（栈顶Activity）
	 */
	public Activity topActivity() {
		if (activityStack == null) { throw new NullPointerException(
				"Activity stack is Null,your Activity must extend BaseActivity"); }
		if (activityStack.isEmpty()) { return null; }
		IKJActivity activity = activityStack.lastElement();
		return (Activity) activity;
	}

	/**
	 * 获取当前Activity（栈顶Activity） 没有找到则返回null
	 */
	public Activity findActivity(Class<?> cls) {
		IKJActivity activity = null;
		for (IKJActivity aty : activityStack) {
			if (aty.getClass().equals(cls)) {
				activity = aty;
				break;
			}
		}
		return (Activity) activity;
	}

	/**
	 * 结束当前Activity（栈顶Activity）
	 */
	public void finishActivity() {
		IKJActivity activity = activityStack.lastElement();
		finishActivity((Activity) activity);
	}

	/**
	 * 结束指定的Activity(重载)
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定的Activity(重载)
	 */
	public void finishActivity(Class<?> cls) {
		for (IKJActivity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity((Activity) activity);
			}
		}
	}

	/**
	 * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
	 *
	 * @param cls
	 */
	public void finishOthersActivity(Class<?> cls) {
		for (IKJActivity activity : activityStack) {
			if (!(activity.getClass().equals(cls))) {
				finishActivity((Activity) activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				((Activity) activityStack.get(i)).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 应用程序退出
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			Runtime.getRuntime().exit(0);
		} catch (Exception e) {
			Runtime.getRuntime().exit(-1);
		}
	}

	/**
	 * @Description: 弹出到指定Activity 移除栈中此Activity上面的所有activity
	 * @param cls
	 */
	public void Pop2Activity(Class<?> cls) {
		if (null == findActivity(cls)) { return; }
		mCurrentAct = (Activity) activityStack.peek();
		while (null != mCurrentAct && !mCurrentAct.getClass().equals(cls)) {
			// mCurrentAct.finish();
			finishActivity(mCurrentAct);
			mCurrentAct = (Activity) activityStack.peek();
		}
	}

	private static class ManagerHolder {
		private static final KJActivityManager instance = new KJActivityManager();
	}


}