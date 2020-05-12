package com.example.bluetoothmeonjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

public class ConfigureButtonActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView input_label;
    private TextView input_command;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_button);

        String labelValue;
        String commandValue;
        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null){
                labelValue = "";
                commandValue = "";
            }else{
                labelValue = extras.getString("Label");
                commandValue = extras.getString("Command");
            }
        }else{
            labelValue = "";
            commandValue = "";
        }

        input_label = findViewById(R.id.edt_Name);
        input_command = findViewById(R.id.edt_Command);
        input_label.setText(labelValue);
        input_command.setText(commandValue);

        toolbar = findViewById(R.id.configure_activity_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.configure_button_activity_actionbar_name);
        toolbar.setSubtitle(labelValue);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onBtnSendClick(View view){
        String value = input_label.getText().toString();
        String value2 = input_command.getText().toString();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("Button Label", value);
        returnIntent.putExtra("Button Command", value2);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
