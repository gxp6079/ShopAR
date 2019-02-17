package com.blade.ShopAR;

import com.vuzix.hud.resources.DynamicThemeApplication;

public class ShopAR_Application extends DynamicThemeApplication {

    @Override
    protected int getNormalThemeResId() {
        return R.style.AppTheme;
    }

    @Override
    protected int getLightThemeResId() {
        return R.style.AppTheme;
    }
}