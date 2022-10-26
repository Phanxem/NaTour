package com.unina.natour.config.observers;

public interface Observable {

    public void registerObserver(Observer observer);
    public void undergisterObserver(Observer observer);
    public void notifyObservers();


}
