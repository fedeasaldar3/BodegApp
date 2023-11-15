package com.example.bodegapp;

import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    CardView goLamparas, goTelas, goTapetes, goNuevo;
    //Button buttonProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goLamparas = (CardView) findViewById(R.id.goLamparas);
        goTelas = (CardView) findViewById(R.id.goTelas);
        goTapetes = (CardView) findViewById(R.id.goTapetes);
        goNuevo = (CardView) findViewById(R.id.goNuevo);

        final Intent intent = new Intent(MainActivity.this, RecyclerActivity.class);

        goLamparas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("opcion", "lamparas");
                startActivity(intent);
            }
        });

        goTelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("opcion", "telas");
                startActivity(intent);
            }
        });

        goTapetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("opcion", "tapetes");
                startActivity(intent);
            }
        });

        goNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Función no Habilitada ;)", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
