import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Nodo {

	public ArrayList<Processo> processos = new ArrayList<>();
	public int processo;
	public int numeroEventos = 100;
	public int numProcessos;

	public Nodo(int processo) throws Exception {
		this.processo = processo;
		carregaProcessos();
		executaNodo();
	}

	private void executaNodo() throws InterruptedException {
		Random rand = new Random();
		for (int i = 0; i < numeroEventos; i++) {
			Thread.sleep((long) rand.nextInt(1000 - (500 - 1)) + 500); // evnts entre 0.5 e 1 seg
			executaEvento();
			
		}
		System.out.println("===Fim Eventos===");
	}

	private void executaEvento() {
		if (Math.random() < processos.get(processo).chance) { //prob de chance
			System.out.println("Enviando mensagem");
			executaEnvioDeMensagem();
		} else {
			System.out.println("Evento local!");
			executaEventoLocal();
		}	
	}

	private void executaEventoLocal() {
		Processo proc = processos.get(processo);
		Evento novoEvento;
		if (proc.eventos.isEmpty()) { //primeiro evento
			novoEvento = new Evento(
					System.currentTimeMillis(),
					processo, 1);
			proc.eventos.add(novoEvento);
		} else { //existem eventos anteriores
			Evento eventoAnterior = proc.eventos.get(proc.eventos.size() - 1);
			novoEvento = new Evento(
					System.currentTimeMillis(),
					processo, eventoAnterior.c + 1);
			proc.eventos.add(novoEvento); //TODO: Avaliar fluxo do algoritmo de lamport
		}
		System.out.println(novoEvento.toString());
	}

	private void executaEnvioDeMensagem() {
		// TODO Auto-generated method stub
	}

	private void executaRecebimentoDeMensagem() {
		// TODO Auto-generated method stub
	}

	private void carregaProcessos() throws NumberFormatException, UnknownHostException {
		try {
			Scanner in = new Scanner(new FileReader("conf.txt"));
			while (in.hasNextLine()) {
				String line = in.nextLine();
				String[] valores = line.split(" ");
				Processo nodo = new Processo(Integer.parseInt(valores[0]), InetAddress.getByName(valores[1]),
						Integer.parseInt(valores[2]), Double.parseDouble(valores[3]));
				processos.add(nodo);
			}
			numProcessos = processos.size();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Arquivo nao encontrado!");
		}
	}
}
