package hu.Mobilalkfejl;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> implements Filterable {

    private ArrayList<JobItem> jobData;
    private ArrayList<JobItem> jobDataAll;
    private Context ncontext;
    private int lastPosition=-1;


JobAdapter(Context context, ArrayList<JobItem> jobs){
    this.jobData=jobs;
    this.jobDataAll=jobs;
    this.ncontext= context;
}



    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(ncontext).inflate(R.layout.list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(JobAdapter.ViewHolder holder, int position) {
    JobItem currentItem= jobData.get(position);

    holder.bindTo(currentItem);

    if (holder.getAdapterPosition()> lastPosition){
        Animation animation = AnimationUtils.loadAnimation(ncontext,R.anim.slide_in_row);
        holder.itemView.startAnimation(animation);
        lastPosition= holder.getAdapterPosition();
    }

    }

    @Override
    public int getItemCount() {
        return jobData.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView desc;
        private TextView salary;

        private TextView phone;
        public ViewHolder( View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.munkaNeveTV);
            desc= itemView.findViewById(R.id.munkaLeirasTV);
            salary= itemView.findViewById(R.id.munkaFizetesTV);
            phone = itemView.findViewById(R.id.munkaPhone);
            itemView.findViewById(R.id.jelentkezesButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

public void bindTo(JobItem currentItem) {
    name.setText(currentItem.getJobName());
    desc.setText(currentItem.getJobDesc());
    salary.setText(currentItem.getJobSalary());
    phone.setText(currentItem.getJobPhone());
    itemView.findViewById(R.id.torlesButton).setOnClickListener(view -> ((KezdolapActivity) ncontext).deleteItem(currentItem));

    itemView.findViewById(R.id.jelentkezesButton).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String phoneNumber = currentItem.getJobPhone(); // A kapcsolattartó telefonszáma
            String message = "Jó napot! Érdeklődöm a(z) " + currentItem.getJobName() + " állásajánlat iránt."; // Az üzenet, amit küldeni szeretne
            ((KezdolapActivity) ncontext).sendSMS(phoneNumber, message);
        }
    });
}

    };
    @Override
    public Filter getFilter() {
        return jobfilter;
    }
    private Filter jobfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<JobItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (charSequence == null ||charSequence.length()==0){
                results.count = jobDataAll.size();
                results.values = jobDataAll;
            }else{
                String filerPattent = charSequence.toString().toLowerCase().trim();
                for (JobItem job : jobDataAll){
                    if(job.getJobName().toLowerCase().contains(filerPattent)){
                        filteredList.add(job);
                    }

                }
                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }


        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            jobData= (ArrayList)filterResults.values;
            notifyDataSetChanged();
        }
    };


    }


