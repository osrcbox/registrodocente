package com.unl.lapc.registrodocente.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.adapter.ClaseEstudianteAdapter;
import com.unl.lapc.registrodocente.dao.ClaseDao;
import com.unl.lapc.registrodocente.dao.EstudianteDao;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Estudiante;

public class MainEstudiantes extends AppCompatActivity {

    private ListView mLeadsList;
    private ClaseDao dao;
    private EstudianteDao daoEstudiante;
    private Clase clase;
    private ClaseEstudianteAdapter mLeadsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_estudiantes);

        mLeadsList = (ListView) findViewById(R.id.listView);

        dao = new ClaseDao(getApplicationContext());
        daoEstudiante = new EstudianteDao(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        clase = bundle.getParcelable("clase");
        if(clase.getId() > 0){
            clase = dao.getClase(clase.getId());
        }

        mLeadsAdapter = new ClaseEstudianteAdapter(getApplicationContext(), daoEstudiante.getEstudiantes(clase));
        mLeadsList.setAdapter(mLeadsAdapter);
        mLeadsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Estudiante cls = (Estudiante) mLeadsList.getItemAtPosition(i);
                if(cls!=null) {
                    editEstudiante(cls);
                }
            }
        });

        setTitle("Estudiantes: " + clase.getNombre());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAddEstudiante);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                editEstudiante(new Estudiante(clase));

            }
        });
    }

    private void editEstudiante(Estudiante estudiante){
        Intent intent = new Intent(this, EditEstudiante.class);

        intent.putExtra("clase", clase);
        intent.putExtra("estudiante", estudiante);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_estudiantes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_ord_apellido) {
            daoEstudiante.ordernarApellidos(clase);
            mLeadsAdapter = new ClaseEstudianteAdapter(getApplicationContext(), daoEstudiante.getEstudiantes(clase));
            mLeadsList.setAdapter(mLeadsAdapter);

            mLeadsAdapter.notifyDataSetChanged();
            mLeadsList.invalidateViews();
            return true;
        }

        if (id == R.id.action_asistencias) {
            Intent intent = new Intent(this, Asistancias.class);
            intent.putExtra("clase", clase);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_notas) {
            Intent intent = new Intent(this, MainNotas.class);
            intent.putExtra("clase", clase);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
