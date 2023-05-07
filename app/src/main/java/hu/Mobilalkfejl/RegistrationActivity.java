package hu.Mobilalkfejl;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

@RequiresApi(api = Build.VERSION_CODES.S)
public class RegistrationActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private  static final int SECRET_KEY = 21;
    private NotificationHandler mNotiHandler;
    private static final String PREF_KEY = MainActivity.class.getPackageName().toString();
    private SharedPreferences preferences;

    EditText registrationNameET;
    EditText registrationEmail ;
    EditText registrationPassword;
    EditText registrationPhoneNumber;
    EditText registrationTrade ;

    EditText registrationPasswordVer;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Bundle registrationBundle = getIntent().getExtras();
        int secret_key= registrationBundle.getInt("SECRET_KEY");
        mNotiHandler = new NotificationHandler(getApplicationContext());
        if(secret_key != 21){
            finish();
        }
        registrationNameET = findViewById(R.id.registrationNameET);
        registrationEmail = findViewById(R.id.registrationEmail);
        registrationPassword = findViewById(R.id.registrationPassword);
        registrationPhoneNumber = findViewById(R.id.registrationPhoneNumber);
        registrationTrade = findViewById(R.id.registrationTrade);
        registrationPasswordVer =findViewById(R.id.registrationPasswordVer);
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

    mAuth = FirebaseAuth.getInstance();



    }


    public void register(View view) {
        String name= registrationNameET.getText().toString();
        String email= registrationEmail.getText().toString();
        String password= registrationPassword.getText().toString();
        String password_Ver = registrationPasswordVer.getText().toString();
        String phoneNumber= registrationPhoneNumber.getText().toString();
        String trade= registrationTrade.getText().toString();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animacioketto);
        if (!password.equals(password_Ver)){
            Toast.makeText(RegistrationActivity.this,"Kérjük ellenőrize a megadott jelszavakat! A jelszavak eltérnek egymástól ",Toast.LENGTH_LONG).show();

        }
        else if(password==null || password==" "  ||  password_Ver==null || password_Ver==" "){
            Toast.makeText(RegistrationActivity.this,"Kérjük ellenőrize a megadott jelszavakat! ",Toast.LENGTH_LONG).show();

        }
        else if (email==null || email==" "){
            Toast.makeText(RegistrationActivity.this,"Kérjük ellenőrize a megadott Email címet!",Toast.LENGTH_LONG).show();
        } else if (name==null || name==" " ||  trade==null || trade==" ") {
            Toast.makeText(RegistrationActivity.this,"Kérjük ellenőrize a megadott Nevet és telfonszámot!",Toast.LENGTH_LONG).show();

        }else{
            view.startAnimation(animation);


            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Log.d(LOG_TAG,"succesful");

                        kezdolapNyit();

                    }else{
                        Toast.makeText(RegistrationActivity.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });

        }


        // Alkalmazd az animációt a nézetre (például egy gombra)




    }

    private void kezdolapNyit() {
        Intent intent = new Intent(this,KezdolapActivity.class);
        intent.putExtra("SECRET_KEY",21);
        startActivity(intent);

    }

    public void cancel(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("SECRET_KEY",21);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}