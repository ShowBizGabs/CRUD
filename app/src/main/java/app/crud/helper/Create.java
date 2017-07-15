package app.crud.helper;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import app.crud.CreateActivity;
import app.crud.ReadActivity;
import app.crud.UpdateActivity;
import app.crud.helper.Http;


public class Create extends AsyncTask<String, Void, String> {

    ReadActivity R = new ReadActivity();

    private Context context;
    private String link = Http.server;
    public Create (Context context) {
        this.context = context;
    }

    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... arg0) {
        String nim          = arg0[0];
        String nama         = arg0[1];
        String alamat       = arg0[2];

        String data;
        BufferedReader bufferedReader;
        String result;
        try {
            data = "?componente=" + URLEncoder.encode(nim, "UTF-8");
            data += "&status=" + URLEncoder.encode(nama, "UTF-8");
            data += "&fecha_inicio=" + URLEncoder.encode(R.TGL_LAHIR, "UTF-8");
            data += "&prodi_id=" + URLEncoder.encode(R.PRODI_ID, "UTF-8");
            data += "&observaciones=" + URLEncoder.encode(alamat, "UTF-8");

                link = link + "create.php" + data;

            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            result = bufferedReader.readLine();
            return result;
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        String jsonStr = result;
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                String query_result = jsonObj.getString("result");
                Toast.makeText(context, query_result, Toast.LENGTH_SHORT).show();
                if (query_result.equals("exito")) {
                    Toast.makeText(context, "Reporte guardado correctamente", Toast.LENGTH_SHORT).show();
                    ((Activity)(context)).finish();
                } else if (query_result.equals("fallo")) {
                    Toast.makeText(context, query_result, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, query_result, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error parsing JSON data.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
        }
    }

}
