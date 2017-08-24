package com.sjl.wallpaper.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 权限请求工具类
 *
 * @author SJL
 * @date 2017/3/22
 */
@TargetApi(Build.VERSION_CODES.M)
public class PermisstionUtil {
    private static final String TAG = "PermisstionUtil";
    //日历
    public static String[] CALENDAR = {Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR};
    //相机
    public static String[] CAMERA = {Manifest.permission.CAMERA};
    //联系人
    public static String[] CONTACTS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.GET_ACCOUNTS};
    //位置
    public static String[] LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    //麦克风
    public static String[] MICROPHONE = {Manifest.permission.RECORD_AUDIO};
    //手机
    public static String[] PHONE = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.ADD_VOICEMAIL, Manifest.permission.USE_SIP, Manifest.permission.PROCESS_OUTGOING_CALLS};
    //传感器
    public static String[] SENSORS = {Manifest.permission.BODY_SENSORS};
    //短信
    public static String[] SMS = {Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_WAP_PUSH, Manifest.permission.RECEIVE_MMS};
    //文件读写
    public static String[] STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static HashMap<String, Object> map = new HashMap<String, Object>();

    /**
     * 版本检测
     *
     * @return
     */
    private static boolean checkSDK() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 权限请求
     *
     * @param context
     * @param permissions        需要请求的权限
     * @param requestCode
     * @param explainMsg         权限解释
     * @param onPermissionResult
     */
    public static void requestPermissions(@NonNull Context context, @NonNull String[] permissions, int requestCode, String explainMsg, OnPermissionResult onPermissionResult) {
        onPermissionResult = initOnPermissionResult(onPermissionResult, permissions, requestCode, explainMsg);
        if (permissions.length == 0) {
            invokeOnRequestPermissionsResult(context, onPermissionResult);
        } else if (context instanceof Activity || (Object) context instanceof Fragment) {
            if (checkSDK()) {
                onPermissionResult.deniedPermissions = getDeniedPermissions(context, permissions);
                if (onPermissionResult.deniedPermissions.length > 0) {//存在被拒绝的权限
                    onPermissionResult.rationalePermissions = getRationalePermissions(context, onPermissionResult.deniedPermissions);
                    if (onPermissionResult.rationalePermissions.length > 0) {//向用户解释请求权限的理由
                        shouldShowRequestPermissionRationale(context, onPermissionResult);
                    } else {
                        invokeRequestPermissions(context, onPermissionResult);
                    }
                } else {//所有权限允许
                    onPermissionResult.grantResults = new int[permissions.length];
                    for (int i = 0; i < onPermissionResult.grantResults.length; i++) {
                        onPermissionResult.grantResults[i] = PackageManager.PERMISSION_GRANTED;
                    }
                    invokeOnRequestPermissionsResult(context, onPermissionResult);
                }
            } else {
                onPermissionResult.grantResults = getPermissionsResults(context, permissions);
                invokeOnRequestPermissionsResult(context, onPermissionResult);
            }
        }
    }

    /**
     * 获取被拒绝的权限
     *
     * @param context
     * @param permissions
     * @return
     */
    private static String[] getDeniedPermissions(Context context, String[] permissions) {
        List<String> list = new ArrayList<String>();
        for (String permission : permissions) {
            if (checkPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                list.add(permission);
            }
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * 获取权限请求结果
     *
     * @param context
     * @param permissions
     * @return
     */
    private static int[] getPermissionsResults(Context context, String[] permissions) {
        int[] results = new int[permissions.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = checkPermission(context, permissions[i]);
        }
        return results;
    }

    private static String[] getRationalePermissions(Context context, String[] deniedPermissions) {
        List<String> list = new ArrayList<String>();
        for (String permission : deniedPermissions) {
            if (context instanceof Activity) {
                if (((Activity) context).shouldShowRequestPermissionRationale(permission)) {
                    list.add(permission);
                }
            } else if ((Object) context instanceof Fragment) {
                if (((Fragment) (Object) context).shouldShowRequestPermissionRationale(permission)) {
                    list.add(permission);
                }
            } else {
                throw new IllegalArgumentException("context 只能是Activity或Fragment");
            }
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * 调用权限请求方法
     *
     * @param context
     * @param onPermissionResult
     */
    private static void invokeRequestPermissions(Context context, OnPermissionResult onPermissionResult) {
        if (context instanceof Activity)
            ((Activity) context).requestPermissions(onPermissionResult.deniedPermissions, onPermissionResult.requestCode);
        else if ((Object) context instanceof Fragment)
            ((Fragment) (Object) context).requestPermissions(onPermissionResult.deniedPermissions, onPermissionResult.requestCode);
    }

    /**
     * 调用权限请求结果回调
     *
     * @param context
     * @param onPermissionResult
     */
    private static void invokeOnRequestPermissionsResult(Context context, OnPermissionResult onPermissionResult) {
        if (context instanceof Activity) {
            if (checkSDK()) {
                ((Activity) context).onRequestPermissionsResult(onPermissionResult.requestCode, onPermissionResult.permissions, onPermissionResult.grantResults);
            } else if (context instanceof ActivityCompat.OnRequestPermissionsResultCallback) {
                ((ActivityCompat.OnRequestPermissionsResultCallback) context).onRequestPermissionsResult(onPermissionResult.requestCode, onPermissionResult.permissions, onPermissionResult.grantResults);
            } else {
                onRequestPermissionsResult(onPermissionResult.requestCode, onPermissionResult.permissions, onPermissionResult.grantResults);
            }
        } else if ((Object) context instanceof Fragment) {
            ((Fragment) (Object) context).onRequestPermissionsResult(onPermissionResult.requestCode, onPermissionResult.permissions, onPermissionResult.grantResults);
        }
    }

    /**
     * 显示权限解释
     *
     * @param context
     * @param onPermissionResult
     */
    private static void shouldShowRequestPermissionRationale(final Context context, final OnPermissionResult onPermissionResult) {
        new AlertDialog.Builder(context instanceof Activity ? context : ((Fragment) (Object) context).getActivity())
                .setTitle("提示")
                .setMessage(onPermissionResult.explainMsg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        invokeRequestPermissions(context, onPermissionResult);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        onPermissionResult.grantResults = getPermissionsResults(context, onPermissionResult.permissions);
                        invokeOnRequestPermissionsResult(context, onPermissionResult);
                    }
                }).show();
    }

    /**
     * 检查权限
     *
     * @param context
     * @param permission
     * @return
     */
    private static int checkPermission(Context context, String permission) {
        return context.checkPermission(permission, Process.myPid(), Process.myUid());
    }

    /**
     * 权限请求结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        synchronized (TAG) {
            OnPermissionResult onPermissionResult = (OnPermissionResult) map.get(String.valueOf(requestCode));
            if (onPermissionResult != null) {
                List<String> deniedPermissions = new ArrayList<String>();
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        deniedPermissions.add(permissions[i]);
                    }
                }
                if (deniedPermissions.size() > 0) {
                    onPermissionResult.denied(requestCode);
                } else {
                    onPermissionResult.granted(requestCode);
                }
                map.remove(String.valueOf(requestCode));
            }
        }
    }

    /**
     * 初始化权限请求回调
     *
     * @param onPermissionResult
     * @param permissions
     * @param requestCode
     * @param explainMsg         @return
     */
    private static OnPermissionResult initOnPermissionResult(OnPermissionResult onPermissionResult, String[] permissions, int requestCode, String explainMsg) {
        synchronized (TAG) {
            if (onPermissionResult == null) {
                onPermissionResult = new OnPermissionResult() {
                    @Override
                    public void granted(int requestCode) {

                    }

                    @Override
                    public void denied(int requestCode) {

                    }
                };
            }
            onPermissionResult.permissions = permissions;
            onPermissionResult.requestCode = requestCode;
            onPermissionResult.explainMsg = explainMsg;
            onPermissionResult.grantResults = new int[0];
            map.put(String.valueOf(requestCode), onPermissionResult);
            return onPermissionResult;
        }
    }

    public abstract static class OnPermissionResult {
        int requestCode;
        String explainMsg;
        String[] permissions;
        String[] deniedPermissions;
        String[] rationalePermissions;
        int[] grantResults;

        //权限允许
        public abstract void granted(int requestCode);

        //权限拒绝
        public abstract void denied(int requestCode);
    }
}
