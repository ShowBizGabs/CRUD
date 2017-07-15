package app.crud;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;

import app.crud.helper.Create;
import app.crud.helper.Http;
import app.crud.helper.Update;

public class UpdateActivity extends AppCompatActivity {

    ReadActivity Re = new ReadActivity();

    private String jsonResult;
    private String url = Http.server + "detail.php?id_reporte=" + Re.MAHASISWA_ID;

    private EditText edit_nim, edit_nama, edit_alamat;
    private TextView text_tgllahir;
    public static TextView text_prodi;

    // Calender
    private Calendar cal;
    private int day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        edit_nim        = (EditText) findViewById(R.id.edit_nim);
        edit_nama       = (EditText) findViewById(R.id.edit_nama);
        text_tgllahir   = (TextView) findViewById(R.id.text_tgllahir);
        text_prodi      = (TextView) findViewById(R.id.text_prodi);
        edit_alamat     = (EditText) findViewById(R.id.edit_alamat);

        accessWebService();
        Log.d("URL ", url);

        text_tgllahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(0);
            }
        });

        text_prodi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Re.STATUS_PRODI = "update";
                startActivity(new Intent(UpdateActivity.this, ProdiActivity.class));
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update();
            }
        });
    }

    private void Update(){
        if (edit_nim.getText().toString().equals("") || edit_nama.getText().toString().equals("") ||
                text_tgllahir.getText().toString().equals("Tanggal lahir") || text_prodi.getText().toString().equals("Pilih prodi") ||
                edit_alamat.getText().toString().equals("")){
            Snackbar.make(edit_nim, "Rellena todos los campos", Snackbar.LENGTH_LONG).show();
        } else {
            new Update(this).execute(
                    edit_nim.getText().toString(), edit_nama.getText().toString(), edit_alamat.getText().toString()
            );
        }
    }

    // build hash set for list view
    public void ListDrawer() {

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("result");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode    = jsonMainNode.getJSONObject(i);
                edit_nim.setText( jsonChildNode.optString("componente") );
                edit_nama.setText( jsonChildNode.optString("status") );
                edit_alamat.setText( jsonChildNode.optString("observaciones") );
                text_prodi.setText( jsonChildNode.optString("prodi_nama") );
                Re.PRODI_ID = jsonChildNode.optString("prodi_id");
                text_tgllahir.setText( jsonChildNode.optString("fecha_inicio1") );
                Re.TGL_LAHIR = jsonChildNode.optString("fecha_inicio2");
                day = Integer.parseInt(jsonChildNode.optString("d"));
                month = Integer.parseInt(jsonChildNode.optString("m"));
                year = Integer.parseInt(jsonChildNode.optString("y"));
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }
    // Async Task to access the web
    private class JsonReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String result) {
            ListDrawer();
        }
    }

    private StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        }

        catch (IOException e) {
            // e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Error..." + e.toString(), Toast.LENGTH_LONG).show();
        }
        return answer;
    }

    public void accessWebService() {
        JsonReadTask task = new JsonReadTask();
        // passes values for the urls string array
        task.execute(new String[]{url});
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, datePickerListener, year, (month - 1), day);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            NumberFormat f = new DecimalFormat("00");
            text_tgllahir.setText(f.format(selectedDay) + "/" + (f.format(selectedMonth + 1)) + "/" + selectedYear );
            Re.TGL_LAHIR = selectedYear + "-" + (f.format(selectedMonth + 1)) + "-" + f.format(selectedDay);
        }
    };

}
