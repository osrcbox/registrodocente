package com.unl.lapc.registrodocente;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.unl.lapc.registrodocente.dao.DBHandler;
import com.unl.lapc.registrodocente.dao.PeriodoDao;
import com.unl.lapc.registrodocente.modelo.PeriodoAcademico;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditPeriodoActivity extends AppCompatActivity {

    private PeriodoAcademico periodo = null;
    private PeriodoDao dao = null;
    private EditText txtNombreEditPeriodo=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_periodo);

        txtNombreEditPeriodo= (EditText)findViewById(R.id.txtNombreEditPeriodo);

        Bundle bundle = getIntent().getExtras();
        periodo = bundle.getParcelable("periodo");

        setValue();

        dao = new PeriodoDao(getApplicationContext());
    }

    private void setValue() {
        if (periodo != null) {
            txtNombreEditPeriodo.setText(periodo.getNombre());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_edit_periodo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save_periodo) {
            guardarPeriodo();
            return true;
        }

        if (id == R.id.action_delete_periodo) {
            eliminarPeriodo();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void guardarPeriodo(){
        periodo.setNombre(txtNombreEditPeriodo.getText().toString());

        if(validate()) {

            if (periodo.getId() == 0) {
                dao.add(periodo);
            } else {
                dao.update(periodo);
            }

            Intent intent = new Intent(this, PeriodosActivity.class);
            startActivity(intent);
        }
    }

    public void eliminarPeriodo(){
        if(periodo.getId() > 0){
            dao.delete(periodo);

            Intent intent = new Intent(this, PeriodosActivity.class);
            startActivity(intent);
        }else{
            Snackbar.make(getCurrentFocus(), "No se puede eliminar porque aún no ha guardado", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    /*private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }*/

    private boolean validate() {
        String nombre = periodo.getNombre();

        boolean v = true;

        if (!(nombre != null && nombre.trim().length() > 0)) {
            txtNombreEditPeriodo.setError("Ingrese un nombre");
            v = false;
        }else{
            boolean e = dao.existe(periodo);
            if(e){
                txtNombreEditPeriodo.setError("Nombre duplicado");
                v = false;
            }
        }

        return v;
    }
}
