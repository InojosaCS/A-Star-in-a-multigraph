/**
*  En este archivo se encuentran los algoritmos de planeamiento de rutas, el de bcaktracking y el de A estrella
*  Explicacion Backtracking:
*  Esta funcion recorre el grafo como lo recorreria el algoritmo DFS, primero busca todos los sucesores con la misma linea de metro,
*  y ve por esa rama recursivamente, lleva una variable llamada current, si current es mayor o igual al minimo actual, entonces corta
*  recursion pues buscamos el menor numero de trasbordos, si se llega a un punto donde no hay mas sucesores no visitados de la misma linea 
*  entonces busca por las otras lineas, y se le suma 1 a current. Si se llega a pFin entonce se actualiza el minimo.
*  Explicacion A estrella:
*  Hace un recorrido similar que Dijkstra, salvo que al buscar en la cola de prioridad selecciona de las alternativas posibles en cada iteración, la alternativa que
*  minimiza el costo estimado desde la estacion inicial hasta la estacion final. En otras palabras utiliza información del problema que le permita decidir cual 
*  podría ser el mejor camino a futuro. A su vez, para cada estacion, existe un tabla que guarda los caminos y el tiempo para cada linea existente, es decir, va calculando      *  con un enfoque greedy el costo minimo para llegar a una estacion por una linea dada, considerando el costo hasta la estacion antecesora y el costo hacer trasbordo
*  (si hubiere) o mantenerse en la misma linea. Al llegar al destino, escoge el camino con menor costo de todos.
*/

import java.util.*; 

/**
 * @author Cristian Inojosa 17-10285
 * @author Alejandro Salazar 16-11080
 */

public class PlanearRutas {

	/**
	 * Variables que guarda los nombres de las lineas de metro
	 */
	private String[] lineasDeMetro;
	
	/**
	 * Variables que guardan el numero de lineas y estaciones
	 */
	private int nroLineas, nroEstaciones;
	
	/**
	 * Variable que representa la cantidad minima de trasbordos para cualquier etapa de la arborescencia 
	 * del recorrido del grafo, sirve para acotar el numero de trasbordos posibles durante el backtracking
	 * Se inicializa en 7 para optimizar el backtracking, ya que en Caracas no hay ninguna ruta que tome mas de 7 
	 * trasbordos
	 */
	public static int  minimo;
	
	/**
	 * Variable se va actualizando en cada etapa del recorrido del grafo
	 */
	private String caminoCompleto;
	
	/**
	 * Variable que sirve para mapear N id's de N vertices con una secuencia de {0,2,3...;N-1} 
	 * se usa para marcar los vertices como visitados en O(n) en complejidad de espacio
	 */
	private HashMap<Integer, Integer> hashing = new HashMap <Integer, Integer>();
	
	/**
	 * Variables que marcan los vertices de inicio y fin de camino que estamos buscando
	 */
	private Vertice verticeInicio, verticeFin;
	
	/**
	 * El priority queue (nota: La complejidad con monticulo de fibonacci no disminunia pues N*logN < N*(nroLados^2)
	 */
	private Set<Vertice> colaDeEspera; 
    
	/**
	 * Constructor de la clase
	 */
	PlanearRutas(String[] lineasDeMetro, int nroLineas, int nroEstaciones, int minimo, HashMap<Integer, Integer> hashing, Vertice verticeInicio, Vertice verticeFin){
		this.lineasDeMetro = lineasDeMetro;
		this.nroLineas = nroLineas;
		this.nroEstaciones = nroEstaciones;
		this.minimo = minimo;
		this.hashing = hashing;
		this.verticeInicio = verticeInicio;
		this.verticeFin = verticeFin;
	}
	
