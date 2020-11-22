import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Processo {

	public ArrayList<Nodo> nodos = new ArrayList<>();
	public int processo;
	
	public Processo(int processo) throws Exception {
		this.processo = processo;
		carregaProcessos();
	}

	private void carregaProcessos() throws NumberFormatException, UnknownHostException {
		try {
			Scanner in = new Scanner(new FileReader("conf.txt"));
			int linha = 0;
			while (in.hasNextLine()) {
				String line = in.nextLine();
				String[] valores = line.split(" ");
				Nodo nodo = new Nodo(
						Integer.parseInt(valores[0]),
						InetAddress.getByName(valores[1]),
						Integer.parseInt(valores[2]),
						Double.parseDouble(valores[3]));
				nodos.add(nodo);
				linha++;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Arquivo nao encontrado!");
		}
	}
}
