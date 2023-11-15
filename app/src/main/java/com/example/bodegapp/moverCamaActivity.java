package com.example.bodegapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class moverCamaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText campo_bodega, campo_referencia, campo_piso, campo_columna, campo_fila;
    TextView titleFragment;
    Button btn_cancelar, btn_enviar;
    Spinner spinner;
    String moverBodega, moverPiso, moverFila, moverColumna, moverParte;
    String parteActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mover_cama);

        campo_bodega = (EditText) findViewById(R.id.campo_bodega);
        campo_referencia = (EditText) findViewById(R.id.campo_referencia);
        campo_piso = (EditText) findViewById(R.id.campo_piso);
        campo_columna = (EditText) findViewById(R.id.campo_columna);
        campo_fila = (EditText) findViewById(R.id.campo_fila);
        btn_cancelar = (Button) findViewById(R.id.btn_cancelar);
        btn_enviar = (Button) findViewById(R.id.btn_enviar);
        spinner = (Spinner) findViewById(R.id.spinner2);
        titleFragment = (TextView) findViewById(R.id.titleFragment);

        iniciarSpinner();
        obtenerDatos();

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);

                // Finalizamos la Activity para volver a la anterior
                finish();
            }
        });

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moverBodega = campo_bodega.getText().toString();
                moverPiso = campo_piso.getText().toString();
                moverFila = campo_fila.getText().toString();
                moverColumna = campo_columna.getText().toString();
                if (moverBodega.equals("") || moverPiso.equals("") || moverFila.equals("") || moverColumna.equals("")){
                    Toast.makeText(getApplicationContext(),"Campos Resqueridos", Toast.LENGTH_SHORT).show();
                }
                else {
                    moverBodega = "B" + moverBodega;
                    String moverCama = moverColumna + moverFila;
                    if (spinner.getSelectedItemPosition() == 0) moverParte = "Cama";
                    else moverParte = "Estiba";

                    Intent i = getIntent();
                    i.putExtra("resBodega", moverBodega);
                    i.putExtra("resPiso", moverPiso);
                    i.putExtra("resCama", moverCama);
                    i.putExtra("resParte", moverParte);
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        });

    }

    private void obtenerDatos() {
        moverBodega = getIntent().getStringExtra("bodega");
        moverPiso = getIntent().getStringExtra("piso");
        moverFila = getIntent().getStringExtra("fila");
        moverColumna = getIntent().getStringExtra("columna");
        moverParte = getIntent().getStringExtra("parte");

        campo_bodega.setText(moverBodega.substring(1));
        campo_piso.setText(moverPiso);
        campo_fila.setText(moverFila);
        campo_columna.setText(moverColumna);

        if (moverParte.equals("Cama")) spinner.setSelection(0);
        else spinner.setSelection(1);
    }

    private void iniciarSpinner() {
        String[] letra = {"Cama", "Estiba"};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, letra));
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parteActual = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
