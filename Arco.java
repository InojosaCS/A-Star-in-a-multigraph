/**
 * 
 */

/**
 * @author Alejandro Salazar
 * @author Cristian Inojosa
 */
public class Arco extends Lado
{
	/**
	 * Constructor de la clase
	 * @param vi Vertice Inicial
	 * @param vf Vertice Final
	 * @param tipo Tipo de lado
	 * @param peso Peso del lado
	 */
	public Arco(Vertice vi, Vertice vf, int tipo, double peso)
	{
		this.a = vi;
		this.b = vf;
		this.tipo = tipo;
		this.peso = peso; 
	}
	
	/**
	 * Crea un arco nuevo.
	 * Recibe los parametros necesarios y llama al constructor de la clase
	 * @param vi Vertice Inicial
	 * @param vf Vertice Final
	 * @param tipo Tipo de lado
	 * @param peso Peso del lado
	 * @return Arco nuevo
	 */
	public static Arco crearArco(Vertice vi, Vertice vf, int tipo, double peso)
	{
		return new Arco(vi, vf, tipo, peso);
	}
	
	/**
	 * Obtiene el extremo inicial del lado
	 * @return Vertice Extremo inicial del lado
	 */
	public Vertice getExtremoInicial()
	{
		return this.a;
	}
	
	/**
	 * Obtiene el extremo final del lado
	 * @return Vertice Extremo final del lado
	 */
	public Vertice getExtremoFinal()
	{
		return this.b;
	}
	
	/**
	 * Indica si un vertice es extremo inicial del lado
	 * @param v Vertice
	 * @return boolean true si v es el extremo incial del lado, false en caso contrario
	 */
	public boolean esExtremoInicial(Vertice v)
	{
		return v == this.a;
	}
	
	/**
	 * Indica si un vertice es extremo final del lado
	 * @param v Vertice
	 * @return boolean true si v es el extremo final del lado, false en caso contrario
	 */
	public boolean esExtremoFinal(Vertice v)
	{
		return v == this.b;
	}

	@Override
	public String toString()
	{
		return String.format("(%s, %s)", this.a, this.b);
	}

	/**
	 * Quitar antes de entregar
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.out.println(crearArco(
			Vertice.crearVertice(0, "a", 0, 0, 0),
			Vertice.crearVertice(1, "b", 1, 1, 0),
			0,
			0
		).toString());
	}
}
