import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

/**
 * 
 * @author Juan
 *A classe Tsp têm todas as informações da instância do problema, os clientes do cluster a partir do
 *qual se criará a rota
 */
public class Tsp {
	private InstanceProblem instance;
	private LinkedList <Customer> cluster;  //não inclui o depósito
    private Route route; // inclui o depósito
	
	public Tsp(InstanceProblem instance, LinkedList <Customer> cluster) {
		this.instance = instance;
		this.cluster=cluster;
		createRouteFromCluster();
	}
	
	//a rota é criada colocando todos os clientes nela. O depósito é colocado no inicio e no fim da rota
    public void createRouteFromCluster(){
    	Customer depot=instance.getDepot();
		route = new Route(cluster);
		route.getRoute().addFirst(depot);
		route.getRoute().addLast(depot);
    }

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	/**
	 * O algoritmo Simulated Annealing é aplicado na rota criada
	 * @return a rota de menor custo
	 */
	public double simulatedAnnealing () {
		
		int sizeRoute = route.getRoute().size();   
		
		Customer c1;
		Customer c2;
		//Simple cases
		if(sizeRoute == 3) //apenas um cliente
			return  route.computeCostOfRoute(instance); 
		else if(sizeRoute == 4) { //2 clientes
			double cost = route.computeCostOfRoute(instance);
			//troco os dois clientes de posição na rota e vejo se da uma melhor solução 
			c1 = route.getRoute().get(1);
			c2 = route.getRoute().get(2);
			route.getRoute().set(1, c2);
			route.getRoute().set(2, c1);
			
			double newCost = route.computeCostOfRoute(instance);
			
			if(newCost <= cost) return newCost;
			else { // caso contrário, desfaço essa troca
				route.getRoute().set(1, c1);
				route.getRoute().set(2, c2);
				return cost;
			}
		}
		
		//Caso geral, função de resfriamento
		//inicializar variáveis de temperatura, resfriamento
 		double temperature = (double) (sizeRoute-2)*(sizeRoute-2);
		double coolingRate = (double) (1/(double)((sizeRoute-2)*(sizeRoute-2)));
		int numberOfChanges = 0;
		int maxNumberOfChanges = (sizeRoute-2)*(sizeRoute-2);
		
		//criação de uma rota aleatória para servir como primeira solução
		Collections.shuffle(cluster);
		createRouteFromCluster();
		
		//o costo dessa rota é criado
		double cost = route.computeCostOfRoute(instance);
		
		//enquanto a condição é cumprida, é criada uma solução vizinha trocando dois clientes de maneira aleatória
		while(temperature >= coolingRate || isLessThanMaxNumberOfChanges(numberOfChanges,maxNumberOfChanges)) {
			int pos1;// = randomInt(0, sizeRoute);
			int pos2;// = randomInt(0, sizeRoute);
			//pega-se duas posições aleatórias de clientes na rota para serem trocadas
			do{
				pos1 = randomInt(1, sizeRoute-1);
				pos2 = randomInt(1, sizeRoute-1);
			}while(pos1 == pos2);// || pos1 == 0 || pos2 == 0 || pos1 == sizeRoute-1 || pos2 == sizeRoute-1);


			//swap pos1 pos2
			c1 = route.getRoute().get(pos1);
			c2 = route.getRoute().get(pos2);
			route.getRoute().set(pos1, c2);
			route.getRoute().set(pos2, c1);
			double newCost = route.computeCostOfRoute(instance);

			boolean changed = false;
			
			//se o custo dessa nova rota com essa troca de posições for menor, a nova solução é aceita como melhor
			if(newCost > cost) { 
				//se o novo custo é maior, só aceitamos essa nova solução se acceptation for true
				boolean acceptation = accept(cost, newCost, temperature);
				if(acceptation == false) { //a troca é desfeita
					route.getRoute().set(pos1, c1);
					route.getRoute().set(pos2, c2);
					changed = false;
				}
				else {
					cost = newCost;
					changed = true;
				}
			}
			else { //temos uma nova solução que é melhor
				cost = newCost;
				changed = true;
			}
			
			
			if(changed == true) {
				numberOfChanges = numberOfChanges + 1;
			}
			else numberOfChanges = numberOfChanges - 1;
			//a temperatura é diminuida, o sistema está esfriando-se
			temperature = temperature - (temperature*coolingRate);
		}
 	    return cost; 
	}

    // retorna um random [min,max[
    public int randomInt (int min, int max) {
	      Random r = new Random();
	      double d = min + r.nextDouble()*(max-min);
	      return (int) d;
    }
    
    //retorna um random [0,1]
    public static double randomDouble() {
    	  Random r = new Random();
	      return r.nextInt(1000)/1000.0;
    }
	
    //devolve se aceita ou não a nova solução vizinha que é pior
    private static boolean accept(double cost, double newCost, double temperature) {
		double deltaE = cost - newCost; //deltaE é sempre negativo
		double prob = Math.pow(Math.E, deltaE/temperature); //função da probabilidade de aceitação é e^(deltaE/temperature)

		double random = randomDouble();
		//accept retorna true se probabilidade de aceitação for maior que um random gerado 
		if(prob > random) return true;
		else return false;
	}
    
    //verifica se a quantidade de trocas feitas pelo algoritmo está entre -maxNumberOfChange e maxNumberOfChange
    private static boolean  isLessThanMaxNumberOfChanges(int numberOfChanges, int maxNumberOfChanges) {
		int inverse = maxNumberOfChanges * -1;
		if(numberOfChanges <= maxNumberOfChanges && numberOfChanges >= inverse) return true;
		return false;
	}

}