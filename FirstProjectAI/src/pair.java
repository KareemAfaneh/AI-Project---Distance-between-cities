/*
 * Project : Search Algorithms for Route Navigation
 * Course : AI
 *	Authors: 
 *	1- Name: Kareem Afaneh 		id: 1190359
 *	1- Name: Ruba Ayed 			id: 1190445
*/

// this class is the pair class, which represent the object used in saving the necessary informations to connect nodes in get the path correctly 
public class pair {
    private String parentCity;		// the parent city of the node
    private String CurrentCity;		// the current city name
    private int parentIndex;		// the parent city index in expanded array list in main class
    private int totalCost;			// the total cost (used in A* algorithm to reach the shortest path)
    public pair(String parentCity, String CurrentCity,int parentIndex,int totalCost)
    {
        this.parentCity = parentCity;
        this.CurrentCity = CurrentCity;
        this.parentIndex = parentIndex;
        this.totalCost = totalCost;
    }

    public String getParentCity() {
        return parentCity;
    }

    public void setParentCity(String parentCity) {
        this.parentCity = parentCity;
    }

    public String getCurrentCity() {
        return CurrentCity;
    }

    public void setCurrentCity(String CurrentCity) {
        this.CurrentCity = CurrentCity;
    }

	public int getParentIndex() {
		return parentIndex;
	}

	public void setParentIndex(int parentIndex) {
		this.parentIndex = parentIndex;
	}

	public int getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}
    }