	/**
     * Este funcion busca los sucesores del vertice pInicio, y llama a la funcion BTRecursivo sobre ellos, y marca el vertice inical 
     * como visitado
     * @param GrafoD grafo
     * @param Vertice inicio
     * @param Vertice Fin
     * @return String caminoCompleto : Es el camino con todas las estaciones y lineas
     */
	public String backtracking(GrafoDirigido grafo, Vertice inicio, Vertice fin) { 
		// Declaracion de variables
		Set<Vertice> sucesores = grafo.sucesores(verticeInicio.getId());
		String caminoActual = inicio.getId() + " "+ inicio.getNombre();
		boolean [] visitados = new boolean [nroEstaciones];
		visitados[hashing.get(verticeInicio.getId())] = true;
		caminoCompleto = "-1";
		
		// Se itera sobre sobre los vecinos del nodo inicial
		for(Vertice verticeAdj: sucesores) {
			// Se busca cuales lines existen entre los nodos y se busca un camino entre ellos
			for(int i=0; i<nroLineas; i++) {
				if (grafo.estaArco(inicio, verticeAdj, i) && !visitados[hashing.get(verticeAdj.getId())]) {
					Arco arco = new Arco(inicio, verticeAdj, i, 0);
					arco = grafo.obtenerArco(inicio, verticeAdj, i);
					visitados[hashing.get(verticeAdj.getId())] = true;
					BTRecursivo((GrafoDirigido) grafo, arco, 0, caminoActual, visitados);
					visitados[hashing.get(verticeAdj.getId())] = false;
				}
			}
		}
		return caminoCompleto;
	}
	
	/**
     * Esta funcion recorre el grafo como lo recorreria el algoritmo DFS, primero busca todos los sucesores con la misma linea de metro,
     * y ve por esa rama recursivamente, lleva una variable llamada current, si current es mayor o igual al minimo actual, entonces corta
     * recursion pues buscamos el menor numero de trasbordos, si se llega a un punto donde no hay mas sucesores no visitados de la misma linea 
     * entonces busca por las otras lineas, y se le suma 1 a current. Si se llega a pFin entonce se actualiza el minimo.
     * @param GrafoD grafo
     * @param Arco arco: Pasa el arco entre el nodo actual y su sucesor
     * @param int current: Numero de trasbordos en esa rama
     * @param String CaminoActual: Va guardando el recorrido de la ruta
     * @param visitados: Guarda cuales estaciones del metro ya han sido visitadas
     */
	 void BTRecursivo(GrafoDirigido grafo, Arco arco, int current, String caminoActual, boolean [] visitados) { 
		// Declaracion de variables
		Vertice inicio = arco.getExtremoInicial();
		Vertice actual = arco.getExtremoFinal();
		int tipo = arco.getTipo();
		
		// Marco la estacion actual como visitada
		visitados[hashing.get(actual.getId())] = true;
		Set<Vertice> sucesores = grafo.sucesores(actual.getId());
		
		// Se actualiza el recorrido con la estacion y la linea actual
		caminoActual += " " + tipo + " " + Integer.toString(actual.getId()) + " " + actual.getNombre();
		String[] helper = caminoActual.split("\\s+");
		
		// Algunas condiciones que entorpecen el recorrido
		if(actual.getId()==verticeInicio.getId() || helper.length>200 || minimo==0 ) {	return;}
		
		// si llega al destino entonces acaba el backtracking y asigna los nuevos valores
		if( actual.getId()==verticeFin.getId() && current < minimo) {
			minimo = current;
			caminoCompleto = caminoActual;
			//System.out.println(caminoCompleto);
			//System.out.println(minimo);
			return;
		}
		// Se verifica primero si tiene algun adyacente en esa misma linea
		for(Vertice verticeAdj: sucesores) {
			// Se chequea si exite una arco del mismo tipo de linea y si es valido visitarla
			if (grafo.estaArco(actual, verticeAdj, tipo) && verticeAdj.getId() != inicio.getId() && !visitados[hashing.get(verticeAdj.getId())]) {
				visitados[hashing.get(verticeAdj.getId())] = true;
				Arco arcoAux = new Arco(actual, verticeAdj, tipo, 0);
				arcoAux = grafo.obtenerArco(actual, verticeAdj, tipo);
				BTRecursivo((GrafoDirigido) grafo, arcoAux, current, caminoActual, visitados);
				// Se marca como falso para que no estorbe en los otros recorridos
				visitados[hashing.get(verticeAdj.getId())] = false;
				}
		}
		
		for(Vertice verticeAdj: sucesores) {
			// Se itera sobre todos los sucesores del nodo actual 
			for(int i=0; i<nroLineas; i++) {
				// Se chequea si exite una arco por cualquier tipo de linea y si es valido visitarla 
				if (grafo.estaArco(actual, verticeAdj, i) && current + 1 < minimo  && !visitados[hashing.get(verticeAdj.getId())]) {
					visitados[hashing.get(verticeAdj.getId())] = true;
					Arco arcoAux = new Arco(actual, verticeAdj, i, 0);
					arcoAux = grafo.obtenerArco(actual, verticeAdj, i);
					if(current+1<minimo && i!=tipo) {
						BTRecursivo((GrafoDirigido) grafo, arcoAux, current+1, caminoActual, visitados);
						//visitados[hashing.get(masCercanox.getId())] = false;
					}
					// Se marca como falso para que no estore en los otros recorridos
					visitados[hashing.get(verticeAdj.getId())] = false;
				}
			}
		}
		// Marco la estacion actual como visitada, por si acaso
		visitados[hashing.get(actual.getId())] = true;
		return;
	}

