package com.kichou.imad.e_commerceapp.utils;

import android.app.Activity;
import android.app.Dialog;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;



import com.kichou.imad.e_commerceapp.R;
import com.kichou.imad.e_commerceapp.controller.sellers.SellerAddProductSuccesDIalogue;


public class ViewDialogue
{

    private Activity activity;
    SellerAddProductSuccesDIalogue sellerAddProductSuccesDIalogue;

    public ViewDialogue(Activity act)
    {
        this.activity = act;
    }

        public void showDialog(String msg)
        {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_dialogue_alert);

            TextView text = dialog.findViewById(R.id.text_dialog);
            text.setText(msg);

            Button dialogButton = dialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        }

    public void showSucessDialog(SellerAddProductSuccesDIalogue sellerAddProductSuccesDIalogue ,String msg)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.sucess_view_dialogue);

        TextView text = dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        this.sellerAddProductSuccesDIalogue = sellerAddProductSuccesDIalogue;


        Button dialogButton = dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sellerAddProductSuccesDIalogue.succes();

            }
        });

        dialog.show();

    }



}
