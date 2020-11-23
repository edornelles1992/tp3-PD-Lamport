import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Nodo extends Data {

	public ArrayList<Processo> processos = new ArrayList<>();
	public int processoId;
	public int numeroEventos = 100;
	public int numProcessos;
	public static Thread recebimento = null;

	public Nodo(int processo) throws Exception {
		super.iniciarSocket();
		this.processoId = processo;
		carregaProcessos();
		iniciaRecebimentoDeMensagens();
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
		if (Math.random() < processos.get(processoId).chance) { //prob de chance
			System.out.println("Enviando mensagem");
			executaEnvioDeMensagem();
		} else {
			System.out.println("Evento local!");
			executaEventoLocal();
		}	
	}

	private void executaEventoLocal() {
		Processo proc = processos.get(processoId);
		Evento novoEvento;
		if (proc.eventos.isEmpty()) { //primeiro evento
			novoEvento = new Evento(
					System.currentTimeMillis(),
					processoId, 1);
			proc.eventos.add(novoEvento);
		} else { //existem eventos anteriores
			Evento eventoAnterior = proc.eventos.get(proc.eventos.size() - 1);
			novoEvento = new Evento(
					System.currentTimeMillis(),
					processoId, eventoAnterior.c + 1);
			proc.eventos.add(novoEvento); //TODO: Avaliar fluxo do algoritmo de lamport
		}
		System.out.println(novoEvento.toString());
	}

	private void executaEnvioDeMensagem() {
		Processo procSelecionado = selecionaProcessoAleatorio();
		// TODO: enviar uma mensagem (vamos fazer via socket DATAGRAM)
		super.conectarCliente(procSelecionado.host, procSelecionado.port);
		super.enviarMensagem(new Mensagem(processoId, valorRelogio()));
		super.desconectarCliente();
	}

	private Processo selecionaProcessoAleatorio() {
		Random rand = new Random();
		int numProcessos = processos.size() - 1;
		int procSorteado = rand.nextInt(numProcessos - (0 - 1) + 0);
		while (procSorteado == processoId) {
			procSorteado = rand.nextInt(numProcessos - (0 - 1) + 0);
		}
		return processos.get(procSorteado);
	}

	private void iniciaRecebimentoDeMensagens() {
		recebimento = (new Thread(){
			@Override
			public void run(){
				while (true) {
					Mensagem mensagem = receberMensagem();
					//TODO: receber a mensagem e atualizar com algoritmo de lamport
				}
			}
		});
		recebimento.start();
	}
	
	public int valorRelogio() {
		return processos.get(processoId).eventos.size();
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
