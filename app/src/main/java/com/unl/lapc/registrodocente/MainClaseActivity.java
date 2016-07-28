package com.unl.lapc.registrodocente;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.unl.lapc.registrodocente.adapter.ClaseEstudianteAdapter;
import com.unl.lapc.registrodocente.adapter.ClasesAdapter;
import com.unl.lapc.registrodocente.dao.ClaseDao;
import com.unl.lapc.registrodocente.dao.EstudianteDao;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.ClaseEstudiante;
import com.unl.lapc.registrodocente.modelo.Estudiante;

public class MainClaseActivity extends AppCompatActivity {

    private ListView mLeadsList;
    private ClaseDao dao;
    private EstudianteDao daoEstudiante;
    private Clase clase;
    private ClaseEstudianteAdapter mLeadsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_clase);

        mLeadsList = (ListView) findViewById(R.id.listView);

        dao = new ClaseDao(getApplicationContext());
        daoEstudiante = new EstudianteDao(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        clase = bundle.getParcelable("clase");
        if(clase.getId() > 0){
            clase = dao.getClase(clase.getId());
        }

        mLeadsAdapter = new ClaseEstudianteAdapter(getApplicationContext(), dao.getEstudiantes(clase));
        mLeadsList.setAdapter(mLeadsAdapter);
        mLeadsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClaseEstudiante cls = (ClaseEstudiante) mLeadsList.getItemAtPosition(i);
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
                editEstudiante(new ClaseEstudiante(clase, new Estudiante()));

            }
        });
    }

    private void editEstudiante(ClaseEstudiante claseEstudiante){
        Intent intent = new Intent(this, EditEstudianteActivity.class);

        intent.putExtra("clase", clase);
        intent.putExtra("claseEstudiante", claseEstudiante);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_clase, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_ord_apellido) {
            dao.ordernarApellidos(clase);
            mLeadsAdapter = new ClaseEstudianteAdapter(getApplicationContext(), dao.getEstudiantes(clase));
            mLeadsList.setAdapter(mLeadsAdapter);

            mLeadsAdapter.notifyDataSetChanged();
            mLeadsList.invalidateViews();
            return true;
        }

        if (id == R.id.action_asistencias) {
            Intent intent = new Intent(this, AsistanciasActivity.class);
            intent.putExtra("clase", clase);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
