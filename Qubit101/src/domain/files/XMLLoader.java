package domain.files;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


public class XMLLoader {
	private File source;
	private Document DOM;
	
	public XMLLoader(File source) throws ParserConfigurationException, SAXException, IOException{
		if(!source.exists()){
			throw new FileNotFoundException();
		}
		this.source=source;
		DocumentBuilderFactory df =DocumentBuilderFactory.newInstance();
		DocumentBuilder d = df.newDocumentBuilder();
		DOM = d.parse(source);
	}
	
	public XMLLoader(Node n) throws ParserConfigurationException{
		DOM = nodeToDOM(n);
	}
	
	public Document getDOM(){
		return DOM;
	}
	
	public Document nodeToDOM(Node n) throws ParserConfigurationException{
		DocumentBuilderFactory df =DocumentBuilderFactory.newInstance();
		DocumentBuilder d = df.newDocumentBuilder();
		Document dom = d.newDocument();
		dom.appendChild(dom.importNode(n, true));
		return dom;
	}
	
	public File getSource(){
		return source;
	}
	
	public Map<String,String> getAttributes(Node n){
		Map<String,String> m = new TreeMap<String,String>();
		for(int i=0;i<n.getAttributes().getLength();i++){
			Node e = n.getAttributes().item(i);
			m.put(e.getNodeName(), e.getNodeValue());
		}
		return m;
	}
	
}
