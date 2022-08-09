package com.unina.natour.views.observers;

public interface Observable {

    public void registerObserver(Observer observer);
    public void undergisterObserver(Observer observer);
    public void notifyObservers();


}
