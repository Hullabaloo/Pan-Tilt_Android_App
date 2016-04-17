package morn.pan_tilt;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

public class main extends Activity {
    public Typeface timerFont;
    public TextView timer;
    public TextView timerBg;
    public TextView center;
    public ProgressBar progressBar;
    public Button startButton;
    public Button stopButton;
    public Button batteryButton;
    public Button setStartButton;
    public Button setEndButton;
    public Button toStartButton;
    public Button toEndButton;
    public ImageButton rightButton;
    public ImageButton leftButton;
    public ImageButton upButton;
    public ImageButton downButton;
    public EditText number;
    public EditText interval;

    public AlertDialog.Builder alertDialog;
    public BluetoothSocket bluetoothSocket = null;
    public BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public InputStream input;
    public OutputStream output;

    public int p = 0, batMin = 660, batMax = 840;
    public String address = "20:15:02:05:70:69";
    public boolean go = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        timerFont = Typeface.createFromAsset(getAssets(), "fonts/digital_clock.ttf");

        timer = (TextView)findViewById(R.id.timer);
        timer.setTypeface(timerFont);

        timerBg = (TextView)findViewById(R.id.timerBg);
        timerBg.setTypeface(timerFont);

        center = (TextView)findViewById(R.id.center);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setProgress(p);

        startButton = (Button)findViewById(R.id.start);
        stopButton = (Button)findViewById(R.id.stop);
        batteryButton = (Button)findViewById(R.id.battery);
        setStartButton = (Button)findViewById(R.id.setStart);
        setEndButton = (Button)findViewById(R.id.setEnd);
        toStartButton = (Button)findViewById(R.id.toStart);
        toEndButton = (Button)findViewById(R.id.toEnd);

        rightButton = (ImageButton)findViewById(R.id.right);
        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            output.write('r');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:

