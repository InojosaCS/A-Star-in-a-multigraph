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
public class GrafoDirigido implements Grafo, Cloneable {
    
    /**
     * Estructura interna en la que se guarda el grafo
     */
    private HashMap<Integer, Pair<Vertice, HashMap<Vertice, HashMap<Integer, Arco>>>> grafo;
    
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
    public GrafoDirigido()
    {
        this.grafo = new HashMap<Integer, Pair<Vertice, HashMap<Vertice, HashMap<Integer, Arco>>>>();
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
                
                this.agregarArco(
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

    /**
     * {@inheritDoc}
     */
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
            new Pair<Vertice, HashMap<Vertice, HashMap<Integer, Arco>>>(
                v,
                new HashMap<Vertice, HashMap<Integer, Arco>>()
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
        
        Pair<Vertice, HashMap<Vertice, HashMap<Integer, Arco>>> h = this.grafo.get(id);
        
        Vertice verticeAEliminar = h.getKey();
        HashMap<Vertice, HashMap<Integer, Arco>> vecinos = h.getValue();
        
        for(Vertice vecino : vecinos.keySet()) {
            this.grafo.get(vecino.getId()).getValue().remove(verticeAEliminar);
        }
        
        this.grafo.remove(id);
        this.numeroDeVertices--;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Vertice> vertices()
    {
        ArrayList<Vertice> vertices = new ArrayList<Vertice>();
        
        for(Pair<Vertice, HashMap<Vertice, HashMap<Integer, Arco>>> vertice : this.grafo.values()) {
            vertices.add(vertice.getKey());
        }
        
        return vertices;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public HashSet<Lado> lados()
    {
        HashSet<Lado> arcos = new HashSet<Lado>();
        
        for(Pair<Vertice, HashMap<Vertice, HashMap<Integer, Arco>>> vertice : this.grafo.values()) {
            for(HashMap<Integer, Arco> h : vertice.getValue().values()) {
                arcos.addAll(h.values());
            }
        }
        
        return arcos;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int grado(int id) throws NoSuchElementException
    {
        return this.gradoInterior(id) + this.gradoExterior(id);
    }

    /** Retorna el grado interno del vertice v.
     *  @param id Identificador del vertice
     *  @return Retornara el grado interno del vertice.
     */
    public int gradoInterior(int id) throws NoSuchElementException
    {
        this.garantizarVertice(id);
        
        int grado = 0;
        
        for(Pair<Vertice, HashMap<Vertice, HashMap<Integer, Arco>>> h : this.grafo.values()) {
    		for(Vertice v : h.getValue().keySet()) {
    			if(v.getId() == id) {
    				grado += h.getValue().get(v).size();
    			}
    		}
    	}
        
        return grado;
    }
    
     /** Retorna el grado externo del vertice u.
     *  @param id Identificador del vertice
     *  @return Retornara el grado externo del vertice.
     */
    public int gradoExterior(int id) throws NoSuchElementException
    {
        this.garantizarVertice(id);
        
        int grado = 0;
        
        for(HashMap<Integer, Arco> h : this.grafo.get(id).getValue().values()) {
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
        Set<Vertice> adyacentes = new HashSet<Vertice>(this.sucesores(id));
        adyacentes.addAll(this.predecesores(id));
        
        return adyacentes;
    }
    

    /** Retorna una lista con los sucesores del vertice con identificador id. Si no hay, se lanza la excepción NoSuchElementException.
     *  @param id Identificador del vertice.
     *  @return Lista con los sucesores del vertice con identificador id.
     */
    public Set<Vertice> sucesores(int id) throws NoSuchElementException
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
        
        for(HashMap<Integer, Arco> h : this.grafo.get(id).getValue().values()) {
            incidentes.addAll(h.values());
        }
        
        return incidentes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Grafo clone()
    {
        GrafoDirigido grafo = new GrafoDirigido();
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
            grafo.agregarArco(
                grafo.obtenerVertice(((Arco)lado).getExtremoInicial().getId()),
                grafo.obtenerVertice(((Arco)lado).getExtremoFinal().getId()),
                lado.getTipo(),
                lado.getPeso()
            );
        }
        
        return grafo;
    }
    
    /**
     * Agrega un Arco al grafo. No agrega un Arco si alguno de los extremos del Arco no existen en el grafo
     * O si ya existe una misma Arco del mismo tipo entre los extremos
     * @param Arco Arco a agregar
     * @return true en caso de exito, false en caso contrario
     */
    public boolean agregarArco(Arco arco)
    {
        Vertice a = arco.getExtremoInicial();
        Vertice b = arco.getExtremoFinal();
        
        if (this.estaArco(a, b, arco.getTipo())) {
            return false;
        }
        
        if (this.grafo.get(a.getId()).getValue().containsKey(b)) {
            this.grafo.get(a.getId()).getValue().get(b).put(arco.getTipo(), arco);
            
            this.numeroDeLados++;
            return true;
        }
        
        HashMap<Integer, Arco> h = new HashMap<Integer, Arco>();
        h.put(arco.getTipo(), arco);
        
        this.grafo.get(a.getId()).getValue().put(b, h);
        
        this.numeroDeLados++;
        return true;
    }
    
    /** Si no existe un arco del tipo tipo entre u y v, crea un nuevo arco y lo agrega en el grafo.
     *  @param u Extremo 1.
     *  @param v Extremo 2.
     *  @param tipo Tipo del arco.
     *  @param p Peso del arco.
     *  @return Retorna verdadero si inserción se realiza, falso sino.
     */
    public boolean agregarArco(Vertice u, Vertice v, int tipo, double p)
    {
        Arco arco = new Arco(u, v, tipo, p);
        return this.agregarArco(arco);
    }
    
    /**
     * Elimina un Arco del grafo.
     * No hace nada si los extremos de la Arco no existen en el grafo.
     * No hace nada si no existe una Arco del mismo tipo entre los extremos en el grafo
     * @param Arco
     * @return true en caso de exito, false en caso contrario
     */
    public boolean eliminarArco(Arco arco)
    {	


        Vertice a = arco.getExtremoInicial();
        Vertice b = arco.getExtremoFinal();
        
        if(!this.estaArco(a, b, arco.getTipo())) {
            return false;
        }

        this.grafo.get(a.getId()).getValue().get(b).remove(arco.getTipo());
        //this.grafo.get(b.getId()).getValue().get(a).remove(arco.getTipo());

        if (this.grafo.get(a.getId()).getValue().get(b).isEmpty()) {
            this.grafo.get(a.getId()).getValue().remove(b);
            //this.grafo.get(b.getId()).getValue().remove(a);
        }

        this.numeroDeLados--;
        
        return true;
    }
    
    /**
     * @param u Extremo 1
     * @param v Extremo 2
     * @param tipo Tipo de la Arco
     * @return true si existe una Arco entre los vertices u y v del tipo tipo en el grafo,
     * false en caso contrario
     * Falla si los extremos no existen en el grafo (retorna false)
     */
    public boolean estaArco(Vertice u, Vertice v, int tipo)
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
     * Obtiene la Arco del tipo tipo que une a u con v
     * @param u Extremo 1
     * @param v Extremo 2
     * @param tipo
     * @return Arco la Arco entre u y v
     * @throws NoSuchElementException Si la Arco no existe
     */
    public Arco obtenerArco(Vertice u, Vertice v, int tipo) throws NoSuchElementException
    {
        if(!this.estaArco(u, v, tipo)) {
            throw new NoSuchElementException("No existe una Arco )entre los vertices" + u + " " + v);
        }
        
        return this.grafo.get(u.getId()).getValue().get(v).get(tipo);
    }
    

    /** Retorna una lista con predecesores del vertice con identificador id. Si no hay, se lanza la excepción NoSuchElementException.
     *  @param id Identificador del vertice.
     *  @return Lista con los  predecesores del vertice con identificador id.
     */
    public ArrayList<Vertice> predecesores(int id) throws NoSuchElementException
    {
    	this.garantizarVertice(id);
    	
    	ArrayList<Vertice> predecesores = new ArrayList<Vertice>();
    	
    	for(Pair<Vertice, HashMap<Vertice, HashMap<Integer, Arco>>> h : this.grafo.values()) {
    		for(Vertice v : h.getValue().keySet()) {
    			if(v.getId() == id) {
    				predecesores.add(h.getKey());
    			}
    		}
    	}
    	
    	return predecesores;
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
