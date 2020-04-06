import java.util.Collection;
import java.util.List;

/**
 * 
 */

/**
 * @author Alejandro Salazar
 * @author Cristian Inojosa
 */
public interface Grafo
{
	/**
	 * Carga en el grafo la informacion almacenada en un archivo
	 * @param archivo Archivo del cual se va a cargar la informacion del grafo
	 * @return boolean true si los datos del archivo son cargados satisfactoriamente, false en caso contrario
	 */
	public boolean cargarGrafo(String archivo);
	
	/**
	 * Obtiene el numero de vertices del grafo
	 * @return int el numero de vertices del grafo
	 */
	public int numeroDeVertices();
	
	/**
	 * Obtiene el numero de lados del grafo
	 * @return int el numero de lados del grafo
	 */
	public int numeroDeLados();
	
	/**
	 * Agrega un nuevo vertice al grafo.
	 * No hace nada si el grafo ya posee un vertice con el mismo identificador
	 * @param v Vertice a agregar
	 * @return true si no existe ningun vertice con el identficados de v y lo agrego al grafo satisfactoriamente, false en caso contrario
	 */
	public boolean agregarVertice(Vertice v);
	
	/**
	 * Crea un vertice y lo agrega al grafo
	 * No hace nada si el grafo ya posee un vertice con el mismo identificador
	 * @param id Id del vertice
	 * @param nombre Nombre del vertice
	 * @param x Coordenada x del vertice
	 * @param y Coordenada y del vertice
	 * @param p Peso del vertice
	 * @return true si no existe ningun vertice con el identficados de v y lo agrego al grafo satisfactoriamente, false en caso contrario
	 */
	public boolean agregarVertice(int id, String nombre, double x, double y, double p);
	
	/**
	 * Obtiene un vertice del grafo
	 * @param id Id del vertice a obtener
	 * @return Vertice
	 */
	public Vertice obtenerVertice(int id);
	
	/**
	 * Indica si en el grafo existe un vertice con un id especificado
	 * @param id Id a buscar
	 * @return boolean true si existe un grafo en el vertice cuyo id es id, false en caso contrario
	 */
	public boolean estaVertice(int id);
	
	/**
	 * Elimina un vertice del grafo
	 * @param id Id del vertice a eliminar
	 * @return true en caso de exito, false en caso contrario
	 */
	public boolean eliminarVertice(int id);
	
	/**
	 * Obtiene los vertices que forman el grafo
	 * @return Vertice[] Lista de vertices que forman el grafo
	 */
	public Collection<Vertice> vertices();
	
	/**
	 * Obtiene los lados que forman el grafo
	 * @return Lado[] Lista de lados que forman el grafo
	 */
	public Collection<Lado> lados();
	
	/**
	 * Obtiene el grado de un vertice en especifico
	 * Si hay varias aristas de distinto tipo entre dos vertices, cada una suma 1 al grado.
	 * @param id Id del vertice cuyo grado deseamos obtener
	 * @return int El grado del vertice cuyo id es id
	 */
	public int grado(int id);
	
	/**
	 * Obtiene los vertices adyacentes a un vertice especifico
	 * @param id Id del vertice cuyos vertices adyacentes deseamos obtener
	 * @return Vertice[] Lista de vertices adyacentes del vertice cuyo id es id
	 */
	public Collection<Vertice> adyacentes(int id);
	
	/**
	 * Obtiene los lados incidentes a un vertice especifico
	 * @param id Id del vertice cuyos lados adyacentes deseamos obtener
	 * @return Vertice[] Lista de lados incidentes en el vertice cuyo id es id
	 */
	public Collection<Lado> incidentes(int id);
	
	/**
	 * Clona el grafo
	 * @return Grafo Una nueva instancia de Grafo con las mismas propiedades que la instancia que utiliza este metodo
	 */
	public Grafo clone();
	
	/**
	 * Obtiene una representacion del grafo en forma de String
	 * @return String una representacion del grafo
	 */
	public String toString();
}
