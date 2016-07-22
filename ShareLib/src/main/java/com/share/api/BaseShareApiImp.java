package com.share.api;

/**
 * share api基类
 * Created by Lynn on 3/28/16.
 */
public abstract class BaseShareApiImp implements IShareApi {
    private IShareCallback callback;

    public IShareCallback getShareCallback() {
        return this.callback;
    }

    public void setShareCallback(IShareCallback callback) {
        this.callback = callback;
    }
}
