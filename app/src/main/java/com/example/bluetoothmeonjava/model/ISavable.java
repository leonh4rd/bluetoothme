package com.example.bluetoothmeonjava.model;

import android.content.Context;

public interface ISavable {
    public void SaveConfig(Context context, String name);
    public void LoadConfig(Context context, String name);
}
