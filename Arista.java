 /**
 * 
 */

/**
 * @author Alejandro Salazar
 * @author Cristian Inojosa
 */
public class Arista extends Lado {

	/**
	 * Contructor de la clase
	 * @param u Vertice sobre el cual la arista incide
	 * @param v Vertice sobre el cual la arista incide
	 * @param tipo Tipo de la arista
	 * @param peso Peso de la arista
	 */
	public Arista(Vertice u, Vertice v, int tipo, double peso)
	{
		this.a = u;
		this.b = v;
		this.tipo = tipo;
		this.peso = peso; 
	}
	
	public static Arista crearArista(Vertice u, Vertice v, int tipo, double peso)
	{
		return new Arista(u, v, tipo, peso);
	}
	
	public Vertice getExtremo1()
	{
		return this.a;
	}
	
	public Vertice getExtremo2()
	{
		return this.b;
	}
	public int getTipo()
	{
		return this.tipo;
	}

	@Override
	public String toString() {
		return String.format("{%s, %s}", this.a, this.b);
	}

	/**
	 * Quitar antes de entregar
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.out.println(
			crearArista(
				Vertice.crearVertice(0, "a", 0, 0, 0),
				Vertice.crearVertice(1, "b", 1, 1, 0),
				0,
				0
			)
		);
	}

}
