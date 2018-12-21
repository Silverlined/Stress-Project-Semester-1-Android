package com.example.dimitriygeorgiev.stressit.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.dimitriygeorgiev.stressit.R;
import com.example.dimitriygeorgiev.stressit.Tcp.ConnectTask;
import com.example.dimitriygeorgiev.stressit.adapters.MeasurementAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static boolean isConnected;
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<Double> sensorValues;
    ConnectTask task;
    MeasurementAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        setSupportActionBar(toolbar);
        sensorValues = new ArrayList<>(4);
        sensorValues.add(0.00);
        sensorValues.add(0.00);
        sensorValues.add(0.00);
        sensorValues.add(0.00);

        adapter = new MeasurementAdapter(this, sensorValues);
        setRecyclerView(recyclerView, adapter);

        task = new ConnectTask(sensorValues, adapter, this);
        task.execute("");
    }

    private void setRecyclerView(RecyclerView recyclerView, MeasurementAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayout.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                toReconnect();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void toReconnect() {
        if (!isConnected) {
            new ConnectTask(sensorValues, adapter, this).execute("");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        task.killTask();
    }

    @Override
    protected void onStop() {
        super.onStop();
        task.killTask();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        task.killTask();
    }
}