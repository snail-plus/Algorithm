package com.whtriples.airPurge.api.param;

public abstract class AbstractParam implements Param {

	@Override
	public Param add() {
		throw new IllegalAccessError("Current param type not support this method!");
	}

	@Override
	public Param add(String name) {
		throw new IllegalAccessError("Current param type not support this method!");
	}

	@Override
	public Param end() {
		throw new IllegalAccessError("Current param type not support this method!");
	}

	@Override
	public Param attr(String name, String text) {
		throw new IllegalAccessError("Current param type not support this method!");
	}

	@Override
	public Param array(String name) {
		throw new IllegalAccessError("Current param type not support this method!");
	}

	@Override
	public Param arrayEnd() {
		throw new IllegalAccessError("Current param type not support this method!");
	}

	@Override
	public Param addValue(String text) {
		throw new IllegalAccessError("Current param type not support this method!");
	}

	@Override
	public Param addValue(byte text) {
		throw new IllegalAccessError("Current param type not support this method!");
	}

	@Override
	public Param addValue(short text) {
		throw new IllegalAccessError("Current param type not support this method!");
	}

	@Override
	public Param addValue(int text) {
		throw new IllegalAccessError("Current param type not support this method!");
	}

	@Override
	public Param addValue(long text) {
		throw new IllegalAccessError("Current param type not support this method!");
	}

	@Override
	public Param addValue(float text) {
		throw new IllegalAccessError("Current param type not support this method!");
	}

	@Override
	public Param addValue(double text) {
		throw new IllegalAccessError("Current param type not support this method!");
	}

	@Override
	public Param addValue(boolean text) {
		throw new IllegalAccessError("Current param type not support this method!");
	}

	@Override
	public Param set(String name, String text) {
		throw new IllegalAccessError("Current param type not support this method!");
	}

	@Override
	public Param set(String name, byte text) {
		throw new IllegalAccessError("Current param type not support this method!");
	}

	@Override
	public Param set(String name, short text) {
		throw new IllegalAccessError("Current param type not support this method!");
	}

	@Override
	public Param set(String name, int text) {
		throw new IllegalAccessError("Current param type not support this method!");
	}

	@Override
	public Param set(String name, long text) {
		throw new IllegalAccessError("Current param type not support this method!");
	}

	@Override
	public Param set(String name, float text) {
		throw new IllegalAccessError("Current param type not support this method!");
	}

	@Override
	public Param set(String name, double text) {
		throw new IllegalAccessError("Current param type not support this method!");
	}

	@Override
	public Param set(String name, boolean text) {
		throw new IllegalAccessError("Current param type not support this method!");
	}

}
