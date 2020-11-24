import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class Data {

	private static DatagramSocket clientSocket;
	private static Integer timeout = 1500;
	
	/**
	 * Cria a conex�o do socket com base no endere�o e porta configurados.
	 */
	protected static void conectarCliente(InetAddress endereco, Integer porta) {
		try {
			clientSocket.connect(endereco, porta);
		} catch (Exception e) {
			System.out.println("Erro ao conectar no servidor!");
			System.out.println("Encerrado Processo");
			comandoParaFechar();
		}
	}
	
	/**
	 * Inicia o socket atribuindo um limite de tempo (timeout).
	 */
	protected static void iniciarSocket(int porta) {
		try {
			clientSocket = new DatagramSocket(porta);
			clientSocket.setSoTimeout(timeout);
		} catch (SocketException e) {
			System.out.println("Erro ao iniciar socket");
			e.printStackTrace();
		}
	}
	
	/**
	 * Fecha a conex�o com o socket.
	 */
	protected static void desconectarCliente() {
		clientSocket.disconnect();
	}
	
	/**
	 * Fecha socket.
	 */
	protected static void fecharSocket() {
		clientSocket.close();
	}
	
	/**
	 * M�todo que envia a mensagem para o client destino.
	 */
	protected static void enviarMensagem(Mensagem mensagem) {
		try {
			byte[] serialized = mensagemToByteArray(mensagem);
			DatagramPacket sendPacket = new DatagramPacket(serialized, serialized.length);
			clientSocket.send(sendPacket);
			receberAck();
		} catch (Exception e) {
			System.out.println("Erro ao enviar mensagem (Processo destino j� terminou a execu��o.)");
			System.out.println("Encerrado Processo");
			comandoParaFechar();
		}
	}
	
	private static void enviarAck(DatagramPacket receiveDatagram) throws Exception {
		try {
			byte[] serialized = new byte[]{};
			DatagramPacket sendPacket = new DatagramPacket(serialized, serialized.length, receiveDatagram.getSocketAddress());
			clientSocket.send(sendPacket);
		} catch (Exception e) {
			throw new Exception("Erro, Ack N�o enviado.");
		}
	}
	
	private static void receberAck() throws Exception {
		try {
			byte[] receiveData = new byte[1024];
			DatagramPacket receiveDatagram = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receiveDatagram);
			byte[] recBytes = receiveDatagram.getData();
			//ack Recebido...
		} catch (Exception e) {
			throw new Exception("Erro, Ack N�o recebido - timeout.");
		}
	}
	
	/**
	 * Converte o objeto mensagem para um byteArray
	 */
	private static byte[] mensagemToByteArray(Mensagem mensagem) {
		try {
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			ObjectOutput oo = new ObjectOutputStream(bStream);
			oo.writeObject(mensagem);
			oo.close();
			return bStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected static Mensagem receberMensagem() {
		try {
			byte[] receiveData = new byte[1024];
			DatagramPacket receiveDatagram = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receiveDatagram);
			enviarAck(receiveDatagram);
			byte[] recBytes = receiveDatagram.getData();
			Mensagem mensagem = byteArrayToMensagem(recBytes);
			return mensagem;
		} catch (IOException e) {
			try {
				Thread.sleep(100l);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return receberMensagem();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao receber mensagem");
			System.out.println("Encerrado Processo");
			comandoParaFechar();
			return null;
		}
	}
	
	/**
	 * Converte de byteArray para o objeto mensagem.
	 */
	private static Mensagem byteArrayToMensagem(byte[] mensagem) {
		try {
			ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(mensagem));
			Mensagem mensagemObj = (Mensagem) iStream.readObject();
			iStream.close();
			return mensagemObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected static void comandoParaFechar() {
		System.out.println("===Fim Eventos===");
		System.out.println("===Digite qualquer tecla para sair===");
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
		System.exit(0);
	}
	
}
