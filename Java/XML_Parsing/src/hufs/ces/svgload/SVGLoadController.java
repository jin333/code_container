package hufs.ces.svgload;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import hufs.ces.grimpan.svg.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;


public class SVGLoadController extends AnchorPane {
	public Stage parentStage = null;

	boolean hasState = false;
	private final String defaultDir = "C:/";
	private String currDir = defaultDir;
	private SVGLoadController SVG;
	public double drawPaneH = 0;
	public double drawPaneW = 0;

	DrawPane drawPane = null;
	File selFile = null;
	long totPathCount = 0;


	public SVGLoadController() {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hufs/ces/svgload/svgload.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		SVG = this;
		try {
			fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		initialize();
	}

	private void initialize() {
		treeView.prefWidthProperty().bind(apLeft.widthProperty());
		treeView.prefHeightProperty().bind(apLeft.heightProperty());

		textArea.prefHeightProperty().bind(apRight.heightProperty());
		textArea.prefWidthProperty().bind(apRight.widthProperty());

		drawPane = new DrawPane();
		drawPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		apRight.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		apRight.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		apRight.widthProperty().addListener((e)->{
			if(drawPaneW < apRight.getWidth()) drawPane.setPrefWidth(apRight.getWidth());
			//drawPane.setPrefWidth(apRight.getWidth());
			textArea.setPrefWidth(apRight.getWidth());
		});
		apRight.heightProperty().addListener((e)->{
			if(drawPaneH < apRight.getHeight()) drawPane.setPrefHeight(apRight.getHeight());
			//drawPane.setPrefHeight(apRight.getHeight());
			textArea.setPrefHeight(apRight.getHeight());
		});








	}
	void initDrawPane() {
		textArea.clear();
		drawPane.shapeList.clear();
		drawPane.shapeList = FXCollections.observableArrayList();
		drawPane.shapeList.addListener((ListChangeListener.Change<? extends SVGGrimShape> change) ->{
			Platform.runLater(() -> {
						while(change.next()){
							if(change.wasAdded()){
								for (SVGGrimShape gsh:change.getAddedSubList()){
									drawPane.getChildren().add(gsh.getShape());
								}
							}
						}
					}
			);
		});
		//drawPane.redraw();
	}

	@FXML
	private MenuItem mnOpen;

	@FXML
	private MenuItem mnLoad;

	@FXML
	private MenuItem mnExit;

	@FXML
	private MenuItem mnAbout;

	@FXML
	private AnchorPane apLeft;

	@FXML
	private TreeView<NamedNode> treeView;

	@FXML
	private ScrollPane apRight;

	@FXML
	private TextArea textArea;

	@FXML
	private Label lblFilePath;

	@FXML
	private ProgressBar progressBar;

	@FXML
	private Label lbFilePercent;

	@FXML
	private Label lbFunction;



	@FXML
	void handleAbout(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setHeaderText("SVG Load Ver 0.2.2");
		alert.setContentText("Programmed by cskim, ces, hufs.ac.kr");

		alert.showAndWait();
	}

	@FXML
	void handleExit(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	void handleLoad(ActionEvent event) {
		if(hasState){
			System.out.println("Other thread is working");
			return;
		}else if(selFile == null){
			System.out.println("selFile is NULL");
			return;
		}
		readShapeFromSVGSaveFile();
	}

	@FXML
	void handleOpen(ActionEvent event) {
		if(hasState){ System.out.println("Other thread is working");return; }
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open XML File");
		fileChooser.setInitialDirectory(new File(currDir));
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("SVG File (*.svg)", "*.svg", "*.SVG"),
				new ExtensionFilter("XML File (*.xml)", "*.xml", "*.XML"));

		File testFile = fileChooser.showOpenDialog(parentStage);

		if (testFile == null) return;

		drawPane.getChildren().clear();
		hasState = true;
		selFile = testFile;
		apRight.setContent(textArea);
		apRight.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		apRight.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		lblFilePath.setText(" File: "+selFile.getPath());

		if (selFile.isFile()) {
			SVGParseTaskFOROPEN parseTask = new SVGParseTaskFOROPEN(treeView);
			parseTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
					new EventHandler<WorkerStateEvent>() {
						@Override
						public void handle(WorkerStateEvent t) {
							hasState = false;
						}
					});
			String fileName = selFile.getName();
			parentStage.setTitle("SVG OPEN - "+fileName);
			this.initDrawPane();
			// Start the Task.
			new Thread(parseTask).start();
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
	@FXML
	void handleTreeViewClicked(MouseEvent event) {
		if(hasState){ System.out.println("Other thread is working");return; }
		Node node = event.getPickResult().getIntersectedNode();
		//System.out.println(node);
		if (node instanceof Text) {
			NamedNode selnode = (NamedNode) ((TreeItem<NamedNode>)treeView.getSelectionModel().getSelectedItem()).getValue();
			System.out.println("Node click: " + selnode);
			String pathDef = selnode.attrMap.get("d");
			String styleDef = selnode.attrMap.get("style");
			SVGPath svgPath = new SVGPath();
			SVGUtils.paintShapeWithSVGStyle(svgPath, styleDef);
			svgPath.setContent(pathDef);
			drawPane.shapeList.add(new SVGGrimPath(SVGUtils.convertSVGPathToPath(svgPath)));
			//drawPane.redraw();
		}
	}
	void readShapeFromSVGSaveFile() {
		hasState = true;
		drawPane.clear();
		apRight.setContent(drawPane);
		apRight.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		apRight.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);


		SVGParseTask parseTask = new SVGParseTask();
		parseTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent t) {
						hasState = false;
					}
				});


		String fileName = selFile.getName();
		parentStage.setTitle("SVG Load - "+fileName);
		this.initDrawPane();

		// Start the Task.
		new Thread(parseTask).start();


	}

	public class SVGParseTask extends Task<ObservableList<SVGGrimShape>> {
		int size;
		double index=1;
		SVGParseTask thisTask = this;
		public ObservableList<SVGGrimShape> gshapeList = FXCollections.observableArrayList();
		public SVGParseTask() {

			totPathCount = countPathNode();
			lbFunction.setText("OPEN");
			lbFilePercent.setText("0%");
			progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);

			gshapeList.addListener((ListChangeListener.Change<? extends SVGGrimShape> change) ->{
				size = countPathNode();
				while(change.next()){
					if(change.wasAdded()){
						//System.out.println(drawPane.shapeList.size()+" "+ totPathCount);
						Platform.runLater(() -> {
							lbFunction.setText("Load");
							if((index/size) <= 1){
								lbFilePercent.setText(
										String.format("%3s",(int)((double) drawPane.shapeList.size()/ totPathCount*100)+"%"));
								progressBar.setProgress(index/size);
								index++;
							}
						});
						drawPane.shapeList.addAll(change.getAddedSubList());
					}
				}
			});
		}
		@Override
		protected ObservableList<SVGGrimShape> call() throws Exception {
			SaxSVGPathParseHandlerFORLOAD saxTreeHandlerFORLOAD = new SaxSVGPathParseHandlerFORLOAD(this);
			try {
				SAXParserFactory saxf = SAXParserFactory.newInstance();
				SAXParser saxParser = saxf.newSAXParser();
				saxParser.parse(new InputSource(new FileInputStream(selFile)), saxTreeHandlerFORLOAD);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return gshapeList;
		}

	}
	public class SVGParseTaskFOROPEN extends Task<ObservableList<SVGGrimShape>> {
		int size;
		double index;
		TreeView<NamedNode> treeView;
		SVGParseTaskFOROPEN thisTask = this;

		public ObservableList<SVGGrimShape> gshapeList = FXCollections.observableArrayList();
		public SVGParseTaskFOROPEN(TreeView<NamedNode> tv) {
			treeView = tv;
			index = 0;
			size = countPathNode()+1;
			lbFunction.setText("OPEN");
			lbFilePercent.setText("0%");


			textArea.textProperty().addListener((e)->{
				if(index <= size){
					Platform.runLater(() -> {
						if((index/size) <= 1){
							lbFilePercent.setText(
									String.format("%3s",(int)(index/size*100.0)+"%"));
							progressBar.setProgress(index/size);
							index++;
						}
					});
				}
			});
		}
		@Override
		protected ObservableList<SVGGrimShape> call() throws Exception {

			SaxSVGPathParseHandlerFOROPEN saxTreeHandlerFOROPEN = new SaxSVGPathParseHandlerFOROPEN(SVG,this,treeView,selFile,textArea);
			try {
				SAXParserFactory saxf = SAXParserFactory.newInstance();
				SAXParser saxParser = saxf.newSAXParser();
				long start = System.currentTimeMillis();
				System.out.println("saxParser.parse start...");
				saxParser.parse(new InputSource(new FileInputStream(selFile)), saxTreeHandlerFOROPEN);
				System.out.println("saxParser.parse Done : "+((System.currentTimeMillis()-start)/1000.0)+"s");
			}
			catch(Exception e){
				e.printStackTrace();
			}

			return null;
		}

	}


	int countPathNode(){

		Scanner inscan = null;
		int pathNodeCount = 0;
		try {
			inscan = new Scanner(selFile);
			String inline = "";
			while (inscan.hasNext()){
				inline = inscan.nextLine();
				if (inline.indexOf("path")>0)
					pathNodeCount++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		inscan.close();
		return pathNodeCount;
	}
}
