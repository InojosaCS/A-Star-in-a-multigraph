/**
 * @author Alejandro Salazar
 * @author Cristian Inojosa
 */

public abstract class Lado
{	
	/**
	 * Vertice en el cual el lado incide (inicial si es dirigido)
	 */
	protected Vertice a;
	
	/**
	 * Vertice en el cual el lado incide (final si es dirigido)
	 */
	protected Vertice b;
	
	/**
	 * Tipo del lado
	 */
	protected int tipo;
	
	/**
	 * Peso del lado
	 */
	protected double peso;
	
	/**
	 * Obtiene el Vertice en el cual el lado incide (inicial si es dirigido)
	 * @return Vertice
	 */
	public Vertice getInicio()
	{
		return this.a;
	}
	/**
	 * Obtiene el Vertice en el cual el lado incide (inicial si es dirigido)
	 * @return Vertice
	 */
	public Vertice getFin()
	{
		return this.b;
	}
	
	/**
	 * Obtiene el peso del lado
	 * @return double peso
	 */
	public double getPeso()
	{
		return this.peso;
	}
	
	/**
	 * Indica si el lado incide en el vertice v
	 * @param v Vertice
	 * @return boolean true si el lado incide en v, false en caso contrario
	 */
	public boolean incide(Vertice v)
	{
		return v == this.a || v == this.b;
	}
	
	/**
	 * Obtiene el tipo del lado
	 * @return int Tipo del lado
	 */
	public int getTipo()
	{
		return this.tipo;
	}
	
	/**
	 * Obtiene una representación del lado de tipo String
	 * @return String representacion del lado
	 */
	abstract public String toString();
}
