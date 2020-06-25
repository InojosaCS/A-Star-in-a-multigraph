import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javafx.util.Pair;

/**
 * 
 */

/**
 * @author Alejandro Salazar
 * @author Cristian Inojosa
 */
public class GrafoNoDirigido implements Grafo, Cloneable {
	
	/**
	 * Estructura interna en la que se guarda el grafo
	 */
	private HashMap<Integer, Pair<Vertice, HashMap<Vertice, HashMap<Integer, Arista>>>> grafo;
	
	/**
	 * Almacena el numero de vertices
	 */
	private int numeroDeVertices;
	
	/**
	 * Almacena el numero de lados
	 */
	private int numeroDeLados;
	
	/**
	 * Constructor de la clase. Crea un grafo vacio.
	 */
	public GrafoNoDirigido()
	{
		this.grafo = new HashMap<Integer, Pair<Vertice, HashMap<Vertice, HashMap<Integer, Arista>>>>();
		this.numeroDeVertices = 0;
		this.numeroDeLados = 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean cargarGrafo(String archivo)
	{
		try {
			BufferedReader Lector = new BufferedReader(new FileReader(archivo));
			Lector.readLine();
			
			int n = Integer.parseInt(Lector.readLine());
			int m = Integer.parseInt(Lector.readLine());
			
			for(int i = 0; i < n; i++) {
				String[] helper = Lector.readLine().split("\\s+");
				
				this.agregarVertice(
					Integer.parseInt(helper[0]),
					helper[1],
					Double.parseDouble(helper[2]),
					Double.parseDouble(helper[3]),
					Double.parseDouble(helper[4])
				);
			}
			
			for(int i = 0; i < m; i++) {
				String[] helper = Lector.readLine().split("\\s+");
				
				this.agregarArista(
					this.obtenerVertice(Integer.parseInt(helper[0])),
					this.obtenerVertice(Integer.parseInt(helper[1])),
					Integer.parseInt(helper[2]),
					Double.parseDouble(helper[3])
				);
			}
			Lector.close();
		} catch(Exception e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int numeroDeVertices()
	{
		return this.numeroDeVertices;
	}

	@Override
	public int numeroDeLados()
	{
		return this.numeroDeLados;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean agregarVertice(Vertice v)
	{
		if (this.estaVertice(v.getId())) {
			return false;
		}
		
		this.grafo.put(
			v.getId(),
			new Pair<Vertice, HashMap<Vertice, HashMap<Integer, Arista>>>(
				v,
				new HashMap<Vertice, HashMap<Integer, Arista>>()
			)
		);
		
		this.numeroDeVertices++;
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean agregarVertice(int id, String nombre, double x, double y, double p)
	{
		Vertice v = new Vertice(id, nombre, x, y, p);
		
		return this.agregarVertice(v);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vertice obtenerVertice(int id) throws NoSuchElementException
	{
		this.garantizarVertice(id);
		return this.grafo.get(id).getKey();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean estaVertice(int id)
	{
		return this.grafo.containsKey(id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean eliminarVertice(int id)
	{
		if (!this.estaVertice(id)) {
			return false;
		}
		
		Pair<Vertice, HashMap<Vertice, HashMap<Integer, Arista>>> h = this.grafo.get(id);
		
		Vertice verticeAEliminar = h.getKey();
		HashMap<Vertice, HashMap<Integer, Arista>> vecinos = h.getValue();
		
		for(Vertice vecino : vecinos.keySet()) {
			this.grafo.get(vecino.getId()).getValue().remove(verticeAEliminar);
		}
		
		this.grafo.remove(id);
		this.numeroDeVertices--;
		return true;
	}

	@Override
	public ArrayList<Vertice> vertices()
	{
		ArrayList<Vertice> vertices = new ArrayList<Vertice>();
		
		for(Pair<Vertice, HashMap<Vertice, HashMap<Integer, Arista>>> vertice : this.grafo.values()) {
			vertices.add(vertice.getKey());
		}
		
		return vertices;
	}

	@Override
	public HashSet<Lado> lados()
	{
		HashSet<Lado> aristas = new HashSet<Lado>();
		
		for(Pair<Vertice, HashMap<Vertice, HashMap<Integer, Arista>>> vertice : this.grafo.values()) {
			for(HashMap<Integer, Arista> h : vertice.getValue().values()) {
				aristas.addAll(h.values());
			}
		}
		
		return aristas;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int grado(int id) throws NoSuchElementException
	{
		this.garantizarVertice(id);
		
        int grado = 0;
        
        for(HashMap<Integer, Arista> h : this.grafo.get(id).getValue().values()) {
    		grado += h.size();
    	}
        
        return grado;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Vertice> adyacentes(int id) throws NoSuchElementException
	{
		this.garantizarVertice(id);
		return this.grafo.get(id).getValue().keySet();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Lado> incidentes(int id) throws NoSuchElementException
	{
		this.garantizarVertice(id);
		ArrayList<Lado> incidentes = new ArrayList<Lado>();
		
		for(HashMap<Integer, Arista> h : this.grafo.get(id).getValue().values()) {
			incidentes.addAll(h.values());
		}
		
		return incidentes;
	}

	@Override
	public Grafo clone()
	{
		GrafoNoDirigido grafo = new GrafoNoDirigido();
		Collection<Vertice> vertices = this.vertices();
		Collection<Lado> lados = this.lados();
		
		for(Vertice vertice : vertices) {
			grafo.agregarVertice(vertice.getId(),
				vertice.getNombre(),
				vertice.getX(),
				vertice.getY(),
				vertice.getPeso()
			);
		}
		
		for(Lado lado : lados) {
			grafo.agregarArista(
				grafo.obtenerVertice(((Arista)lado).getExtremo1().getId()),
				grafo.obtenerVertice(((Arista)lado).getExtremo2().getId()),
				lado.getTipo(),
				lado.getPeso()
			);
		}
		
		return grafo;
	}
	
	/**
	 * Agrega una arista al grafo.
	 * No agrega la arista si alguno de los extremos de la arista no existen en el grafo
	 * O si ya existe una misma arista del mismo tipo entre los extremos
	 * @param arista Arista a agregar
	 * @return true en caso de exito, false en caso contrario
	 */
	public boolean agregarArista(Arista arista)
	{
		Vertice a = arista.getExtremo1();
		Vertice b = arista.getExtremo2();
		
		if (this.estaArista(a, b, arista.getTipo())) {
			return false;
		}
		
		if (this.grafo.get(a.getId()).getValue().containsKey(b)) {
			this.grafo.get(a.getId()).getValue().get(b).put(arista.getTipo(), arista);
			this.grafo.get(b.getId()).getValue().get(a).put(arista.getTipo(), arista);
			
			this.numeroDeLados++;
			return true;
		}
		
		HashMap<Integer, Arista> h = new HashMap<Integer, Arista>();
		h.put(arista.getTipo(), arista);
		
		this.grafo.get(a.getId()).getValue().put(b, h);
		this.grafo.get(b.getId()).getValue().put(a, new HashMap<Integer, Arista>(h));
		
		this.numeroDeLados++;
		return true;
	}
	
	/**
	 * Crea una arista y la añade al grafo
	 * Internamente llama a agregarArista(Arista arista)
	 * @param u
	 * @param v
	 * @param tipo
	 * @param p
	 * @return
	 */
	public boolean agregarArista(Vertice u, Vertice v, int tipo, double p)
	{
		Arista arista = new Arista(u, v, tipo, p);
		return this.agregarArista(arista);
	}
	
	/**
	 * Elimina una arista del grafo.
	 * No hace nada si los extremos de la arista no existen en el grafo.
	 * No hace nada si no existe una arista del mismo tipo entre los extremos en el grafo
	 * @param arista
	 * @return true en caso de exito, false en caso contrario
	 */
	public boolean eliminarArista(Arista arista)
	{
		Vertice a = arista.getExtremo1();
		Vertice b = arista.getExtremo2();
		
		if(!this.estaArista(a, b, arista.getTipo())) {
			return false;
		}
		
		this.grafo.get(a.getId()).getValue().get(b).remove(arista.getTipo());
		this.grafo.get(b.getId()).getValue().get(a).remove(arista.getTipo());
		
		if (this.grafo.get(a.getId()).getValue().get(b).isEmpty()) {
			this.grafo.get(a.getId()).getValue().remove(b);
			this.grafo.get(b.getId()).getValue().remove(a);
		}
		
		this.numeroDeLados--;
		
		return true;
	}
	
	/**
	 * @param u Extremo 1
	 * @param v Extremo 2
	 * @param tipo Tipo de la Arista
	 * @return true si existe una arista entre los vertices u y v del tipo tipo en el grafo,
	 * false en caso contrario
	 * Falla si los extremos no existen en el grafo (retorna false)
	 */
	public boolean estaArista(Vertice u, Vertice v, int tipo)
	{
		try {
			this.garantizarVertice(u.getId());
			this.garantizarVertice(v.getId());
		} catch(NoSuchElementException e) {
			return false;
		}
		
		if(this.grafo.get(u.getId()).getKey() != u || this.grafo.get(v.getId()).getKey() != v) {
			return false;
		}
		
		return this.grafo.get(u.getId()).getValue().containsKey(v)
			&& this.grafo.get(u.getId()).getValue().get(v).containsKey(tipo);
	}
	
	/**
	 * Obtiene la arista del tipo tipo que une a u con v
	 * @param u Extremo 1
	 * @param v Extremo 2
	 * @param tipo
	 * @return Arista la arista entre u y v
	 * @throws NoSuchElementException Si la arista no existe
	 */
	public Arista obtenerArista(Vertice u, Vertice v, int tipo) throws NoSuchElementException
	{
		if(!this.estaArista(u, v, tipo)) {
			throw new NoSuchElementException("No existe una arista entre los vertices" + u + " " + v);
		}
		
		return this.grafo.get(u.getId()).getValue().get(v).get(tipo);
	}
	
	/**
	 * @param id Id de un vertice del grafo
	 * @throws NoSuchElementException si el vertice no esta en el grafo
	 */
	private void garantizarVertice(int id) throws NoSuchElementException
	{
		if (!this.estaVertice(id))
		{
			throw new NoSuchElementException("No existe ningún vertice con id " + id);
		}
	}
}
