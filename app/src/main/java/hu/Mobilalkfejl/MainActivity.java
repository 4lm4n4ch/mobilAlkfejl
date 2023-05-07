package hu.Mobilalkfejl;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

@RequiresApi(api = Build.VERSION_CODES.S)
public class MainActivity extends AppCompatActivity {
    private  static final int SECRET_KEY = 21;
    private NotificationHandler mNotiHandler;
    private FirebaseAuth mAuth;
    private static final String PREF_KEY = MainActivity.class.getPackageName().toString();
    private static final String LOG_TAG = RegistrationActivity.class.getName();

    private SharedPreferences preferences;
    private     Animation animation;
    EditText editTextEmail;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        mNotiHandler = new NotificationHandler(getApplicationContext());
        mAuth= FirebaseAuth.getInstance();
         animation = AnimationUtils.loadAnimation(this, R.anim.animacioharom);

    }

    public void registration(View view) {
        Intent regisrationIntent = new Intent(this, RegistrationActivity.class);
        regisrationIntent.putExtra("SECRET_KEY",21);
        startActivity(regisrationIntent);
    }

    public void instrukcio(View view) {
        Intent instrukcioIntent = new Intent(this, InstrukcioActivity.class);
        instrukcioIntent.putExtra("SECRET_KEY",21);
        startActivity(instrukcioIntent);
    }

    public void login(View view) {
        String email= editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        view.startAnimation(animation);

        if (TextUtils.isEmpty(email)) {
            // Show an error message for the email field
            editTextEmail.setError("Email is required.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            // Show an error message for the password field
            editTextPassword.setError("Password is required.");
            return;
        }



        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(LOG_TAG,"succesful log in");
                    kezdolapNyit();
                }
                else{
                    Toast.makeText(MainActivity.this,"Nem tudtuk belejentkeztettni! A következő hibaüzenettel "+ task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void kezdolapNyit() {
        Intent intent = new Intent(this,KezdolapActivity.class);
        startActivity(intent);

    }
    @Override
    protected void onPause() {
        super.onPause();
       
    }
}