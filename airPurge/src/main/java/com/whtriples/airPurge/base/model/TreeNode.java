package com.whtriples.airPurge.base.model;

import java.util.List;

public class TreeNode {
	private String id;
	private String text;
	private boolean expanded;
	private List<TreeNode> children;
    private String parent_code;
    private String tree_level;
    
    public TreeNode(String id,String text,String parent_code){
    	this.id=id;
    	this.text=text;
    	this.parent_code=parent_code;
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

	

	public TreeNode() {
		super();
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public String getParent_code() {
		return parent_code;
	}

	public void setParent_code(String parent_code) {
		this.parent_code = parent_code;
	}

	public String getTree_level() {
		return tree_level;
	}

	public void setTree_level(String tree_level) {
		this.tree_level = tree_level;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	
}
