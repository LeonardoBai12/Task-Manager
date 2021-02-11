package com.example.bancodedadossqlite;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskAdapter {

    public static class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> implements Filterable{

        private List<Task> taskList;
        private List<Task> taskListFull;


        public Adapter() {
        }

        public void updateList(List<Task> taskList) {
            this.taskList = taskList;
            taskListFull = new ArrayList<>(taskList);
            this.notifyDataSetChanged();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.row_tasklist, parent, false);
            return new MyViewHolder(item);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.textTask.setText(taskList.get(position).getTask());

            onClickGeneretaPdf(holder.imageButton,
                    taskList.get(position).getCardLink(),
                    holder.itemView.getContext());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(holder.itemView.getContext(), EditTaskActivity_.class);
                    Bundle bundle = new Bundle();

                    bundle.putSerializable("Task", taskList.get(position));
                    intent.putExtras(bundle);

                    ((Activity) holder.itemView.getContext()).startActivityForResult(intent, 1);

                    notifyItemChanged(position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    MainActivity.deleteTask(holder.itemView.getContext(), taskList.get(position));
                    return false;
                }
            });

        }

        @Override
        public int getItemCount() {
            if (!(this.taskList == null)) {
                return this.taskList.size();
            } else {
                return 0;
            }
        }

        @Override
        public Filter getFilter() {
            return mealFilter;
        }

        private Filter mealFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Task> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(taskListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Task item : taskListFull) {

                        if (item.getTask().toLowerCase().contains(filterPattern) ||
                                item.getTask().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }

                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                taskList.clear();
                taskList.addAll((List) results.values);
                notifyDataSetChanged();
            }

        };

        static class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.textTask)
            TextView textTask;

            @BindView(R.id.imageButton)
            ImageButton imageButton;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public void openLink(Context context, String url) {
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/viewerng/viewer?url=" + url + "&saveAsFile=yes"));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url + "&saveAsFile=yes"));
            context.startActivity(intent);
        }

        private void onClickGeneretaPdf(ImageButton button, String url, Context context) {
            button.setOnClickListener(v -> {
                if (isConnected(context)) {
                    if(Patterns.WEB_URL.matcher(url).matches())
                        openLink(context, url);
                    else
                        Toast.makeText(context,"Invalid URL", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context,"Verify your connection!", Toast.LENGTH_LONG).show();
                }
            });
        }

        public static boolean isConnected(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetInfo != null && activeNetInfo.isConnected();
        }
    }




}
