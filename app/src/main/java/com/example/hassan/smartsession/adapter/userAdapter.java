package com.example.hassan.smartsession.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hassan.smartsession.R;
import com.example.hassan.smartsession.model.Students;

import java.util.ArrayList;

public class userAdapter extends RecyclerView.Adapter<userAdapter.ViewHolder> {

    private ArrayList<Students> users;
  public userAdapter(ArrayList<Students> users) {
      this.users = users;
  }

    @NonNull
    @Override
    public userAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull userAdapter.ViewHolder viewHolder, int i) {
        viewHolder.subject.setText("CourseCode : "+users.get(i).getSubject());
        viewHolder.depart.setText("Department : "+users.get(i).getDepartment());
        viewHolder.status.setText("Status : "+users.get(i).getStatus());
        viewHolder.date.setText("Date : "+users.get(i).getDate());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView subject,depart,status,date;

        public ViewHolder(@NonNull View view) {
            super(view);
            subject=(TextView) view.findViewById(R.id.tv_subject);
            depart=(TextView) view.findViewById(R.id.tv_department);
            status=(TextView) view.findViewById(R.id.tv_status);
            date=(TextView) view.findViewById(R.id.tv_date);
        }
    }
}