                        try {
                            output.write('s');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                }
                return false;
            }
        });

        leftButton = (ImageButton)findViewById(R.id.left);
        leftButton.setRotation(180);
        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            output.write('l');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:

                        try {
                            output.write('s');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                }
                return false;
            }
        });

        upButton = (ImageButton)findViewById(R.id.up);
        upButton.setRotation(270);
        upButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            output.write('u');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:

                        try {
                            output.write('s');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                }
                return false;
            }
        });

        downButton = (ImageButton)findViewById(R.id.down);
        downButton.setRotation(90);
        downButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            output.write('d');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:

                        try {
                            output.write('s');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                }
                return false;
            }
        });

        number = (EditText)findViewById(R.id.number);
        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                timer();
            }
        });

        interval = (EditText)findViewById(R.id.interval);
        interval.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                timer();
            }
        });

        timer();

        alertDialog = new AlertDialog.Builder(this);

        new connect().execute();
    }

    public void setStart(View view){
        try {
            output.write('x');
        } catch (IOException e) {
            e.printStackTrace();
        }
        request(null);
    }

    public void setEnd(View view){
        try {
            output.write('y');
        } catch (IOException e) {
            e.printStackTrace();
        }
        request(null);
    }

    public void toStart(View view){
        try {
            output.write('a');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toEnd(View view){
        try {
            output.write('b');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(View view) {
        try {
            while(input.available() > 0)
                input.read();
            output.write(String.format("+ %d %d", Integer.parseInt(number.getText().toString()), Math.round(Float.parseFloat(interval.getText().toString()) * 100)).getBytes());
            SystemClock.sleep(500);
            request(null);
        } catch(IOException e) {
            Log.d("BLUETOOTH", e.getMessage());
        }
    }

    public void stop(View view) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.stop))
                .setMessage(getString(R.string.stopDialog))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            output.write('-');
                            request(null);
                        } catch (IOException e) {
                            Log.d("BLUETOOTH", e.getMessage());
                        }
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .show();
    }

    public void request(View view) {
        if(bluetoothSocket != null && bluetoothSocket.isConnected()) {
            String buff = "";
            try {
                output.write('*');
                int i = 0;
                char a;
                while(input.available() == 0 && i < 100) {
                    i++;
                    SystemClock.sleep(10);
                }
                SystemClock.sleep(100);
                if(input.available() > 0)
                    while(true) {
                        if(input.available() > 0) {
                            a = (char) input.read();
                            if(a == '\n') {
                                break;
                            } else
                                buff += a;
                        }
                    }
                else
                    new connect().execute();
            } catch(IOException e) {
                Log.d("BLUETOOTH", e.getMessage());
            }
            Scanner scanner = new Scanner(buff);
            int n = scanner.nextInt();
            int i = scanner.nextInt();
            int time = scanner.nextInt();
            int left = scanner.nextInt();
            int angleX = scanner.nextInt();
            int angleY = scanner.nextInt();
            int voltage = scanner.nextInt();
            if(left > 0) {
                lock();
                timer.setText(String.format("%02d:%02d:%02d", (int) Math.floor(left / 3600.0), (int) Math.floor(left % 3600 / 60.0), left % 3600 % 60));
                go = true;
                p = Math.round(1000 * (1 - (float)left / time));
                if(Integer.parseInt(number.getText().toString()) != n)
                    number.setText(String.format("%d", n));
                if(Math.abs(Float.parseFloat(interval.getText().toString()) - i / 100.0) > 0.00001)
                    interval.setText(String.format("%s", i / 100.0));
                progressBar.setProgress(p);
            } else {
                go = false;
                unlock();
                timer();
                p = 0;
                progressBar.setProgress(p);
            }
            center.setText(String.format("%+d°\n%+d°", angleX, angleY));
            batteryButton.setText(Math.round(100.0 * (float) (voltage - batMin) / (batMax - batMin)) + "%");
        } else
            new connect().execute();
    }

    class connect extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setIndeterminate(true);
            lock();
            batteryButton.setText("");
            stopButton.setEnabled(false);
            startButton.setVisibility(View.VISIBLE);
            stopButton.setVisibility(View.INVISIBLE);
        }
        @Override
        protected Void doInBackground(Void... params) {
            if(bluetoothAdapter.isEnabled()) {
                if(!(bluetoothSocket != null && bluetoothSocket.isConnected()))
                    try {
                        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
                        Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                        bluetoothSocket = (BluetoothSocket)m.invoke(device, 1);
                        bluetoothSocket.connect();
                    } catch(IOException e) {
                        Log.d("BLUETOOTH", e.getMessage());
                    } catch(SecurityException e) {
                        Log.d("BLUETOOTH", e.getMessage());
                    } catch(NoSuchMethodException e) {
                        Log.d("BLUETOOTH", e.getMessage());
                    } catch(IllegalArgumentException e) {
                        Log.d("BLUETOOTH", e.getMessage());
                    } catch(IllegalAccessException e) {
                        Log.d("BLUETOOTH", e.getMessage());
                    } catch(InvocationTargetException e) {
                        Log.d("BLUETOOTH", e.getMessage());
                    }
                try {
                    input = bluetoothSocket.getInputStream();
                    output = bluetoothSocket.getOutputStream();
                } catch(IOException e) {
                    Log.d("BLUETOOTH", e.getMessage());
                }
            } else
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(bluetoothSocket != null && bluetoothSocket.isConnected()) {
                progressBar.setIndeterminate(false);
                if(go)
                    stopButton.setEnabled(true);
                else
                    unlock();
                request(null);
            } else
            if(bluetoothAdapter.isEnabled())
                alertDialog.setTitle(getString(R.string.error))
                        .setMessage(getString(R.string.noConnectDialog))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new connect().execute();
                            }
                        })
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!go)
                                    finish();
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                finish();
                            }
                        })
                        .show();
        }
    }

    @Override
    public void onBackPressed() {
        if(go)
            moveTaskToBack(true);
        else
            super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == -1)
            new connect().execute();
        else
            finish();
    }

    public void lock() {
        number.setEnabled(false);
        interval.setEnabled(false);
        startButton.setEnabled(false);
        startButton.setVisibility(View.INVISIBLE);
        stopButton.setEnabled(true);
        stopButton.setVisibility(View.VISIBLE);
        rightButton.setEnabled(false);
        leftButton.setEnabled(false);
        upButton.setEnabled(false);
        downButton.setEnabled(false);
        setStartButton.setEnabled(false);
        setEndButton.setEnabled(false);
        toStartButton.setEnabled(false);
        toEndButton.setEnabled(false);
    }

    public void unlock() {
        number.setEnabled(true);
        interval.setEnabled(true);
        startButton.setEnabled(true);
        startButton.setVisibility(View.VISIBLE);
        stopButton.setEnabled(false);
        stopButton.setVisibility(View.INVISIBLE);
        rightButton.setEnabled(true);
        leftButton.setEnabled(true);
        upButton.setEnabled(true);
        downButton.setEnabled(true);
        setStartButton.setEnabled(true);
        setEndButton.setEnabled(true);
        toStartButton.setEnabled(true);
        toEndButton.setEnabled(true);
    }

    public void timer(){
        float left = 0;
        if(!number.getText().toString().equals("") && !interval.getText().toString().equals("")) {
            left = Integer.parseInt(number.getText().toString()) * Float.parseFloat(interval.getText().toString());
        }
        if(!go)
            timer.setText(String.format("%02d:%02d:%02d", (int) Math.floor(left / 3600.0), (int) Math.floor(left % 3600 / 60.0), Math.round(left % 3600 % 60)));
    }
}