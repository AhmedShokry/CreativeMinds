package com.smartvision.creativeminds.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smartvision.creativeminds.R;
import com.smartvision.creativeminds.models.JsonResponseModel;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<JsonResponseModel> dataSet;
    Activity activity;

    public RecyclerAdapter(ArrayList<JsonResponseModel> data, Activity activity) {
        this.dataSet = data;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_raw, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        if (dataSet.get(listPosition).getFork() == true) {
            holder.recyclerRawLayout.setBackgroundColor(Color.GREEN);
        } else {

        }


        if (dataSet.get(listPosition).getName() != null) {
            holder.repoNameTextView.setText(dataSet.get(listPosition).getName().toString());
        } else {
            holder.repoNameTextView.setText("no data");
        }
        if (dataSet.get(listPosition).getDescription() != null) {
            holder.descriptionTextView.setText(dataSet.get(listPosition).getDescription().toString());
        } else {
            holder.descriptionTextView.setText("no data");
        }
        if (dataSet.get(listPosition).getOwner().getLogin() != null) {
            holder.usernameTextView.setText(dataSet.get(listPosition).getOwner().getLogin().toString());
        } else {
            holder.usernameTextView.setText("no data");
        }


        holder.recyclerRawLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String rep = dataSet.get(listPosition).getHtml_url();
                String owner = dataSet.get(listPosition).getOwner().getHtml_url();
                showConfirmRemoveDialog(rep, owner);
                return false;
            }
        });

    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView repoNameTextView,
                descriptionTextView,
                usernameTextView;
        LinearLayout recyclerRawLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.repoNameTextView = (TextView) itemView.findViewById(R.id.recycler_item_repo_name);
            this.descriptionTextView = (TextView) itemView.findViewById(R.id.recycler_item_description);
            this.usernameTextView = (TextView) itemView.findViewById(R.id.recycler_item_user_name);
            this.recyclerRawLayout = (LinearLayout) itemView.findViewById(R.id.recycler_raw_layout);
        }
    }

    private Dialog showConfirmRemoveDialog(final String repositoryURL, final String ownerURl) {
        final Dialog dialog = new Dialog(activity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.confirm_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Button repositoryButton = (Button) dialog.findViewById(R.id.dialog_repository_button);
        Button ownerButton = (Button) dialog.findViewById(R.id.dialog_owner_button);

        repositoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (repositoryURL != null) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(repositoryURL));
                    activity.startActivity(myIntent);
                } else {
                    Toast.makeText(activity, "there's no link", Toast.LENGTH_SHORT).show();
                }

            }
        });

        ownerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (ownerURl != null) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ownerURl));
                    activity.startActivity(myIntent);
                } else {
                    Toast.makeText(activity, "there's no link", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }


}
