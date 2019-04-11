import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A classe Instance Problem têm a matriz de adjacência, o número de clientes, os clientes, o depósito e seu 
 * e seu identificador, a lista dos clientes visitados
 * @author Juan
 *
 */
public class InstanceProblem {
	private double [][] adjacencyMatrix;
	private int numberofCustomers;
	private Customer [] customers;
	private int idDepot;
	private Customer depot;
	private ArrayList <Customer> visitedCustomers;

	public InstanceProblem() {
		this.visitedCustomers = new ArrayList <Customer> ();
	}
	
	public Customer getDepot() {
		return depot;
	}

	public void setDepot(Customer depot) {
		this.depot = depot;
	}

	public int getNumberofCustomers() {
		return numberofCustomers;
	}

	public void setNumberofCustomers(int numberofCustomers) {
		this.numberofCustomers = numberofCustomers;
	}

	public ArrayList<Customer> getVisitedCustomers() {
		return visitedCustomers;
	}

	public void setVisitedCustomers(ArrayList<Customer> visitedCustomers) {
		this.visitedCustomers = visitedCustomers;
	}

	public double[][] getAdjacencyMatrix() {
		return adjacencyMatrix;
	}

	public void setAdjacencyMatrix(double[][] adjacencyMatrix) {
		this.adjacencyMatrix = adjacencyMatrix;
	}
	
	public int getNumberOfCustomers() {
		return numberofCustomers;
	}

	public void setNumberOfCustomers(int numberOfNodes) {
		this.numberofCustomers = numberOfNodes;
	}
	
	public Customer[] getCustomers() {
		return customers;
	}

	public void setCustomers(Customer[] nodes) {
		this.customers = nodes;
	}
	
	public int getIdDepot() {
		return idDepot;
	}

	public void setIdDepot(int idDepot) {
		this.idDepot = idDepot;
	}
	
	@Override
	public String toString() {
		return "InstanceProblem [" +
				//"adjacencyMatrix="
				//+ Arrays.toString(adjacencyMatrix) +
				"numberofCustomers = "
				+ numberofCustomers + ", idDepot = " + idDepot + "]";
	}

    //a matriz de adjacència é criada calculando o custo(distância) entre cada cliente i e cada cliente j
	public void createAdjacencyMatrix () {
		adjacencyMatrix = new double [numberofCustomers][numberofCustomers];
		for (int i = 0; i < numberofCustomers; i++) {
			for (int j = 0; j < numberofCustomers; j++) {
				adjacencyMatrix[i][j] = customers[i].euclideanDistance (customers[j]);
			}
				
		}
	}	
	
	//leitura do arquivo de dados no formato das instâncias Augerat
	public void inicializeInstanceFromFIle (String filename) {
		
		InputStream is;
		try {
			is = new FileInputStream (filename);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			br.readLine();
			br.readLine();
			br.readLine();
			
			String dimensionString = br.readLine();
			dimensionString = dimensionString.replace("DIMENSION : ", "");
			numberofCustomers = Integer.parseInt(dimensionString); 

			//cria-se um vetor de clientes de tamanho numberodCustormers para a instância
			customers = new Customer [numberofCustomers];
			
			br.readLine();
			String capacityString = br.readLine();
			capacityString = capacityString.replace("CAPACITY : ", "");
			Truck.capacity = Integer.parseInt(capacityString); //capacidade dos veículos
			br.readLine();
			//para cada cliente é lido o seu id, e sua posição
			int id;
			int i;
			int x;
			int y;
			String line;
			for(i  =0; i < numberofCustomers; i++) {
				line = br.readLine();
				
				Scanner sc = new Scanner(line);
				id = sc.nextInt();
				x = sc.nextInt();
				y = sc.nextInt();
				sc.close();
				
				Customer newNode = new Customer(id, x, y);
				customers[i] = newNode;
			}
			
            br.readLine(); 
			
			int demand;
			//para cada cliente é lida sua demanda
			for(i=0;i<numberofCustomers;i++) {
				line = br.readLine();
				
				Scanner sc = new Scanner(line);
				sc.nextInt();
				demand = sc.nextInt();
				customers[i].setDemand(demand);
				
				sc.close();
			}
			br.readLine(); 
			
			line = br.readLine();
			Scanner sc = new Scanner(line);
			idDepot = sc.nextInt();
			//criação do depósito
			depot = new Customer(idDepot, customers[idDepot-1].getX(), customers[idDepot-1].getY());
			
			sc.close();
			br.close();
			
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(NoSuchElementException e) {
			System.out.println("Arquivo em formato inapropriado");
		}
		catch(NumberFormatException e) {
			System.out.println("Arquivo em formato inapropriado");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		//cria-se a matriz de adjacencia
		createAdjacencyMatrix ();
			
	}

}
