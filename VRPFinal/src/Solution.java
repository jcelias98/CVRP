import java.util.ArrayList;

/**
 * 
 * @author Juan
 * A classe Solution tem o conjunto de todas as rotas calculadas pelo algoritmo e o custo total encontrado
 */
public class Solution {
	
	private ArrayList <Route> routes = new ArrayList <Route>(); 
	private double totalCost=0;

	public void addRoute (Route route){
		routes.add(route);
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}
	

	public ArrayList<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(ArrayList<Route> routes) {
		this.routes = routes;
	}

	@Override
	public String toString() {
		return routes + "\ntotalCost=" + totalCost + "\n";
	}
	
}
