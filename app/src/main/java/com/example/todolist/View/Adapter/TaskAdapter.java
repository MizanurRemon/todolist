package com.example.todolist.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.View.Activity.MainActivity;
import com.example.todolist.View.RoomDB.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    List<Task> tasks;
    MainActivity mainActivity;

    public TaskAdapter(List<Task> tasks, MainActivity mainActivity) {
        this.tasks = tasks;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.task_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {

        Task response = tasks.get(position);
        holder.titleText.setText(response.taskName);
        holder.positionText.setText(String.valueOf(position + 1) + ".");
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    private OnItemClickListener onClickListener;
    private OnItemDeleteListener onItemDeleteListener;

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    public interface OnItemDeleteListener {
        void OnItemDelete(int position);
    }

    public void setOnClickListener(OnItemClickListener onClickListener, OnItemDeleteListener onItemDeleteListener) {
        this.onClickListener = onClickListener;
        this.onItemDeleteListener = onItemDeleteListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, positionText;
        ImageView deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.titleText);
            positionText = itemView.findViewById(R.id.positionText);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onClickListener.OnItemClick(position);
                        }
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemDeleteListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemDeleteListener.OnItemDelete(position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mainActivity.edit_function(getAdapterPosition());
                    return false;
                }
            });
        }
    }
}
