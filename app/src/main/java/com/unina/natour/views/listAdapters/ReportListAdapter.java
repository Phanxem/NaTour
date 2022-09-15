package com.unina.natour.views.listAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.unina.natour.R;
import com.unina.natour.controllers.DettagliSegnalazioneController;
import com.unina.natour.controllers.ImportaFileGPXController;
import com.unina.natour.models.ElementReportModel;
import com.unina.natour.views.activities.NaTourActivity;

import java.io.File;
import java.util.List;

public class ReportListAdapter extends ArrayAdapter<ElementReportModel> {

    private NaTourActivity activity;

    public ReportListAdapter(Context context, List<ElementReportModel> reports, NaTourActivity activity) {
        super(context, R.layout.list_element_report,reports);
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ElementReportModel report = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.list_element_report, parent, false);
        }

        TextView textView_titolo = convertView.findViewById(R.id.listElementReport_textView_titolo);
        TextView textView_dateOfInput = convertView.findViewById(R.id.listElementReport_textView_data);
        RelativeLayout relativeLayout_listElement = convertView.findViewById(R.id.listElementReport_relativeLayout_listElement);

        textView_titolo.setText(report.getTitolo());
        textView_dateOfInput.setText(report.getDateOfInput());

        relativeLayout_listElement.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DettagliSegnalazioneController.openDettagliSegnalazioneActivity(activity, report.getId());
            }
        });
        return convertView;
    }

}
