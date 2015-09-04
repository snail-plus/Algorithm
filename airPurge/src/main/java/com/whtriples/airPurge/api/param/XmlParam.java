package com.whtriples.airPurge.api.param;

import java.io.IOException;
import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class XmlParam extends AbstractParam implements Param {

	private Document document;

	private Node node;
	
	private XmlParam() {
		super();
	}

	public static XmlParam builder() {
		XmlParam param = new XmlParam();
		Document document = DocumentHelper.createDocument();
		param.document = document;
		param.node = document;
		return param;
	}

	public Param end() {
		node = ((Element) node).getParent();
		return this;
	}

	public Param add(String name) {
		if (node instanceof Element) {
			node = ((Element) node).addElement(name);
		} else {
			node = ((Document) node).addElement(name);
		}
		return this;
	}
	
	public Param attr(String name, String text) {
		((Element) node).addAttribute(name, text);
		return this;
	}

	public Param set(String name, String text) {
		if(text == null) {
			text = "";
		}
		((Element) node).addElement(name).setText(text);
		return this;
	}

	public Param set(String name, byte text) {
		return set(name, String.valueOf(text));
	}

	public Param set(String name, short text) {
		return set(name, String.valueOf(text));
	}

	public Param set(String name, int text) {
		return set(name, String.valueOf(text));
	}

	public Param set(String name, long text) {
		return set(name, String.valueOf(text));
	}

	public Param set(String name, float text) {
		return set(name, String.valueOf(text));
	}

	public Param set(String name, double text) {
		return set(name, String.valueOf(text));
	}

	public Param set(String name, boolean text) {
		return set(name, String.valueOf(text));
	}

	public String toString() {
		return toString(DEFAULT_FORMAT);
	}

	public String toString(boolean format) {
		OutputFormat of = null;
		if (format) {
			of = OutputFormat.createPrettyPrint();
		} else {
			of = OutputFormat.createCompactFormat();
		}

		try {
			StringWriter out = new StringWriter();
			XMLWriter writer = new XMLWriter(out, of);
			writer.write(document);
			writer.flush();

			return out.toString();
		} catch (IOException e) {
			throw new RuntimeException("IOException while generating textual representation: " + e.getMessage());
		}
	}

}
