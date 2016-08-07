package com.unl.lapc.registrodocente.fragment;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.activity.MainClase;
import com.unl.lapc.registrodocente.dao.AsistenciaDao;
import com.unl.lapc.registrodocente.dao.CalendarioDao;
import com.unl.lapc.registrodocente.dao.ClaseDao;
import com.unl.lapc.registrodocente.dao.EstudianteDao;
import com.unl.lapc.registrodocente.modelo.Asistencia;
import com.unl.lapc.registrodocente.modelo.Calendario;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Estudiante;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class FragmentAsistancias extends Fragment {

    private Clase clase;

    //private ListView mLeadsList;
    private AsistenciaDao dao;
    private ClaseDao daoClase;
    private EstudianteDao daoEstudiante;
    private CalendarioDao daoCalendario;

    //private RegistroAsistenciaAdapter mLeadsAdapter;
    private Date fecha = new Date();
    private TableLayout tlAsistencias;
    private List<Asistencia> asistencias;
    private List<Estudiante> estudiantes;
    private Calendario calendario;

    @Override
    //protected void onCreate(Bundle savedInstanceState) {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_asistencias);

        View view = inflater.inflate(R.layout.fragment_asistencias, container, false);
        setHasOptionsMenu(true);

        dao = new AsistenciaDao(getContext());
        daoClase = new ClaseDao(getContext());
        daoEstudiante = new EstudianteDao(getContext());
        daoCalendario = new CalendarioDao(getContext());

        Bundle bundle = getArguments();
        clase = bundle.getParcelable("clase");

        //mLeadsList = (ListView) findViewById(R.id.listViewAsistencias);
        //mLeadsAdapter = new RegistroAsistenciaAdapter(getApplicationContext(), dao.getEstudiantes(clase));
        //mLeadsList.setAdapter(mLeadsAdapter);
        estudiantes = daoEstudiante.getEstudiantes(clase);

        FloatingActionButton btnAsiToday = (FloatingActionButton) view.findViewById(R.id.btnAsiToday);
        btnAsiToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendario newCal = daoCalendario.get(clase.getPeriodo(), fecha);
                registrar(newCal);
            }
        });

        FloatingActionButton btnAsiNext = (FloatingActionButton) view.findViewById(R.id.btnAsiNext);
        btnAsiNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });

        FloatingActionButton btnAsiPrev = (FloatingActionButton) view.findViewById(R.id.btnAsiPrev);
        btnAsiPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prev();
            }
        });

        tlAsistencias = (TableLayout) view.findViewById(R.id.tlAsistencias);

        calendario = daoCalendario.get(clase.getPeriodo(), new Date());
        if (calendario != null) {
            mostrarDia(calendario);
        } else {
            //Snackbar.make(getView(), "Este día no está registrdo en el calendario académico", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            getActivity().setTitle("Asistencias: " + clase.getNombre());
        }

        return view;
    }

    @Override
    //public boolean onCreateOptionsMenu(Menu menu) {
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_asistencias, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_periodo_clase) {
            showDialogToday();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    DatePicker dp;

    private void showDialogToday(){
        View myView = View.inflate(getContext(), R.layout.content_dlg_periodo_clase, null);

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setView(myView);
        builder.setTitle("Asistencia del día");
        builder.setCancelable(false);

        Calendar c = GregorianCalendar.getInstance();
        c.setTime(fecha);

        dp = (DatePicker)myView.findViewById(R.id.dpFechaPeriodoClase);
        dp.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        builder.setPositiveButton("Registrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                GregorianCalendar calendarBeg=new GregorianCalendar(dp.getYear(),dp.getMonth(),dp.getDayOfMonth());
                Date fecha=calendarBeg.getTime();
                Calendario newCal = daoCalendario.get(clase.getPeriodo(), fecha);
                if(newCal != null) {
                    registrar(newCal);
                    mostrarDia(newCal);
                    dialog.dismiss();
                }else{
                    Snackbar.make(null, "Este día no está registrdo en el calendario académico", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        builder.setNeutralButton("Borrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                GregorianCalendar calendarBeg=new GregorianCalendar(dp.getYear(),dp.getMonth(),dp.getDayOfMonth());
                Date fecha=calendarBeg.getTime();
                Calendario newCal = daoCalendario.get(clase.getPeriodo(), fecha);
                if(newCal != null) {
                    dao.borrarAsistencias(clase, fecha);
                    mostrarDia(newCal);
                    dialog.dismiss();
                }
            }
        });

        builder.setNegativeButton("Cerrar",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.dismiss();
            }
        });

        AlertDialog alert=builder.create();
        alert.show();
    }

    private void registrar(Calendario calendario){
        if(calendario != null) {
            if(calendario.getEstado().equals(Calendario.ESTADO_ACTIVO)){

                asistencias = dao.getAsistencias(clase, fecha);
                boolean any = asistencias.size() > 0;

                for(Estudiante c : estudiantes) {
                    Asistencia asi = null;

                    for (Asistencia ra : asistencias) {
                        if (ra.getEstudiante().getId() == c.getId()) {
                            asi = ra;
                        }
                    }

                    if (asi == null) {
                        asi = new Asistencia(0, fecha, c.getClase(), c, calendario);
                        asi.setEstado(any ? "F" : "P");
                        dao.add(asi);
                    }
                }

                mostrarDia(calendario);
            }else{
                Snackbar.make(tlAsistencias, "Este día no está registrdo en el calendario académico como feriado", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }else{
            Snackbar.make(tlAsistencias, "Este día no está registrdo en el calendario académico", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private void mostrarDia(Calendario calendario){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        this.fecha = calendario.getFecha();

        ((MainClase)getActivity()).getSupportActionBar().setTitle(clase.getNombre() + ": Asistencias (" + sd.format(fecha)+ " - " + calendario.getEstado() + ")");

        tlAsistencias.removeAllViews();
        asistencias = dao.getAsistencias(clase, fecha);

        int i = 0;
        for(Estudiante c : estudiantes){
            i++;
            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            Asistencia asi = null;

            for (Asistencia ra : asistencias){
                if(ra.getEstudiante().getId() == c.getId()){
                    asi = ra;
                }
            }

            TextView tv = new TextView(getContext());
            tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(18);
            tv.setPadding(0, 5, 0, 5);
            tv.setText(c.getOrden() + ". ");
            row.addView(tv);

            TextView tv1 = new TextView(getContext());
            tv1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            tv1.setGravity(Gravity.LEFT);
            tv1.setTextSize(18);
            tv1.setPadding(0, 5, 0, 5);
            tv1.setText(c.getNombresCompletos());
            row.addView(tv1);

            if(asi!=null) {

                RadioButton rb1 = new RadioButton(getContext());
                rb1.setText("P");
                //row.addView(rb1);

                RadioButton rb2 = new RadioButton(getContext());
                rb2.setText("F");
                //row.addView(rb2);


                RadioButton rb3 = new RadioButton(getContext());
                rb3.setText("J");
                //row.addView(rb3);

                RadioGroup rg = new RadioGroup(getContext());
                rg.setTag(asi);
                rg.setOrientation(LinearLayout.HORIZONTAL);
                rg.addView(rb1);
                rg.addView(rb2);
                rg.addView(rb3);

                if (asi.getEstado().equals("P")) {
                    rg.check(rb1.getId());
                } else if (asi.getEstado().equals("F")) {
                    rg.check(rb2.getId());
                } else {
                    rg.check(rb3.getId());
                }

                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        RadioButton rb = (RadioButton) radioGroup.findViewById(i);
                        Asistencia as = (Asistencia) radioGroup.getTag();
                        as.setEstado(rb.getText().toString());
                        dao.update(as);
                    }
                });


                row.addView(rg);
            }

            tlAsistencias.addView(row);
        }

        //Snackbar.make(this.getCurrentFocus(), "Fecha seleccionada: " , Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    private void next(){
        //Calendar c = GregorianCalendar.getInstance();
        //c.setTime(fecha);
        //c.add(Calendar.DAY_OF_MONTH, 1);

        Calendario newcal = daoCalendario.getNext(clase.getPeriodo(), fecha);

        if(newcal != null) {
            mostrarDia(newcal);
        }
    }

    private void prev(){
        //Calendar c = GregorianCalendar.getInstance();
        //c.setTime(fecha);
        //c.add(Calendar.DAY_OF_MONTH, -1);

        Calendario newcal = daoCalendario.getPrevius(clase.getPeriodo(), fecha);
        if(newcal != null) {
            mostrarDia(newcal);
        }
    }

    /*
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainClaseActivity.class);
        intent.putExtra("clase", clase);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }*/
}
