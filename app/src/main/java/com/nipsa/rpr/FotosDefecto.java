package com.nipsa.rpr;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class FotosDefecto extends AppCompatActivity {

    public DBRevisiones dbRevisiones;
    private String equipoActual, defectoActual, tramoActual;
    public ImageView ivFoto1, ivFoto2;
    public TextView tvTitulo;
    private int targetW;
    private int targetH;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotos_defecto);

        dbRevisiones = new DBRevisiones(this);
        this.equipoActual = Aplicacion.equipoActual;
        this.defectoActual = Aplicacion.defectoActual;
        this.tramoActual = Aplicacion.tramoActual;
        tvTitulo = (TextView) findViewById(R.id.tvTituloDefecto);
        ivFoto1 = (ImageView) findViewById(R.id.fotoDefecto1);
        ivFoto2 = (ImageView) findViewById(R.id.fotoDefecto2);

        if (hayFoto(1)) {
            tvTitulo.setText(getResources().getString(R.string.titulo_fotos_defecto) +
                    " " + Aplicacion.defectoActual);
        }
        ivFoto1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (hayFoto(1)) {
                    dialogoBorrarFoto(1);
                }
                return false;
            }
        });
        ivFoto2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (hayFoto(2)) {
                    dialogoBorrarFoto(2);
                }
                return false;
            }
        });

    }

    /**
     * Si se pausa la actividad, se cerrará para evitar errores al mostrar las fotos
     */
    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

    /**
     * No se visaulizarán las fotos hasta que no se haya creado la actividad. De lo contrario el ImageView
     * todavía no tiene asignado un tamaño y se obtendrá error
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            visualizarFoto1();
            visualizarFoto2();
        }
    }

    /**
     * Lanza el dialogo para confirmar borrar la foto
     *
     * @param numFoto
     */
    public void dialogoBorrarFoto(final int numFoto) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);

        dialogo.setTitle(R.string.titulo_confirmar_borrar_foto)
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        borrarFoto(numFoto);
                        finish();
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.cancel();
                    }
                });

        dialogo.create().show();

    }

    /**
     * Borrar el archivo de la foto y borrar el nombre del archivo en la BDD. Si la foto que se borra
     * es la número 1, si hay foto 2, la foto 2 pasará a ser la foto 1 en la BDD
     *
     * @param numFoto
     */
    public void borrarFoto (int numFoto) {
        Cursor c;

        if (numFoto == 1) {
            c = dbRevisiones.solicitarFotoDefecto(DBRevisiones.TABLA_DEFECTOS, "Foto1",
                    equipoActual, defectoActual, tramoActual);

            if (c != null) {
                c.moveToFirst();
                String nombreFoto = c.getString(0);
                nombreFoto = nombreFoto.substring(nombreFoto.lastIndexOf("/") + 1);
                dbRevisiones.actualizarItemDefecto(Aplicacion.defectoActual, "Foto1", "");
                borrarArchivoFoto(nombreFoto);

                if (hayFoto(2)) {
                    c = dbRevisiones.solicitarFotoDefecto(DBRevisiones.TABLA_DEFECTOS, "Foto2",
                            equipoActual, defectoActual, tramoActual);

                    if (c != null) {
                        c.moveToFirst();
                        String nombreFoto2 = c.getString(0);
                        nombreFoto2 = nombreFoto2.substring(nombreFoto.lastIndexOf("/") + 1);
                        dbRevisiones.actualizarItemDefecto(Aplicacion.defectoActual,
                                "Foto1", nombreFoto2);
                        dbRevisiones.actualizarItemDefecto(Aplicacion.defectoActual,
                                "Foto2", "");
                    }
                }
                Aplicacion.print("La foto " + nombreFoto + " se ha borrado correctamente");
            }
        } else {
            c = dbRevisiones.solicitarFotoDefecto(DBRevisiones.TABLA_DEFECTOS, "Foto2",
                    equipoActual, defectoActual, tramoActual);

            if (c != null) {
                c.moveToFirst();
                String nombreFoto = c.getString(0);
                nombreFoto = nombreFoto.substring(nombreFoto.lastIndexOf("/") + 1);
                dbRevisiones.actualizarItemDefecto(Aplicacion.defectoActual, "Foto2", "");
                borrarArchivoFoto(nombreFoto);
                Aplicacion.print("La foto " + nombreFoto + " se ha borrado correctamente");

            }

        }
        c.close();

    }

    /**
     * Realiza las operaciones necesarias para visualizar la foto 1
     */
    public void visualizarFoto1 () {
        Cursor c = dbRevisiones.solicitarFotoDefecto(DBRevisiones.TABLA_DEFECTOS, "Foto1",
                equipoActual, defectoActual, tramoActual);

        if (c.getCount() > 0) {
            c.moveToFirst();
            String nombreFoto1 = c.getString(0);

            if (!nombreFoto1.equals("")) {
                ponerFoto(nombreFoto1, ivFoto1);
            }
        }

    }

    /**
     * Realiza las operaciones necesarias para visualizar la foto 2
     */
    public void visualizarFoto2 () {
        Cursor c = dbRevisiones.solicitarFotoDefecto(DBRevisiones.TABLA_DEFECTOS, "Foto2",
                equipoActual, defectoActual, tramoActual);

        if (c.getCount() > 0) {
            c.moveToFirst();
            String nombreFoto2 = c.getString(0);

            if (!nombreFoto2.equals("")) {
                ponerFoto(nombreFoto2, ivFoto2);
            }
        }

    }

    /**
     * Se recupera el archivo que contiene la imagen y se llama al método que asignará la imagen al View
     *
     * @param nombreFoto
     * @param imageView
     */
    public void ponerFoto (String nombreFoto, ImageView imageView) {
        String ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +
                "/" + Aplicacion.revisionActual + "/" + Aplicacion.equipoActual + "/";
        File archivo = Aplicacion.recuperarArchivo(ruta, nombreFoto);

        if (archivo != null) {
            setPic(archivo, imageView);

        }

    }

    /**
     * Comprueba si hay alguna foto almacenada en la BDD (en la BDD sólo se guarda el nombre del archivo)
     *
     * @param numFoto
     * @return
     */
    public boolean hayFoto (int numFoto) {
        boolean hayFoto = false;
        Cursor cursor;

        if (numFoto == 1) {
            cursor = dbRevisiones.solicitarFotoDefecto(DBRevisiones.TABLA_DEFECTOS, "Foto1",
                    equipoActual, defectoActual, tramoActual);
        } else {
            cursor = dbRevisiones.solicitarFotoDefecto(DBRevisiones.TABLA_DEFECTOS, "Foto2",
                    equipoActual, defectoActual, tramoActual);
        }

        cursor.moveToFirst();
        String foto = cursor.getString(0);

        if (!foto.equals("")) {
            hayFoto = true;
        }


        return hayFoto;
    }

    /**
     * Método que establecerá una imagen en el ImageView. Detecta el tamaño del ImageView y de la imagen
     * para escalar la imagen y ajustarla al ImageView. Detecta la orientación de la imagen para rotarla
     * y mostrar la orientación correcta en el ImageView
     *
     * @param archivo que contiene la imagen
     * @param imageView
     */
    private void setPic(File archivo, ImageView imageView) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        int photoW, photoH;
        float rotation = 0f;
        // Obtiene las dimensiones del View
        targetW = imageView.getWidth();
        targetH = imageView.getHeight();

        // Obtiene la orientación del bitmap (imagen) y la rota si es necesario
        try {
            // Se obtiene la orientación
            ExifInterface exif = new ExifInterface(archivo.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            // Se asigna la orientación a la imagen
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotation = 90f;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotation = 180f;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotation = 270f;
                    break;
                default:
                    break;
            }
        } catch (Exception e) {

        }
        // Se rota la imagen
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);

        // Get the dimensions of the bitmap
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(archivo.getAbsolutePath(), bmOptions);
        photoW = bmOptions.outWidth;
        photoH = bmOptions.outHeight;

        try {
            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmapOriginal = BitmapFactory.decodeFile(archivo.getAbsolutePath(), bmOptions);
            Bitmap bitmapRotada = Bitmap.createBitmap(bitmapOriginal, 0, 0, bitmapOriginal.getWidth(),
                    bitmapOriginal.getHeight(), matrix, true);
            imageView.setImageBitmap(bitmapRotada);
        } catch (Exception e) {

        }
    }

    /**
     * Borrar el archivo del cual se pasa el nombre por parámetro
     *
     * @param nombreFoto
     */
    public void borrarArchivoFoto (String nombreFoto) {
        String ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +
                "/" + Aplicacion.revisionActual + "/" + Aplicacion.equipoActual + "/";
        File f = Aplicacion.recuperarArchivo(ruta, nombreFoto);

        if (f != null) {
            f.delete();
        }

    }

}