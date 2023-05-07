package hu.Mobilalkfejl;

import android.Manifest;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class KezdolapActivity extends AppCompatActivity {
    private static final String LOG_TAG = KezdolapActivity.class.getName();
    private boolean viewRow = false;
    private FirebaseUser user;
    private int queryLimit = 10;
    private RecyclerView recyclerView;
    private ArrayList<JobItem> jobList;
    private JobAdapter jobAdapter;
    private int gridNum = 1;
    private NotificationHandler mNotiHandler;



    private FirebaseFirestore firestore;
    private CollectionReference mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kezdolap);


        ActionBar actionBar = getSupportActionBar(); // Hivatkozz az ActionBar objektumra
        if (actionBar != null) {
            actionBar.setTitle("Kezdolap"); // Állítsd be a címsort
        }
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Authentaced user");
        } else {
            finish();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, gridNum));

        mNotiHandler = new NotificationHandler(getApplicationContext());
        jobList = new ArrayList<>(); // Inicializálja a jobList objektumot

        firestore = FirebaseFirestore.getInstance();
        mItems = firestore.collection("Items");

        queryData(); // Hívja meg a queryData() metódust miután inicializálta a firestore és mItems változókat
        loadJobsFromResources();

        jobList.addAll(loadJobsFromResources());
        jobAdapter = new JobAdapter(this, jobList);
        recyclerView.setAdapter(jobAdapter);




        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(powerReciver, filter);
        if (jobList != null && !jobList.isEmpty()) {
            Random random = new Random(); // Létrehoz egy új Random objektumot
            int randomIndex = random.nextInt(jobList.size()); // Véletlenszerűen választ egy indexet a jobList méretének határain belül
            String jobName = jobList.get(randomIndex).getJobName(); // Kiválasztja a véletlenszerűen választott indexű állás nevét
            mNotiHandler.send("Egy állás neve: " + jobName);
        }

    }

    private void queryData() {
        if (jobList != null) {
            jobList.clear();
        }
        jobList.addAll(loadJobsFromResources());
        mItems.orderBy("JobName").limit(queryLimit).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                JobItem job = document.toObject(JobItem.class);
                job.setId(document.getId());
                jobList.add(job);
            }
            jobAdapter.notifyDataSetChanged();
        });
    }

    BroadcastReceiver powerReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            switch (action) {
                case Intent.ACTION_POWER_CONNECTED:
                    queryLimit = 10;
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    queryLimit = 5;
                    break;
            }
            queryData();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(powerReciver);
    }

    public void deleteItem(JobItem item) {
        DocumentReference reference = mItems.document(item._getId());
        reference.delete().addOnSuccessListener(success -> {
            Toast.makeText(this.getApplicationContext(), "Sikeresen törölte az állást!", Toast.LENGTH_LONG).show();
        }).addOnFailureListener(fail -> {
            Toast.makeText(this.getApplicationContext(), "Valami hiba adodót a tölés során!", Toast.LENGTH_LONG).show();
        });
        queryData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        Log.d(LOG_TAG, "onCreateOptionsMenu called");
        getMenuInflater().inflate(R.menu.job_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                jobAdapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.logOut) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(KezdolapActivity.this, MainActivity.class));
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {

        super.onPause();

        if (jobList != null && !jobList.isEmpty()) {
            Random random = new Random(); // Létrehoz egy új Random objektumot
            int randomIndex = random.nextInt(jobList.size()); // Véletlenszerűen választ egy indexet a jobList méretének határain belül
            String jobName = jobList.get(randomIndex).getJobName(); // Kiválasztja a véletlenszerűen választott indexű állás nevét
            mNotiHandler.send("Egy állás neve: " + jobName);
        }

    }







    @Override
    protected void onResume() {
        super.onResume();
    }

    private ArrayList<JobItem> loadJobsFromResources() {
        ArrayList<JobItem> jobs = new ArrayList<>();
        String[] jobNames = getResources().getStringArray(R.array.munkak);
        String[] jobDescriptions = getResources().getStringArray(R.array.leirasok);
        String[] jobSalaries = getResources().getStringArray(R.array.ber);
        String[] jobPhones = getResources().getStringArray(R.array.telefon);

        for (int i = 0; i < jobNames.length; i++) {
            jobs.add(new JobItem(jobNames[i], jobDescriptions[i], jobSalaries[i],jobPhones[i]));
        }

        return jobs;
    }

    public void sendSMS(String phoneNumber, String message) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        } else {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this.getApplicationContext(), "SMS-el küldve", Toast.LENGTH_LONG).show();
        }

    }
}

