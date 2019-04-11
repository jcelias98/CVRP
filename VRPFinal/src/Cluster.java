import java.util.ArrayList;
import java.util.LinkedList;

public class Cluster {
	
	private double GCx; //coordenada x do centro geom�trico do cluster
	private double GCy; //coordenada y do centro geom�trico do cluster
	private  LinkedList <Customer> customers = new LinkedList <Customer>(); //lista de clientes no cluster
	private int availableCapacity; //capacidade dispon�vel no momento

	public Cluster() {
	}	
	
	@Override
	public String toString() {
		return "Cluster [GCx=" + GCx + ", GCy=" + GCy + ", customers="
				+ customers + ", availableCapacity=" + availableCapacity + "]\n";
	}

	public int getAvailableCapacity() {
		return availableCapacity;
	}

	public void setAvailableCapacity(int availableCapacity) {
		this.availableCapacity = availableCapacity;
	}

	public double getGCx() {
		return GCx;
	}

	public void setGCx(double gCx) {
		GCx = gCx;
	}

	public double getGCy() {
		return GCy;
	}

	public void setGCy(double gCy) {
		GCy = gCy;
	}

	public LinkedList<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(LinkedList<Customer> customers) {
		this.customers = customers;
	}
	
	/**
	 * Calcula qual � o cliente que ainda n�o foi clusterizado que est� mais perto de centro geom�trico do cluster
	 * @param instance
	 * @return cliente mais pr�ximo do GC do cluster
	 */
	public Customer findClosestUnClusteredCustomerToGC (InstanceProblem instance) {
		Customer closestCustomer = null;
		double closestDistance = Double.POSITIVE_INFINITY;
		double distanceToGC = 0;
		int idDepot = instance.getIdDepot();
		
		for (int i = 0; i < instance.getNumberOfCustomers(); i++) {
			Customer c = instance.getCustomers()[i];
			if (i+1 == idDepot || c.isInCluster() == true) { 
				continue;
			}
			else {
				//calcula a dist�ncia do cliente c ao centro geom�trico do cluster
				distanceToGC = c.euclideanDistance (this.GCx, this.GCy);
				if (distanceToGC < closestDistance) {
					closestDistance = distanceToGC;
				    closestCustomer = c;
				}
			}
		}
		return closestCustomer;
		
	}
	
	//calcula o coordenada x do centro geom�trico
	public double computeGCx () {
		int sum = 0;
		for (int i = 0; i < customers.size(); i++) {
			sum = sum + customers.get(i).getX();
		}
		return sum/customers.size();
	}
	
	//calcula o coordenada y do centro geom�trico
	public double computeGCy () {
		int sum = 0;
		for (int i = 0; i < customers.size(); i++) {
			sum = sum + customers.get(i).getY();
		}
		return sum/customers.size();
	}
	
	//adiciona o cliente c ao cluster, e adiciona c ao conjunto de clientes visitados da inst�ncia
	public void addCustomerToCluster (Customer c, InstanceProblem instance) {
		customers.add(c);
		c.setInCluster(true);
		instance.getVisitedCustomers().add(c);
	}

}