	 /**
     * Esta funcion busca los adyacentes del vertice pInicio, y llama a la funcion BTRecursivo sobre ellos, y marca el vertice inical 
     * como visitado
     * @param GrafoD grafo
     * @param Vertice inicio
     * @param Vertice Fin
     * @return String caminoCompleto : Es el camino con todas las estaciones y lineas
     */
	public String backtracking(GrafoNoDirigido grafo, Vertice inicio, Vertice fin) { 
		// Declaracion de variables
		Set<Vertice> adyacentes = grafo.adyacentes(verticeInicio.getId());
		String caminoActual = inicio.getId() + " "+ inicio.getNombre();
		boolean [] visitados = new boolean [nroEstaciones];
		visitados[hashing.get(verticeInicio.getId())] = true;
		caminoCompleto = "-1";
		
		// Se itera sobre sobre los vecinos del nodo inicial
		for(Vertice verticeAdj: adyacentes) {
			// Se busca cuales lines existen entre los nodos y se busca un camino entre ellos
			for(int i=0; i<nroLineas; i++) {
				if (grafo.estaArista(inicio, verticeAdj, i) && !visitados[hashing.get(verticeAdj.getId())]) {
					Arista arista = new Arista(inicio, verticeAdj, i, 0);
					arista = grafo.obtenerArista(inicio, verticeAdj, i);
					visitados[hashing.get(verticeAdj.getId())] = true;
					BTRecursivo((GrafoNoDirigido) grafo, inicio, arista, 0, caminoActual, visitados);
					visitados[hashing.get(verticeAdj.getId())] = false;
				}
			}
		}
		return caminoCompleto;
	}
	
