package com.whtriples.airPurge.api.param;

public interface Param {
	
	public static final boolean DEFAULT_FORMAT = false;
	
	public abstract Param add();
	public abstract Param add(String name);
	public abstract Param end();
	public abstract Param attr(String name, String text);
	public abstract Param array(String name);
	public abstract Param arrayEnd();
	
	public abstract Param addValue(String text);
	public abstract Param addValue(byte text);
	public abstract Param addValue(short text);
	public abstract Param addValue(int text);
	public abstract Param addValue(long text);
	public abstract Param addValue(float text);
	public abstract Param addValue(double text);
	public abstract Param addValue(boolean text);
	
	public abstract Param set(String name, String text);
	public abstract Param set(String name, byte text);
	public abstract Param set(String name, short text);
	public abstract Param set(String name, int text);
	public abstract Param set(String name, long text);
	public abstract Param set(String name, float text);
	public abstract Param set(String name, double text);
	public abstract Param set(String name, boolean text);
	
	public abstract String toString();
	public abstract String toString(boolean format);

}
