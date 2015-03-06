package com.sifiso.codetribe.summarylib.toolbox;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sifiso.codetribe.summarylib.model.Category;
import com.sifiso.codetribe.summarylib.model.ResponseData;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class BohaRequest extends Request<JSONArray> {

    private Listener<JSONArray> listener;
    private ErrorListener errorListener;
    private long start, end;

    public BohaRequest(int method, String url, ErrorListener listener) {
        super(method, url, listener);
    }

    public BohaRequest(int method, String url,
                       Listener<JSONArray> responseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = responseListener;
        this.errorListener = errorListener;
        start = System.currentTimeMillis();
        Log.i(LOG, "...Cloud Server communication started ...");

    }


    @Override
    protected Response<JSONArray> parseNetworkResponse(
            NetworkResponse response) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        try {
            Gson gson = new Gson();
            String resp = new String(response.data);
           // Log.i(LOG, "response string length returned: " + resp.length());
            try {

                jsonArray = new JSONArray(resp);

               // Log.i(LOG, "array response length returned: " + jsonArray.length());

                if (jsonArray != null) {
                    return Response.success(jsonArray,
                            HttpHeaderParser.parseCacheHeaders(response));
                }
            } catch (Exception e) {
                // ignore, it's a zipped response
                jsonObject = new JSONObject(resp);
                //Log.i(LOG, "Object2 response length returned: " + e.toString());
                //Log.i(LOG, "Object3 response length returned: " + jsonObject.get("articles"));


                return Response.success(jsonObject.optJSONArray("articles"),
                        HttpHeaderParser.parseCacheHeaders(response));
            }

            InputStream is = new ByteArrayInputStream(response.data);
            ZipInputStream zis = new ZipInputStream(is);
            @SuppressWarnings("unused")
            ZipEntry entry;
            ByteArrayBuffer bab = new ByteArrayBuffer(2048);

            while ((entry = zis.getNextEntry()) != null) {
                int size = 0;
                byte[] buffer = new byte[2048];
                while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
                    bab.append(buffer, 0, size);
                }
                resp = new String(bab.toByteArray());
                jsonArray = new JSONArray(resp);
            }
        } catch (Exception e) {
            VolleyError ve = new VolleyError("Exception parsing server data", e);
            errorListener.onErrorResponse(ve);

            return Response.error(new VolleyError(ve));

        }
        end = System.currentTimeMillis();
        Log.e(LOG, "#### comms elapsed time in seconds: " + getElapsed(start, end));
        return Response.success(jsonArray,
                HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(JSONArray response) {
        end = System.currentTimeMillis();
        listener.onResponse(response);
    }

    public static double getElapsed(long start, long end) {
        BigDecimal m = new BigDecimal(end - start).divide(new BigDecimal(1000));
        return m.doubleValue();
    }

    static final String LOG = "BohaRequest";
}
