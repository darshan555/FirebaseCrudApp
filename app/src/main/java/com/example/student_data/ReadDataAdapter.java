package com.example.student_data;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ReadDataAdapter extends RecyclerView.Adapter<ReadDataAdapter.ReadDataHolder> {
    MainActivity mainActivity;
    ArrayList<StudentData> allData;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ReadDataAdapter(MainActivity mainActivity, ArrayList<StudentData> allData) {
        this.allData = allData;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ReadDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReadDataHolder(LayoutInflater.from(mainActivity).inflate(R.layout.item_data,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReadDataAdapter.ReadDataHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nameView.setText(allData.get(position).getName());
        holder.emailView.setText(allData.get(position).getEmail());
        holder.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.startActivity(new Intent(mainActivity,updateActivity.class).putExtra("key",allData.get(position).getUid()));
            }
        });
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                builder.setMessage("Are you sure want to delete?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("user").document(allData.get(position).getUid()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(mainActivity, "Delete Successfully", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(mainActivity, "Error!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return allData.size();
    }

    class ReadDataHolder extends RecyclerView.ViewHolder {
        TextView nameView,emailView;
        Button buttonUpdate,buttonDelete;
        public ReadDataHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.nameTV);
            emailView = itemView.findViewById(R.id.emailTV);
            buttonUpdate = itemView.findViewById(R.id.updBTN);
            buttonDelete = itemView.findViewById(R.id.delBTN);
        }
    }
}
