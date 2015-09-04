package com.whtriples.airPurge.config;

import java.util.List;

public class ComboxModel {

    private String id;
    private String text;
    private String pid;
    
    private List<ComboxModel> children;

    public List<ComboxModel> getChildren() {
        return children;
    }

    public void setChildren(List<ComboxModel> children) {
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

}
