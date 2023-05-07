package hu.Mobilalkfejl;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InstrukcioActivity extends AppCompatActivity {
    private NotificationHandler mNotiHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNotiHandler = new NotificationHandler(getApplicationContext());
        setContentView(R.layout.activity_instrukcio);

        Bundle instrukcioBundle = getIntent().getExtras();
        int secret_key = instrukcioBundle.getInt("SECRET_KEY");

        if (secret_key != 21) {
            finish();
        }

        Button ertemButton = findViewById(R.id.button);
        ertemButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.S)
            @Override
            public void onClick(View v) {
               mainNyitas();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void mainNyitas() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
