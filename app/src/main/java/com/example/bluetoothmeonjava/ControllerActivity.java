package com.example.bluetoothmeonjava;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluetoothmeonjava.model.Command;

public class ControllerActivity extends AppCompatActivity {

    private static int ACTIVITY_RESULT_FORWARD = 1;
    private static int ACTIVITY_RESULT_REAR = 2;
    private static int ACTIVITY_RESULT_STOP = 3;
    private static int ACTIVITY_RESULT_RIGHT = 4;
    private static int ACTIVITY_RESULT_LEFT = 5;
    private static int ACTIVITY_RESULT_ROTATE_RIGHT = 6;
    private static int ACTIVITY_RESULT_ROTATE_LEFT = 7;
    private static int ACTIVITY_RESULT_INCREASE_SPEED = 8;
    private static int ACTIVITY_RESULT_DECREASE_SPEED = 9;
    private static int ACTIVITY_RESULT_HYPERDRIVE = 13;
    private static int ACTIVITY_RESULT_RESTORE_MINIMUM_VALUE = 14;
    private static int ACTIVITY_RESULT_FIRE_CANNON = 15;
    private static int ACTIVITY_RESULT_ROTATE_CW_CANNON = 16;
    private static int ACTIVITY_RESULT_ROTATE_CCW_CANNON = 17;
    private static int ACTIVITY_RESULT_UP_CANNON = 18;
    private static int ACTIVITY_RESULT_DOWN_CANNON = 19;
    private static int ACTIVITY_RESULT_HELP = 10;
    public static int ENABLE_BLUETOOTH = 11;
    public static int SELECT_PAIRED_DEVICE = 12;

    private Command forward;
    private Command rear;
    private Command left;
    private Command right;
    private Command stop;
    private Command increase_speed;
    private Command decrease_speed;
    private Command rotate_left;
    private Command rotate_right;
    private Command hyperdrive;
    private Command restore_minimum_values;
    private Command fire_cannon;
    private Command rotate_cw_cannon;
    private Command rotate_ccw_cannon;
    private Command up_cannon;
    private Command down_cannon;
    private Command help;

    private Button btn_forward;
    private Button btn_rear;
    private Button btn_stop;
    private Button btn_right;
    private Button btn_left;
    private Button btn_rotate_right;
    private Button btn_rotate_left;
    private Button btn_increase_speed;
    private Button btn_decrease_speed;
    private Button btn_help;
    private Toolbar toolbar;
    private Button btn_hyperdrive;
    private Button btn_restore_minimum_values;
    private Button btn_fire_cannon;
    private Button btn_up_cannon;
    private Button btn_down_cannon;
    private Button btn_rotate_cw_cannon;
    private Button btn_rotate_ccw_cannon;
    private ActionBar actionbar;
    private TextView console;

