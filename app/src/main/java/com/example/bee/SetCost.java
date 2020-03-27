package com.example.bee;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * This class implements a dialog that shows the user with initial estimated fare, and provide
 * the option to add more on top of the estimated fare.
 */
public class SetCost extends DialogFragment {
    private OnFragmentInteractionListener listener;
    private double oldCost;
    private double newCost;

    SetCost(double cost) {
        this.oldCost = cost;
    }

    public interface OnFragmentInteractionListener {
        void postRequest(double cost);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Dialog);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.set_cost_fragment);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        dialog.setCanceledOnTouchOutside(false);
        setCancelable(false);
        TextView textView = dialog.findViewById(R.id.old_cost);
        textView.setText("$ " + String.format("%.2f", oldCost));
        EditText editText = dialog.findViewById(R.id.enter_cost);
        editText.setText(String.format("%.2f", oldCost));
        Button confirmBtn = dialog.findViewById(R.id.confirm_btn);
        Button cancelBtn = dialog.findViewById(R.id.cancel_btn);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String costString = editText.getText().toString();
                if (!costString.isEmpty()) {
                    try {
                        newCost = Double.parseDouble(costString);
                        if (newCost < oldCost) {
                            // New cost entered cannot be lower than the estimated cost
                            String text = "Value less than estimated fair";
                            Toast toast = Toast.makeText(SetCost.this.getActivity(), text, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.show();
                        } else {
                            // Cost is valid, post request to Firebase
                            listener.postRequest(newCost);
                        }
                    } catch (Exception e) {
                        Toast toast = Toast.makeText(SetCost.this.getActivity(), e.toString(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                    }
                } else {
                    String text = "Invalid Amount";
                    Toast toast = Toast.makeText(SetCost.this.getActivity(), text, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        return dialog;
    }
}
