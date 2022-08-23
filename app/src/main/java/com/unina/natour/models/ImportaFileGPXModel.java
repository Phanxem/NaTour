package com.unina.natour.models;

import android.os.Environment;

import com.unina.natour.views.observers.Observable;
import com.unina.natour.views.observers.Observer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImportaFileGPXModel implements Observable {

    private File currentDirectory;
    private List<File> files;

    private List<Observer> observers;


    public ImportaFileGPXModel(){
        this.files = new ArrayList<File>();
        this.observers = new ArrayList<Observer>();
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


    @Override
    public void registerObserver(Observer observer) {
        if(!observers.contains(observer)) observers.add(observer);
    }

    @Override
    public void undergisterObserver(Observer observer) {
        if(observers.contains(observer)) observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer : observers){
            observer.update();
        }
    }

}
