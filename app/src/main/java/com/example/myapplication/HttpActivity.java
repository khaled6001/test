package com.example.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

public abstract class HttpActivity extends AppCompatActivity implements ConnectivityChangeListener {
    protected final String ADDRESS = "https://192.168.1.5/";
    protected String PAGE;
    NetworkService networkService;
    boolean mBound = false;
    private NetworkChangeReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        networkReceiver = NetworkChangeReceiver.getInstance(this);

        registerReceiver(networkReceiver, filter);
    }

    @Override
    public void onConnectivityChange(boolean isConnected) {
        if (isConnected) {
            // do something when the conenctivity is restored
        } else {
            // do something when the connectivity is lost
        }
    }

    protected void send(Map<String, String> params) {
        if(!networkReceiver.isConnected()) {
            Toast.makeText(HttpActivity.this, getResources().getString(R.string.verify_your_Internet), Toast.LENGTH_SHORT).show();
            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, this.ADDRESS + this.PAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        responseReceived(response, params);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error occurred, show error message
                        Toast.makeText(HttpActivity.this, "Error occurred: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        // Add request to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    protected abstract void responseReceived(String response, Map<String, String> params);

    @Override
    public Resources getResources() {
        return super.getResources();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start the service
        Intent intent = new Intent(this, NetworkService.class);
        startService(intent);
        // Connect this activity to the service
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Disconnect this activity from service
        unbindService(connection);
        // Stop the service
        Intent intent = new Intent(this, NetworkService.class);
        stopService(intent);
    }


    /** Defines callbacks for service binding, passed to bindService(). */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to NetworkService, cast the IBinder and get LocalService instance.
            NetworkService.LocalBinder binder = (NetworkService.LocalBinder) service;
            networkService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    public void onDestroy() {
        if (networkReceiver != null) {
            unregisterReceiver(networkReceiver);
            networkReceiver = null;
        }
        super.onDestroy();
    }
}
