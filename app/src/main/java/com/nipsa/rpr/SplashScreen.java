package com.nipsa.rpr;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    // Se establece la duración de la pantalla
    private static final long SPLASH_SCREEN_DELAY = 500;
    private int SOLICITUD_PERMISO_WRITE_EXTERNAL_STORAGE = 0;
    public static final String DIRECTORIO_ENTRADA = "/RPR/INPUT/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // Si no se ha leido por primera vez la BDD de los defectos, se lee (sólo se leerá la primera vez que se abre la app)
        String nombreArchivoBD = "/data/data/com.nipsa.rpr/databases/" + DBGlobal.DATABASE_NAME;
        File archivoBD = new File(nombreArchivoBD);
        if(!archivoBD.exists()) {
            Aplicacion.lecturaBDDefectos();
        }
        // Si no se ha creado el arbol de directorios de entrada, se crea
        String directorioEntrada = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                        + DIRECTORIO_ENTRADA;
        File fEntrada = new File(directorioEntrada);
        if (!fEntrada.exists()) {
            fEntrada.mkdirs();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Se define la tarea que se ejecutará tras el paso del tiempo establecido en SPLASH_SCREEN_DELAY
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //Comprobamos que el usuario ha concedido los permisos necesarios
                comprobarPermisosEscritura();

                // Se llama a la actividad MostrarRevisiones
                Intent intent = new Intent(SplashScreen.this, MostrarRevisiones.class);
                startActivity(intent);

                // Se cierra la actividad para que no pueda ser llamada con el boton BACK
                finish();
            }
        };

        // Se define un Timer que ejecutará el retardo
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);

    }

    public void comprobarPermisosEscritura() {
        // Se comprueba si hay permiso para escribir datos en el terminal
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
        } else { // Si no hay permiso se solicita
            solicitarPermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Sin el permiso"+
                            " la aplicación no podrá almacenar los datos recogidos y " +
                            "no funcionará correctamente.",
                    SOLICITUD_PERMISO_WRITE_EXTERNAL_STORAGE, this);
        }

    }

    /*
     * Si no hay permiso para escribir datos en el terminal se solicita permiso al usuario
     */
    public static void solicitarPermiso(final String permiso, String justificacion,
                                        final int requestCode, final Activity actividad) {
        // Recordará la decision del usuario si ha marcado la opción de no vovler a mostrar el mensaje
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad,
                permiso)){
            // Muestra un mensaje del motivo por el que se solicita el permiso
            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ActivityCompat.requestPermissions(actividad,
                                    new String[]{permiso}, requestCode);
                        }})
                    .show();
        } else { // Solicita el permiso
            ActivityCompat.requestPermissions(actividad,
                    new String[]{permiso}, requestCode);
        }
    }

    /*
     * Se recoge la respuesta del usuario
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        // Si el usuario concede el permiso se continúa con la actividad principal
        if (requestCode == SOLICITUD_PERMISO_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length== 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else { // Si el usuario no concede el permiso se le avisa de que la aplicación fallará
                Toast.makeText(this, "Sin el permiso la aplicación puede fallar",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}
