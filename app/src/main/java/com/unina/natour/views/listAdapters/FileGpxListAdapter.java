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
import com.unina.natour.controllers.ImportaFileGPXController;

import java.io.File;
import java.util.List;

public class FileGpxListAdapter extends ArrayAdapter<File>{

    private ImportaFileGPXController importaFileGPXController;

    public FileGpxListAdapter(Context context, List<File> files, ImportaFileGPXController controller) {
        super(context, R.layout.list_element_risultato_ricerca_punto,files);

        this.importaFileGPXController = controller;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        File file = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.list_element_file, parent, false);
        }

        ImageView imageView_fileIcon = convertView.findViewById(R.id.listElementFile_imageView_iconaFile);
        TextView textView_fileName =  convertView.findViewById(R.id.listElementFile_textView_nomeFile);
        RelativeLayout relativeLayout_listItem = convertView.findViewById(R.id.listElementFile_relativeLayout_listElement);


        if(file.isDirectory()) imageView_fileIcon.setImageDrawable(getContext().getDrawable(R.drawable.ic_directory));
        else imageView_fileIcon.setImageDrawable(getContext().getDrawable(R.drawable.ic_file));

        textView_fileName.setText(file.getName());

        relativeLayout_listItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(file.isDirectory()){
                    Log.i("LIST:", "directory cliccata");
                    importaFileGPXController.openDirectory(file);
                    return;
                }

                Log.i("LIST:", "file cliccato");
                boolean result = importaFileGPXController.readGPXFile(file);
                if(result) Log.d("LISTADAPTGPX", "conversione effettuata");
                Log.d("LISTADAPTGPX", "conversione NON effettuata");
            }
        });
        return convertView;
    }

}
