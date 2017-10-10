package com.example.hivian.my_contact_manager.recyclers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hivian.my_contact_manager.R;
import com.example.hivian.my_contact_manager.utilities.BitmapUtility;

/**
 * Created by hivian on 10/10/17.
 */


class ContactViewHolder extends RecyclerView.ViewHolder {

    private ImageView image;
    private TextView name;
    private TextView phone;

    ContactViewHolder(View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.row_image);
        name = (TextView) itemView.findViewById(R.id.row_name);
        phone = (TextView) itemView.findViewById(R.id.row_phone);
    }

    void bind(ContactData data){
        name.setText(data.getName());
        phone.setText(data.getPhone());
        if (data.getImage() != null) {
            image.setImageDrawable(BitmapUtility.setBitmapCircular(
                    image.getResources(), data.getImage()));
        }
    }
}
