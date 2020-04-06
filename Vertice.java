/**
 * 
 */

/**
 * @author Alejandro Salazar
 * @author Cristian Inojosa
 */
public class Vertice
{
	
	/**
	 * Id del vertice
	 */
	private int id;
	
	/**
	 * Nombre del vertice
	 */
	private String nombre;
	
	/**
	 * Coordenda x del vertice
	 */
	private double x;
	
	/**
	 * Coordenada y del vertice
	 */
	private double y;
	
	/**
	 * Peso del vertice
	 */
	private double peso;
	
	/**
	 * Constructor de la clase. Crea un nuevo objeto y configura los parametros iniciales.
	 * @param id Id del vertice
	 * @param nombre Nombre del vertice
	 * @param x Coordenada x del vertice
	 * @param y Coordenada y del vertice
	 * @param peso Peso del vertice
	 */
	public Vertice(int id, String nombre, double x, double y, double peso)
	{
		this.id = id;
		this.nombre = nombre;
		this.x = x;
		this.y = y;
		this.peso = peso;
	}
	
	/**
	 * Metodo estatico para crear un vertice.
	 * Recibe los parametros necesarios para crear un vertice y llama al constructor
	 * @param id Id del vertice
	 * @param nombre Nombre del vertice
	 * @param x Coordenada x del vertice
	 * @param y Coordenada y del vertice
	 * @param peso Peso del vertice
	 * @return Vertice nuevo
	 */
	public static Vertice crearVertice(int id, String nombre, double x, double y, double peso)
	{
		return new Vertice(id, nombre, x, y, peso);
	}
	
	/**
	 * Obtiene el peso del vertice
	 * @return double Peso del vertice
	 */
	public double getPeso()
	{
		return this.peso;
	}
	
	/**
	 * Obtiene el id del vertice
	 * @return int Id del vertice
	 */
	public int getId()
	{
		return this.id;
	}
	
	/**
	 * Obtiene el nombre del vertice
	 * @return String nombre del vertice
	 */
	public String getNombre()
	{
		return this.nombre;
	}
	
	/**
	 * Obtiene la coordenada x del vertice
	 * @return double coordanada x del vertice
	 */
	public double getX()
	{
		return this.x;
	}
	
	/**
	 * Obtiene la coordenada y del vertice
	 * @return double coordenada y del vertice
	 */
	public double getY()
	{
		return this.y;
	}
	
	/**
	 * Devuelve una representacion del vertice en forma de String
	 * @return String representacion del vertice
	 */
	public String toString()
	{
		return String.format("%d", this.id);
	}
}
