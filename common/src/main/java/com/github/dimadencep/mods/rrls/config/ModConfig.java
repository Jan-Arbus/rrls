/*
 * Copyright 2023 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://github.com/dima-dencep/rrls/blob/HEAD/LICENSE
 */

package com.github.dimadencep.mods.rrls.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.Calendar;

@Config(name = "rrls")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public HideType hideType = HideType.RELOADING;

    @ConfigEntry.Gui.Tooltip
    public boolean rgbProgress = false;

    @ConfigEntry.Gui.Tooltip
    public boolean forceClose = false;

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public ShowIn showIn = ShowIn.ALL;

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public Type type = Type.PROGRESS;

    public String reloadText = "Edit in config!";

    public boolean resetResources = false;

    @ConfigEntry.Gui.Tooltip
    public boolean reInitScreen = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean earlyPackStatusSend = true;

    @ConfigEntry.Gui.Tooltip
    public float animationSpeed = 1000.0F;

    @ConfigEntry.Gui.Excluded
    public AprilFool aprilFool = AprilFool.ON_APRIL;

    public enum ShowIn {
        DISABLED,
        ONLY_GAME,
        ONLY_GUI,
        ALL;

        public boolean canShow(boolean isGame) {
            if (this == DISABLED) return false;

            if (this == ALL) return true;

            if (isGame) {
                return this == ONLY_GAME;
            } else {
                return this == ONLY_GUI;
            }
        }
    }

    public enum HideType {
        ALL,
        LOADING,
        RELOADING,
        NONE;

        public boolean canHide(boolean reloading) {
            if (this == NONE) return false;

            if (this == ALL) return true;

            if (reloading) {
                return this == RELOADING;
            } else {
                return this == LOADING;
            }
        }
    }

    public enum Type {
        PROGRESS,
        TEXT
    }

    public enum AprilFool {
        ON_APRIL,
        ALWAYS,
        DISABLED;

        private static Calendar calendar;

        public boolean canUes() {
            if (this == ALWAYS)
                return true;

            try {
                if (this == ON_APRIL) {
                    if (calendar == null) {
                        calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                    }

                    return calendar.get(Calendar.MONTH) == Calendar.APRIL && calendar.get(Calendar.DAY_OF_MONTH) == 1;
                }
            } catch (Throwable ignored) {
            }

            return false;
        }
    }
}
