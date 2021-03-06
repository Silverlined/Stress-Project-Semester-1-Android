package com.example.dimitriygeorgiev.stressit.Tcp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class TcpClient {

    public static final String TAG = TcpClient.class.getSimpleName();
    public static final int SERVER_PORT = 5045;
    private String mServerIp; //server IP address
    private Socket socket;
    // message to send to the server
    private String mServerMessage;
    // sends message received notifications
    private OnMessageReceived mMessageListener = null;
    // while this is true, the server will continue running
    private boolean mRun = false;
    // used to send messages
    private PrintWriter mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;
    private boolean exceptionStatus = true;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpClient(OnMessageReceived listener, String mIpAddress) {
        this.mMessageListener = listener;
        this.mServerIp = mIpAddress;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void sendMessage(final char message) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mBufferOut != null) {
                    Log.d(TAG, "Sending: " + message);
                    mBufferOut.println(message);
                    mBufferOut.flush();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Close the connection and release the members
     */
    public void stopClient() {

        mRun = false;
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }

        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;
    }

    public void run() {

        mRun = true;

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(mServerIp);

            Log.d("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            socket = new Socket(serverAddr, SERVER_PORT);
            socket.setKeepAlive(true);
            if (socket.isConnected()) {
                exceptionStatus = false;
                try {

                    //sends the message to the server
                    mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                    //receives the message which the server sends back
                    mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //in this while the client listens for the messages sent by the server
                    while (mRun) {
                        if (!socket.isClosed())
                            mServerMessage = mBufferIn.readLine();
                        if (mServerMessage != null && mMessageListener != null) {
                            //call the method messageReceived from MyActivity class
                            mMessageListener.messageReceived(mServerMessage);
                        }

                    }

                    Log.d("RESPONSE FROM SERVER", "S: Received Message: '" + mServerMessage + "'");

                } catch (SocketException e) {
                    exceptionStatus = true;
                    e.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } finally {
                    socket.close();
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getExceptionStatus() {
        return exceptionStatus;
    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the Activity
    //class at on AsyncTask doInBackground
    public interface OnMessageReceived {
        void messageReceived(String message);
    }
}