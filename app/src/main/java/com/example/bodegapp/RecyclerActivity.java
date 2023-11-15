package com.example.bodegapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Vibrator;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bodegapp.Objetos.Adapter;
import com.example.bodegapp.Objetos.FirebaseReferences;
import com.example.bodegapp.Objetos.Producto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Vibrator vibrator;

    //FirebaseDatabase firebaseDatabase;
    //DatabaseReference firebaseReferences;

    RecyclerView rv;
    List<Producto> productos;
    Adapter adapter;
    FloatingActionButton fab;
    CoordinatorLayout lay_fragment;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference firebaseReferences;
    //DatabaseReference firebaseReferences = firebaseDatabase.getReference();
    ArrayList<Producto> filteredList;
    int pesoFilteredlist = 0;

    static boolean calledAlready = false;
    Producto producto;
    //ImageView bt_barcode;
    EditText et_buscar;

    int filtroCama = 0;
    int numeroCama = 0;

    String camaActual, pisoActual, bodegaActual, parteActual;
    ImageView bt_filtro;
    LinearLayout cuadroFiltro;
    EditText filtroSupBodega, filtroSupPiso, filtroSupColumna, filtroSupFila;
    Spinner spinnerFiltro;

    //Mover Cama
    Button moverCama;
    //Después de Result
    String opcion2Global, textCamaGlobal, textPisoGlobal, textBodegaGlobal, textParteGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Obtener opción
        Intent intent = getIntent();
        final String opcion2 = intent.getStringExtra("opcion");
        opcion2Global = opcion2;
        //Toast.makeText(getApplicationContext(), opcion2, Toast.LENGTH_SHORT).show();

        //bt_barcode = (ImageView) findViewById(R.id.bt_barcode);
        et_buscar = (EditText) findViewById(R.id.et_buscar);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        lay_fragment = (CoordinatorLayout) findViewById(R.id.lay_fragment);

        rv = (RecyclerView) findViewById(R.id.recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));

        bt_filtro = (ImageView) findViewById(R.id.bt_filtro);
        cuadroFiltro = (LinearLayout) findViewById(R.id.cuadroFiltro);
        filtroSupBodega = (EditText) findViewById(R.id.filtroSupBodega);
        filtroSupPiso = (EditText) findViewById(R.id.filtroSupPiso);
        filtroSupColumna = (EditText) findViewById(R.id.filtroSupColumna);
        filtroSupFila = (EditText) findViewById(R.id.filtroSupFila);
        spinnerFiltro = (Spinner) findViewById(R.id.spinnerFiltro);

        moverCama = (Button) findViewById(R.id.moverCama);

        productos = new ArrayList<>();

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                Bundle bundle = new Bundle();
                bundle.putBoolean("edit", false);
                bundle.putString("opcion", opcion2);
                AgregarFragment addFragment = new AgregarFragment();
                addFragment.setArguments(bundle);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.lay_fragment, addFragment).commit();
                fab.hide();
            }
        });

        if (!calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }
        DatabaseReference firebaseReferences = firebaseDatabase.getReference();
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //iniciarFirebase1();
        iniciarFirebase(opcion2);
        iniciarBucador();

        //Spinner de filtro
        /*ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getContext(), R.array.partes, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltro.setAdapter(adapterSpinner);
        spinnerFiltro.setOnItemSelectedListener();*/

        String[] letra = {"Cama", "Estiba"};
        spinnerFiltro.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, letra));
        spinnerFiltro.setOnItemSelectedListener(this);

    }

    /*public void iniciarFirebase1(){
        FirebaseApp.initializeApp(getApplicationContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseReferences = firebaseDatabase.getReference();
    }*/

    public void iniciarFirebase(final String opcion2) {
        adapter = new Adapter(productos);
        rv.setAdapter(adapter);

        firebaseDatabase.getReference().child("productos").child(opcion2)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        productos.removeAll(productos);
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            producto = snapshot.getValue(Producto.class);
                            productos.add(producto);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        //productos.size();
        Toast.makeText(getApplicationContext(), "elementos: " + productos.size(), Toast.LENGTH_SHORT).show();

        adapter.setOnClickListener(new Adapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                ArrayList<Producto> arrAux;
                if (filteredList != null) arrAux = filteredList;
                else arrAux = (ArrayList<Producto>) productos;

                arrAux.get(position);
                //Toast.makeText(getApplicationContext(),"Ref: " + productos.get(position).getReferencia().toString(), Toast.LENGTH_SHORT).show();
                if (filtroCama == 0) {
                    filtroCama = 1;
                    Toast.makeText(getApplicationContext(), "Filtro Activo", Toast.LENGTH_SHORT).show();
                    camaActual = arrAux.get(position).getCama();
                    pisoActual = String.valueOf(arrAux.get(position).getPiso());
                    bodegaActual = arrAux.get(position).getLugar();
                    filtroSupBodega.setText(String.valueOf(bodegaActual.charAt(1)));
                    filtroSupPiso.setText(pisoActual);
                    filtroSupColumna.setText(String.valueOf(camaActual.charAt(0)));
                    filtroSupFila.setText(String.valueOf(camaActual.charAt(1)));
                    parteActual = arrAux.get(position).getParte();
                    if (parteActual.equals("Cama")) spinnerFiltro.setSelection(0);
                    else spinnerFiltro.setSelection(1);
                    cuadroFiltro.setVisibility(View.VISIBLE);
                    filter("", camaActual, pisoActual, bodegaActual, parteActual);
                } else {
                    filtroCama = 0;
                    Toast.makeText(getApplicationContext(), "Busqueda por Referencia", Toast.LENGTH_SHORT).show();
                    filtroSupBodega.setText("");
                    filtroSupPiso.setText("");
                    filtroSupColumna.setText("");
                    filtroSupFila.setText("");
                    cuadroFiltro.setVisibility(View.GONE);
                    filter("", "", "", "", "");
                }
                numeroCama = position;
                vibrator.vibrate(100);
            }

            @Override
            public void onDeleteClick(final int position) {
                vibrator.vibrate(200);

                AlertDialog.Builder alerta = new AlertDialog.Builder(RecyclerActivity.this);
                alerta.setMessage("Estas seguro de eliminar este elemento?")
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Producto p = new Producto();
                                //productos.get(position).getUid();
                                //producto.getUid();
                                //p.setUid(productos.get(UUID.randomUUID().toString()));
                                //Toast.makeText(getApplicationContext(), "Hola", Toast.LENGTH_SHORT).show();
                                if (filteredList == null) {
                                    firebaseDatabase.getReference().child(FirebaseReferences.PRODUCTO_REFERENCE)
                                            .child(opcion2)
                                            .child(productos.get(position).getUid()).removeValue();
                                } else {
                                    firebaseDatabase.getReference().child(FirebaseReferences.PRODUCTO_REFERENCE)
                                            .child(opcion2)
                                            .child(filteredList.get(position).getUid()).removeValue();
                                }

                                adapter.notifyItemRemoved(position);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("ELIMINAR");
                titulo.show();

            }

            @Override
            public void onEditClick(int position) {
                String uid2, referencia2, bodega2, piso2, cama2, columna2, fila2, parte2;
                if (filteredList == null) {
                    uid2 = productos.get(position).getUid();
                    referencia2 = productos.get(position).getReferencia();
                    bodega2 = productos.get(position).getLugar();
                    bodega2 = bodega2.substring(1);
                    piso2 = String.valueOf(productos.get(position).getPiso());
                    cama2 = productos.get(position).getCama();
                    columna2 = cama2.substring(0, 1);
                    fila2 = cama2.substring(1);
                    parte2 = productos.get(position).getParte();
                } else {
                    uid2 = filteredList.get(position).getUid();
                    referencia2 = filteredList.get(position).getReferencia();
                    bodega2 = filteredList.get(position).getLugar();
                    bodega2 = bodega2.substring(1);
                    piso2 = String.valueOf(filteredList.get(position).getPiso());
                    cama2 = filteredList.get(position).getCama();
                    columna2 = cama2.substring(0, 1);
                    fila2 = cama2.substring(1);
                    parte2 = filteredList.get(position).getParte();
                }

                AgregarFragment agregarFragment = new AgregarFragment();

                Bundle bundle = new Bundle();
                bundle.putBoolean("edit", true);
                bundle.putString("uid", uid2);
                bundle.putString("referencia", referencia2);
                bundle.putString("bodega", bodega2);
                bundle.putString("piso", piso2);
                bundle.putString("columna", columna2);
                bundle.putString("fila", fila2);
                bundle.putString("parte", parte2);
                bundle.putString("opcion", opcion2);

                agregarFragment.setArguments(bundle);

                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.lay_fragment, agregarFragment).commit();
                fab.hide();
            }


        });

    }

    private void iniciarBucador() {

        et_buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (filtroCama == 0) {
                    filter(s.toString(), "", "", "", "");
                } else if (filtroCama == 1) {
                    filter(s.toString(), camaActual, pisoActual, bodegaActual, parteActual);
                }
            }
        });
    }

    private void filter(String text, String textCama, String textPiso, String textBodega, String textParte) {
        filteredList = new ArrayList<>();

        if (filtroCama == 0) {
            for (Producto item : productos) {
                if (item.getReferencia().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        } else if (filtroCama == 1) {
            for (Producto item : productos) {
                if (item.getCama().contains(textCama)
                        && String.valueOf(item.getPiso()).contains(textPiso)
                        && item.getLugar().contains(textBodega)
                        && item.getReferencia().toLowerCase().contains(text.toLowerCase())
                        && item.getParte().contains(textParte)) {
                    filteredList.add(item);
                }
            }
            textCamaGlobal = textCama;
            textPisoGlobal = textPiso;
            textBodegaGlobal = textBodega;
            textParteGlobal = textParte;
        }
        adapter.filterList(filteredList);
    }

    public void openCodeLecture(View view) {
        Intent intent = new Intent(getApplicationContext(), BarCodeActivity.class);
        startActivityForResult(intent, 1);
        //startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            // Si es así mostramos mensaje de cancelado por pantalla.
            Toast.makeText(this, "Resultado cancelado", Toast.LENGTH_SHORT)
                    .show();
        } else {
            if ((requestCode == 1) && (resultCode == RESULT_OK)) {
                String valorCodigo = data.getStringExtra("Data");
                et_buscar.setText(valorCodigo);
            }
            if ((requestCode == 0) && (resultCode == RESULT_OK)) {

                String resBodega, resPiso, resCama, resParte;
                resBodega = data.getExtras().getString("resBodega");
                resPiso = data.getExtras().getString("resPiso");
                resCama = data.getExtras().getString("resCama");
                resParte = data.getExtras().getString("resParte");

                int piso=0;
                piso = Integer.parseInt(resPiso);
                Producto p = new Producto();
                p.setLugar(resBodega);
                p.setPiso(piso);
                p.setCama(resCama);
                p.setParte(resParte);

                for (int i = 0; i<pesoFilteredlist; i++){
                    p.setUid(filteredList.get(i).getUid());
                    p.setReferencia(filteredList.get(i).getReferencia());
                    firebaseDatabase.getReference().child(FirebaseReferences.PRODUCTO_REFERENCE)
                            .child(opcion2Global)
                            .child(filteredList.get(i).getUid()).setValue(p);
                }

            }
        }
    }

    public void abrirCuadroFiltro(View view) {
        if (cuadroFiltro.getVisibility() == View.GONE) cuadroFiltro.setVisibility(View.VISIBLE);
        else cuadroFiltro.setVisibility(View.GONE);
    }

    public void abrirBuscarFiltro(View view) {
        String fBodega, fPiso, fColumna, fFila, fCama;
        bodegaActual = "B" + filtroSupBodega.getText().toString();
        pisoActual = filtroSupPiso.getText().toString();
        fColumna = filtroSupColumna.getText().toString();
        fFila = filtroSupFila.getText().toString();
        camaActual = fColumna + fFila;
        filtroCama = 1;
        filter("", camaActual, pisoActual, bodegaActual, parteActual);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parteActual = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void abrirMoverCama(View view) {
        pesoFilteredlist = filteredList.size();
        if (filtroSupBodega.getText().toString().isEmpty() || filtroSupPiso.getText().toString().isEmpty()
                || filtroSupFila.getText().toString().isEmpty() || filtroSupColumna.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Se requieren Campos", Toast.LENGTH_SHORT).show();
        } else {
            String parte2;
            int spinnnerFiltroPosition = spinnerFiltro.getSelectedItemPosition();
            if (spinnnerFiltroPosition == 0) parte2 = "Cama";
            else parte2 = "Estiba";

            //Toast.makeText(getApplicationContext(), "parte: " + parte2, Toast.LENGTH_SHORT).show();
            Intent moverCamaIntent = new Intent(this, moverCamaActivity.class);

            moverCamaIntent.putExtra("bodega", bodegaActual);
            moverCamaIntent.putExtra("piso", pisoActual);
            moverCamaIntent.putExtra("columna", filtroSupColumna.getText().toString());
            moverCamaIntent.putExtra("fila", filtroSupFila.getText().toString());
            moverCamaIntent.putExtra("parte", parte2);

            startActivityForResult(moverCamaIntent, 0);

        }
    }
}