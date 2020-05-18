package com.example.bluetoothmeonjava.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;

import com.example.bluetoothmeonjava.R;

public class Command implements ISavable {

    public Command(){
        command = "0";
        label = "Undefined";
        uniqueId = label;
    }

    public Command(String label, String command, String uniqueId){
        this.label = label;
        this.command = command;
        this.uniqueId = uniqueId;
    }

    private String command;
    private String label;
    private String uniqueId;

    public void setLabel(String value){this.label = value;}
    public String getLabel(){return this.label;}
    public void setCommand(String value){this.command = value;}
    public String getCommand(){return this.command;}

    @Override
    public void SaveConfig(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(uniqueId + "_label", getLabel());
        editor.putString(uniqueId + "_command", getCommand());
        editor.apply();
    }

    @Override
    public void LoadConfig(Context context, String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        setLabel(sharedPreferences.getString(uniqueId + "_label", getLabel()));
        setCommand(sharedPreferences.getString(uniqueId + "_command", getCommand()));
    }
}
