package hufs.ces.grimpan.svg;


import hufs.ces.svgload.NamedNode;
import hufs.ces.svgload.SVGLoadController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.shape.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class SaxSVGPathParseHandlerFOROPEN extends DefaultHandler{
	File file ;
	TextArea textArea;
	TreeView<NamedNode> treeView;
	StringBuilder svgSB = new StringBuilder();
	private SVGLoadController SVG ;
	private SVGLoadController.SVGParseTaskFOROPEN task;

	public SaxSVGPathParseHandlerFOROPEN(SVGLoadController SVG, SVGLoadController.SVGParseTaskFOROPEN task, TreeView<NamedNode> tv, File f, TextArea ta){
		this.file = f;
		this.SVG = SVG;

		this.task = task;
		this.textArea = ta;
		this.treeView = tv;
	}
	@Override
	public void startElement(String uri, String qName, String lName, Attributes atts){

		//System.out.println("start lName="+lName);
		SVGPath svgPath = new SVGPath();
		if (lName.equals("svg")){
			svgSB.append("<svg");
			for(int i =0; i < atts.getLength(); i++){
				if (atts.getLocalName(i).equals("height")) {
					System.out.println("drawPane Max H : "+atts.getValue(i).replaceAll("[^0-9]", ""));
					this.SVG.drawPaneH = new Double(atts.getValue(i).replaceAll("[^0-9]", "")) ;
					this.SVG.setPrefHeight(this.SVG.drawPaneH );
				}
				if (atts.getLocalName(i).equals("width")) {
					System.out.println("drawPane Max W : "+atts.getValue(i).replaceAll("[^0-9]", ""));
					this.SVG.drawPaneW = new Double(atts.getValue(i).replaceAll("[^0-9]", "")) ;
					this.SVG.setPrefWidth(this.SVG.drawPaneW);
				}
				svgSB.append(" "+atts.getLocalName(i));
				svgSB.append("=");
				svgSB.append("'"+atts.getValue(i)+"'");
				/*
				System.out.println("atts.getValue(i) : "+atts.getValue(i));
				System.out.println("atts.getQName(i) : "+atts.getQName(i));
				System.out.println("atts.getLocalName(i) : "+atts.getLocalName(i));*/
			}
			svgSB.append("/>");
		}
		else if (lName.equals("path")){
			String pathDef = atts.getValue("d");
			//System.out.println("d="+pathDef);
			String styleDef = atts.getValue("style");
			if (styleDef!=null && !styleDef.equals("")){
				SVGUtils.paintShapeWithSVGStyle(svgPath, styleDef);
			}
			else {
				StringBuilder sb = new StringBuilder();
				styleDef = atts.getValue("fill");
				if (styleDef!=null && !styleDef.equals("")){
					sb.append("fill:");
					sb.append(styleDef);
					sb.append("; ");
				}
				styleDef = atts.getValue("stroke");
				if (styleDef!=null && !styleDef.equals("")){
					sb.append("stroke:");
					sb.append(styleDef);
					sb.append("; ");
				}
				styleDef = atts.getValue("stroke-width");
				if (styleDef!=null && !styleDef.equals("")){
					sb.append("stroke-width:");
					sb.append(styleDef);
					sb.append("; ");
				}
				SVGUtils.paintShapeWithSVGStyle(svgPath, sb.toString());
			}
			svgPath.setContent(pathDef);
			//System.out.println("svgPath="+svgPath);

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (lName.equals("line")) {
			double x1 = Double.parseDouble(atts.getValue("x1"));
			double y1 = Double.parseDouble(atts.getValue("y1"));
			double x2 = Double.parseDouble(atts.getValue("x2"));
			double y2 = Double.parseDouble(atts.getValue("y2"));
			Line line = new Line(x1, y1, x2, y2);
			String styleDef = atts.getValue("style");
			SVGUtils.paintShapeWithSVGStyle(line, styleDef);
		}
		else if (lName.equals("ellipse")) {
			double cx = Double.parseDouble(atts.getValue("cx"));
			double cy = Double.parseDouble(atts.getValue("cy"));
			double rx = Double.parseDouble(atts.getValue("rx"));
			double ry = Double.parseDouble(atts.getValue("ry"));
			Ellipse ellipse = new Ellipse(cx, cy, rx, ry);
			String styleDef = atts.getValue("style");
			SVGUtils.paintShapeWithSVGStyle(ellipse, styleDef);
		}
		else if (lName.equals("polygon")) {
			Polygon polygon = new Polygon();
			String styleDef = atts.getValue("style");
			SVGUtils.paintShapeWithSVGStyle(polygon, styleDef);
			
			String spoints = atts.getValue("points");
			String[] spairs = spoints.split("\\s");
			ObservableList<Double> points = polygon.getPoints();
			for (String pa:spairs) {
				String[] po = pa.split(",");
				points.add(Double.parseDouble(po[0]));
				points.add(Double.parseDouble(po[1]));
			}
		}
		else if (lName.equals("polyline")) {
			Polyline polyline = new Polyline();
			String styleDef = atts.getValue("style");
			SVGUtils.paintShapeWithSVGStyle(polyline, styleDef);
			
			String spoints = atts.getValue("points");
			String[] spairs = spoints.split("\\s");
			ObservableList<Double> points = polyline.getPoints();
			for (String pa:spairs) {
				String[] po = pa.split(",");
				points.add(Double.parseDouble(po[0]));
				points.add(Double.parseDouble(po[1]));
			}
		}
		String temp;
		if (lName.equals("svg")){
			temp = svgSB.toString();
		}else{
			temp = SVGUtils.getSVGElementFromSVGPath(svgPath);
		}
		//System.out.println("temp : "+temp);
		//temp = SVGUtils.getSVGElementFromSVGPath(svgPath);
		//System.out.println("svgPath : "+svgPath.getContent());
		//System.out.println("SVGUtils.getSVGElementFromSVGPath(svgPath) : "+SVGUtils.getSVGElementFromSVGPath(svgPath));
		//Document doc = db.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
		Document doc = null;
		DocumentBuilder builder = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Element root = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new ByteArrayInputStream(temp.getBytes()));
			root = doc.getDocumentElement();
			root.normalize();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		Element finalRoot = root;
		Platform.runLater(() -> {
			if(lName.equals("svg")){
				TreeItem<NamedNode> test = new TreeItem<NamedNode>(new NamedNode(finalRoot));
				treeView.setRoot(test);
				treeView.getRoot().hashCode();
			}else{
				treeView.getRoot().getChildren().add(new TreeItem<NamedNode>(new NamedNode(finalRoot)));
			}
			if(lName.equals("svg")){
				textArea.appendText(temp.toString()+"\n");
			}else{
				textArea.appendText("\t"+temp.toString()+"\n");
			}
		});

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void endElement(String uri, String qName, String lName){
		//System.out.println("start lName="+lName);
		if(lName.equals("svg")){
			textArea.appendText("</svg>");
		}
	}
	private TreeItem<NamedNode> createNodeFromDom(NamedNode node) {
		return new TreeItem<NamedNode>(node) {
			private boolean isLeaf;
			private boolean isFirstTimeChildren = true;
			private boolean isFirstTimeLeaf = true;

			@Override
			public ObservableList<TreeItem<NamedNode>> getChildren() {
				if (isFirstTimeChildren) {
					isFirstTimeChildren = false;

					// First getChildren() call, so we actually go off and
					// determine the children of the File contained in this TreeItem.
					super.getChildren().setAll(buildChildren(node));
				}
				return super.getChildren();
			}

			@Override
			public boolean isLeaf() {
				if (isFirstTimeLeaf) {
					isFirstTimeLeaf = false;
					NamedNode node = (NamedNode) getValue();
					isLeaf = !node.hasChildNodes();
				}

				return isLeaf;
			}

			private ObservableList<TreeItem<NamedNode>> buildChildren(NamedNode node) {
				if (node != null && node.hasChildNodes()) {
					NodeList nodes = node.getChildNodes();
					if (nodes != null) {
						ObservableList<TreeItem<NamedNode>> children = FXCollections.observableArrayList();

						for (int i=0; i<nodes.getLength(); ++i) {
							NamedNode chnode = new NamedNode(nodes.item(i));
							if (chnode.isText() && Pattern.matches("(\\t|\\s)*",chnode.getNodeValue()))
								continue;
							children.add(createNodeFromDom(chnode));
						}

						return children;
					}
				}

				return FXCollections.emptyObservableList();
			}
		};
	}
}
