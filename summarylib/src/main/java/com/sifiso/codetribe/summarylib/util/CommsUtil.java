package com.sifiso.codetribe.summarylib.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;


import android.content.Context;


import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;
import com.sifiso.codetribe.summarylib.model.ResponseData;
import com.sifiso.codetribe.summarylib.util.bean.CommsException;
import com.sifiso.codetribe.summarylib.util.bean.NetworkUnavailableException;

public class CommsUtil {
    public static ResponseData getData(Context ctx, String request)
            throws CommsException, NetworkUnavailableException {
        Log.d(COMMS, "getData: sending request: .......\n" + request);
        HttpURLConnection con = null;
        // Check if there's a proxy set up for internet access
        @SuppressWarnings("deprecation")
        String heita = Settings.Global.getString(ctx.getContentResolver(),
                Settings.Global.HTTP_PROXY);
        Log.i(COMMS, "##### proxy on device: " + heita);

        URL url;
        String response = null;
        InputStream is = null;
        ResponseData webResp = null;
        // check network
        WebCheckResult res = WebCheck.checkNetworkAvailability(ctx);
        if (res.isNetworkUnavailable()) {
            throw new NetworkUnavailableException();
        }
        try {
            url = new URL(request);
            if (heita == null) {
                con = (HttpURLConnection) url.openConnection(); // There is no
                // proxy defined
                Log.i(COMMS, "##### heita is null: " + heita);
            } else {
                Log.i(COMMS, "##### heita is not null " + heita);
                int ix = heita.lastIndexOf(":");
                String p = heita.substring(ix + 1);
                try {
                    int port = Integer.parseInt(p);
                    String host = heita.substring(0, ix);
                    Proxy proxy = new Proxy(Proxy.Type.HTTP,
                            new InetSocketAddress(host, port));
                    con = (HttpURLConnection) url.openConnection(proxy);
                    Log.d(COMMS, "Got connection using proxy: " + heita);
                } catch (Exception e) {
                    Log.e(COMMS, "*** Error trying to get proxy from device", e);
                    con = (HttpURLConnection) url.openConnection();
                }
            }
            con.setRequestMethod("POST");
            con.connect();
            is = con.getInputStream();
            int httpCode = con.getResponseCode();
            String msg = con.getResponseMessage();
            Log.d(COMMS, "### HTTP response code: " + httpCode + " msg: " + msg);
            response = readStream(is);
            Log.d(COMMS, "### RESPONSE: \n" + response);
            // check for html
            int idx = response.indexOf("DOCTYPE html");
            if (idx > -1) {
                Log.e(COMMS, "@@@ ERROR RESPONSE, some html received:\n"
                        + response);
                throw new NetworkUnavailableException();
            }
            Gson gson = new Gson();
            webResp = gson.fromJson(response, ResponseData.class);

            if (webResp != null) {

                Log.i(COMMS, "Back-end status code: " + webResp.getStatusCode()
                        + " msg: " + webResp.getStatusDescription());

                // Log.i("TAG BAG",
                // webResp.getAppointmentDTO().getAppointDescript()+"");
            } else {
                Log.e(COMMS,
                        "&&&&&&& ++ It's a Houston kind of problem: json deserializer returned null");
                webResp = new ResponseData();
                webResp.setStatusCode(9999);
                webResp.setStatusDescription("Funny problem, Haha! response is NULL!");
            }

        } catch (IOException e) {
            Log.e(COMMS, "Houston, we have an IOException. F%$%K!", e);
            if (res.isMobileConnected()) {
                throw new NetworkUnavailableException();
            } else {
                throw new CommsException(CommsException.CONNECTION_ERROR);
            }
        } catch (Exception e) {
            throw new CommsException(CommsException.CONNECTION_ERROR);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                // Log.e(COMMS,
                // "Unable to close input stream - should be no problem.");
            }
        }

        return webResp;
    }

    private static String readStream(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in), 1024);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

    private static final String COMMS = "CommsUtil";
}
