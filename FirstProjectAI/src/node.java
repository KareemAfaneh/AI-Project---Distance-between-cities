/*
 * Project : Search Algorithms for Route Navigation
 * Course : AI
 *	Authors: 
 *	1- Name: Kareem Afaneh 		id: 1190359
 *	1- Name: Ruba Ayed 			id: 1190445
*/
import java.util.ArrayList;
import java.util.HashMap;

// this class is the node class, which represent each city as a separated object with different attributes  
public class node {
	private String CityName;															// the city name 
	private ArrayList<String> connectedCities=new ArrayList<String>();					// array list that saves the connected cities
	HashMap<String, int[]> CarAndWalkingDistances = new HashMap<String, int[]>();		// hash map that saves the car and walking distances
	private int heuristic1=0;	// aerial distance heuristic
	private int heuristic2=0;	// walking distance heuristic
	
	public node(String CityName) {
		this.CityName=CityName;
	}

	public String getCityName() {
		return CityName;
	}

	public void setCityName(String cityName) {
		CityName = cityName;
	}

	public ArrayList<String> getConnectedCities() {
		return connectedCities;
	}
	// to add a new city to the connected cities with the current city node
	public void AddConnectedCity(String connectedCity,int CarDistance,int WalkingDistance) {
		this.connectedCities.add(connectedCity);
		int []tmp = new int[2];
		tmp[0]=CarDistance;
		tmp[1]=WalkingDistance;
		CarAndWalkingDistances.put(connectedCity,tmp);
	}

	public int[] getCarAndWalkingDistances(String city) {
		return CarAndWalkingDistances.get(city);
	}

	public int getHeuristic1() {
		return heuristic1;
	}

	public void setHeuristic1(int heuristic1) {
		this.heuristic1 = heuristic1;
	}

	public int getHeuristic2() {
		return heuristic2;
	}

	public void setHeuristic2(int heuristic2) {
		this.heuristic2 = heuristic2;
	}
	public int getHeurisitc(int Heurisitc){
		if(Heurisitc==1)
			return heuristic1;
		else 
			return heuristic2;
	}
	public int getCarORWalkingDistances(String city,int Heurisitc) {
		if(Heurisitc==1)
			return CarAndWalkingDistances.get(city)[1];
		else 
			return CarAndWalkingDistances.get(city)[0];
	}
}
