package com.muskan.sehyog_ekpehel;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

    View view;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        view = itemView;
    }

    public  void setDetails(Context context, String title, String image)
    {
        TextView titletv = view.findViewById(R.id.TitleView);
        ImageView imagetv = view.findViewById(R.id.imageView);

        titletv.setText(title);
        Picasso.get().load(image).into(imagetv);
    }


}