    ConnectionThread connection;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        console = findViewById(R.id.txt_console);
        console.setMovementMethod(new ScrollingMovementMethod());
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        if(actionbar != null){
            actionbar.setTitle(R.string.controller_activity_actionbar_name);
        }

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter == null){
            actionbar.setSubtitle("Bluetooth not found");
        }else{
            if(!adapter.isEnabled()){
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, ENABLE_BLUETOOTH);
            }
        }

        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message message){
                StringBuilder builder = new StringBuilder();
                Bundle bundle = message.getData();
                String data = bundle.getString("ERROR");
                if(data != null) {
                    actionbar.setSubtitle(data);
                    return;
                }
                byte[] byteArray = bundle.getByteArray("CONTROL");
                if(byteArray != null) {
                    String response = new String(byteArray);
                    console.append(response);
                    return;
                }
                data = bundle.getString("CONNECT");
                if(data != null){
                    String deviceName = bundle.getString("deviceName");
                    String deviceAddress = bundle.getString("deviceAddress");
                    builder.append("Connectado a ");
                    builder.append(deviceName);
                    actionbar.setSubtitle(builder.toString());
                    return;
                }
                data = bundle.getString("ERROR ON WRITING");
                if(data != null){
                    console.append("\n" + data.toString());
                }
            }
        };

        forward = new Command(getString(R.string.button_label_forward), "1");
        rear = new Command(getString(R.string.button_label_rear), "2");
        left = new Command(getString(R.string.button_label_left), "3");
        right = new Command(getString(R.string.button_label_right), "4");
        stop = new Command(getString(R.string.button_label_stop), "5");
        increase_speed = new Command(getString(R.string.button_label_increase_speed), "6");
        decrease_speed = new Command(getString(R.string.button_label_decrease_speed), "7");
        rotate_left = new Command(getString(R.string.button_label_rotate_left), "8");
        rotate_right = new Command(getString(R.string.button_label_rotate_right), "9");
        hyperdrive = new Command(getString(R.string.button_label_hyperdrive), "10");
        restore_minimum_values = new Command(getString(R.string.button_label_restore_value), "11");
        fire_cannon = new Command(getString(R.string.button_label_fire_cannon), "30");
        rotate_cw_cannon = new Command(getString(R.string.button_label_rotate_clockwise_cannon), "33");
        rotate_ccw_cannon = new Command(getString(R.string.button_label_rotate_clockwise_cannon), "34");
        up_cannon = new Command(getString(R.string.button_label_raise),"31");
        down_cannon = new Command(getString(R.string.button_label_low), "32");

        help = new Command("Help", "100");

        btn_forward = findViewById(R.id.btn1);
        btn_rear = findViewById(R.id.btn4);
        btn_stop = findViewById(R.id.btn5);
        btn_right = findViewById(R.id.btn3);
        btn_left = findViewById(R.id.btn2);
        btn_rotate_left = findViewById(R.id.btn9);
        btn_rotate_right = findViewById(R.id.btn8);
        btn_increase_speed = findViewById(R.id.btn6);
        btn_decrease_speed = findViewById(R.id.btn7);
        btn_help = findViewById(R.id.btn_help);
        btn_hyperdrive = findViewById(R.id.btn10);
        btn_fire_cannon = findViewById(R.id.btn13);
        btn_rotate_cw_cannon = findViewById(R.id.btn16);
        btn_rotate_ccw_cannon = findViewById(R.id.btn15);
        btn_up_cannon = findViewById(R.id.btn12);
        btn_down_cannon = findViewById(R.id.btn14);
        btn_restore_minimum_values = findViewById(R.id.btn11);

        btn_forward.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(), ConfigureButtonActivity.class);
                intent.putExtra("Label", forward.getLabel());
                intent.putExtra("Command", forward.getCommand());
                startActivityForResult(intent, ACTIVITY_RESULT_FORWARD);
                return true;
            }
        });

        btn_rear.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(), ConfigureButtonActivity.class);
                intent.putExtra("Label", rear.getLabel());
                intent.putExtra("Command", rear.getCommand());
                startActivityForResult(intent, ACTIVITY_RESULT_REAR);
                return true;
            }
        });

        btn_left.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(), ConfigureButtonActivity.class);
                intent.putExtra("Label", left.getLabel());
                intent.putExtra("Command", left.getCommand());
                startActivityForResult(intent, ACTIVITY_RESULT_LEFT);
                return true;
            }
        });

        btn_right.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(), ConfigureButtonActivity.class);
                intent.putExtra("Label", right.getLabel());
                intent.putExtra("Command", right.getCommand());
                startActivityForResult(intent, ACTIVITY_RESULT_RIGHT);
                return true;
            }
        });

        btn_stop.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(), ConfigureButtonActivity.class);
                intent.putExtra("Label", stop.getLabel());
                intent.putExtra("Command", stop.getCommand());
                startActivityForResult(intent, ACTIVITY_RESULT_STOP);
                return true;
            }
        });

        btn_rotate_right.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(), ConfigureButtonActivity.class);
                intent.putExtra("Label", rotate_right.getLabel());
                intent.putExtra("Command", rotate_right.getCommand());
                startActivityForResult(intent, ACTIVITY_RESULT_ROTATE_RIGHT);
                return true;
            }
        });

        btn_rotate_left.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(), ConfigureButtonActivity.class);
                intent.putExtra("Label", rotate_left.getLabel());
                intent.putExtra("Command", rotate_left.getCommand());
                startActivityForResult(intent, ACTIVITY_RESULT_ROTATE_LEFT);
                return true;
            }
        });

        btn_increase_speed.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(), ConfigureButtonActivity.class);
                intent.putExtra("Label", increase_speed.getLabel());
                intent.putExtra("Command", increase_speed.getCommand());
                startActivityForResult(intent, ACTIVITY_RESULT_INCREASE_SPEED);
                return true;
            }
        });

        btn_decrease_speed.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(), ConfigureButtonActivity.class);
                intent.putExtra("Label", decrease_speed.getLabel());
                intent.putExtra("Command", decrease_speed.getCommand());
                startActivityForResult(intent, ACTIVITY_RESULT_DECREASE_SPEED);
                return true;
            }
        });

        btn_hyperdrive.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(), ConfigureButtonActivity.class);
                intent.putExtra("Label", hyperdrive.getLabel());
                intent.putExtra("Command", hyperdrive.getCommand());
                startActivityForResult(intent, ACTIVITY_RESULT_HYPERDRIVE);
                return true;
            }
        });

        btn_restore_minimum_values.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(), ConfigureButtonActivity.class);
                intent.putExtra("Label", restore_minimum_values.getLabel());
                intent.putExtra("Command", restore_minimum_values.getCommand());
                startActivityForResult(intent, ACTIVITY_RESULT_RESTORE_MINIMUM_VALUE);
                return true;
            }
        });

        btn_fire_cannon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(), ConfigureButtonActivity.class);
                intent.putExtra("Label", fire_cannon.getLabel());
                intent.putExtra("Command", fire_cannon.getCommand());
                startActivityForResult(intent, ACTIVITY_RESULT_FIRE_CANNON);
                return true;
            }
        });

        btn_rotate_cw_cannon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(), ConfigureButtonActivity.class);
                intent.putExtra("Label", rotate_cw_cannon.getLabel());
                intent.putExtra("Command", rotate_cw_cannon.getCommand());
                startActivityForResult(intent, ACTIVITY_RESULT_ROTATE_CW_CANNON);
                return true;
            }
        });

        btn_rotate_ccw_cannon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(), ConfigureButtonActivity.class);
                intent.putExtra("Label", rotate_ccw_cannon.getLabel());
                intent.putExtra("Command", rotate_ccw_cannon.getCommand());
                startActivityForResult(intent, ACTIVITY_RESULT_ROTATE_CCW_CANNON);
                return true;
            }
        });

        btn_up_cannon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(), ConfigureButtonActivity.class);
                intent.putExtra("Label", up_cannon.getLabel());
                intent.putExtra("Command", up_cannon.getCommand());
                startActivityForResult(intent, ACTIVITY_RESULT_UP_CANNON);
                return true;
            }
        });

        btn_down_cannon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(), ConfigureButtonActivity.class);
                intent.putExtra("Label", down_cannon.getLabel());
                intent.putExtra("Command", down_cannon.getCommand());
                startActivityForResult(intent, ACTIVITY_RESULT_DOWN_CANNON);
                return true;
            }
        });

        btn_help.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(), ConfigureButtonActivity.class);
                startActivityForResult(intent, ACTIVITY_RESULT_HELP);
                return true;
            }
        });

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (connection != null){
            connection.cancel();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.controller_activity_actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.action_paired_devices:
                Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
                startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBtnForwardClick(View view){
        if(connection != null){
            connection.write(forward.getCommand().getBytes());
        }
    }

    public void onBtnRearClick(View view){
        if(connection != null){
            connection.write(rear.getCommand().getBytes());
        }
    }

    public void onBtnLeftClick(View view){
        if(connection != null){
            connection.write(left.getCommand().getBytes());
        }
    }

    public void onBtnRightClick(View view){
        if(connection != null){
            connection.write(right.getCommand().getBytes());
        }
    }

    public void onBtnRotateLeftClick(View view){
        if(connection != null){
            connection.write(rotate_left.getCommand().getBytes());
        }
    }

    public void onBtnRotateRightClick(View view){
        if(connection != null){
            connection.write(rotate_right.getCommand().getBytes());
        }
    }

    public void onBtnIncreaseSpeedClick(View view){
        if(connection != null){
            connection.write(increase_speed.getCommand().getBytes());
        }
    }

    public void onBtnDecreaseSpeedClick(View view){
        if(connection != null){
            connection.write(decrease_speed.getCommand().getBytes());
        }
    }

    public void onBtnHyperdriveClick(View view){
        if(connection != null){
            connection.write(hyperdrive.getCommand().getBytes());
        }
    }

    public void onBtnRestoreMinimumValuesClick(View view){
        if(connection != null){
            connection.write(restore_minimum_values.getCommand().getBytes());
        }
    }

    public void onBtnStopClick(View view){
        if(connection != null){
            connection.write(stop.getCommand().getBytes());
        }
    }

    public void onBtnFireCannonClick(View view){
        if(connection != null){
            connection.write(fire_cannon.getCommand().getBytes());
        }
    }

    public void onBtnRotateCWCannonClick(View view){
        if(connection != null){
            connection.write(rotate_cw_cannon.getCommand().getBytes());
        }
    }

    public void onBtnRotateCCWCannonClick(View view){
        if(connection != null){
            connection.write(rotate_ccw_cannon.getCommand().getBytes());
        }
    }

    public void onBtnUpCannonClick(View view){
        if(connection != null){
            connection.write(up_cannon.getCommand().getBytes());
        }
    }

    public void onBtnDownCannonClick(View view){
        if(connection != null){
            connection.write(down_cannon.getCommand().getBytes());
        }
    }

    public void onBtnHelpClick(View view){
        if(connection != null){
            connection.write(help.getCommand().getBytes());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String value = data.getStringExtra("Button Label");
        String value2 = data.getStringExtra("Button Command");

        if (requestCode == ENABLE_BLUETOOTH) {
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "Bluetooth active", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == SELECT_PAIRED_DEVICE){
            if(resultCode == RESULT_OK){
                String devName = data.getStringExtra("btDeviceName");
                String devAddress = data.getStringExtra("btDeviceAddress");
                actionbar.setSubtitle(devName + ": " + devAddress);
                connection = new ConnectionThread(handler, devAddress);
                connection.start();
            }
        } else if(requestCode == ACTIVITY_RESULT_FORWARD){
            if(resultCode == RESULT_OK){
                forward.setLabel(value);
                forward.setCommand(value2);
                btn_forward.setText(forward.getLabel());
            }
        }else if(requestCode == ACTIVITY_RESULT_REAR){
            if(resultCode == RESULT_OK){
                rear.setLabel(value);
                rear.setCommand(value2);
                btn_rear.setText(rear.getLabel());
            }
        }else if(requestCode == ACTIVITY_RESULT_LEFT){
            if(resultCode == RESULT_OK){
                left.setLabel(value);
                left.setCommand(value2);
                btn_left.setText(left.getLabel());
            }
        }else if(requestCode == ACTIVITY_RESULT_RIGHT){
            if(resultCode == RESULT_OK){
                right.setLabel(value);
                right.setCommand(value2);
                btn_right.setText(right.getLabel());
            }
        }else if(requestCode == ACTIVITY_RESULT_ROTATE_LEFT){
            if(resultCode == RESULT_OK){
                rotate_left.setLabel(value);
                rotate_left.setCommand(value2);
                btn_rotate_left.setText(rotate_left.getLabel());
            }
        }else if(requestCode == ACTIVITY_RESULT_ROTATE_RIGHT){
            if(resultCode == RESULT_OK){
                rotate_right.setLabel(value);
                rotate_right.setCommand(value2);
                btn_rotate_right.setText(rotate_right.getLabel());
            }
        }else if(requestCode == ACTIVITY_RESULT_INCREASE_SPEED){
            if(resultCode == RESULT_OK){
                increase_speed.setLabel(value);
                increase_speed.setCommand(value2);
                btn_increase_speed.setText(increase_speed.getLabel());
            }
        }else if(requestCode == ACTIVITY_RESULT_DECREASE_SPEED){
            if(resultCode == RESULT_OK){
                decrease_speed.setLabel(value);
                decrease_speed.setCommand(value2);
                btn_decrease_speed.setText(decrease_speed.getLabel());
            }
        }else if(requestCode == ACTIVITY_RESULT_HELP){
            if(resultCode == RESULT_OK){
                //help.setText(value + value2);
            }
        }else if(requestCode == ACTIVITY_RESULT_HYPERDRIVE){
            if(resultCode == RESULT_OK){
                hyperdrive.setLabel(value);
                hyperdrive.setCommand(value2);
                btn_hyperdrive.setText(hyperdrive.getLabel());
            }
        }else if(requestCode == ACTIVITY_RESULT_RESTORE_MINIMUM_VALUE){
            if(resultCode == RESULT_OK){
                restore_minimum_values.setLabel(value);
                restore_minimum_values.setCommand(value2);
                btn_restore_minimum_values.setText(restore_minimum_values.getLabel());
            }
        }else if(requestCode == ACTIVITY_RESULT_FIRE_CANNON){
            if(resultCode == RESULT_OK){
                fire_cannon.setLabel(value);
                fire_cannon.setCommand(value2);
                btn_fire_cannon.setText(fire_cannon.getLabel());
            }
        }else if(requestCode == ACTIVITY_RESULT_ROTATE_CW_CANNON){
            if(resultCode == RESULT_OK){
                rotate_cw_cannon.setLabel(value);
                rotate_cw_cannon.setCommand(value2);
                btn_rotate_cw_cannon.setText(rotate_cw_cannon.getLabel());
            }
        }else if(requestCode == ACTIVITY_RESULT_ROTATE_CCW_CANNON){
            if(resultCode == RESULT_OK){
                rotate_ccw_cannon.setLabel(value);
                rotate_ccw_cannon.setCommand(value2);
                btn_rotate_ccw_cannon.setText(rotate_ccw_cannon.getLabel());
            }
        }else if(requestCode == ACTIVITY_RESULT_UP_CANNON){
            if(resultCode == RESULT_OK){
                up_cannon.setLabel(value);
                up_cannon.setCommand(value2);
                btn_up_cannon.setText(up_cannon.getLabel());
            }
        }else if(requestCode == ACTIVITY_RESULT_DOWN_CANNON){
            if(resultCode == RESULT_OK){
                down_cannon.setLabel(value);
                down_cannon.setCommand(value2);
                btn_down_cannon.setText(down_cannon.getLabel());
            }
        }
    }

}
