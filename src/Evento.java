
/**
 * Evento local: m i c l
 * Envio de mensagem: m i c s d
 * Recebimento de mensagem: m i c r s t
 * m = tempo nodo do local (ms)
 * i = ID nodo local
 * c = valor do relogio (enviado) + id (concatenado)
 * d = ID do nodo destino da msg
 * s = envio de mensagem (envio de msg) ou ID do nodo remetente da mensagem (quando recebimento)
 * r = recebimento de mensagem
 * l = mensagem local
 * t = valor do relógio lógico recebido com a mensagem
 */
public class Evento {

	public long m;
	public int i;
	public int c;
	public int d;
	public TipoEvento tipo;
	public int t;
	public int s;
	
	//Evento Local
	public Evento(long m, int i , int c) {
		this.m = m;
		this.i = i;
		this.c = c;
		this.tipo = TipoEvento.LOCAL;
	}
	
	@Override
	public String toString() {
		switch (tipo) {
		case LOCAL:
			return m + " " + i + " " + c + i + " " + tipo.getValue();
		case ENVIO:
			return m + " " + i + " " + c + i + " " + tipo.getValue() + " " + d;
		case RECEBIMENTO:
			return m + " " + i + " " + c + i + " " + tipo.getValue() + " " + s + " " + t;
		default:
			return "Tipo da mensagem não informado.";
		}
	}

}
