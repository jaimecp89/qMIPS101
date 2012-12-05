package domain.files;

import java.io.IOException;
import javax.xml.stream.XMLOutputFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;



public class XMLSaver {
	XMLStreamWriter xt;
    private boolean firstLine = true;
	
	public XMLSaver(Document d, File f) throws FileNotFoundException, XMLStreamException, IOException{
		XMLOutputFactory xof = XMLOutputFactory.newInstance();
        File fext = null;
        if(getExtension(f).equalsIgnoreCase(".qcml")){
            fext=f;
        }else
            fext = new File(f.getAbsolutePath()+".qcml");
        xt = xof.createXMLStreamWriter(new FileWriter(fext));
		generateXML(d.getFirstChild(),"");
        xt.flush();
		xt.close();
	}
	
	private void generateXML(Node n, String desp) throws XMLStreamException{
        boolean empty = n.getChildNodes().getLength()==0;
        if(firstLine){
             xt.writeCharacters(desp);
             firstLine=false;
        }
        else
            xt.writeCharacters("\n" + desp);
        if(empty)
            xt.writeEmptyElement(n.getNodeName());
        else
            xt.writeStartElement(n.getNodeName());
		Map<String, String> at = getAttributes(n);
		Iterator<Map.Entry<String, String>> it = at.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, String> s = it.next();
			xt.writeAttribute(s.getKey(), s.getValue());
		}
		for(int i=0;i<n.getChildNodes().getLength();i++){
			if(n.getChildNodes().item(i).getNodeName().equalsIgnoreCase("#text")){
				xt.writeCharacters(n.getChildNodes().item(i).getTextContent());
			}else
				generateXML(n.getChildNodes().item(i),desp+"\t");
		}
        if(!(n.getChildNodes().getLength()==1 && n.getFirstChild().getNodeType()==Node.TEXT_NODE) && !empty)
           xt.writeCharacters("\n"+desp);
        if(!empty)
            xt.writeEndElement();
	}
	
	private Map<String,String> getAttributes(Node n){
		Map<String,String> m = new TreeMap<String,String>();
		for(int i=0;i<n.getAttributes().getLength();i++){
			Node e = n.getAttributes().item(i);
			m.put(e.getNodeName(), e.getNodeValue());
		}
		return m;
	}

    private String getExtension(File f){
        String name = f.getName();
        if(name.lastIndexOf(".")==-1){
            return "";
        }else
            return name.substring(name.lastIndexOf("."),name.length());
    }
}
