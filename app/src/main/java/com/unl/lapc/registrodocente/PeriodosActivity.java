package com.unl.lapc.registrodocente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.unl.lapc.registrodocente.adapter.PeriodosAdapter;
import com.unl.lapc.registrodocente.dao.PeriodoDao;
import com.unl.lapc.registrodocente.modelo.PeriodoAcademico;

public class PeriodosActivity extends AppCompatActivity {

    private ListView mLeadsList;
    private PeriodoDao dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periodos);

        mLeadsList = (ListView) findViewById(R.id.listView);

        dao = new PeriodoDao(getApplicationContext());

        // Inicializar el adaptador con la fuente de datos.
        PeriodosAdapter mLeadsAdapter = new PeriodosAdapter(getApplicationContext(), dao.getAll());

        //Relacionando la lista con el adaptador
        mLeadsList.setAdapter(mLeadsAdapter);

        mLeadsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PeriodoAcademico per = (PeriodoAcademico) mLeadsList.getItemAtPosition(i);
                if(per != null){
                    editAction(per);
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_periodos, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_periodo) {
            editAction(new PeriodoAcademico());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void editAction(PeriodoAcademico per){
        Intent intent = new Intent(this, EditPeriodoActivity.class);
        intent.putExtra("periodo", per);
        startActivity(intent);
    }
}
