package com.unina.natour.models;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImportaFileGPXModel extends NaTourModel {

    private File currentDirectory;
    private List<File> files;


    public ImportaFileGPXModel(){
        super();

        this.files = new ArrayList<File>();
    }


    public void set(File currentDirectory, List<File> files){
        this.currentDirectory = currentDirectory;
        this.files.clear();
        this.files.addAll(files);

        notifyObservers();
    }

    public File getCurrentDirectory() {
        return this.currentDirectory;
    }

    public void setCurrentDirectory(File currentDirectory){
        this.currentDirectory = currentDirectory;
    }

    public boolean hasParentDirectory() {
        if(currentDirectory == null) return false;
        //if(currentDirectory.equals(Environment.getExternalStorageDirectory())) return false;

        File parent = currentDirectory.getParentFile();
        if(parent == null) return false;
        if(parent.isHidden() || !parent.canRead()) return false;
        if(!parent.isDirectory()) return false;
        return true;
    }

    public File getParentDirectory() {
        if(currentDirectory == null) return null;
        return currentDirectory.getParentFile();
    }

    public List<File> getFiles() {
        return files;
    }


    public void clear(){
        this.currentDirectory = null;
        this.files.clear();
    }
}
