package com.example.bluetoothmeonjava.model;

public class Command {
    public Command(){
        command = "0";
        label = "Undefined";
    }

    public Command(String label, String command){
        this.label = label;
        this.command = command;
    }

    private String command;
    private String label;

    public void setLabel(String value){this.label = value;}
    public String getLabel(){return this.label;}
    public void setCommand(String value){this.command = value;}
    public String getCommand(){return this.command;}

}
