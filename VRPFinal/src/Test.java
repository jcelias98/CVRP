
public class Test {

	public static void main(String[] args) {
		
		//� criada uma inst�ncia do problema a partir do arquivo de entrada
		InstanceProblem instance = new InstanceProblem();
		instance.inicializeInstanceFromFIle ("C:\\Users\\Elias\\VRPFinal\\Entradas\\A-n33-k5.vrp");
		
		System.out.println(instance);
		
		long initialTime = System.currentTimeMillis();
		//roda-se o algoritmo centroide para o problema de roteamento de ve�culos baseado na inst�ncia criada anteriormente
		CentroideVRP cen=new CentroideVRP(instance);
		
		Solution solution=cen.runCentroideVRP(5);
		long finalTime = System.currentTimeMillis();
		System.out.println("Tempo de Execu��o:");
		//� calculado o tempo de execu��o do centroide VRPC para essa inst�ncia em ms
		System.out.println(finalTime-initialTime);
		System.out.println(solution);
			
	}
	

}
