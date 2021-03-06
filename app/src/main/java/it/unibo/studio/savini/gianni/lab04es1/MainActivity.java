package it.unibo.studio.savini.gianni.lab04es1;

import android.Manifest;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_STORAGE_PERMISSION = 100;

    private EditText editText;
    private Button btnWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Inizializzo gli elementi della mia view
        editText = (EditText) findViewById(R.id.editText);
        btnWrite = (Button) findViewById(R.id.button);

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Controllo di avere i permessi per scrivere su file
                 */
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                    writeText(editText.getText().toString());
                } else {//se non ho i permessi, Chiedo all'utente di darmeli
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_STORAGE_PERMISSION  );
                }
            }
        });
    }

    public void onRequestPermisionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        if(requestCode == REQUEST_STORAGE_PERMISSION) {

        }
    }

    /**
     * Metodo col quale scrivo/sovrascrivo su file
     *
     * @param text Is the string which will write to file.
     */
    private void writeText(final String text) {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(dir, "prova.txt");
            FileOutputStream fileOutputStream = null;
            try {
                byte[] data = text.getBytes();
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data);
                fileOutputStream.flush();
                /**
                 * Se tutto è andato a buon fine, avverto l'utente che l'operazione
                 * si è conclusa in modo corretto
                 */
                Toast.makeText(this, "Scrittura completata correttamente", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                /**
                 * Se qualcosa è andato storto, mostro all'utente l'eccezione lanciata dall'app
                 */
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            } finally {
                /**
                 * Comunque vadano le cose, il file deve essere chiuso prima di uscire dal metodo
                 */
                if(fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
