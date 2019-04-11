
public class Test {

	public static void main(String[] args) {
		
		//é criada uma instância do problema a partir do arquivo de entrada
		InstanceProblem instance = new InstanceProblem();
		instance.inicializeInstanceFromFIle ("C:\\Users\\Elias\\VRPFinal\\Entradas\\A-n33-k5.vrp");
		
		System.out.println(instance);
		
		long initialTime = System.currentTimeMillis();
		//roda-se o algoritmo centroide para o problema de roteamento de veículos baseado na instância criada anteriormente
		CentroideVRP cen=new CentroideVRP(instance);
		
		Solution solution=cen.runCentroideVRP(5);
		long finalTime = System.currentTimeMillis();
		System.out.println("Tempo de Execução:");
		//é calculado o tempo de execução do centroide VRPC para essa instância em ms
		System.out.println(finalTime-initialTime);
		System.out.println(solution);
			
	}
	

}