	/**
     * Esta funcion recorre el grafo como lo recorreria el algoritmo DFS, primero busca todos los sucesores con la misma linea de metro,
     * y ve por esa rama recursivamente, lleva una variable llamada current, si current es mayor o igual al minimo actual, entonces corta
     * recursion pues buscamos el menor numero de trasbordos, si se llega a un punto donde no hay mas sucesores no visitados de la misma linea 
     * entonces busca por las otras lineas, y se le suma 1 a current. Si se llega a pFin entonce se actualiza el minimo.
     * @param GrafoD grafo
     * @param Vertice inicio: Como el TAD no diferencia entre {u,v} y {v,u} pero para el recorrido importa, entonces le pasamos u
     * @param Arco arco: Pasa el arco entre el nodo actual y su sucesor
     * @param int current: Numero de trasbordos en esa rama
     * @param String CaminoActual: Va guardando el recorrido de la ruta
     * @param visitados: Guarda cuales estaciones del metro ya han sido visitadas
    */
	void BTRecursivo(GrafoNoDirigido grafo, Vertice previo, Arista arista, int current, String caminoActual, boolean [] visitados) { 
		//  Como el TAD no diferencia entre {u,v} y {v,u} buscamos cual es cuale
		Vertice inicio = previo.getId() == arista.getExtremo1().getId() ? arista.getExtremo1() : arista.getExtremo2() ;
		Vertice actual = previo.getId() == arista.getExtremo2().getId() ? arista.getExtremo1() : arista.getExtremo2() ;
		int tipo = arista.getTipo();
		
		// Marista la estacion actual como visitada
		visitados[hashing.get(actual.getId())] = true;
		Set<Vertice> adyacentes = grafo.adyacentes(actual.getId());
		
		caminoActual += " " + tipo + " " + Integer.toString(actual.getId()) + " " + actual.getNombre();
		String[] helper = caminoActual.split("\\s+");
		
		// Algunas condiciones que entorpecen el recorrido
		if(actual.getId()==verticeInicio.getId() || helper.length>200 || minimo==0 ) {	return;}
		
		// si llega al destino entonces acaba el backtracking y asigna los nuevos valores
		if( actual.getId()==verticeFin.getId() && current < minimo) {
			minimo = current;
			caminoCompleto = caminoActual;
			//System.out.println(caminoCompleto);
			//System.out.println(minimo);
			return;
		}
		// Se verifica primero si tiene algun adyacente en esa misma linea
		for(Vertice verticeAdj: adyacentes) {
			// Se chequea si exite una arista del mismo tipo de linea y si es valido visitarla
			if (grafo.estaArista(actual, verticeAdj, tipo) && verticeAdj.getId() != inicio.getId() && !visitados[hashing.get(verticeAdj.getId())]) {
				visitados[hashing.get(verticeAdj.getId())] = true;
				Arista aristaAux = new Arista(actual, verticeAdj, tipo, 0);
				aristaAux = grafo.obtenerArista(actual, verticeAdj, tipo);
				BTRecursivo((GrafoNoDirigido) grafo, actual, aristaAux, current, caminoActual, visitados);
				// Se marca como falso para que no estorbe en los otros recorridos
				visitados[hashing.get(verticeAdj.getId())] = false;
				}
		}
		
		for(Vertice verticeAdj: adyacentes) {
			// Se itera sobre todos los adyacentes del nodo actual 
			for(int i=0; i<nroLineas; i++) {
				// Se chequea si exite una arista por cualquier tipo de linea y si es valido visitarla 
				if (grafo.estaArista(actual, verticeAdj, i) && current + 1 < minimo  && !visitados[hashing.get(verticeAdj.getId())]) {
					visitados[hashing.get(verticeAdj.getId())] = true;
					Arista aristaAux = new Arista(actual, verticeAdj, i, 0);
					aristaAux = grafo.obtenerArista(actual, verticeAdj, i);
					if(current+1<minimo && i!=tipo) {
						BTRecursivo((GrafoNoDirigido) grafo, actual, aristaAux, current+1, caminoActual, visitados);
					}
					// Se marca como falso para que no estore en los otros recorridos
					visitados[hashing.get(verticeAdj.getId())] = false;
				}
			}
		}
		// Marco la estacion actual como visitada, por si acaso
		visitados[hashing.get(actual.getId())] = true;
		return;
	}

