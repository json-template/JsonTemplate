package com.github.jsontemplate.modelbuild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TypeSpec {

    private String typeName;
    private String singleParam;
    private List<String> listParam = new ArrayList<>();
    private Map<String, String> mapParam = new HashMap<>();

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        if (typeName != null) {
            this.typeName = typeName;
        }
    }

    public String getSingleParam() {
        return singleParam;
    }

    public void setSingleParam(String singleParam) {
        this.singleParam = singleParam;
    }

    public List<String> getListParam() {
        return listParam;
    }

    public void setListParam(List<String> listParam) {
        this.listParam = listParam;
    }

    public Map<String, String> getMapParam() {
        return mapParam;
    }

    public void setMapParam(Map<String, String> mapParam) {
        this.mapParam = mapParam;
    }
}
