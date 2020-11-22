import java.net.InetAddress;

public class Nodo {
	public int id;
	public InetAddress host;
	public Integer port;
	public Double chance;

	public Nodo(int id, InetAddress host, Integer port, Double chance) {
		super();
		this.id = id;
		this.host = host;
		this.port = port;
		this.chance = chance;
	}
}
