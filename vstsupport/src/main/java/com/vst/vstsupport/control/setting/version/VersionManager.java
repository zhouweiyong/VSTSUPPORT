package com.vst.vstsupport.control.setting.version;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.vst.vstsupport.R;
import com.vst.vstsupport.VstApplication;
import com.vst.vstsupport.config.UrlConstants;
import com.vstecs.android.funframework.net.xokhttp.OkHttpManage;
import com.vstecs.android.funframework.net.xokhttp.OkhttpError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetSuccuss;
import com.vstecs.android.funframework.net.xokhttp.https.RequestResult;
import com.vstecs.android.funframework.net.xokhttp.params.AjaxParams;
import com.vstecs.android.funframework.net.xokhttp.request.PostRequest;
import com.vstecs.android.funframework.ui.KJActivityManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * @author samy
 * @description：版本管理类；
 * @date 2014年10月10日 下午12:19:15
 */
public class VersionManager {

    public static final int HANDLER_HAVE_NEW = 20;
    public static final int HANDLER_NO_HAVE_NEW = 21;

    // 获取新版本信息
    private static final int HANDLER_GET_LATEST_VERSION = 11;
    private static final int HANDLER_DOWNLOAD_PROGRESS = 12;
    private static final int HANDLER_DOWNLOAD_FINISH = 13;
    private static final int HANDLER_DOWNLOAD_ERROR = 14;
    private static final int HANDLER_DOWNLOAD_SIZE = 15;
    private static final int HANDLER_DOWNLOAD_STOP = 16;
    /**
     * 本地保存目录
     */
    //private static String localDir = Environment.getExternalStorageDirectory().getPath() + "/hkmallu/update/";
    private static String localDir = getCachPath() + "/vst/update/";
    /**
     * 本地保存文件名
     */
    private static String localFileName = "vstsupport.apk";
    /**
     * 每次进入程序只自动提示一次
     */
    private Handler mHandlerVersion;
    /**
     * 防止多次点击检查更新
     */
    private boolean updating;
    private Context context;
    // private long updateTime;
    private Object lock = new Object();
    /**
     * 是否用户自行更新
     */
    private boolean isSilent = true;
    /**
     * 判断是否是自动检查
     */
    private boolean isZi = true;
    /**
     * 对话框
     */
    private Dialog updateDialog;
    /**
     * 是否取消更新
     */
    private volatile boolean isCancel = false;
    /**
     * 本地当前版本信息
     */
    private PackageInfo currentVerInfo;
    /**
     * 服务器最新版本信息
     */
    private VersionBean latestVerInfo;
    /**
     * 进度条
     */
    private ProgressBar downloadBar;
    /**
     * 进度值
     */
    private TextView progressTv;
    private long fileSize;
    /**
     * @description：直接文件操作更新进度条；
     * @author samy
     * @date 2014年10月27日 上午9:58:02
     */
    private Thread thread;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_GET_LATEST_VERSION:
                    if (updateDialog != null && updateDialog.isShowing()) {
                        updateDialog.dismiss();
                    }
                    // 获取最新版本
                    if (isCancel) {
                        break;
                    }
                    // 比较新版本与当前版本
                    compareVersion();
                    break;
                case HANDLER_DOWNLOAD_SIZE:
                    downloadBar.setMax(100);
                    break;
                case HANDLER_DOWNLOAD_PROGRESS:
                    // 更新进度
                    long downLen = (Long) msg.obj;
                    int progress = (int) (downLen * 100 / fileSize);
                    progressTv.setText(downLen * 100 / fileSize + "%");
                    downloadBar.setProgress(progress);
                    break;
                case HANDLER_DOWNLOAD_FINISH:
                    // 下载完成,转到安装界面
                    installApkByPath(context, new File(localDir, localFileName).getAbsolutePath());
                    updateDialog.dismiss();
                    break;
                case HANDLER_DOWNLOAD_STOP:
                    if (updateDialog != null && updateDialog.isShowing()) {
                        updateDialog.dismiss();
                    }
                    break;
                case HANDLER_DOWNLOAD_ERROR:
                    // 下载发生错误
                    String errMsg = (String) msg.obj;
                    if (errMsg == null || errMsg.length() <= 0) {
                        errMsg = context.getResources().getString(R.string.errmsg);
                    }
                    Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show();
                    if (updateDialog != null && updateDialog.isShowing()) {
                        updateDialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }
    };
    public VersionManager(Context context) {
        super();
        this.context = context;
    }

    private static String getCachPath() {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = VstApplication.getInstance().getExternalCacheDir().getPath();
        } else {
            cachePath = VstApplication.getInstance().getCacheDir().getPath();
        }
        return cachePath;
    }

    /**
     * @description：由应用包名得到应用信息
     */
    public PackageInfo getPkgInfoByName(Context context, String pkgName) {
        PackageInfo pkgInfo = null;
        PackageManager pm = context.getPackageManager();
        try {
            // 0代表是获取版本信息
            pkgInfo = pm.getPackageInfo(pkgName, 0);
        } catch (Exception e) {
        }
        return pkgInfo;
    }

    /**
     * @description： 由路径得到app信息
     */
    public ApplicationInfo getApkInfoByPath(Context context, String absPath) {
        ApplicationInfo appInfo = null;
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES);
        if (pkgInfo != null) {
            appInfo = pkgInfo.applicationInfo;
            /* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
            appInfo.sourceDir = absPath;
            appInfo.publicSourceDir = absPath;
        }
        return appInfo;
    }

    /**
     * APK下载完成后跳转到安装
     *
     * @param context
     * @param absPath
     */
    public void installApkByPath(Context context, String absPath) {
        chmod("777", absPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + absPath), "application/vnd.android.package-archive");
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());// 如果不加上这句的话在apk安装完成之后点击单开会崩溃
    }

    public void chmod(String permission, String path) {
        try {
            String command = "chmod " + permission + " " + path;
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @description：检查版本更新;isSilent 是否自动检查更新
     * @author samy
     * @date 2014年10月9日 下午6:12:18
     */
    public void checkVersion(boolean isSilent, boolean isZi, Handler mHandlerVersion) {
        setSilent(isSilent);
        this.isZi = isZi;
        this.mHandlerVersion = mHandlerVersion;
        isCancel = false;
        if (!NetUtil.isNetworkAvailable(context)) {
            Toast.makeText(context, R.string.error_response, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isSilent() && !isZi) {
            updateDialog = new Dialog(context, R.style.MMTheme);
            RotateAnimation rotate = new RotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setRepeatCount(-1);
            rotate.setDuration(1500);
            updateDialog.setContentView(R.layout.dialog_updating);
            updateDialog.findViewById(R.id.update_loading_iv).startAnimation(rotate);
            if (!updateDialog.isShowing()) {
                updateDialog.show();
            }

        }
        if (!updating) {
            updating = true;
            getLatestVersion();
        }
    }

    /**
     * @description：从服务器获取新版本信息
     * @author samy
     * @date 2014年10月9日 下午6:08:46
     */
    private void getLatestVersion() {
        currentVerInfo = getCurrentVersionInfo();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.putCommonTypeParam("type", "2");
        PostRequest<RequestResult<VersionBean>> request = new PostRequest<RequestResult<VersionBean>>(UrlConstants.GETVERSIONINFO, ajaxParams, new OnNetSuccuss<RequestResult<VersionBean>>() {
            @Override
            public void onSuccess(RequestResult<VersionBean> response) {
                if (response.success) {
                    updating = false;
                    if (updateDialog != null && updateDialog.isShowing()) {
                        updateDialog.dismiss();
                    }
                    if (isCancel) {
                        return;
                    }
                    if (isZi) {
                        Toast.makeText(context, response.msg, Toast.LENGTH_SHORT).show();
                    } else {
                        latestVerInfo = response.rs;
                        compareVersion();
                    }
                } else {
                }
            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError okhttpError) {
                if (!isSilent() && isZi) {
                    Toast.makeText(context, okhttpError.errMsg, Toast.LENGTH_SHORT).show();
                }
                if (updateDialog != null && updateDialog.isShowing()) {
                    updateDialog.dismiss();
                }
                if (isCancel) {
                    return;
                }
            }
        }, new TypeToken<RequestResult<VersionBean>>() {
        }.getType());
        OkHttpManage.getInstance().execute(request);
    }

    /**
     * @description：获取本地当前版本信息
     * @author samy
     * @date 2014年10月9日 下午6:09:08
     */
    public PackageInfo getCurrentVersionInfo() {
        PackageManager pm = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            PackageInfo packInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packInfo;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * @description：比较本地版本和服务器版本
     * @author samy
     * @date 2014年10月9日 下午6:09:17
     */
    private void compareVersion() {
        if (updateDialog != null) {
            updateDialog.dismiss();
        }
        if (latestVerInfo == null || currentVerInfo == null) {// 获取版本信息失败
            mHandlerVersion.sendEmptyMessage(HANDLER_NO_HAVE_NEW);
            if (!isSilent() && isZi) {
                //提示：已经是最新版本
                Toast.makeText(context, "已经是最新版本", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (latestVerInfo.getVersionNo() <= currentVerInfo.versionCode) {// 无需更新
            mHandlerVersion.sendEmptyMessage(HANDLER_NO_HAVE_NEW);
            if (!isSilent()) {
                Toast.makeText(context, "已经是最新版本", Toast.LENGTH_SHORT).show();
            }
            return;
        } else {// 有新版本
            mHandlerVersion.sendEmptyMessage(HANDLER_HAVE_NEW);
            try {
                String negativeText = context.getResources().getString(R.string.negative_text);
                if (latestVerInfo.isForceUpdate == 1) {
                    negativeText = context.getResources().getString(R.string.common_logout);
                }

                String ver = context.getResources().getString(R.string.version, latestVerInfo.versionName);
                String updateSize = context.getString(R.string.update_size, latestVerInfo.fileSize);
                String updateContent = context.getString(R.string.update_content, latestVerInfo.updateInfo);
                String updateTitle = context.getString(R.string.update_title, latestVerInfo.versionName);
                // TODO 替换掉当前的默认对话框
                final Dialog dialog = new Dialog(context, R.style.VerSionDialogTheme);
                View view = View.inflate(context, R.layout.dialog_update_confirm, null);
                dialog.setContentView(view);
                dialog.setCancelable(latestVerInfo.isForceUpdate != 1);
                ((TextView) view.findViewById(R.id.update_content)).setText(latestVerInfo.updateInfo.replace("\\n","\n"));
                view.findViewById(R.id.update_confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        updateVersion();
                    }
                });

                TextView negativeTv = (TextView) view.findViewById(R.id.update_cancle);
                negativeTv.setText(negativeText);
                negativeTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (latestVerInfo.isForceUpdate == 1) {
                            // 退出系统
                            KJActivityManager.create().AppExit(context);
                        } else {
                            mHandlerVersion.sendEmptyMessage(0);
                        }
                    }
                });
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            } catch (Exception e) {
            }
        }
    }

    /**
     * @description：更新版本
     * @author samy
     * @date 2014年10月17日 下午3:03:39
     */
    protected void updateVersion() {
        File apkFile = new File(localDir, localFileName);
        /**
         * 如果之前已经下载过就直接安装
         */
        if (apkFile.exists()) {
            ApplicationInfo appInfo = getApkInfoByPath(context, apkFile.getAbsolutePath());
            if (appInfo != null && appInfo.packageName.equals(latestVerInfo.packageName)) {
                // 如果包名相同
                PackageInfo pkgInfo = getPkgInfoByName(context, appInfo.packageName);
                if (pkgInfo != null && pkgInfo.versionCode == latestVerInfo.getVersionNo()) {
                    // 如果版本号等于最新版本号
                    try {
                        // 直接跳到安装界面
                        installApkByPath(context, apkFile.getCanonicalPath());
                    } catch (Exception e) {
                        Toast.makeText(context, context.getResources().getString(R.string.update_error), Toast.LENGTH_SHORT).show();
                        apkFile.delete();
                    }
                    return;
                }
            }
        }
        File dir = new File(localDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        /**
         * 去下载安装；
         */
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(context.getResources().getString(R.string.upgrade)).setView(addLayout()).setCancelable(false);
            // 判断强制更新
            if (latestVerInfo.isForceUpdate != 1) {
                builder.setNegativeButton(context.getResources().getString(R.string.common_calcle), new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isCancel = true;// 取消下载
                        // isCancel = false;
                    }
                });
            }
            builder.show();
            downloadFile2(apkFile);
        } catch (Exception e) {
        }
    }

    private void downloadFile2(final File saveFile) {
        thread = new Thread(new Runnable() {
            @SuppressWarnings("resource")
            @Override
            public void run() {
                InputStream inStream = null;
                FileOutputStream fos = null;
                try {
                    fileSize = Integer.parseInt(latestVerInfo.getFileSize().replace("byte", ""));
                    if (fileSize > 0) {
                        Log.d("md", "fileSize:" + fileSize);
                        handler.sendEmptyMessage(HANDLER_DOWNLOAD_SIZE);
                    } else {
                        handler.sendEmptyMessage(HANDLER_DOWNLOAD_ERROR);
                        return;
                    }
                    // TODO 测试用 暂时写死
                    String fileName = latestVerInfo.getDownloadUrl().substring(latestVerInfo.getDownloadUrl().lastIndexOf("/") + 1);
                    String downloadUrl = latestVerInfo.getDownloadUrl();
                    URL url = new URL(downloadUrl);
                    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    inStream = urlConn.getInputStream();
                    fos = new FileOutputStream(saveFile);
                    byte[] buffer = new byte[1024];
                    int offset = 0;
                    long sum = 0;
                    while ((offset = inStream.read(buffer, 0, 1024)) != -1) {
                        if (isCancel) {
                            // 暂停退出
                            handler.obtainMessage(HANDLER_DOWNLOAD_STOP).sendToTarget();
                            return;
                        } else {
                            fos.write(buffer, 0, offset);
                            sum += offset;
                            handler.obtainMessage(HANDLER_DOWNLOAD_PROGRESS, sum).sendToTarget();
                        }
                        // handler.obtainMessage(HANDLER_DOWNLOAD_PROGRESS, sum,
                        // 0).sendToTarget();

                    }
//                    if (sum == fileSize) {
                    handler.sendEmptyMessage(HANDLER_DOWNLOAD_FINISH);
//                    } else {
//                        handler.obtainMessage(HANDLER_DOWNLOAD_ERROR, context.getResources().getString(R.string.handler_download_error)).sendToTarget();
//                    }
                } catch (Exception e) {
                    handler.obtainMessage(HANDLER_DOWNLOAD_ERROR, context.getResources().getString(R.string.handler_download_error1)).sendToTarget();
                } finally {
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                        if (inStream != null) {
                            inStream.close();
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
        thread.start();
    }

    public boolean isSilent() {
        synchronized (lock) {
            return isSilent;
        }
    }

    public void setSilent(boolean isSilent) {
        synchronized (lock) {
            this.isSilent = isSilent;
        }
    }

    public RelativeLayout addLayout() {
        RelativeLayout relativeLayout = new RelativeLayout(context);
        TextView textView = new TextView(context);
        textView.setId(R.id.version_tv01);
        textView.setTextSize(15);
        textView.setTextColor(Color.BLACK);
        textView.setText(context.getResources().getString(R.string.schedule));
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParams1.setMargins(10, 10, 10, 10);
        relativeLayout.addView(textView, layoutParams1);

        progressTv = new TextView(context);
        progressTv.setId(R.id.version_tv02);
        progressTv.setTextSize(15);
        progressTv.setTextColor(Color.BLACK);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams2.addRule(RelativeLayout.RIGHT_OF, 1);
        layoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        layoutParams2.setMargins(0, 10, 10, 10);
        relativeLayout.addView(progressTv, layoutParams2);

        downloadBar = new ProgressBar(context);
        downloadBar.setId(R.id.version_tv03);
        downloadBar.setMax(100);
        downloadBar.setProgress(25);
        downloadBar.setMinimumHeight(20);
        ProgressBeanUtil.setFieldValue(downloadBar, "mOnlyIndeterminate", new Boolean(false));
        downloadBar.setIndeterminate(false);
        downloadBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_bar_states));
        // downloadBar.setProgressDrawable(context.getResources().getDrawable(android.R.drawable.progress_horizontal));
        downloadBar.setIndeterminateDrawable(context.getResources().getDrawable(android.R.drawable.progress_indeterminate_horizontal));
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20);

        layoutParams3.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        layoutParams3.addRule(RelativeLayout.BELOW, 1);
        layoutParams3.setMargins(10, 10, 10, 10);
        relativeLayout.addView(downloadBar, layoutParams3);
        return relativeLayout;
    }
}