package com.unina.natour.models;

public class ElementReportModel extends NaTourModel{
    private long id;
    private String titolo;
    private String dateOfInput;

    public ElementReportModel(){
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDateOfInput() {
        return dateOfInput;
    }

    public void setDateOfInput(String dateOfInput) {
        this.dateOfInput = dateOfInput;
    }

    public void clear(){
        this.id = -1;
        this.titolo = null;
        this.dateOfInput = null;
    }
}
