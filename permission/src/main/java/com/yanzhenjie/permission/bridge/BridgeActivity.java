/*
 * Copyright © Zhenjie Yan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.permission.bridge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;

import com.yanzhenjie.permission.overlay.setting.AlertWindowSettingPage;
import com.yanzhenjie.permission.overlay.setting.OverlaySettingPage;
import com.yanzhenjie.permission.runtime.setting.RuntimeSettingPage;
import com.yanzhenjie.permission.source.ContextSource;

import androidx.annotation.NonNull;

/**
 * <p>
 * Request permission.
 * </p>
 * Created by Zhenjie Yan on 2017/4/27.
 */
public final class BridgeActivity extends Activity {

    private static final String KEY_TYPE = "KEY_TYPE";
    private static final String KEY_PERMISSIONS = "KEY_PERMISSIONS";

    /**
     * Request for permissions.
     */
    static void requestAppDetails(Context context) {
        Intent intent = new Intent(context.getPackageName() + ".permission.bridge");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(KEY_TYPE, BridgeRequest.TYPE_APP_DETAILS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Request for permissions.
     */
    static void requestPermission(Context context, String[] permissions) {
        Intent intent = new Intent(context.getPackageName() + ".permission.bridge");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(KEY_TYPE, BridgeRequest.TYPE_PERMISSION);
        intent.putExtra(KEY_PERMISSIONS, permissions);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Request for setting.
     */
    static void permissionSetting(Context context) {
        Intent intent = new Intent(context.getPackageName() + ".permission.bridge");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(KEY_TYPE, BridgeRequest.TYPE_PERMISSION_SETTING);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Request for package install.
     */
    static void requestInstall(Context context) {
        Intent intent = new Intent(context.getPackageName() + ".permission.bridge");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(KEY_TYPE, BridgeRequest.TYPE_INSTALL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Request for overlay.
     */
    static void requestOverlay(Context context) {
        Intent intent = new Intent(context.getPackageName() + ".permission.bridge");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(KEY_TYPE, BridgeRequest.TYPE_OVERLAY);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Request for alert window.
     */
    static void requestAlertWindow(Context context) {
        Intent intent = new Intent(context.getPackageName() + ".permission.bridge");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(KEY_TYPE, BridgeRequest.TYPE_ALERT_WINDOW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Request for notify.
     */
    static void requestNotify(Context context) {
        Intent intent = new Intent(context.getPackageName() + ".permission.bridge");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(KEY_TYPE, BridgeRequest.TYPE_NOTIFY);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Request for notification listener.
     */
    static void requestNotificationListener(Context context) {
        Intent intent = new Intent(context.getPackageName() + ".permission.bridge");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(KEY_TYPE, BridgeRequest.TYPE_NOTIFICATION_LISTENER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int operation = intent.getIntExtra(KEY_TYPE, 0);
        switch (operation) {
            case BridgeRequest.TYPE_APP_DETAILS: {
                Intent appDetailsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                appDetailsIntent.setData(Uri.fromParts("package", getPackageName(), null));
                startActivityForResult(appDetailsIntent, BridgeRequest.TYPE_APP_DETAILS);
                break;
            }
            case BridgeRequest.TYPE_PERMISSION: {
                String[] permissions = intent.getStringArrayExtra(KEY_PERMISSIONS);
                requestPermissions(permissions, BridgeRequest.TYPE_PERMISSION);
                break;
            }
            case BridgeRequest.TYPE_PERMISSION_SETTING: {
                RuntimeSettingPage setting = new RuntimeSettingPage(new ContextSource(this));
                setting.start(BridgeRequest.TYPE_PERMISSION_SETTING);
                break;
            }
            case BridgeRequest.TYPE_INSTALL: {
                Intent manageIntent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                manageIntent.setData(Uri.fromParts("package", getPackageName(), null));
                startActivityForResult(manageIntent, BridgeRequest.TYPE_INSTALL);
                break;
            }
            case BridgeRequest.TYPE_OVERLAY: {
                OverlaySettingPage settingPage = new OverlaySettingPage(new ContextSource(this));
                settingPage.start(BridgeRequest.TYPE_OVERLAY);
                break;
            }
            case BridgeRequest.TYPE_ALERT_WINDOW: {
                AlertWindowSettingPage settingPage = new AlertWindowSettingPage(new ContextSource(this));
                settingPage.start(BridgeRequest.TYPE_ALERT_WINDOW);
                break;
            }
            case BridgeRequest.TYPE_NOTIFY: {
                Intent settingIntent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                settingIntent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivityForResult(settingIntent, BridgeRequest.TYPE_NOTIFY);
                break;
            }
            case BridgeRequest.TYPE_NOTIFICATION_LISTENER: {
                Intent settingIntent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                startActivityForResult(settingIntent, BridgeRequest.TYPE_NOTIFY);
                break;
            }
            default: {
                throw new AssertionError("This should not be the case.");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        Messenger.send(this);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Messenger.send(this);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}