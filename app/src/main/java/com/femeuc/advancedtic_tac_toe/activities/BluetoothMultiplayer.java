package com.femeuc.advancedtic_tac_toe.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.femeuc.advancedtic_tac_toe.R;
import com.femeuc.advancedtic_tac_toe.classes.SocketHandler;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class BluetoothMultiplayer extends AppCompatActivity {

    Button hostMatchButton, listPairedDevicesButton;
    ListView pairedDevicesListView;

    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice[] bluetoothDevicesArray;

    public static int REQUEST_TO_ENABLE_BLUETOOTH = 1;

    public static final String NAME = "Bluetooth Multiplayer";
    public static final UUID MY_UUID = UUID.fromString("1787c2e5-407e-42ce-94e2-d49c27bd4103");
    public static final String IS_DEVICE_HOST = "com.femeuc.advancedtic_tac_toe.IS_DEVICE_HOST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_multiplayer);

        findViewsById();
        setOnClickListeners();
        prepareBluetooth();

    }



    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket
            // because mmServerSocket is final.
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code.
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned.
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (socket != null) {
                    // A connection was accepted. Perform work associated with
                    // the connection in a separate thread.

                    SocketHandler.setSocket(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getApplicationContext(), BluetoothMultiplayerBoard.class);
                    intent.putExtra(IS_DEVICE_HOST, true);
                    startActivity(intent);
                    break;
                }
            }
        }

    }


    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            SocketHandler.setSocket(mmSocket);
            Intent intent = new Intent(getApplicationContext(), BluetoothMultiplayerBoard.class);
            intent.putExtra(IS_DEVICE_HOST, false);
            startActivity(intent);
        }

    }




    private void prepareBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not supported in this device.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if(!bluetoothAdapter.isEnabled()) {
                Intent enablueBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enablueBluetoothIntent, REQUEST_TO_ENABLE_BLUETOOTH);
            }
        }
    }

    private void setOnClickListeners() {

        listPairedDevicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetoothAdapter.isEnabled()) {
                    Set<BluetoothDevice> bluetoothDeviceSet = bluetoothAdapter.getBondedDevices();
                    String[] devicesNames = new String[bluetoothDeviceSet.size()];
                    bluetoothDevicesArray = new BluetoothDevice[bluetoothDeviceSet.size()];
                    int index = 0;

                    if(bluetoothDeviceSet.size() > 0) {
                        for(BluetoothDevice device : bluetoothDeviceSet) {
                            bluetoothDevicesArray[index] = device;
                            devicesNames[index] = device.getName();
                            index++;
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, devicesNames);
                        pairedDevicesListView.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth must be activated in order to list paired devoces", Toast.LENGTH_SHORT).show();
                }
            }
        });

        hostMatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetoothAdapter.isEnabled()) {
                    AcceptThread acceptThread = new AcceptThread();
                    acceptThread.start();
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth must be activated in order to host a match", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pairedDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConnectThread connectThread = new ConnectThread(bluetoothDevicesArray[position]);
                connectThread.start();
            }
        });
    }

    private void findViewsById() {
        hostMatchButton = findViewById(R.id.hostMatchButton);
        listPairedDevicesButton = findViewById(R.id.listPairedDevicesButton);
        pairedDevicesListView = findViewById(R.id.pairedDevicesListView);
    }

}
