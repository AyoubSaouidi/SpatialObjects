package com.example.spatialobjects;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;

import java.util.ArrayList;

public class AdapterObject extends RecyclerView.Adapter<AdapterObject.HolderClass>{

    // Variables
    private Context context;
    private ArrayList<Object> objectsList;

    //DB HELPER
    DbHelper dbHelper;

    //Constructor
    public AdapterObject(Context context, ArrayList<Object> objectsList) {
        this.context = context;
        this.objectsList = objectsList;

        dbHelper= new DbHelper(context);
    }

    class HolderClass extends RecyclerView.ViewHolder {

        //Views
        CircularImageView profileImageView_card;
        TextView designationTextView,longitudeTextView,latitudeTextView,altitudeTextView;
        ImageButton moreBtn;

        public HolderClass(@NonNull View itemView) {
            super(itemView);

            // INIT Views
            profileImageView_card = itemView.findViewById(R.id.profileImageView_card);
            designationTextView = itemView.findViewById(R.id.designationTextView);
            longitudeTextView = itemView.findViewById(R.id.longitudeTextView);
            latitudeTextView = itemView.findViewById(R.id.latitudeTextView);
            altitudeTextView = itemView.findViewById(R.id.altitudeTextView);
            moreBtn = itemView.findViewById(R.id.moreBtn);
        }
    }

    @NonNull
    @Override
    public HolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate Layout
        View view = LayoutInflater.from(context).inflate(R.layout.card_object,parent,false);

        return new HolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderClass holder, int position) {
        // get DATA, set Data, handel view clicks in this method
        // get DATA
        Object obj = objectsList.get(position);
        String id = obj.getId();
        String designation = obj.getDesignation();
        String image = obj.getImage();
        String longitude = obj.getLongitude();
        String latitude = obj.getLatitude();
        String altitude = obj.getAltitude();
        String description = obj.getDescription();
        String added_date = obj.getAdded_date();
        String updated_date = obj.getUpdated_date();

        //set DATA to views
        holder.designationTextView.setText(designation);
        holder.longitudeTextView.setText(longitude);
        holder.latitudeTextView.setText(latitude);
        holder.altitudeTextView.setText(altitude);
        // Image Null or NOT
        if(image.equals(null)) {
            holder.profileImageView_card.setImageResource(R.drawable.ic_object);
        }
        else {
            holder.profileImageView_card.setImageURI(Uri.parse(image));
        }

        //Handle Click on ITEM to see more Details
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ObjectDetailsActivity.class);
                intent.putExtra("OBJECT_ID",id);
                context.startActivity(intent);
            }
        });

        //Handle Clicks [ TOggle --> EDIT or DELETE]
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMoreDialog(""+position,
                        ""+id,
                        ""+designation,
                        ""+image,
                        ""+longitude,
                        ""+latitude,
                        ""+altitude,
                        ""+description,
                        ""+added_date,
                        ""+updated_date);
            }
        });
    }

    private void ShowMoreDialog(String position,final String id,final String designation,final String image,
                                final String longitude,final String latitude,final String altitude,final String description,
                                final String added_date,final String updated_date){
        // OPTIONS [ CAMERA or GALLERY]
        String[] options = {"Edit","Delete"};

        // SHOW DIALOG and Store option chosen
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // SET items and options
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                // CHOICE handling
                if(choice == 0) {
                    // EDIT
                    Intent intent = new Intent(context,AddUpdateActivity.class);
                    intent.putExtra("ID",id);
                    intent.putExtra("DESIGNATION",designation);
                    intent.putExtra("IMAGE",image);
                    intent.putExtra("LONGITUDE",longitude);
                    intent.putExtra("LATITUDE",latitude);
                    intent.putExtra("ALTITUDE",altitude);
                    intent.putExtra("DESCRIPTION",description);
                    intent.putExtra("ADDED_DATE",added_date);
                    intent.putExtra("UPDATED_DATE",updated_date);
                    intent.putExtra("isEdit_Mode",true);

                    context.startActivity(intent);
                }
                else if (choice == 1 ) {
                    dbHelper.deleteObject(id);
                    ((MainActivity)context).onResume();
                }
            }

    });

        //Create and Show The DIALOG Alert
        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return objectsList.size();
    }

}
