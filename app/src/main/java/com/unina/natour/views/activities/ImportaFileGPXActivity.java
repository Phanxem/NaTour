package com.unina.natour.views.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unina.natour.R;
import com.unina.natour.controllers.ImportaFileGPXController;
import com.unina.natour.models.ImportaFileGPXModel;
import com.unina.natour.views.dialogs.MessageDialog;
import com.unina.natour.views.observers.Observer;

import java.io.File;
import java.util.List;
@RequiresApi(api = Build.VERSION_CODES.N)
public class ImportaFileGPXActivity extends NaTourActivity {


    ImportaFileGPXController importaFileGPXController;

    ImportaFileGPXModel importaFileGPXModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importa_file_gpx);

        importaFileGPXController = new ImportaFileGPXController(this);

        importaFileGPXModel = importaFileGPXController.getImportaFileGPXModel();
        importaFileGPXModel.registerObserver(this);
        addModel(importaFileGPXModel);

        ListView listView = findViewById(R.id.ImportaFileGPX_listView_listaFiles);
        importaFileGPXController.initListViewFiles(listView);

        importaFileGPXController.openRootDirectory();

        pressIconBack();
        pressParentDirectory();
    }



    public void pressIconBack() {
        ImageView imageView_iconBack = findViewById(R.id.ImportaFileGPX_imageView_iconaIndietro);
        imageView_iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void pressParentDirectory() {
        RelativeLayout relativeLayout_parentDirectory = findViewById(R.id.ImportaFileGPX_relativeLayout_directoryPadre);
        relativeLayout_parentDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importaFileGPXController.openParentDirectory();
            }
        });
    }



    @Override
    public void update() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateDirectoriesList();
            }
        });
    }

    private void updateDirectoriesList(){
        TextView textView_directoryName = findViewById(R.id.ImportaFileGPX_textView_nomeDirectory);
        textView_directoryName.setText(importaFileGPXModel.getCurrentDirectory().getPath());

        RelativeLayout relativeLayout_parentDirectory = findViewById(R.id.ImportaFileGPX_relativeLayout_directoryPadre);
        if(importaFileGPXModel.hasParentDirectory()){
            relativeLayout_parentDirectory.setVisibility(View.VISIBLE);
            Log.i("AGDMSDFòSDFNSLDFKMSDF", "AFMSDFLKSDM");
            return;
        }
        relativeLayout_parentDirectory.setVisibility(View.GONE);
        Log.i("not haosf", "asdgfhgjhkjlgfdsasdgfhjhjfdsa");
    }
}