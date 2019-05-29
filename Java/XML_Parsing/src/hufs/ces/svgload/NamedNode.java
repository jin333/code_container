package hufs.ces.svgload;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NamedNode {

	private Node node = null;
	public Map<String,String> attrMap = null;
	
	public NamedNode(Node node) {
		this.node = node;
		if (isElement()) {
			attrMap = new HashMap<>();
			NamedNodeMap nodeMap = node.getAttributes();
			if (nodeMap!=null) {
				for (int i=0; i<nodeMap.getLength(); ++i) {
					attrMap.put(nodeMap.item(i).getNodeName(), nodeMap.item(i).getNodeValue());
				}
			}
		}
	}

    public NamedNode() {

    }

    public Node getNode() {
		return node;
	}
	
	public String getNodeValue() {
		return node.getNodeValue();
	}
	
	public boolean hasChildNodes() {
		return node.hasChildNodes();
	}
	
	public NodeList getChildNodes() {
		return node.getChildNodes();
	}
	
	@Override
	public String toString() {
		if (isText())
			return "Text:"+'"'+node.getNodeValue()+'"';
		else if (isElement()) {
			StringBuilder sb = new StringBuilder();
			sb.append(node.getNodeName());
			sb.append(':');
			sb.append("{ ");
			for (String key:attrMap.keySet()) {
				sb.append(key+":"+attrMap.get(key)+" ");
			}
			sb.append('}');
			return sb.toString();
		}
		return "";
	}
	public boolean isElement() {
		return node.getNodeType()==Node.ELEMENT_NODE;
	}
	public boolean isText() {
		return node.getNodeType()==Node.TEXT_NODE;
	}

}
