import java.io.Serializable;

public class Mensagem implements Serializable {

	static final long serialVersionUID = 4296383380001025316L;
	int processoOrigem;
	int vrRlgOrigem;

	public Mensagem(int processoOrigem, int vrRlgOrigem) {
		super();
		this.processoOrigem = processoOrigem;
		this.vrRlgOrigem = vrRlgOrigem;
	}

}
