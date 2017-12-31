package com.lerebourg.max.memeavolonte;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class Download extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.lerebourg.max.memeavolonte.action.FOO";
    private static final String ACTION_BAZ = "com.lerebourg.max.memeavolonte.action.BAZ";
    private static final String ACTION_RAC = "com.lerebourg.max.memeavolonte.action.RAC";

    public static String getApi() {
        return api;
    }
    private static String api = "http://lerebourg-baratier.hanotaux.fr/api/";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.lerebourg.max.memeavolonte.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.lerebourg.max.memeavolonte.extra.PARAM2";

    public Download() {
        super("Download");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, Download.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }
    public static void startActionRac(Context context, String param1, String param2) {
        Intent intent = new Intent(context, Download.class);
        intent.setAction(ACTION_RAC);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, Download.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            } else if (ACTION_RAC.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionRac(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        Log.d("Max", "Thread service name : " + Thread.currentThread().getName());
        try {
            String surl = api + param1;
            if (param2.length() != 0) surl+="/"+param2;
            URL url = new URL(surl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            Log.i("Url, Code et Reponse", surl + " " + connection.getResponseMessage() + " " + connection.getResponseCode());
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                copyInputStreamToFile(connection.getInputStream(), new File(getCacheDir() +"/"+ param1 + ".json"));
                Log.d("Download", param1 + ".json DL");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (param1.equals("adverts")) {
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Adverts.ADVERTS));
        } else if (param1.equals("advert")) {
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Advert.ADVERT));
        }
    }

    private void copyInputStreamToFile(InputStream in, File file) {
        try {
            OutputStream ou = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                ou.write(buf, 0, len);
            }
            ou.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    String post(String url, String json) throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    private void handleActionBaz(String param1, String param2) {
        Log.d("Max", "Thread service name : " + Thread.currentThread().getName());
        try {
            String res = post(api+param1, param2);
            Log.d("Download", res);
            if (res.length() > 0) {
                Log.i("youpi","lol");
                try{
                    File ff=new File(getCacheDir() +"/"+ param1 + ".json"); // définir l'arborescence
                    ff.createNewFile();
                    FileWriter ffw=new FileWriter(ff);
                    ffw.write(res);
                    ffw.close(); // fermer le fichier à la fin des traitements
                } catch (Exception e) {}

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Add.ADD));
    }
    private void handleActionRac(String param1, String param2) {
        Log.d("Max", "Thread service name : " + Thread.currentThread().getName());
        try {
            URL url = new URL(api + param1 + "/" + param2);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.connect();
            Log.i("Code et Reponse", connection.getResponseMessage() + " " + connection.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Advert.DELETE));
    }
}
