import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*; 

/**
 * @author Cristian Inojosa 17-10285
 * @author Alejandro Salazar 16-11080
 */

public class PlanearTrasbordos {
	/**
	 * Variables globales, sus nombres describe el uso de cada uno
	 */
	public static String[] lineasDeMetro;
	public static int nroLineas, nroEstaciones, minimo = Integer.MAX_VALUE;
	public static String caminoCompleto;
	public static HashMap<Integer, Integer> hashing = new HashMap <Integer, Integer>();
	public static Vertice verticeInicio, verticeFin;
    
	/**
     * Llama a las otras funciones para cargar eLa Graformacion y hacer el backtracking
     * (De 18 casos de prueba, generados por los autores, el tiempo maximo esperado por 
     * el algoritmo es alredor de dos minutos en dos casos, en los otros 16 fue instantaneo;
     * probado en una laptop con Windows10, procesador Intel Core i5-8265U @ 1.60GHz 1.80GHz y 8gb RAM)
     * @param args Archivo con la nformacion que el usuario proporciona
     */
	public static void main(String[] args) {
	    if(args.length != 4){
            System.err.println("Uso: java PlanearTrasbordos <mapa> <lineas> <pIni> <pFin>");
            return;
        }
        try{
        	BufferedReader Lector = new BufferedReader(new FileReader(args[0]));
			Grafo grafo;
			Grafo grafoInducido;
			
			String orientacion = Lector.readLine();
			if(orientacion.contentEquals("N") || orientacion.contentEquals("n")) {
				grafo = new GrafoNoDirigido();
				grafoInducido = new GrafoNoDirigido();
			} else if(orientacion.contentEquals("D") || orientacion.contentEquals("d")) {
				grafo = new GrafoDirigido();
				grafoInducido = new GrafoDirigido();
			} else {
				System.out.println("La primera linea del archivo debe contener N o D");
				Lector.close();
				return;
			}
			Lector.close(); 
			
			leerEstaciones(args[0]);
			grafo.cargarGrafo(args[0]);
			grafoInducido = grafo;
			CambiarTexto b = new CambiarTexto();
			b.recuperarArchivo(args[0], lineasDeMetro);
			
			// Busca los vertices iniciales y finales
			Collection<Vertice> vertices = grafo.vertices();
			nroEstaciones = vertices.size();
			
			verticeInicio = new Vertice (-1, "", -1, -1, -1);
			verticeFin = new Vertice (-1, "", -1, -1, -1);
			
			int i=0;
			for(Vertice vertice: vertices) {
				hashing.put(vertice.getId(), i);
				i++;
				if(vertice.getNombre().contentEquals(args[2])) 
					verticeInicio = grafo.obtenerVertice(vertice.getId());
				if(vertice.getNombre().contentEquals(args[3])) 	
					verticeFin = grafo.obtenerVertice(vertice.getId());
			}
		
			if(verticeInicio.getNombre().contentEquals("") || verticeFin.getNombre().contentEquals("")) {
				System.out.println("<pIncio> o <pFin> no pertence al conjunto de estaciones en <mapa>, chequee que esten escritas igual");
				return;
			}
		
			System.out.println("El programa termina cuando aparezca el mensaje de finalizado para cada grafo, por favor espere \n");
			
			String respuesta1, respuesta2;
			
			if(grafo instanceof GrafoNoDirigido) {
				respuesta2 = backtracking((GrafoNoDirigido)grafo, verticeInicio, verticeFin);
				inducirGrafo(args[1], (GrafoNoDirigido) grafoInducido);
				minimo = Integer.MAX_VALUE;
				respuesta1 = backtracking((GrafoNoDirigido)grafo, verticeInicio, verticeFin);
        	}else {
        		respuesta2 = backtracking((GrafoDirigido)grafo, verticeInicio, verticeFin);
				inducirGrafo(args[1], (GrafoDirigido) grafoInducido);
				minimo = Integer.MAX_VALUE;
				respuesta1 = backtracking((GrafoDirigido)grafoInducido, verticeInicio, verticeFin);
        	}
			
			if(respuesta1.contentEquals("-1"))  
				System.out.println("No hay camino en el grafo inducido entre " + verticeInicio.getNombre() + " y " + verticeFin.getNombre()); 
			else {
				String[] Auxiliar = respuesta1.split("\\s+");
				String numeroMinimoTrasbordos = Auxiliar[0], ultimaEstacionCambiada = Auxiliar[1] + " " + Auxiliar[2], ultimaLineaCambiada = Auxiliar[3];
				System.out.println("Cuando el grafo esta inducido");
				for(i=3; i<Auxiliar.length; i+=3) {
					if(!Auxiliar[i].contentEquals(ultimaLineaCambiada)) {
						System.out.println("Tome la linea " + ultimaLineaCambiada + " desde " + ultimaEstacionCambiada + " hasta " + Auxiliar[i-2]+" "+ Auxiliar[i-1]);
						ultimaEstacionCambiada = Auxiliar[i-2]+" "+ Auxiliar[i-1];
						ultimaLineaCambiada = Auxiliar[i];
					}
				}
				System.out.println("Tome la linea " + ultimaLineaCambiada + " desde " + ultimaEstacionCambiada + " hasta " +  verticeFin.getId() + " " +verticeFin.getNombre());
				System.out.println("Trasbordos totales: " + numeroMinimoTrasbordos);
			}
			
			System.out.println("\n#### Finalizado trayecto Grafo Inducido #### \n");
			
			if(respuesta2.contentEquals("-1"))  
				System.out.println("No hay camino en el grafo " + verticeInicio.getNombre() + " y " + verticeFin.getNombre()); 
			else {
				String[] Auxiliar = respuesta2.split("\\s+");
				String numeroMinimoTrasbordos = Auxiliar[0], ultimaEstacionCambiada = Auxiliar[1] + " " + Auxiliar[2], ultimaLineaCambiada = Auxiliar[3];
				System.out.println("Cuando todas las lineas estan operativas");
				for(i=3; i<Auxiliar.length; i+=3) {
					if(!Auxiliar[i].contentEquals(ultimaLineaCambiada)) {
						System.out.println("Tome la linea " + ultimaLineaCambiada + " desde " + ultimaEstacionCambiada + " hasta " + Auxiliar[i-2]+" "+Auxiliar[i-1]);
						ultimaEstacionCambiada = Auxiliar[i-2]+" "+Auxiliar[i-1];
						ultimaLineaCambiada = Auxiliar[i];
					}
				}
				System.out.println("Tome la linea " + ultimaLineaCambiada + " desde " + ultimaEstacionCambiada + " hasta " + verticeFin.getId() + " " +verticeFin.getNombre());
				System.out.println("Trasbordos totales: " + numeroMinimoTrasbordos);
			}
			
			System.out.println("\n#### Finalizado trayecto Grafo Completo #### \n");
			
        } catch (IOException e) {
            System.out.println("Error leyendo el archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error en el formato del archivo");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
	}

	/**
     * Lee cuales y cuantas lineas de metro hay en el archivo proporcionado por el usuario y mapea los nombres de 
     * las lineas de metro con un entero, modifica el archivo cambiando los nombre de las lineas por los entero 
     * recien mapeados (Luego se devuelve el archivo a su estado original)
     * @param Archivo Archivo con la nformacion que el usuario proporciona
     */
	private static boolean leerEstaciones(String archivo) {
		try {
			// Leer las lineasDeMetro y contar cuantas lineas de metro hay
	        BufferedReader Lector = new BufferedReader(new FileReader(archivo));
	        String linea = Lector.readLine();
	        
	        // Se lee las cantida de estaciones y lineas
	        int n = Integer.parseInt(Lector.readLine());
            int m = Integer.parseInt(Lector.readLine());
            
            // Se itera sobre el archivo hasta llegar a los lados
            for(int i=0; i<n; i++) {
    	        linea = Lector.readLine();
            }
            // Se extrae informacion de los lados sobre que lineas existen 
            LinkedList<String> ciudadesa = new LinkedList<String>();
            for(int i=0; i<m; i++) {
    	        String[] helper = Lector.readLine().split("\\s+");
    	        boolean aux = true;
    	        for(String s: ciudadesa) {
    	        	if(s.contentEquals(helper[2])) {
    	        		aux = false;
    	        		break;
    	        	}
    	        }
    	        if(aux) ciudadesa.add(helper[2]);
            } 
            //  se crea un array con las ciudades y  se mapean en el archivo
        	nroLineas = ciudadesa.size();
            lineasDeMetro = new String [nroLineas];
			CambiarTexto f = new CambiarTexto();
            
            for(int i=0; i<lineasDeMetro.length; i++) {
            	lineasDeMetro[i] = ciudadesa.get(i);
				f.modificarArchivo(archivo, "\\b"+lineasDeMetro[i]+"\\b", Integer.toString(i));
            }           
	        Lector.close();
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	/**
     * Lee cuales lineas de metro se conservaran, luego itera sobre los lados de una copia grafo completo y los que no sean de esas lineas
     * son eliminados, por efectos practicos no se eliminan los nodos para evitar lanzar una excepcion en caso que el pInicio o pfin
     * este en uno de ellos y se pueda usar el mismo algoritmo para el grafo y el grafo inducido. 
     * @param Archivo1 Archivo con la informacion sobre que lineas se conservarn
     * @param grafoInducido Es una grafo igual al grafo completo; al cual se le eliminaran los Arcos que no sean de la lineas validas
     * @return True si lee la informacion correctamente, falso si no
     */
	private static boolean inducirGrafo(String archivo1, GrafoDirigido grafoInducido) {
		try {
			// Leer las lineasDeMetro y contar cuantas lineas de metro hay
	        BufferedReader Lector = new BufferedReader(new FileReader(archivo1));
	        LinkedList<String> lineasInducidas = new LinkedList<String>();
	        
	        while (true) {
	            String linea = Lector.readLine();
	            if (linea == null) break;
	            lineasInducidas.add(linea);
	        }
	        Lector.close();
	        
	        for(Lado Arco: grafoInducido.lados()) {
	        	boolean auxiliar = true;
	        	for(String line : lineasInducidas) {
	        		if(lineasDeMetro[Arco.getTipo()].contentEquals(line)) {
	        			auxiliar = false;
	        		}
	        	}
	 	        
	 	        Arco aux = new Arco (Arco.getInicio(), Arco.getFin(), Arco.getTipo(), Arco.getPeso());
	 	        aux = grafoInducido.obtenerArco(Arco.getInicio(), Arco.getFin(), Arco.getTipo());
	 	        
	 	        if(auxiliar) {
	 	        	grafoInducido.eliminarArco(aux);
	 	        }
	        	
	        }
	        
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	/**
     * Este funcion busca los sucesores del vertice pInicio, y llama a la funcion BTRecursivo sobre ellos, y marca el vertice inical 
     * como visitado
     * @param GrafoD grafo
     * @param Vertice inicio
     * @param Vertice Fin
     * @return String caminoCompleto : Es el camino con todas las estaciones y lineas
     */
	public static String backtracking(GrafoDirigido grafo, Vertice inicio, Vertice fin) { 
		// Declaracion de variables
		Set<Vertice> sucesores = grafo.sucesores(verticeInicio.getId());
		int current=0;
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
	static void BTRecursivo(GrafoDirigido grafo, Arco arco, int current, String caminoActual, boolean [] visitados) { 
		// Declaracion de variables
		Vertice inicio = arco.getExtremoInicial();
		Vertice actual = arco.getExtremoFinal();
		int tipo = arco.getTipo();
		
		// Marco la estacion actual como visitada
		visitados[hashing.get(actual.getId())] = true;
		Set<Vertice> sucesores = grafo.sucesores(actual.getId());
		
		// Se actualiza el recorrido con la estacion y la linea actual
		caminoActual += " " + lineasDeMetro[tipo] + " " + Integer.toString(actual.getId()) + " " + actual.getNombre();
		String[] helper = caminoActual.split("\\s+");
		
		// Algunas condiciones que entorpecen el recorrido
		if(actual.getId()==verticeInicio.getId() || helper.length>200 || minimo==0 ) {	return;}
		
		// si llega al destino entonces acaba el backtracking y asigna los nuevos valores
		if( actual.getId()==verticeFin.getId() && current < minimo) {
			minimo = current;
			caminoCompleto = minimo + " " + caminoActual;
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
					if(i==tipo)	{ // Do nothing :)
					}else if(current+1<minimo) {
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
     * Lee cuales lineas de metro se conservaran, luego itera sobre los lados de una copia grafo completo y los que no sean de esas lineas
     * son eliminados, por efectos practicos no se eliminan los nodos para evitar lanzar una excepcion en caso que el pInicio o pfin
     * este en uno de ellos y se pueda usar el mismo algoritmo para el grafo y el grafo inducido. 
     * @param Archivo1 Archivo con la informacion sobre que lineas se conservarn
     * @param grafoInducido Es una grafo igual al grafo completo; al cual se le eliminaran la aristas que no sean de la lineas validas
     * @return True si lee la informacion correctamente, falso si no
     */
	private static boolean inducirGrafo(String archivo1, GrafoNoDirigido grafoInducido) {
		try {
			// Leer las lineasDeMetro y contar cuantas lineas de metro hay
	        BufferedReader Lector = new BufferedReader(new FileReader(archivo1));
	        LinkedList<String> lineasInducidas = new LinkedList<String>();
	        
	        while (true) {
	            String linea = Lector.readLine();
	            if (linea == null) break;
	            lineasInducidas.add(linea);
	        }
	        Lector.close();
	        
	        for(Lado Arista: grafoInducido.lados()) {
	        	boolean auxiliar = true;
	        	for(String line : lineasInducidas) {
	        		if(lineasDeMetro[Arista.getTipo()].contentEquals(line)) {
	        			auxiliar = false;
	        		}
	        	}
	 	        
	 	        Arista aux = new Arista (Arista.getInicio(), Arista.getFin(), Arista.getTipo(), Arista.getPeso());
	 	        aux = grafoInducido.obtenerArista(Arista.getInicio(), Arista.getFin(), Arista.getTipo());
	 	        
	 	        if(auxiliar) {
	 	        	grafoInducido.eliminarArista(aux);
	 	        }
	        }
	        //System.out.println(grafoInducido.numeroDeLados());
	        //System.out.println(grafoInducido.numeroDeVertices());
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	/**
     * Esta funcion busca los adyacentes del vertice pInicio, y llama a la funcion BTRecursivo sobre ellos, y marca el vertice inical 
     * como visitado
     * @param GrafoD grafo
     * @param Vertice inicio
     * @param Vertice Fin
     * @return String caminoCompleto : Es el camino con todas las estaciones y lineas
     */
	public static String backtracking(GrafoNoDirigido grafo, Vertice inicio, Vertice fin) { 
		// Declaracion de variables
		Set<Vertice> adyacentes = grafo.adyacentes(verticeInicio.getId());
		int current=0;
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
	static void BTRecursivo(GrafoNoDirigido grafo, Vertice previo, Arista arista, int current, String caminoActual, boolean [] visitados) { 
		//  Como el TAD no diferencia entre {u,v} y {v,u} buscamos cual es cuale
		Vertice inicio = previo.getId() == arista.getExtremo1().getId() ? arista.getExtremo1() : arista.getExtremo2() ;
		Vertice actual = previo.getId() == arista.getExtremo2().getId() ? arista.getExtremo1() : arista.getExtremo2() ;
		int tipo = arista.getTipo();
		
		// Marista la estacion actual como visitada
		visitados[hashing.get(actual.getId())] = true;
		Set<Vertice> adyacentes = grafo.adyacentes(actual.getId());
		
		caminoActual += " " + lineasDeMetro[tipo] + " " + Integer.toString(actual.getId()) + " " + actual.getNombre();
		String[] helper = caminoActual.split("\\s+");
		
		// Algunas condiciones que entorpecen el recorrido
		if(actual.getId()==verticeInicio.getId() || helper.length>200 || minimo==0 ) {	return;}
		
		// si llega al destino entonces acaba el backtracking y asigna los nuevos valores
		if( actual.getId()==verticeFin.getId() && current < minimo) {
			minimo = current;
			caminoCompleto = minimo + " " + caminoActual;
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
					if(i==tipo)	{ // Do nothing :)
					}else if(current+1<minimo) {
						BTRecursivo((GrafoNoDirigido) grafo, actual, aristaAux, current+1, caminoActual, visitados);
					}
					// Se marca como falso para que no estore en los otros recorridos
					visitados[hashing.get(verticeAdj.getId())] = false;
				}
			}
		}
		// Marista la estacion actual como visitada, por si acaso
		visitados[hashing.get(actual.getId())] = true;
		return;
	}

}