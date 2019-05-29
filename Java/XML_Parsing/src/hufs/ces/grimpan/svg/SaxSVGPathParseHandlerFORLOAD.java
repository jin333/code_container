package hufs.ces.grimpan.svg;


import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import hufs.ces.svgload.SVGLoadController;
import javafx.collections.ObservableList;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.SVGPath;

public class SaxSVGPathParseHandlerFORLOAD extends DefaultHandler{

	private SVGLoadController.SVGParseTask task;
	public SaxSVGPathParseHandlerFORLOAD(SVGLoadController.SVGParseTask task){
		this.task = task;
	}
	@Override
	public void startElement(String uri, String qName, String lName, Attributes atts){
		//System.out.println("start lName="+lName);
		if (lName.equals("path")){
			SVGPath svgPath = new SVGPath();
			String pathDef = atts.getValue("d");
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
			task.gshapeList.add(new SVGGrimPath(SVGUtils.convertSVGPathToPath(svgPath)));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TOpath node count=DO Auto-generated catch block
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
			task.gshapeList.add(new SVGGrimLine(line));
		}
		else if (lName.equals("ellipse")) {
			double cx = Double.parseDouble(atts.getValue("cx"));
			double cy = Double.parseDouble(atts.getValue("cy"));
			double rx = Double.parseDouble(atts.getValue("rx"));
			double ry = Double.parseDouble(atts.getValue("ry"));
			Ellipse ellipse = new Ellipse(cx, cy, rx, ry);
			String styleDef = atts.getValue("style");
			SVGUtils.paintShapeWithSVGStyle(ellipse, styleDef);
			task.gshapeList.add(new SVGGrimEllipse(ellipse));
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
			task.gshapeList.add(new SVGGrimPolygon(polygon));
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
			task.gshapeList.add(new SVGGrimPolyline(polyline));
		}
		
	}
}
