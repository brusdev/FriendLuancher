package com.brusdev.friendluancher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import android.view.View;
import android.widget.TextView;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder>{

    int mMode;
    Activity mActivity;
    List<ApkInfo> apkInfos;

    public int getMode() {
        return mMode;
    }

    public void setMode(int mode) {
        this.mMode = mode;
    }

    public AppsAdapter(Activity activity, List<ApkInfo> list){

        mActivity = activity;

        apkInfos = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public CardView cardView;
        public ImageView imageView;
        public TextView textView_App_Name;
        public TextView textView_App_Package_Name;

        public ViewHolder (View view){

            super(view);

            cardView = (CardView) view.findViewById(R.id.card_view);
            imageView = (ImageView) view.findViewById(R.id.imageview);
            textView_App_Name = (TextView) view.findViewById(R.id.Apk_Name);
            textView_App_Package_Name = (TextView) view.findViewById(R.id.Apk_Package_Name);
        }
    }

    @Override
    public AppsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view2 = LayoutInflater.from(mActivity).inflate(R.layout.cardview_layout,parent,false);

        ViewHolder viewHolder = new ViewHolder(view2);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){

        ApkInfoExtractor apkInfoExtractor = new ApkInfoExtractor(mActivity);

        ApkInfo apkInfo = apkInfos.get(position);
        final String ApplicationPackageName = apkInfo.getPackageName();
        String ApplicationLabelName = apkInfo.getLabel();
        Drawable drawable = apkInfoExtractor.getAppIconByPackageName(ApplicationPackageName);

        viewHolder.textView_App_Name.setText(ApplicationLabelName);

        viewHolder.textView_App_Package_Name.setText(String.format("Width: %d, height: %d", drawable.getMinimumWidth(), drawable.getMinimumHeight()));

        viewHolder.imageView.setImageDrawable(drawable);

        //Adding click listener on CardView to open clicked application directly from here .
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = mActivity.getPackageManager().getLaunchIntentForPackage(ApplicationPackageName);
                if(intent != null){

                    if (mMode == 1) {
                        mActivity.startActivity(intent);
                    } else {
                        Intent data = new Intent();
                        data.putExtra("packageName",ApplicationPackageName);
                        mActivity.setResult(RESULT_OK, data);
                        mActivity.finish();
                    }
                }
                else {

                    Toast.makeText(mActivity,ApplicationPackageName + " Error, Please Try Again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount(){

        return apkInfos.size();
    }

}