	/**
	 *  Hace un recorrido similar que Dijkstra, salvo que al buscar en la cola de prioridad selecciona de las alternativas posibles en cada iteración, la alternativa que
     *  minimiza el costo estimado desde la estacion inicial hasta la estacion final. En otras palabras utiliza información del problema que le permita decidir cual 
     *  podría ser el mejor camino a futuro. A su vez, para cada estacion, existe un tabla que guarda los caminos y el tiempo para cada linea existente, es decir, va calculando 
     *  con un enfoque greedy el costo minimo para llegar a una estacion por una linea dada, considerando el costo hasta la estacion antecesora y el costo hacer trasbordo
     *  (si hubiere) o mantenerse en la misma linea. Al llegar al destino, escoge el camino con menor costo de todos.
     * @param GrafoD grafo
     * @return String respuesta1: Es el camino con todas las estaciones y lineas
     */
	public String aEstrellaConDijkstra(GrafoDirigido grafo) { 
		
		double [][] valoresVertice = new double[nroEstaciones][nroLineas]; // Aca se guardaran los costos minimo de llegar a cada estacion segun la linea
		String [][] caminosVertice = new String[nroEstaciones][nroLineas]; // Aca se guardaran los caminos costos minimo de llegar a cada estacion segun la linea
		String respuesta1 = "-1"; // String donde se guardara la respuesta
		
		// Se inicializan todos los valores como 99999 (neutro para comparar los caminos), y los caminos como vacios
		for(Vertice vertice: grafo.vertices()) {
			for(int i=0; i<nroLineas; i++) {
				valoresVertice[hashing.get(vertice.getId())][i] = 99999;
				caminosVertice[hashing.get(vertice.getId())][i] = "";
			}
		}
	
		// Se inicializan todos los valores del nodo incial
		for(int i=0; i<nroLineas; i++) {
			valoresVertice[hashing.get(verticeInicio.getId())][i] = 0;
			caminosVertice[hashing.get(verticeInicio.getId())][i] = verticeInicio.getId() + " " + verticeInicio.getNombre();
		}
		
		colaDeEspera = new HashSet<Vertice>(); // Se crea la cola de espera
		colaDeEspera.add(verticeInicio); // Se el vertice incial, que es con el comienza la busqueda
		boolean [] visitados = new boolean[nroEstaciones];
		visitados[hashing.get(verticeInicio.getId())] = true; // Se marca el vertice incial como visitado
		
		while(!colaDeEspera.isEmpty()) { 
			Vertice minimo = extraerMinimo(valoresVertice);
			// Si toca expandir el vertice destino es porque ya hemos llegado, se escoge el menor camino y se termina el programa
			if(minimo.getId()==verticeFin.getId()) { 				
				double minRespuesta = 99999;
				for(int i=0; i<nroLineas; i++) {
					if(valoresVertice[hashing.get(verticeFin.getId())][i] < minRespuesta) {
						minRespuesta = valoresVertice[hashing.get(verticeFin.getId())][i];
						respuesta1 = Double.toString(valoresVertice[hashing.get(verticeFin.getId())][i]) + " " +  (caminosVertice[hashing.get(verticeFin.getId())][i]);
					}
				}
				break;
			}

			Set<Vertice> sucesores = grafo.sucesores(minimo.getId()); // se buscan los sucesores del vertice minimo

			for(Vertice vertice: sucesores) {
				if(!visitados[hashing.get(vertice.getId())]) {
					// Para cada linea de metro se busca un resultado
					for(int i=0; i<nroLineas; i++) {
						// se ve que exista una linea entre los dos arcos, y de estar se le extrae informacion
						if(grafo.estaArco(minimo, vertice, i)) {
							Arco arco = new Arco(minimo, vertice, i, grafo.obtenerArco(minimo, vertice, i).getPeso());
							arco = grafo.obtenerArco(minimo, vertice, i);
							String helper = "";
							double minimoActual = valoresVertice[hashing.get(vertice.getId())][i];
							// Para cada linea de metro del antecesor del vertice se busca el camino minimo entre seguir por la misma linea o hacer un trasbordo
							for(int j=0; j<nroLineas; j++) {
								// Si se regresa al origen se reinicia este parametro
								if(minimo.getId()==verticeInicio.getId()) caminosVertice[hashing.get(minimo.getId())][j] = verticeInicio.getId() + " " + verticeInicio.getNombre();
								// Si se trata de la misma linea
								if(i==j && valoresVertice[hashing.get(minimo.getId())][j] + arco.getPeso() < minimoActual && minimo.getId()!=verticeInicio.getId()) {
									minimoActual = valoresVertice[hashing.get(minimo.getId())][j] + arco.getPeso();
									helper = caminosVertice[hashing.get(minimo.getId())][j] + " " + i + " " + vertice.getId() + " " + vertice.getNombre();
								// Si hay que hacer transferencia
								}else if(valoresVertice[hashing.get(minimo.getId())][j] + minimo.getPeso() + arco.getPeso() < minimoActual) {
									minimoActual = valoresVertice[hashing.get(minimo.getId())][j] + minimo.getPeso() + arco.getPeso();
									helper = caminosVertice[hashing.get(minimo.getId())][j] + " " + i + " " + vertice.getId() + " " + vertice.getNombre();	 
								}
							}
							valoresVertice[hashing.get(vertice.getId())][i] = minimoActual;
							if(minimo.getId()==verticeInicio.getId()) {
								caminosVertice[hashing.get(vertice.getId())][i] = " " + helper;
							}else{
								caminosVertice[hashing.get(vertice.getId())][i] = helper;
							}
						}
					}
					colaDeEspera.add(vertice); // se agrega el vertice recien analizado a la cola de priridad
					visitados[hashing.get(minimo.getId())] = true; // se marca la estacion actual como visitada
				}
			}
		}
		return respuesta1;
	}

