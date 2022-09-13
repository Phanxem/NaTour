package com.unina.natour.views.dialogs;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.unina.natour.views.activities.NaTourActivity;

public class NaTourDialog extends DialogFragment {

    public final String TAG = this.getClass().getSimpleName();

    private NaTourActivity activity;

    public NaTourDialog(){}

    public NaTourDialog(NaTourActivity activity){
        this.activity = activity;
    }

    public NaTourActivity getNaTourActivity() {
        return activity;
    }

    public void setNaTourActivity(NaTourActivity activity) {
        this.activity = activity;
    }
}
