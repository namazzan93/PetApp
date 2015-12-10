package com.iruka.myhealingpet_test;

import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectThread extends Thread {
    private ArrayList<String> params;
    private final int PORT = 5000;
    private final String ADDR = "52.192.54.7";
    private boolean ret= false;
    private Object resultObject;

    public ConnectThread(ArrayList<String> p) {
        params=p;
    }

    public boolean getResult() { return ret; }
    public Object getResultObject() { return resultObject; }

    public void run() {
        try {
            Socket sock = new Socket(ADDR, PORT);

            ObjectOutputStream outstream = new ObjectOutputStream(sock.getOutputStream());
            outstream.writeObject(params);
            outstream.flush();

            ObjectInputStream instream = new ObjectInputStream(sock.getInputStream());
            resultObject = instream.readObject();

            ret = ((ArrayList<String>)resultObject).get(0).equals("true") ? true : false;
            System.out.println(ret);
            Log.d("MainActivity", "서버에서 받은 메세지 : " + resultObject);

            sock.close();

        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }
}