	/**
	 *  Hace un recorrido similar que Dijkstra, salvo que al buscar en la cola de prioridad selecciona de las alternativas posibles en cada iteración, la alternativa que
     *  minimiza el costo estimado desde la estacion inicial hasta la estacion final. En otras palabras utiliza información del problema que le permita decidir cual 
     *  podría ser el mejor camino a futuro. A su vez, para cada estacion, existe un tabla que guarda los caminos y el tiempo para cada linea existente, es decir, va calculando 
     *  con un enfoque greedy el costo minimo para llegar a una estacion por una linea dada, considerando el costo hasta la estacion antecesora y el costo hacer trasbordo
     *  (si hubiere) o mantenerse en la misma linea. Al llegar al destino, escoge el camino con menor costo de todos.
     * @param GrafoND grafo
     * @return String respuesta1: Es el camino con todas las estaciones y lineas
     */
	public String aEstrellaConDijkstra(GrafoNoDirigido grafo) { 
		
		double [][] valoresVertice = new double[nroEstaciones][nroLineas]; // Aca se guardaran los costos minimo de llegar a cada estacion segun la linea
		String [][] caminosVertice = new String[nroEstaciones][nroLineas]; // Aca se guardaran los caminos costos minimo de llegar a cada estacion segun la linea
		String respuesta1 = "-1"; // String donde se guardara la respuesta
		
		// Se inicializan todos los valores como 99999 (neutro para comparar los caminos), y los caminos como vacios
		for(Vertice vertice: grafo.vertices()) {
			for(int i=0; i<nroLineas; i++) {
				valoresVertice[hashing.get(vertice.getId())][i] = 99999;
				caminosVertice[hashing.get(vertice.getId())][i] = "";
			}
		}
	
		// Se inicializan todos los valores del nodo incial
		for(int i=0; i<nroLineas; i++) {
			valoresVertice[hashing.get(verticeInicio.getId())][i] = 0;
			caminosVertice[hashing.get(verticeInicio.getId())][i] = verticeInicio.getId() + " " + verticeInicio.getNombre();
		}
		
		colaDeEspera = new HashSet<Vertice>(); // Se crea la cola de espera
		colaDeEspera.add(verticeInicio); // Se el vertice incial, que es con el comienza la busqueda
		boolean [] visitados = new boolean[nroEstaciones];
		visitados[hashing.get(verticeInicio.getId())] = true; // Se marca el vertice incial como visitado
		
		while(!colaDeEspera.isEmpty()) { 
			Vertice minimo = extraerMinimo(valoresVertice);
			// Si toca expandir el vertice destino es porque ya hemos llegado, se escoge el menor camino y se termina el programa
			if(minimo.getId()==verticeFin.getId()) { 				
				double minRespuesta = 99999;
				for(int i=0; i<nroLineas; i++) {
					if(valoresVertice[hashing.get(verticeFin.getId())][i] < minRespuesta) {
						minRespuesta = valoresVertice[hashing.get(verticeFin.getId())][i];
						respuesta1 = Double.toString(valoresVertice[hashing.get(verticeFin.getId())][i]) + " " +  (caminosVertice[hashing.get(verticeFin.getId())][i]);
					}
				}
				break;
			}

			Set<Vertice> adyacentes = grafo.adyacentes(minimo.getId()); // se buscan los sucesores del vertice minimo

			for(Vertice vertice: adyacentes) {
				if(!visitados[hashing.get(vertice.getId())]) {
					// Para cada linea de metro se busca un resultado
					for(int i=0; i<nroLineas; i++) {
						// se ve que exista una linea entre los dos arcos, y de estar se le extrae informacion
						if(grafo.estaArista(minimo, vertice, i)) {
							Arista arista = new Arista(minimo, vertice, i, grafo.obtenerArista(minimo, vertice, i).getPeso());
							arista = grafo.obtenerArista(minimo, vertice, i);
							String helper = "";
							double minimoActual = valoresVertice[hashing.get(vertice.getId())][i];
							// Para cada linea de metro del antecesor del vertice se busca el camino minimo entre seguir por la misma linea o hacer un trasbordo
							for(int j=0; j<nroLineas; j++) {
								// Si se regresa al origen se reinicia este parametro
								if(minimo.getId()==verticeInicio.getId()) caminosVertice[hashing.get(minimo.getId())][j] = verticeInicio.getId() + " " + verticeInicio.getNombre();
								// Si se trata de la misma linea
								if(i==j && valoresVertice[hashing.get(minimo.getId())][j] + arista.getPeso() < minimoActual && minimo.getId()!=verticeInicio.getId()) {
									minimoActual = valoresVertice[hashing.get(minimo.getId())][j] + arista.getPeso();
									helper = caminosVertice[hashing.get(minimo.getId())][j] + " " + i + " " + vertice.getId() + " " + vertice.getNombre();
								// Si hay que hacer transferencia
								}else if(valoresVertice[hashing.get(minimo.getId())][j] + minimo.getPeso() + arista.getPeso() < minimoActual) {
									minimoActual = valoresVertice[hashing.get(minimo.getId())][j] + minimo.getPeso() + arista.getPeso();
									helper = caminosVertice[hashing.get(minimo.getId())][j] + " " + i + " " + vertice.getId() + " " + vertice.getNombre();	 
								}
							}
							valoresVertice[hashing.get(vertice.getId())][i] = minimoActual;
							if(minimo.getId()==verticeInicio.getId()) {
								caminosVertice[hashing.get(vertice.getId())][i] = " " + helper;
							}else{
								caminosVertice[hashing.get(vertice.getId())][i] = helper;
							}
						}
					}
					colaDeEspera.add(vertice); // se agrega el vertice recien analizado a la cola de priridad
					visitados[hashing.get(minimo.getId())] = true; // se marca la estacion actual como visitada
				}
			}
		}
		return respuesta1;
	}

