/**
 * Copyright 2015 Stuart Kent
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.github.stkent.amplify.tracking;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.R;
import com.github.stkent.amplify.tracking.interfaces.IApplicationInfoProvider;
import com.github.stkent.amplify.utils.ApplicationUtils;
import com.github.stkent.amplify.utils.StringUtils;

public class ApplicationInfoProvider implements IApplicationInfoProvider {

    private final Context applicationContext;

    public ApplicationInfoProvider(@NonNull final Context context) {
        this.applicationContext = context.getApplicationContext();
    }

    @Override
    public long getFirstInstalledTimeMs() throws PackageManager.NameNotFoundException {
        return getPackageInto().firstInstallTime;
    }

    @Override
    public long getLastUpdatedTimeMs() throws PackageManager.NameNotFoundException {
        return getPackageInto().lastUpdateTime;
    }

    @NonNull
    @Override
    public String getDeviceName() {
        final String manufacturer = Build.MANUFACTURER;
        final String model = Build.MODEL;

        String deviceName;

        if (model.startsWith(manufacturer)) {
            deviceName = StringUtils.capitalize(model);
        } else {
            deviceName = StringUtils.capitalize(manufacturer) + " " + model;
        }

        return deviceName == null ? "Unknown Device" : deviceName;
    }

    @NonNull
    @Override
    public String getVersionName() throws PackageManager.NameNotFoundException {
        return getPackageInto().versionName;
    }

    @NonNull
    @Override
    public String getVersionDisplayString() throws PackageManager.NameNotFoundException {
        final int applicationVersionCode = getPackageInto().versionCode;

        // todo: prefer String.format here
        return getVersionName() + " (" + applicationVersionCode + ")";
    }

    @NonNull
    @Override
    public String getFeedbackEmailAddress() throws IllegalStateException {
        try {
            return applicationContext.getString(R.string.amp_feedback_email);
        } catch (Resources.NotFoundException e) {
            throw new IllegalArgumentException("R.string.amp_feedback_email"
                    + "resource not found, you must set this in your strings file for the feedback util to function", e);
        }
    }

    private PackageInfo getPackageInto() throws PackageManager.NameNotFoundException {
        return ApplicationUtils.getPackageInfo(applicationContext, 0);
    }

}
