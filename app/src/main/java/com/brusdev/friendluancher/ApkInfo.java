package com.brusdev.friendluancher;

public class ApkInfo {

    private String mLabel;
    private String mPackageName;


    public String getLabel() {
        return mLabel;
    }

    public String getPackageName() {
        return mPackageName;
    }


    public ApkInfo(String label, String packageName) {
        this.mLabel = label;
        this.mPackageName = packageName;
    }
}
