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
package com.github.stkent.amplify.tracking.checks;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.ClockUtil;
import com.github.stkent.amplify.tracking.interfaces.IApplicationInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;

import java.util.concurrent.TimeUnit;

public class WarmUpDaysCheck implements IEventCheck<Long> {

    private final long warmUpPeriodDays;

    public WarmUpDaysCheck(final long warmUpPeriodDays) {
        this.warmUpPeriodDays = warmUpPeriodDays;
    }

    @Override
    public boolean shouldBlockFeedbackPrompt(
            @NonNull final Long cachedEventValue,
            @NonNull final IApplicationInfoProvider applicationInfoProvider) {
        return cachedEventValue == Long.MAX_VALUE ||
                (ClockUtil.getCurrentTimeMillis() - cachedEventValue) <= TimeUnit.DAYS.toMillis(warmUpPeriodDays);
    }

    @NonNull
    @Override
    public String getStatusString(
            @NonNull final Long cachedEventValue,
            @NonNull final IApplicationInfoProvider applicationInfoProvider) {
        final Long daysSinceFirstEvent = TimeUnit.MILLISECONDS.toDays(ClockUtil.getCurrentTimeMillis() - cachedEventValue);
        return "Warm-up period: " + warmUpPeriodDays + " days. Time since first event: " + daysSinceFirstEvent + " days.";
    }

    @NonNull
    @Override
    public String getTrackingKey() {
        return "WARM_UP_DAYS_CHECK";
    }

}
