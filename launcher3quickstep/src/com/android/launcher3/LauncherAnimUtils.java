/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.android.launcher3;

import android.util.FloatProperty;
import android.util.Log;
import android.view.View;

public class LauncherAnimUtils {
    private static final String TAG = "LauncherAnimUtils";
    public static final FloatProperty<View> SCALE_PROPERTY =
            new FloatProperty<View>("scale") {
                @Override
                public Float get(View view) {
                    return view.getScaleX();
                }

                @Override
                public void setValue(View view, float scale) {
                    if (Float.isNaN(scale)) {
                        Log.d(TAG, "Ignore scaleX NaN");
                        return;
                    }
                    view.setScaleX(scale);
                    view.setScaleY(scale);
                }
            };

    /** Increase the duration if we prevented the fling, as we are going against a high velocity. */
    public static int blockedFlingDurationFactor(float velocity) {
        return (int) Utilities.boundToRange(Math.abs(velocity) / 2, 2f, 6f);
    }
}