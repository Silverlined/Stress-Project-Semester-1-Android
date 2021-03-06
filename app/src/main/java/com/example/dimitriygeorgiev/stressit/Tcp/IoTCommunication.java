package com.example.dimitriygeorgiev.stressit.Tcp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.dimitriygeorgiev.stressit.activities.MainActivity;
import com.example.dimitriygeorgiev.stressit.adapters.MeasurementAdapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class IoTCommunication extends AsyncTask<String, String, Boolean> {

    @SuppressLint("StaticFieldLeak")
    private List<Double> mValues;
    private MeasurementAdapter adapter;
    private TcpClient mTcpClient;
    @SuppressLint("StaticFieldLeak")
    private Context mContext;

    public IoTCommunication(List<Double> values, MeasurementAdapter adapter, Context mContext) {
        this.mValues = values;
        this.adapter = adapter;
        this.mContext = mContext;
    }

    @Override
    protected Boolean doInBackground(String... address) {
        if (address[0] == null) {
            return true;
        }
        //we create a TCPClient object
        //here the messageReceived method is implemented
        mTcpClient = new TcpClient(message1 -> publishProgress(message1), address[0]);
        mTcpClient.run();
        MainActivity.isConnected = true;

        return mTcpClient.getExceptionStatus();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        //response received from server
        Log.d("test", "response " + values[0]);
        //process server response here....
        String[] tempValues = values[0].split(",");
        tempValues[2] = getMicroSiemens(tempValues[2]);
        for (int i = 0; i < tempValues.length; i++) {
            mValues.remove(i);
            mValues.add(i, Double.parseDouble(tempValues[i]));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(Boolean notConnected) {
        super.onPostExecute(notConnected);
        if (notConnected) {
            Toast.makeText(mContext, "Server is not responding", Toast.LENGTH_SHORT).show();
            MainActivity.isConnected = false;
        }
    }

    public void killTask() {
        if (mTcpClient != null) {
            mTcpClient.stopClient();
        }
    }

    private String getMicroSiemens(String tempValue) {
        double voltage = Integer.valueOf(tempValue) * 0.125;
        double current = voltage / 220;              // 220k resistor
        double microSiemens = (1 / (((3.3 - voltage / 1000) / current) * Math.pow(10, 6)) * Math.pow(10, 6)) ;
        return String.valueOf(round(microSiemens, 2));
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}