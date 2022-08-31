/*
 * Project : Search Algorithms for Route Navigation
 * Course : AI
 *	Authors: 
 *	1- Name: Kareem Afaneh 		id: 1190359
 *	1- Name: Ruba Ayed 			id: 1190445
*/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// this class represent the main class in our project, which contains the interface and the algorithms built
public class main extends Application {
	
	HashMap<String, node> Cities = new HashMap<String, node>(); // to save the cities as nodes in a hash map based on city name (string)
	String GoalCity;	// string of the goal city
	String StartCity;	// string of the start city
	int Heuristic=1;	// this variable to determine the heuristic chosen in A*
	ArrayList<pair> expandedCities=new ArrayList<pair>();	// this array list used in all algorithms to detect the visited cities 
	ArrayList<String> path=new ArrayList<String>();			// this array list used in all algorithms to save the path from start city to goal city
	ArrayList<pair> fringe=new ArrayList<pair>(); 			// this array list used in all algorithms to save the potential cities to visit
	ArrayList<String> VisitedDFS=new ArrayList<String>();	// this array list used in DFS algorithm to avoid loops
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ReadEdges();
		GridPane root = new GridPane();
		BorderPane border = new BorderPane();
		root.setPadding(new Insets(10,10,10,10));
		root.setAlignment(Pos.CENTER);
		root.setVgap(20);
		root.setHgap(15);
		border.setBackground(new Background(new BackgroundFill(new RadialGradient(
		        0, 0, 0, 0, 1, true,                  	//sizing
		        CycleMethod.NO_CYCLE,              	   	//cycling
		        new Stop(0, Color.YELLOW),    			//colors
		        new Stop(1, Color.LIGHTBLUE)), CornerRadii.EMPTY, Insets.EMPTY)));
		ComboBox<String> cb = new ComboBox<>();
		cb.setPrefWidth(200);
		cb.getItems().addAll("A* with Heuristic1" ,"A* with Heuristic2","DFS","BFS");
		ComboBox<String> GoalBox = new ComboBox<>();
		GoalBox.setPrefWidth(200);
		GoalBox.getItems().addAll("A","B","D","G");
		//GoalBox.getItems().addAll("Aka","Bethlehem","Dura","Haifa","Halhoul","Hebron","Jenin","Jericho","Jerusalem","Nablus","Nazareth","Qalqilya","Ramallah","Ramleh","Sabastia","Safad","Salfit","Tubas","Tulkarm","Yafa"	);
		ComboBox<String> StartBox = new ComboBox<>();
		StartBox.setPrefWidth(200);
		StartBox.getItems().addAll("A","B","D","G");
		//StartBox.getItems().addAll("Aka","Bethlehem","Dura","Haifa","Halhoul","Hebron","Jenin","Jericho","Jerusalem","Nablus","Nazareth","Qalqilya","Ramallah","Ramleh","Sabastia","Safad","Salfit","Tubas","Tulkarm","Yafa"	);
		Text titleP = new Text();
		titleP.setText("Welcome to our AI Project");
		titleP.setFont(Font.font ("Arial", 40));
		titleP.setFill(Color.BROWN);
		Text titleD = new Text();
		titleD.setText("This Project Done By: Kareem Afaneh & Ruba Ayed\n\t\t\tThanks for using");
		titleD.setFont(Font.font ("Arial", 20));
		titleD.setFill(Color.SEAGREEN);
		Label labelP=new Label();
		labelP.setText("Choose Algorithm: ");
		labelP.setFont(Font.font("Arial"));
		Label labelGoal=new Label();
		labelGoal.setText("Choose Goal City: ");
		labelGoal.setFont(Font.font("Arial"));
		Label labelStart=new Label();
		labelStart.setText("Choose Start City: ");
		labelStart.setFont(Font.font("Arial"));
		border.setAlignment(titleP, Pos.CENTER);
		border.setAlignment(titleD, Pos.CENTER);
		Button run=new Button("Run");
		run.setPrefWidth(320);
		root.add(labelStart, 0, 0);
		root.add(StartBox, 1, 0);
		root.add(labelGoal, 0, 1);
		root.add(GoalBox, 1, 1);
		root.add(labelP, 0, 2);
		root.add(cb, 1, 2);
		root.add(run, 0, 3,2,2);
		// when click run check if the values was chosen, and then start the simulate the algorithm chosen 
		run.setOnAction(e->{
			try {
				if(GoalBox.getValue() != null && StartBox.getValue() != null && cb.getValue() != null){
					StartCity=StartBox.getValue();
					GoalCity=GoalBox.getValue();
					ReadNodes();
					expandedCities.clear();
					if(cb.getValue().equals("A* with Heuristic1")) {
						Heuristic=1;
						AStarAlgorithm();
					}
					else if(cb.getValue().equals("A* with Heuristic2")) {
						Heuristic=2;
						AStarAlgorithm();
					}
					else if(cb.getValue().equals("DFS")){
						DFS();
					}
					else if(cb.getValue().equals("BFS")){
						BFS();
					}
					secondStage();
					fringe.clear();
					expandedCities.clear();
					path.clear();
				}
			}catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		});
		border.setCenter(root);
		border.setTop(titleP);
		border.setBottom(titleD);
		Scene scene = new Scene(border,500,400);
		primaryStage.setTitle("AI Project");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public void secondStage() throws FileNotFoundException{
		GridPane nroot = new GridPane();
		nroot.setVgap(20);
		nroot.setHgap(15);
		nroot.setPadding(new Insets(10,10,10,10));
		nroot.setAlignment(Pos.TOP_LEFT);
		InputStream stream = new FileInputStream("Pal.PNG");
	    Image image = new Image(stream);
	    //Creating the image view
	    ImageView imageView = new ImageView();
	    //Setting image to the image view
	    imageView.setImage(image);
	    //Setting the image view parameters
	    imageView.setX(10);
	    imageView.setY(10);
	    imageView.setFitWidth(220);
	    imageView.setPreserveRatio(true);
	    Label labelPath=new Label();
	    labelPath.setText("The Path from: "+StartCity+" to: "+GoalCity);
	    labelPath.setFont(Font.font("Arial"));
	    Label labelVisited=new Label();
	    labelVisited.setText("The Visited nodes: ");
	    labelVisited.setFont(Font.font("Arial"));
	    TextArea textArea1 = new TextArea();
	    TextArea textArea2 = new TextArea();
	    nroot.setBackground(new Background(new BackgroundFill(new RadialGradient(
		        0, 0, 0, 0, 1, true,                  	//sizing
		        CycleMethod.NO_CYCLE,                 	//cycling
		        new Stop(0, Color.YELLOW),    			//colors
		        new Stop(1, Color.LIGHTBLUE)), CornerRadii.EMPTY, Insets.EMPTY)));
	    String pa="",ex="";
	    for(int i=path.size()-1;i>0;i--)
	    	pa+=(path.get(i)+" --> ");
	    pa+=path.get(0);
	    for(int i=0;i<expandedCities.size()-1;i++)
	    	ex+=(expandedCities.get(i).getCurrentCity()+" --> ");
	    ex+=expandedCities.get(expandedCities.size()-1).getCurrentCity();
	    textArea1.setText(pa);
	    textArea2.setText(ex);
		nroot.add(labelPath, 0, 0);
		nroot.add(textArea1, 0, 1);
		nroot.add(labelVisited, 0, 2);
		nroot.add(textArea2, 0, 3);
		nroot.add(imageView, 1, 0,4,5);
		Scene scene2 = new Scene(nroot,800,600);
		Stage ne= new Stage();
		ne.setTitle("Algorithm Result");
		ne.setScene(scene2);
		ne.show();
	}
	// this method is used to read the Aerial Distance from the chosen goal 
	public void ReadNodes() throws FileNotFoundException {
		File selectedFile = new File("nodesAerialDis.txt");
		Scanner myReader = new Scanner(selectedFile);
		int flag = 1;
		while (myReader.hasNextLine()) {
			String data = myReader.nextLine();
			if (flag != 1) {
				String[] words = data.split("\\s");
				if(words[0].equals(GoalCity)){
					for(int i=1;i<words.length;i++){
						Cities.get(words[i]).setHeuristic1(Integer.parseInt(words[i+1]));
						i++;
					}
					break;
				}
			}
			flag++;
		}
		myReader.close();
		// after reading the aerial distance go and calculate the Heuristic 2 (The shortest path between each node and the goal) 
		findingHeuristic2();
	}
	// in this method we read and save the cities in node class as objects, with the Car distance and walking distance 
	public void ReadEdges() throws FileNotFoundException{
		File selectedFile = new File("edges.txt");
		Scanner myReader = new Scanner(selectedFile);
		int flag = 1; 
		while (myReader.hasNextLine()) {
			String data = myReader.nextLine();
			if (flag!=1){
				String[] words = data.split("\\s");
				if(!(Cities.containsKey(words[0]))){
					node tmpnode=new node(words[0]);
					Cities.put(words[0], tmpnode);
				}
				if(!(Cities.containsKey(words[1]))){
					node tmpnode1=new node(words[1]);
					Cities.put(words[1], tmpnode1);
				}
				Cities.get(words[0]).AddConnectedCity(words[1], Integer.parseInt(words[2]), Integer.parseInt(words[3]));
				Cities.get(words[1]).AddConnectedCity(words[0], Integer.parseInt(words[2]), Integer.parseInt(words[3]));
			}
			flag++;
		}
		myReader.close();
	}
	// finding the heuristic 2 using A* algorithm between each city and goal city
	void findingHeuristic2(){
		for (node i : Cities.values()) {
			String currentCity=i.getCityName();
			if(currentCity.equals(GoalCity))
				Cities.get(currentCity).setHeuristic2(0);
			else {
				int h2Cost=0;
				expandedCities.add(new pair(null,currentCity,-1,0));
				for(String j : Cities.get(currentCity).getConnectedCities()){
					fringe.add(new pair(currentCity,j,expandedCities.size()-1,Cities.get(j).getCarAndWalkingDistances(currentCity)[1]));
				}
				AStarFinderForH2();
				int j=expandedCities.size()-1;
				while(expandedCities.get(j).getParentCity()!=null){
					h2Cost+=Cities.get(expandedCities.get(j).getCurrentCity()).getCarAndWalkingDistances(expandedCities.get(j).getParentCity())[1];
					j=expandedCities.get(j).getParentIndex();
				}
				Cities.get(currentCity).setHeuristic2(h2Cost);
				expandedCities.clear();
				fringe.clear();
			}
		}
	}
	// repeat the algorithm until finding the shortest path
	void AStarFinderForH2(){
		int min=1000000,pointer=0;
		for(int i=0;i<fringe.size();i++){
			if(fringe.get(i).getTotalCost()+Cities.get(fringe.get(i).getCurrentCity()).getHeuristic1()<min){
				min=fringe.get(i).getTotalCost()+Cities.get(fringe.get(i).getCurrentCity()).getHeuristic1();
				pointer=i;
			}
		}
		expandedCities.add(new pair(fringe.get(pointer).getParentCity(),fringe.get(pointer).getCurrentCity(),fringe.get(pointer).getParentIndex(),fringe.get(pointer).getTotalCost()));
		if(fringe.get(pointer).getCurrentCity().equals(GoalCity))
			return;
		for(String j : Cities.get(fringe.get(pointer).getCurrentCity()).getConnectedCities()){
			fringe.add(new pair(fringe.get(pointer).getCurrentCity(),j,expandedCities.size()-1,fringe.get(pointer).getTotalCost()+Cities.get(j).getCarAndWalkingDistances(fringe.get(pointer).getCurrentCity())[1]));
		}
		fringe.remove(pointer);
		AStarFinderForH2();
	}
	// this method is the the general method of the A* algorithm used in our project
	void AStarAlgorithm(){
		// if the start city was the goal city 
		if(StartCity.equals(GoalCity)){
			expandedCities.add(new pair(null,StartCity,-1,0));
			path.add(StartCity);
		}
		else {
			expandedCities.add(new pair(null,StartCity,-1,0));
			for(String j : Cities.get(StartCity).getConnectedCities()){
				fringe.add(new pair(StartCity,j,expandedCities.size()-1,Cities.get(j).getCarORWalkingDistances(StartCity,Heuristic)));
			}
			int PathCost=0;
			AStarFinderOptomalPath();
			int j=expandedCities.size()-1;
			// this while loop is used to get the shortest path using the expanded nodes
			while(expandedCities.get(j).getParentCity()!=null){
				path.add(expandedCities.get(j).getCurrentCity());
				PathCost+=Cities.get(expandedCities.get(j).getCurrentCity()).getCarORWalkingDistances(expandedCities.get(j).getParentCity(),Heuristic);
				j=expandedCities.get(j).getParentIndex();
			}
			path.add(StartCity);
			fringe.clear();
		}
	}
	// repeating the A* algorithm until reaching to the shortest path (use minimum of total cost and heuristic)
	void AStarFinderOptomalPath(){
		int min=1000000,pointer=0;
		for(int i=0;i<fringe.size();i++){
			if(fringe.get(i).getTotalCost()+Cities.get(fringe.get(i).getCurrentCity()).getHeurisitc(Heuristic)<min){
				min=fringe.get(i).getTotalCost()+Cities.get(fringe.get(i).getCurrentCity()).getHeurisitc(Heuristic);
				pointer=i;
			}
		}
		expandedCities.add(new pair(fringe.get(pointer).getParentCity(),fringe.get(pointer).getCurrentCity(),fringe.get(pointer).getParentIndex(),fringe.get(pointer).getTotalCost()));
		if(fringe.get(pointer).getCurrentCity().equals(GoalCity))
			return;
		for(String j : Cities.get(fringe.get(pointer).getCurrentCity()).getConnectedCities()){
			fringe.add(new pair(fringe.get(pointer).getCurrentCity(),j,expandedCities.size()-1,fringe.get(pointer).getTotalCost()+Cities.get(j).getCarORWalkingDistances(fringe.get(pointer).getCurrentCity(), Heuristic)));
		}
		fringe.remove(pointer);
		AStarFinderOptomalPath();
	}
	// this method represent the BFS algorithm in our project 
	public void BFS() {
		// if the start city was the goal city 
		if(StartCity.equals(GoalCity)){
			expandedCities.add(new pair(null,StartCity,-1,0));
			path.add(StartCity);
			return;
		}
		expandedCities.add(new pair(null,StartCity,-1,0));
		for(String j : Cities.get(StartCity).getConnectedCities()){
			fringe.add(new pair(StartCity,j,expandedCities.size()-1,0));
		}
		BFScontinue(1);
		int j=expandedCities.size()-1;
		// this while loop is used to get the  path using the expanded nodes
		while(expandedCities.get(j).getParentCity()!=null){
			path.add(expandedCities.get(j).getCurrentCity());
			j=expandedCities.get(j).getParentIndex();
		}
		path.add(StartCity);
		fringe.clear();
	}
	// repeating the BFS algorithm until reaching to the goal, by increasing the level one by one 
	public void BFScontinue(int index){
		if(fringe.get(index).getCurrentCity().equals(GoalCity)){
			expandedCities.add(new pair(fringe.get(index).getParentCity(),fringe.get(index).getCurrentCity(),fringe.get(index).getParentIndex(),0));
			return;
		}
		expandedCities.add(new pair(fringe.get(index).getParentCity(),fringe.get(index).getCurrentCity(),fringe.get(index).getParentIndex(),0));
		for(String j : Cities.get(fringe.get(index).getCurrentCity()).getConnectedCities()){
			fringe.add(new pair(fringe.get(index).getCurrentCity(),j,expandedCities.size()-1,0));
		}
		BFScontinue(index+1);
	}
	// this method represent the DFS algorithm in our project 
	public void DFS(){
		// if the start city was the goal city 
		if(StartCity.equals(GoalCity)){
			expandedCities.add(new pair(null,StartCity,-1,0));
			path.add(StartCity);
			return;
		}
		expandedCities.add(new pair(null,StartCity,-1,0));
		VisitedDFS.add(StartCity);
		for(String j : Cities.get(StartCity).getConnectedCities()){
			fringe.add(new pair(StartCity,j,expandedCities.size()-1,0));
		}
		DFScontinue();
		int j=expandedCities.size()-1;
		// this while loop is used to get the path using the expanded nodes
		while(expandedCities.get(j).getParentCity()!=null){
			path.add(expandedCities.get(j).getCurrentCity());
			j=expandedCities.get(j).getParentIndex();
		}
		path.add(StartCity);
		fringe.clear();
		VisitedDFS.clear();
	}
	// repeating the DFS algorithm until reaching to the goal, by going to the left of each new node but with saving visited nodes to avoid any loop
	public void DFScontinue(){
		int index=fringe.size()-1;
		if(fringe.get(index).getCurrentCity().equals(GoalCity)){
			expandedCities.add(new pair(fringe.get(index).getParentCity(),fringe.get(index).getCurrentCity(),fringe.get(index).getParentIndex(),0));
			return;
		}
		expandedCities.add(new pair(fringe.get(index).getParentCity(),fringe.get(index).getCurrentCity(),fringe.get(index).getParentIndex(),0));
		// check if the city was visited, don't add its connected cities to the fringe to avoid enter in any loop
		if(!(VisitedDFS.contains(fringe.get(index).getCurrentCity()))){
			VisitedDFS.add(fringe.get(index).getCurrentCity());
			for(String j : Cities.get(fringe.get(index).getCurrentCity()).getConnectedCities()){
				fringe.add(new pair(fringe.get(index).getCurrentCity(),j,expandedCities.size()-1,0));
			}
		}
		fringe.remove(index);
		DFScontinue();
	}
}
