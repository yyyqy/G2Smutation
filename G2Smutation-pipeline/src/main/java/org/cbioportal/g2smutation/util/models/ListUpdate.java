package org.cbioportal.g2smutation.util.models;

import java.util.List;

public class ListUpdate {
    private List<String> listOld;
    private List<String> listNew;

    public List<String> getListOld() {
        return listOld;
    }

    public void setListOld(List<String> listOld) {
        this.listOld = listOld;
    }

    public List<String> getListNew() {
        return listNew;
    }

    public void setListNew(List<String> listNew) {
        this.listNew = listNew;
    }

}
