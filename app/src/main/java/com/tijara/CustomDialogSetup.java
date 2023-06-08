package com.tijara;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class CustomDialogSetup extends AlertDialog {

    CustomDialog type;
    View dialogView;
    LottieAnimationView image;
    LinearLayout footer;
    Button tidak;
    Button ya;
    TextView title;
    TextView deskription;

    protected CustomDialogSetup(Context context, int themeResId, CustomDialog type) {
        super(context, themeResId);
        this.type = type;
        dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_custom, null);
        setView(dialogView);
        image = dialogView.findViewById(R.id.ic_image);
        footer = dialogView.findViewById(R.id.footer);
        tidak = dialogView.findViewById(R.id.isconfirm);
        title = dialogView.findViewById(R.id.title);
        ya = dialogView.findViewById(R.id.yes);
        deskription = dialogView.findViewById(R.id.deskription);

        image.setAnimation(Integer.valueOf(type.toString()));
        if(type == CustomDialog.CONFIRMATION){
            footer.setVisibility(View.VISIBLE);
            tidak.setVisibility(View.VISIBLE);
        }

        if(type == CustomDialog.ERROR){
            footer.setVisibility(View.VISIBLE);
            tidak.setVisibility(View.GONE);
        }

        if(type == CustomDialog.LOADING){
            footer.setVisibility(View.GONE);
        }

        if(type == CustomDialog.SUCCESS){
            footer.setVisibility(View.VISIBLE);
            tidak.setVisibility(View.GONE);
        }

        if(type == CustomDialog.WARNING){
            footer.setVisibility(View.VISIBLE);
            tidak.setVisibility(View.GONE);
        }
        deskription.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
    }

    public void setJudul(String val){
        title.setText(val);
        title.setVisibility(View.VISIBLE);
    }

    public void setDeskripsi(String val){
        deskription.setText(val);
        deskription.setVisibility(View.VISIBLE);
    }

    public void setListenerOK(View.OnClickListener listener) {
        ya.setOnClickListener(listener);
    }

    public void setListenerTidak(View.OnClickListener listener) {
        tidak.setOnClickListener(listener);
    }
}
