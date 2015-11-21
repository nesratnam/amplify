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
package com.github.stkent.amplify.tracking.trackers;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.helpers.BaseTest;
import com.github.stkent.amplify.helpers.FakeSettings;
import com.github.stkent.amplify.helpers.StubLogger;
import com.github.stkent.amplify.tracking.ClockUtil;
import com.github.stkent.amplify.tracking.interfaces.IApplicationInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;

import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class LastTimeTrackerTest extends BaseTest {

    private LastTimeTracker lastTimeTracker;

    private FakeSettings<Long> fakeSettings;

    @Mock
    private IApplicationInfoProvider mockApplicationInfoProvider;
    @Mock
    private IEvent mockEvent;
    @Mock
    private IEventCheck<Long> mockEventCheck;

    @Override
    public void localSetUp() {
        fakeSettings = new FakeSettings<>();

        lastTimeTracker = new LastTimeTracker(
                new StubLogger(), fakeSettings,
                mockApplicationInfoProvider);

        lastTimeTracker.trackEvent(mockEvent, mockEventCheck);
    }

    @Test
    public void testThatFirstEventTimeIsRecorded() {
        // Arrange
        final long fakeEventTime = MARCH_18_2014_838PM_UTC;

        // Act
        triggerEventAtTime(mockEvent, fakeEventTime);

        // Assert

        // Check that the correct event time was saved:
        final Long savedEventTime = fakeSettings.getEventValue(mockEvent, mockEventCheck);

        assertEquals("The correct time should have been recorded for this event", Long.valueOf(fakeEventTime), savedEventTime);
    }

    @SuppressLint("Assert")
    @Test
    public void testThatSecondEventTimesIsRecorded() {
        // Arrange Act
        final long fakeEventTimeEarlier = MARCH_18_2014_838PM_UTC;
        final long fakeEventTimeLater
                = fakeEventTimeEarlier + TimeUnit.DAYS.toMillis(1);

        assert fakeEventTimeEarlier < fakeEventTimeLater;

        // Act
        triggerEventAtTime(mockEvent, fakeEventTimeEarlier);
        triggerEventAtTime(mockEvent, fakeEventTimeLater);

        // Assert

        // Check that the earlier event time was saved, and the second event time ignored:
        final Long savedEventTime = fakeSettings.getEventValue(mockEvent, mockEventCheck);

        assertEquals("The correct (latest) time should have been recorded for this event",
                Long.valueOf(fakeEventTimeLater), savedEventTime);
    }

    private void triggerEventAtTime(@NonNull final IEvent event, final long time) {
        ClockUtil.setFakeCurrentTimeMillis(time);
        lastTimeTracker.notifyEventTriggered(event);
    }

}
