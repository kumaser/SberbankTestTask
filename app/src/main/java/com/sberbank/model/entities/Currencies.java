package com.sberbank.model.entities;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root
public class Currencies {
    @Attribute(name = "Date")
    private String date;
    @Attribute
    private String name;
    @ElementList(inline = true)
    private List<Currency> currencies = new ArrayList<>();

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Currency> getCurrencies() {
        return currencies;
    }
    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }
}
