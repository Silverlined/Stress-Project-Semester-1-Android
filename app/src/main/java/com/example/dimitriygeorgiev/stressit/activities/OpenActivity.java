package com.example.dimitriygeorgiev.stressit.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.dimitriygeorgiev.stressit.R;

public class OpenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
    }

    public void createConnection(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
