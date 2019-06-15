/*
 * Copyright (C) 2015 - 2019 ExoMedia Contributors
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

package com.devbrackets.android.exomedia.util

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*

/**
 * A Utility class to help determine characteristics about the device
 */
class DeviceUtil {
    companion object {
        protected val NON_COMPATIBLE_DEVICES: MutableList<Device>

        init {
            NON_COMPATIBLE_DEVICES = LinkedList()
            NON_COMPATIBLE_DEVICES.add(Device("Amazon"))
        }
    }

    fun supportsExoPlayer(context: Context): Boolean {
        return if (!isNotCompatible(NON_COMPATIBLE_DEVICES)) {
            true
        } else Build.MANUFACTURER.equals("Amazon", ignoreCase = true) && (isDeviceTV(context) || Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
    }

    /**
     * Determines if the current device is not compatible based on the list of devices
     * that don't correctly support the ExoPlayer
     *
     * @param nonCompatibleDevices The list of devices that aren't compatible
     * @return True if the current device is not compatible
     */
    fun isNotCompatible(nonCompatibleDevices: List<Device>): Boolean {
        for (device in nonCompatibleDevices) {
            if (Build.MANUFACTURER.equals(device.manufacturer, ignoreCase = true)) {
                return device.model?.let {
                    Build.DEVICE.equals(it, ignoreCase = true)
                } ?: true
            }
        }

        return false
    }

    /**
     * Determines if the current device is a TV.
     *
     * @param context The context to use for determining the device information
     * @return True if the current device is a TV
     */
    fun isDeviceTV(context: Context): Boolean {
        //Since Android TV is only API 21+ that is the only time we will compare configurations
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val uiManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            return uiManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION
        }

        return false
    }

    data class Device(
            val manufacturer: String,
            val model: String? = null
    )
}