	/**
	 *  Busca en la cola de prioridad selecciona de las alternativas posibles en cada iteración, la alternativa que
     *  minimiza el costo estimado desde la estacion inicial hasta la estacion final.
     * @param Double [][] valoresVertice: costo de llegar a una estacion por las distintas lineas
     * @return Vertice: Retorna el vertice que cumple con la condicion de minimo
     */
	private Vertice extraerMinimo(double [][] valoresVertice) {
		
		double minimaDistancia = 999999; // Se usa como neutro en la primera iteracion, luego cambia al valor minimo segun la iteracion
		Vertice minimo = new Vertice(-1, "", -1, -1, -1); // vertice donde se guardara el vertice minimo
		
		for(Vertice vertice: colaDeEspera) {
			// Se calcula la distancia euclideana desde el vertice actual hasta el vertice final, como parte de la heuristica
			double distanciaAlVerticeFinal = 900*Math.sqrt(Math.pow(vertice.getX() - verticeFin.getX(),2) + Math.pow(vertice.getY() + verticeFin.getY(),2) );
			
			for(int i=0; i<nroLineas; i++) {
				// Se busca los valore minimos por cada linea de metro para la estacion de metro actual
				// Si el valor de la linea es menor al minimo; el minimo se actualiza
				if(valoresVertice[hashing.get(vertice.getId())][i] + distanciaAlVerticeFinal < minimaDistancia) {
					minimaDistancia = valoresVertice[hashing.get(vertice.getId())][i] + distanciaAlVerticeFinal;
					minimo = vertice;
				}
			}
		}
		
		colaDeEspera.remove(minimo); // Quita el elemento de la lista, porque se va a expandir
		return minimo; // Retorna la estacion de metro con el minimo valor de acuerdo a la heuristica
	}
	


}