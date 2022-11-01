package com.example.a1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ViewHolder> {

    Context context;

    ArrayList<String> names;
    ArrayList<String> images;



    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView rowImage;
        TextView rowName;
        TextView rowDate;
        TextView rowRatio;
        TextView rowEye;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rowImage = itemView.findViewById(R.id.cardImageView);
            rowName = itemView.findViewById(R.id.patientName);
            rowDate = itemView.findViewById(R.id.patientDate);
            rowRatio = itemView.findViewById(R.id.patientRatio);
            rowEye = itemView.findViewById(R.id.patientEye);

        }
    }

    public ProgramAdapter(Context context, ArrayList<String> nameInput, ArrayList<String> images){
        this.context = context;
        this.images = images;
        this.names = nameInput;
    }

    @NonNull
    @Override
    public ProgramAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ProgramAdapter.ViewHolder holder, int position) {

        Glide.with(context).load(images.get(position)).into(holder.rowImage);

        String imageName = names.get(position);
        String[] splitted = imageName.split("\\s+");

        holder.rowName.setText(splitted[0]);
        holder.rowDate.setText(splitted[3]);
        holder.rowRatio.setText(splitted[2]);
        holder.rowEye.setText(splitted[1]);

    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
