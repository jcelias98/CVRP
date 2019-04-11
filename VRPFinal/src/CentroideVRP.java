import java.util.ArrayList;


/**
 * 
 * @author Juan
 * A classe CentroideVRP t�m a lista de clusters criados no algoritmo e a inst�ncia do problema que vai ser resolvido
 */
public class CentroideVRP {
	
	private ArrayList <Cluster> clusters = new ArrayList <Cluster>();
	private InstanceProblem instance;
	
	
	public CentroideVRP(InstanceProblem instance) {
		this.instance = instance;
	}
	
	public ArrayList<Cluster> getClusters() {
		return clusters;
	}

	public void setClusters(ArrayList<Cluster> clusters) {
		this.clusters = clusters;
	}

	/**
	 * Algoritmo baseado em Centroide aplicado para a inst�ncia do problema de roteamento de ve�culos capacitado
	 * @param maxIterSimulated 
	 * @return melhor solu��o com as rotas e o custo total
	 */
	public Solution runCentroideVRP(int maxIterSimulated) {
		constructCluster (); //fase 1: constru��o dos clusters
		Solution solution = aplyingTSPClusters(maxIterSimulated); //encontrar a melhor solu��o resolvendo o TSP em cada cluster
		Solution bestSolution = solution;
		
		do {
			//fase 2: ajustamento de clusters
			boolean resp = clusterAdjustment();
			if (!resp) {
				break;
			}
			else {
				 //se os clusters foram ajustados, resolveu-se o TSP usando Simulated Annealing para cada cluster 
                 solution = aplyingTSPClusters(maxIterSimulated); 
                 if (solution.getTotalCost() < bestSolution.getTotalCost()){
                	 bestSolution = solution;
                 }
 			}
				
		} while (true);
		return bestSolution;
	}
	
	/**
	 * Resolve o TSP usando Simulated Annealing para cada um dos clusters
	 * @param maxIterSimulated
	 * @return todas as rotas e o custo total de todas as rotas
	 */
	public Solution aplyingTSPClusters(int maxIterSimulated){
		Solution solution =  new Solution();
		
        double totalCost = 0;
		for (int i = 0; i < clusters.size(); i++) {
			Tsp tspi = new Tsp(instance, clusters.get(i).getCustomers());
			//para o cluster i � chamado o Simulated Annealing para encontrar a rota com o melhor custo
			double bestCost = tspi.simulatedAnnealing();
			tspi.getRoute().setCostRoute(bestCost);
			Route bestRoute=tspi.getRoute();
			
			/*Como o Simulated Annealing pode dar resultados diferentes para cada chamada devido aos valores rand�micos que s�o
			 * usados, chamamos o Simulated Annealing maxIterSimulated vezes e peagamos a melhor rota encontra nessas maxIterSimulated
			 * itera��es
			 * */
			for (int j = 1; j < maxIterSimulated; j++){
				double cost = tspi.simulatedAnnealing();
				tspi.getRoute().setCostRoute(cost);
	            if (cost<bestCost){
	            	bestCost=cost;
	            	bestRoute = tspi.getRoute();
	            }
			}
			
			solution.addRoute(bestRoute);
			totalCost = totalCost + bestCost ;
		}
		solution.setTotalCost(totalCost);
		return solution;
	}
	
	
	
    //para gerar a semente de cada cluster � encontrado o cliente que mais distante do dep�sito e que ainda n�o foi clusterizado
	public Customer findFarthestUnClusteredCustomerToDepot () {
		Customer farthestCustomer = null;
		double farthestDistance = 0;
		double distanceToDepot = 0;
		int idDepot = instance.getIdDepot();
		
		for (int i = 0; i < instance.getNumberOfCustomers(); i++) {
			Customer c = instance.getCustomers()[i];
			if (i+1 == idDepot || c.isInCluster() == true) { 
				continue;
			}
			else {
				/*se o cliente c n�o est� no cluster e n�o � o dep�sito, ent�o pega-se da matriz de adjac�ncia
				 *  a dist�ncia ao dep�sito*/
				distanceToDepot = instance.getAdjacencyMatrix()[i][idDepot-1];
				if (distanceToDepot > farthestDistance) {
					farthestDistance = distanceToDepot;
					farthestCustomer = c;
				}
			}
			
		}
		return farthestCustomer;
		
	}
	
	//Fase 1: construi todos os clusters da int�ncia do problema
    public void constructCluster () {
		
		int Q = Truck.capacity;
		//enquanto ainda existam clientes ainda n�o visitados, contruir os clusters
		while (instance.getVisitedCustomers().size() < (instance.getNumberOfCustomers()-1)) {
			Customer vj = findFarthestUnClusteredCustomerToDepot (); //vj ser� a semente do cluster
			Cluster l = new Cluster ();
			//o cluster l � criado e adicionado ao conjunto de clusters
			clusters.add(l);
			l.setAvailableCapacity(Q);
			//enquanto ainda existam clientes ainda n�o visitados e a demanda do cliente n�o ultrapassa a capacidade restante dispon�vel no ve�culo  
			while (instance.getVisitedCustomers().size() < (instance.getNumberOfCustomers()-1) && l.getAvailableCapacity() >= vj.getDemand()) {
				// ent�o o cliente vj � adicionado no cluster e o centro geom�trico � recalculado
				l.addCustomerToCluster (vj, instance);
				l.setAvailableCapacity(l.getAvailableCapacity() - vj.getDemand());
				l.setGCx(l.computeGCx());
				l.setGCy(l.computeGCy());
				vj = l.findClosestUnClusteredCustomerToGC (instance);
			}
		}
    }
    
    //Fase 2: ajustamento dos clusters gerados na fase 1
    public boolean clusterAdjustment () {
    	boolean doAdjustment = false;
    	for (int i = 0; i < clusters.size(); i++) {
    		Cluster li=clusters.get(i);
    		/* Verifica-se para cada cliente i do cluster li se ele est� mais pr�ximo do centroide 
    		 * do cluster vizinho lk do que o centroide de seu cluster atual. 
    		 * Caso isso ocorra e a capacidade do cluster vizinho n�o seja ultrapassada, ent�o
    		 * esse cliente vai para o cluster vizinho e os centroides de ambos grupos s�o recalculados.  */
    		for (int k=0; k< li.getCustomers().size();k++) {
    			Customer vk=li.getCustomers().get(k);
    			for (int j = 0; j < clusters.size(); j++) {
    				Cluster lj = clusters.get(j);
    				
    				double distanceVkToGCLj = vk.euclideanDistance (lj.getGCx(), lj.getGCy());
    				double distanceVkToGCLi = vk.euclideanDistance (li.getGCx(), li.getGCy());
    				if (i != j && distanceVkToGCLj < distanceVkToGCLi && lj.getAvailableCapacity() >= vk.getDemand()) {
    					li.getCustomers().remove(vk);
    					lj.getCustomers().add(vk);
    					li.setAvailableCapacity(li.getAvailableCapacity() + vk.getDemand());
    					lj.setAvailableCapacity(lj.getAvailableCapacity() - vk.getDemand());
    					
    					// recompute geometric center
    					li.setGCx(li.computeGCx());
    					li.setGCy(li.computeGCy());
    					
    					lj.setGCx(lj.computeGCx());
    					lj.setGCy(lj.computeGCy());
    					
    					
    					doAdjustment = true;
    				}
    			}
    		}
    	}
    	return doAdjustment;
    }

}
