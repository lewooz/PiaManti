package com.levent.pia;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class KilolukAdapter extends RecyclerView.Adapter<KilolukAdapter.MyViewHolder> {

    LayoutInflater inflater;
    ArrayList<kilolukurun> mDataList;
    Context context;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public KilolukAdapter(Context context, ArrayList<kilolukurun> list) {

        inflater = LayoutInflater.from(context);
        this.mDataList=list;
        this.context =context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v =inflater.inflate(R.layout.kiloluk_row, parent,false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        mAuth= FirebaseAuth.getInstance();
        currentUser= mAuth.getCurrentUser();
        kilolukurun secilenurun = new kilolukurun();
        secilenurun= mDataList.get(position);
        holder.setData(secilenurun);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentUser==null){
                Intent intent = new Intent(context,uyegiris.class);
                context.startActivity(intent);
                }else
                    {
                        Intent intent = new Intent(context,UrunCesitSecim.class);
                        context.startActivity(intent);
                    }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            image = itemView.findViewById(R.id.imageview);

        }

        public void setData(kilolukurun secilenurun) {

            this.image.setImageResource(secilenurun.getImageId());

        }
    }
}
