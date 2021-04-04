package com.example.firebasecrudproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private OnCallBack onCallBack;

    private Context context;

    private List<Notes> notes;


    public NotesAdapter(Context context, List<Notes> notes) {
        this.context = context;
        this.notes = notes;
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {

        holder.textView.setText(notes.get(position).getText());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onButtonDeleteClick(notes.get(position));
            }
        });


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onCallBack.onButtonEditClick(notes.get(position));

            }
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageButton delete, edit;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            textView = itemView.findViewById(R.id.listtext);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);
        }
    }

    public interface OnCallBack {

        void onButtonDeleteClick(Notes notes);

        void onButtonEditClick(Notes notes);


    }
}
