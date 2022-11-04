package com.unina.natour.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.unina.natour.R;
import com.unina.natour.controllers.VisualizzaSegnalazioniController;
import com.unina.natour.models.ElementReportModel;
import com.unina.natour.models.VisualizzaSegnalazioniModel;

import java.util.List;

public class VisualizzaSegnalazioniActivity extends NaTourActivity {

    private VisualizzaSegnalazioniController visualizzaSegnalazioniController;

    private VisualizzaSegnalazioniModel visualizzaSegnalazioniModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_segnalazioni);

        this.visualizzaSegnalazioniController = new VisualizzaSegnalazioniController(this);
        this.visualizzaSegnalazioniModel = visualizzaSegnalazioniController.getModel();

        ListView listView = findViewById(R.id.ViewReports_listView_risultati);
        visualizzaSegnalazioniController.initListViewReports(listView);

        update();

        pressIconBack();
    }

    public void pressIconBack(){
        NaTourActivity activity = this;

        ImageView imageView_iconBack = findViewById(R.id.ViewReports_imageView_iconaIndietro);
        imageView_iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void update(){
        TextView textView_itineraryName = findViewById(R.id.ViewReports_textView_titoloItinerario);
        textView_itineraryName.setText(visualizzaSegnalazioniModel.getItineraryName());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                visualizzaSegnalazioniController.notifyListAdapter();
            }
        });
    }

    @Override
    protected void onResume() {
        visualizzaSegnalazioniController.initModel(visualizzaSegnalazioniModel.getItineraryId());
        update();

        List<ElementReportModel> reports = visualizzaSegnalazioniModel.getReports();
        if(reports.isEmpty()) finish();

        super.onResume();
    }

}