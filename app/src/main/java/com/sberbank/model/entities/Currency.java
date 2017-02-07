package com.sberbank.model.entities;

import com.sberbank.model.database.DBColumn;
import com.sberbank.model.database.DBTable;
import com.sberbank.model.database.PrimaryKey;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import static com.sberbank.model.database.DBColumnType.INTEGER;
import static com.sberbank.model.database.DBColumnType.TEXT;

@Root(name = "Valute")
@DBTable(name = "Currency")
public class Currency {
    @PrimaryKey
    @DBColumn(name = "_id", type = INTEGER)
    private Integer dbId;
    @Attribute(name = "ID")
    @DBColumn(name = "ID", type = TEXT)
    private String id;
    @Element(name = "NumCode")
    @DBColumn(name = "NumCode", type = INTEGER)
    private Integer numCode;
    @Element(name = "CharCode")
    @DBColumn(name = "CharCode", type = TEXT)
    private String charCode;
    @Element(name = "Nominal")
    @DBColumn(name = "Nominal", type = INTEGER)
    private Integer nominal;
    @Element(name = "Name")
    @DBColumn(name = "Name", type = TEXT)
    private String name;
    @Element(name = "Value")
    @DBColumn(name = "Value", type = TEXT)
    private String value;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Integer getNumCode() {
        return numCode;
    }
    public void setNumCode(Integer numCode) {
        this.numCode = numCode;
    }
    public String getCharCode() {
        return charCode;
    }
    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }
    public Integer getNominal() {
        return nominal;
    }
    public void setNominal(Integer nominal) {
        this.nominal = nominal;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public Double getValueDouble() {
        try {
            return Double.parseDouble(value.replace(',', '.'));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
