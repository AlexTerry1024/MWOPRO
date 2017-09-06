package mwpro.com.mwproapplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MyDBProcessing {

    public static MyDBProcessing process = null;

    Handler handle;

    String strBaseUrl = "";
    String strJSon = "";
    public static final MediaType JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    public static MyDBProcessing getInstance()
    {
        if(process == null)
            process = new MyDBProcessing();

        return process;
    }

    public String GetURLPRODServices()
    {
        String strURLPROD = "";
        if (Constants.APPLICATION_SERVER_ID == 1) {

            strURLPROD = "http://prewebservices.mobiwoom.com/";
        }
        else if (Constants.APPLICATION_SERVER_ID == 0) {

            strURLPROD = "http://webservicenew.mobiwoom.com/";
        }
        else if (Constants.APPLICATION_SERVER_ID == 3) {

            strURLPROD = "http://prodservices.mobiwoom.com/";
        }
        else if (Constants.APPLICATION_SERVER_ID == 2) {

            strURLPROD = "http://anotemservices.mobiwoom.com/";
        }

        return strURLPROD;
    }

    public  String encodeURIComponent(String s) throws UnsupportedEncodingException {
        String result;

        try {
            result = URLEncoder.encode(s, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException e) {
            result = s;
        }

        return result;
    }
    public String JSONTOSTRING(String json) throws JSONException, UnsupportedEncodingException {
        String strResult = "";

        JSONObject object = new JSONObject(json);

        Iterator<String> iterator = object.keys();

        String strKey = "";

        while(iterator.hasNext())
        {
            strKey = iterator.next();

            strResult += strKey + "=" + encodeURIComponent(object.getString(strKey)) + "&";
        }

        strResult = strResult.substring(0, strResult.length() - 1);

        return strResult;
    }

    public String GetURLPROD()
    {
        String strURLPROD = "";
        if (Constants.APPLICATION_SERVER_ID == 1) {
            strURLPROD = "https://preprodapi.mobiwoom.com/";
        }
        else if (Constants.APPLICATION_SERVER_ID == 0) {
            strURLPROD = "http://api.mobiwoom.com:90/";
        }
        else if (Constants.APPLICATION_SERVER_ID == 3) {
            strURLPROD = "https://prodapi.mobiwoom.com:444/";
        }
        else if (Constants.APPLICATION_SERVER_ID == 2) {
            strURLPROD = "http://anotemapi.mobiwoom.com:100/";
        }

        return strURLPROD;
    }

    public void SendPost(String params, Handler handles)
    {
        Thread test = new Thread(mRunnable);

        strJSon = params;
        handle = handles;

        test.start();
    }

    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {

            String data = "";
            RequestBody body = null;
            String strUrl = "";

            try {
                String strText = JSONTOSTRING(strJSon);
                body = RequestBody.create(JSON, JSONTOSTRING(strJSon));
                strUrl = GetURLPROD();
                OkHttpClient.Builder builder = new OkHttpClient().newBuilder();

                builder.readTimeout(2, TimeUnit.MINUTES);
                builder.writeTimeout(2, TimeUnit.MINUTES);

                OkHttpClient client = builder.build();
                Request request = new Request.Builder()
                        .url(strUrl)
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new Callback()
                {
                    @Override public void onFailure(Call call, IOException e) {
                        e.printStackTrace();

                        Message message = new Message();

                        message.arg2 = Constants.NET_ERR;

                        handle.sendMessage(message);

                    }
                    @Override public void onResponse(Call call, Response response) throws IOException {
                        try (ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                            Message message = new Message();
                            message.arg2 = Constants.NET_SUC;
                            message.obj = response.body().string();

                            handle.sendMessage(message);
                        }
                    }

                });
            } catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            }
            //SystemClock.sleep(5000);
            //mHandler.sendEmptyMessage(0);
        }
    };
}
