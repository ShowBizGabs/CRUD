package app.crud;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

import app.crud.helper.Create;

public class CreateActivity extends AppCompatActivity {

    ReadActivity Re = new ReadActivity();

    private EditText edit_nim, edit_nama;
    public static EditText edit_alamat;
    private TextView text_tgllahir;
    public static TextView text_prodi;

    // Calender
    private Calendar cal;
    private int day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        edit_nim        = (EditText) findViewById(R.id.edit_nim);
        edit_nama       = (EditText) findViewById(R.id.edit_nama);
        text_tgllahir   = (TextView) findViewById(R.id.text_tgllahir);
        text_prodi      = (TextView) findViewById(R.id.text_prodi);
        edit_alamat     = (EditText) findViewById(R.id.edit_alamat);

        //        C A L E N D E R
        cal     = Calendar.getInstance();
        day     = cal.get(Calendar.DAY_OF_MONTH);
        month   = cal.get(Calendar.MONTH);
        year    = cal.get(Calendar.YEAR);

        text_tgllahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { showDialog(0); }
        });

        text_prodi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Re.STATUS_PRODI = "create";
                startActivity(new Intent(CreateActivity.this, ProdiActivity.class));
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
                Create();
            }
        });
    }

    private void Create(){
        if (edit_nim.getText().toString().equals("") || edit_nama.getText().toString().equals("") ||
                text_tgllahir.getText().toString().equals("Tanggal lahir") || text_prodi.getText().toString().equals("Pilih prodi") ||
                edit_alamat.getText().toString().equals("")){
            Snackbar.make(edit_nim, "Debes rellenar todos los campos", Snackbar.LENGTH_LONG).show();
        } else {
            new Create(this).execute(
                    edit_nim.getText().toString(), edit_nama.getText().toString(), edit_alamat.getText().toString()
            );
        }
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, datePickerListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            NumberFormat f = new DecimalFormat("00");
            text_tgllahir.setText(f.format(selectedDay) + "/" + (f.format(selectedMonth + 1)) + "/" + selectedYear );
            Re.TGL_LAHIR = selectedYear + "-" + (f.format(selectedMonth + 1)) + "-" + f.format(selectedDay);
            Log.d("Fecha de nacimiento", Re.TGL_LAHIR);
        }
    };

}
