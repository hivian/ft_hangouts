package com.example.hivian.ft_hangouts;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class ContactInfo extends AppCompatActivity {
    private ImageView imageView;
    private TextView name;
    private TextView lastName;
    private TextView phone;
    private TextView email;
    private TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        getSupportActionBar().setTitle("Options");

        if (MainActivity.getPurple()) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.holo_purple)));
        } else {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        }

        Contact contact = (Contact) getIntent().getSerializableExtra("contact");

        if (contact != null) {
            imageView = (ImageView) findViewById(R.id.info_image);
            name = (TextView) findViewById(R.id.info_name);
            lastName = (TextView) findViewById(R.id.info_lastName);
            phone = (TextView) findViewById(R.id.info_phone);
            email = (TextView) findViewById(R.id.info_email);
            address = (TextView) findViewById(R.id.info_address);

            if (contact.getImage() != null) {
                Bitmap imageBm = DbBitmapUtility.getImage(contact.getImage());
                imageView.setImageBitmap(imageBm);
            }
            name.setText(contact.getName());
            lastName.setText(contact.getLastName());
            phone.setText(contact.getPhone());
            email.setText(contact.getEmail());
            address.setText(contact.getAddress());
        }
    }

    public void toSmsManager(View view) {
        name = (TextView) findViewById(R.id.info_name);
        phone = (TextView) findViewById(R.id.info_phone);

        Intent intent = new Intent(this, ContactSms.class);

        intent.putExtra("name", name.getText().toString());
        intent.putExtra("phone", phone.getText().toString());
        startActivity(intent);
    }

    public void callContact(View view) {
        phone = (TextView) findViewById(R.id.info_phone);

        Intent callIntent = new Intent(Intent.ACTION_CALL);

        callIntent.setData(Uri.parse("tel:" + phone.getText().toString()));
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }

    public void editContact(View view) {
        DBHandler db = new DBHandler(this);

        name = (TextView) findViewById(R.id.info_name);

        Contact contact = db.getContactByName(name.getText().toString());
        Intent intent = new Intent(this, ContactEdition.class);
        intent.putExtra("contact", contact);
        startActivity(intent);
    }

    public void deleteContact(View view) {
        final DBHandler db = new DBHandler(this);

        new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.alert_delete_message))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.alert_delete_ok),
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        TextView textView = (TextView) findViewById(R.id.info_name);
                        Log.d("DEBUG", textView.getText().toString());
                        Contact contact = db.getContactByName(textView.getText().toString());
                        db.deleteContact(contact);
                        List<SmsContent> allSms = db.getAllSmsFromContact(contact.getId());
                        for (SmsContent sms : allSms) {
                            db.deleteSms(sms);
                        }
                        MainActivity.getAdapter().notifyDataSetChanged();

                        Intent intent = new Intent(ContactInfo.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.alert_delete_cancel), null)
                .show();
    }
}
