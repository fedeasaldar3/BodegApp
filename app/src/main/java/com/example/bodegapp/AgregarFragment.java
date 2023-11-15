package com.example.bodegapp;


import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bodegapp.Objetos.FirebaseReferences;
import com.example.bodegapp.Objetos.Producto;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class AgregarFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    EditText campo_bodega, campo_referencia, campo_piso, campo_columna, campo_fila;
    TextView titleFragment;
    Button btn_cancelar, btn_enviar;
    Spinner spinner;
    String parte, uid2;
    Boolean edit;
    Integer opcion;
    String opcion2, parte2;

    CoordinatorLayout fondoLayout;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseReferences;

    public AgregarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_agregar, container, false);

        campo_bodega = (EditText)v.findViewById(R.id.campo_bodega);
        campo_referencia = (EditText)v.findViewById(R.id.campo_referencia);
        campo_piso = (EditText)v.findViewById(R.id.campo_piso);
        campo_columna = (EditText)v.findViewById(R.id.campo_columna);
        campo_fila = (EditText)v.findViewById(R.id.campo_fila);
        btn_cancelar = (Button) v.findViewById(R.id.btn_cancelar);
        btn_enviar = (Button) v.findViewById(R.id.btn_enviar);
        spinner = (Spinner) v.findViewById(R.id.spinner2);
        titleFragment = (TextView) v.findViewById(R.id.titleFragment);

        fondoLayout = (CoordinatorLayout) v.findViewById(R.id.fondoLayout);

        if (getArguments() != null){
            opcion2 = getArguments().getString("opcion").toString();
            edit = getArguments().getBoolean("edit");
            if (edit){
                opcion = 1;
                String referencia2, bodega2, piso2, cama2, columna2, fila2;
                uid2 = getArguments().getString("uid");
                referencia2 = getArguments().getString("referencia");
                bodega2 = getArguments().getString("bodega");
                piso2 = getArguments().getString("piso");
                columna2 = getArguments().getString("columna");
                fila2 = getArguments().getString("fila");
                parte2 = getArguments().getString("parte").toString();

                //Toast.makeText(getContext(),"parte: " + parte2, Toast.LENGTH_SHORT).show();

                titleFragment.setText("Editar Producto");
                campo_referencia.setText(referencia2);
                campo_bodega.setText(bodega2);
                campo_columna.setText(columna2);
                campo_fila.setText(fila2);
                campo_piso.setText(piso2);
                //Toast.makeText(getContext(),"parte2: " + parte2, Toast.LENGTH_SHORT).show();

            } else opcion = 0;
        }

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getContext(), R.array.partes, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(this);

        if (edit){
            if (parte2.equals("Cama")||parte2.equals("Estiba")){
                if (parte2.equals("Cama")){
                    spinner.setSelection(1);
                }
                else if (parte2.equals("Estiba")){
                    spinner.setSelection(2);
                }
            }
        }

        iniciarFirebaseFragment();

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviardatos();
            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(AgregarFragment.this).commit();
                FloatingActionButton floatingActionButton = (FloatingActionButton)getActivity().findViewById(R.id.fab);
                floatingActionButton.show();
                ((RecyclerActivity)getActivity()).iniciarFirebase(opcion2);
            }
        });

        fondoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    private void enviardatos() {
        String refecencia = campo_referencia.getText().toString();
        String lugar = "B" + campo_bodega.getText().toString();
        //int piso = Integer.parseInt(campo_piso.getText().toString());
        //String cama = campo_columna.getText().toString() + campo_fila.getText().toString();
        String piso2 = campo_piso.getText().toString();
        String columna2 = campo_columna.getText().toString();
        String fila2 = campo_fila.getText().toString();

        if (refecencia.equals("")||lugar.equals("B")||piso2.equals("")||columna2.equals("")||fila2.equals("")||parte.equals("Opcion")){
            validacion(refecencia, lugar, piso2, columna2, fila2, parte);
        } else {

            int piso = Integer.parseInt(piso2);
            String cama = columna2+fila2;
            Producto p = new Producto();

            p.setReferencia(refecencia);
            p.setLugar(lugar);
            p.setPiso(piso);
            p.setCama(cama);
            p.setParte(parte);

            if (opcion == 0) {
                p.setUid(UUID.randomUUID().toString());
                firebaseReferences.child(FirebaseReferences.PRODUCTO_REFERENCE)
                        .child(opcion2)
                        .child(p.getUid())
                        .setValue(p);

                Toast.makeText(getContext(), "Agregado", Toast.LENGTH_SHORT).show();
                limpiarCajas();
            } else if (opcion == 1){
                p.setUid(uid2);
                firebaseReferences.child(FirebaseReferences.PRODUCTO_REFERENCE)
                        .child(opcion2)
                        .child(p.getUid())
                        .setValue(p);

                Toast.makeText(getContext(), "Actualizado", Toast.LENGTH_SHORT).show();

                getFragmentManager().beginTransaction().remove(AgregarFragment.this).commit();
                FloatingActionButton floatingActionButton = (FloatingActionButton)getActivity().findViewById(R.id.fab);
                floatingActionButton.show();
                ((RecyclerActivity)getActivity()).iniciarFirebase(opcion2);
            }
        }
    }

    private void limpiarCajas() {
        campo_referencia.setText("");
        /*campo_piso.setText("");
        campo_bodega.setText("");
        campo_columna.setText("");
        campo_fila.setText("");*/
    }

    private void validacion(String refecencia, String lugar, String piso2, String columna2, String fila2, String parte2) {
        if (refecencia.equals("")){
            campo_referencia.setError("Requerido");
        }
        else if (lugar.equals("B")){
            campo_bodega.setError("Requerido");
        }
        else if (piso2.equals("")){
            campo_piso.setError("Requerido");
        }
        else if (columna2.equals("")){
            campo_columna.setError("Requerido");
        }
        else if (fila2.equals("")){
            campo_fila.setError("Requerido");
        }
        else if (parte2.equals("Opcion")){
            Toast.makeText(getContext(),"Debes elegir una opción", Toast.LENGTH_SHORT).show();
        }
    }

    private void iniciarFirebaseFragment() {
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseReferences = firebaseDatabase.getReference();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parte = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
