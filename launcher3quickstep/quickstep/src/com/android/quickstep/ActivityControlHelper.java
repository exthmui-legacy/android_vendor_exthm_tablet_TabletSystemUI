/*
 * Copyright (C) 2018 The Android Open Source Project
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
package com.android.quickstep;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import com.android.launcher3.BaseDraggingActivity;
import com.android.launcher3.DeviceProfile;
import com.android.launcher3.anim.AnimatorPlaybackController;
import com.android.systemui.shared.system.RemoteAnimationTargetCompat;

/**
 * Utility class which abstracts out the logical differences between Launcher and RecentsActivity.
 */
@TargetApi(Build.VERSION_CODES.P)
public interface ActivityControlHelper<T extends BaseDraggingActivity> {

    void onTransitionCancelled(T activity, boolean activityVisible);

    int getSwipeUpDestinationAndLength(DeviceProfile dp, Context context, Rect outRect);

    void onSwipeUpToRecentsComplete(T activity);

    default void onSwipeUpToHomeComplete() { }
    void onAssistantVisibilityChanged(float visibility);

    @NonNull HomeAnimationFactory prepareHomeUI(T activity);

    @Nullable
    T getCreatedActivity();

    default boolean isResumed() {
        BaseDraggingActivity activity = getCreatedActivity();
        return activity != null && activity.hasBeenResumed();
    }

    @UiThread
    @Nullable
    <T extends View> T getVisibleRecentsView();

    @UiThread
    boolean switchToRecentsIfVisible(Runnable onCompleteCallback);

    Rect getOverviewWindowBounds(Rect homeBounds, RemoteAnimationTargetCompat target);

    boolean shouldMinimizeSplitScreen();

    default boolean deferStartingActivity() {
        return true;
    }

    void onLaunchTaskFailed(T activity);

    void onLaunchTaskSuccess(T activity);

    interface ActivityInitListener {
    }

    interface AnimationFactory {

        default void onRemoteAnimationReceived() { }

        void createActivityController(long transitionLength);

        default void adjustActivityControllerInterpolators() { }

        default void onTransitionCancelled() { }

        default void setShelfState() { }

        /**
         */
        default void setRecentsAttachedToAppWindow() { }
    }

    interface HomeAnimationFactory {

        @NonNull RectF getWindowTargetRect();

        @NonNull AnimatorPlaybackController createActivityAnimationToHome();

        default void playAtomicAnimation() {
            // No-op
        }

        static RectF getDefaultWindowTargetRect(DeviceProfile dp) {
            final int halfIconSize = dp.iconSizePx / 2;
            final float targetCenterX = dp.availableWidthPx / 2f;
            final float targetCenterY = dp.availableHeightPx;
            // Fallback to animate to center of screen.
            return new RectF(targetCenterX - halfIconSize, targetCenterY - halfIconSize,
                    targetCenterX + halfIconSize, targetCenterY + halfIconSize);
        }

    }
}