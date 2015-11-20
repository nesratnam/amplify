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

import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by bobbake4 on 11/20/15.
 */
public class TrackedEventTest {

    @Test
    public void testGetTrackingKey() throws Exception {

        String trackedEventPrefixKey = "AMPLIFY_";
        String iEventKey = "MOCK_EVENT";
        String iEventCheckKey = "MOCK_EVENT_CHECK";

        IEvent iEvent = Mockito.mock(IEvent.class);
        Mockito.when(iEvent.getTrackingKey()).thenReturn(iEventKey);

        IEventCheck iEventCheck = Mockito.mock(IEventCheck.class);
        Mockito.when(iEventCheck.getTrackingKey()).thenReturn(iEventCheckKey);

        TrackedEvent trackedEvent = new TrackedEvent(iEvent, iEventCheck);
        Assert.assertEquals(trackedEventPrefixKey + iEventKey + "_" + iEventCheckKey, trackedEvent.getTrackingKey());

    }
}