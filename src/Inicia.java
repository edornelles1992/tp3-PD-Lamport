public class Inicia {

	public static void main(String[] args) throws Exception {
		
		if (args.length != 1) {
			System.out.println("Usage: java Main <numero processo>\n");
			return;
		}
		
		new Nodo(Integer.parseInt(args[0]));
	}

}
