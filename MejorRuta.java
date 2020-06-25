/**
 * Programa que, dado un mapa, líneas operativas y los nombres de
 * dos paradas, indique las líneas que se deben tomar, y dónde realizar
 * la transferencia entre ellas para realizar el trayecto lo mas rapido posible.
 * El algoritmo utilizado es A estrella. Se utiliza la distancia euclideana como
 * funcion (junto al costo real hasta una estacion dada) para estimar el tiempo 
 * faltante, se promedio el tiempo entre varios tramos del recorrido para calcular 
 * una constante que multiplica a la distancia euclideana para que esta sea representativa. 
 * Luego se comparan los resultados cuando se minimizan los trasbordos.  
 * Para el minimo de trasbordos se usa el mismo algoritmo que en el proyecto 2, el cual
 * retorna el camino y en este archivo hay una funcion que calcula el tiempo del mismo
 * La parte de lectura del input se divide en dos archivos, el presente y CambiarTexto.java
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*; 

/**
 * @author Cristian Inojosa 17-10285
 * @author Alejandro Salazar 16-11080
 */

public class MejorRuta {

	/**
	 * Variables que guarda los nombres de las lineas de metro
	 */
	private static String[] lineasDeMetro;
	
	/**
	 * Variables globales, sus nombres describe el uso de cada uno
	 */
	private static int nroLineas, nroEstaciones;
	
	/**
	 * Variable que representa la cantidad minima de trasbordos para cualquier etapa de la arborescencia 
	 * del recorrido del grafo, sirve para acotar el numero de trasbordos posibles durante el backtracking
	 * Se inicializa en 7 para optimizar el backtracking, ya que en Caracas no hay ninguna ruta que tome mas de 7 
	 * trasbordos
	 */
	public static int  minimo = 7;
	
	/**
	 * Variable que sirve para mapear N id's de N vertices con una secuencia de {0,2,3...;N-1} 
	 * se usa para marcar los vertices como visitados en O(n) en complejidad de espacio
	 */
	private static HashMap<Integer, Integer> hashing = new HashMap <Integer, Integer>();
	
	/**
	 * Variables que marcan los vertices de inicio y fin de camino que estamos buscando
	 */
	private static Vertice verticeInicio, verticeFin;
	
	/**
	 * El priority queue (nota: La complejidad con monticulo de fibonacci no disminunia pues N*logN < N*(nroLados^2)
	 */
	public static Set<Vertice> colaDeEspera; 
    

	/**
     * Llama a las otras funciones para cargar la informacion del grafo,
     * llamar a A Estrella, hacer el Backtracking, e imprimir la respuesta.
     * @param args Archivo con la nformacion que el usuario proporciona
     */
	public static void main(String[] args) {
	    if(args.length != 4){
            System.err.println("Uso: java MejorRuta <mapa> <lineas> <pIni> <pFin>");
            return;
        }
        try{
        	BufferedReader Lector = new BufferedReader(new FileReader(args[0]));
			Grafo grafo;
			Grafo grafoInducido;
			// Se le da orientacion al grafo
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
			// Se carga su info
			leerEstaciones(args[0]);
			grafo.cargarGrafo(args[0]);
			grafoInducido = grafo;
			CambiarTexto b = new CambiarTexto();
			b.recuperarArchivo(args[0], lineasDeMetro);
			
			// Busca los vertices iniciales y finales y se mapean los verices
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
			// Si alguno de los vertices no esta, se lanza un mensaje de error
			if(verticeInicio.getNombre().contentEquals("") || verticeFin.getNombre().contentEquals("")) {
				System.out.println("<pIncio> o <pFin> no pertence al conjunto de estaciones en <mapa>, chequee que esten escritas igual");
				return;
			}
			System.out.println("Caminos entre " + verticeInicio.getNombre() + " y " + verticeFin.getNombre() + " (El programa termina cuando aparezca el mensaje de finalizado):\n");
			
			LinkedList<String> respuestas = new LinkedList<String>(); // Lista enlazada donde se guardaran Strings con las respuestas
			String grafoInducidoAEstrella = "-1", grafoInducidoBacktracking;
			PlanearRutas rutas = new PlanearRutas(lineasDeMetro, nroLineas, nroEstaciones, minimo, hashing, verticeInicio, verticeFin);
			
			// Se llama a las funciones para guardar los caminos en unos Strings a los cuales luego se les dara el formato de salida
			if(grafo instanceof GrafoNoDirigido) {
				inducirGrafo(args[1], (GrafoNoDirigido) grafoInducido);
				grafoInducidoAEstrella = rutas.aEstrellaConDijkstra((GrafoNoDirigido) grafoInducido);
				grafoInducidoBacktracking = rutas.backtracking((GrafoNoDirigido)grafoInducido, verticeInicio, verticeFin);
				grafoInducidoBacktracking = obtenerTiempo((GrafoNoDirigido)grafoInducido, grafoInducidoBacktracking) + grafoInducidoBacktracking;
			}
			else {
				inducirGrafo(args[1], (GrafoDirigido) grafoInducido);
				grafoInducidoAEstrella = rutas.aEstrellaConDijkstra((GrafoDirigido) grafoInducido);
				grafoInducidoBacktracking = rutas.backtracking((GrafoDirigido)grafoInducido, verticeInicio, verticeFin);
				grafoInducidoBacktracking = obtenerTiempo((GrafoDirigido)grafoInducido, grafoInducidoBacktracking) + grafoInducidoBacktracking;
			}
			
			// En el caso hipotetico, que el algoritmo no haya retornada el camino minimo, se retornara el camino minimo conocido en el programa
			String[] h3, h4;
			h3 = grafoInducidoAEstrella.split("\\s+");
			h4 = grafoInducidoBacktracking.split("\\s+");
			
			if(Double.parseDouble(h3[0])>Double.parseDouble(h4[0])) grafoInducidoAEstrella = grafoInducidoBacktracking; 
			
			// Se agregan las respuestas en la lista para luego iterar sobre ellas
			respuestas.add(grafoInducidoAEstrella);
			respuestas.add(grafoInducidoBacktracking);
			
			// Se le da el formato a la respuesta con la informacion proporcionada por el algoritmo
			String a = "", auxiliar [] = new String[] {"Con las lineas inducidas:", "Con las lineas inducidas minimizando transbordos:"};
			int k=0;
			
			// Se itera y se imprime las respuestas con su formato correspondiente
			for(String respuesta: respuestas) {	
				if(respuesta.contentEquals("-1")) { 
					System.out.println("No hay camino en el grafo inducido entre " + verticeInicio.getNombre() + " y " + verticeFin.getNombre() + a + "\n"); 
					a = " minimizando trasbordos";
				}else {
					String[] Auxiliar = respuesta.split("\\s+");
					String tiempoMinimo = Auxiliar[0], ultimaEstacionCambiada = Auxiliar[1] + " " + Auxiliar[2]; 
					int ultimaLineaCambiada = Integer.parseInt(Auxiliar[3]);
					System.out.println(auxiliar[k]);
					
					for(i=3; i<Auxiliar.length; i+=3) {
						if(Integer.parseInt(Auxiliar[i])!=(ultimaLineaCambiada)) {
							System.out.println("Tome la linea " + lineasDeMetro[ultimaLineaCambiada] + " desde " + ultimaEstacionCambiada + " hasta " + Auxiliar[i-2]+" "+ Auxiliar[i-1]);
							ultimaEstacionCambiada = Auxiliar[i-2]+" "+ Auxiliar[i-1];
							ultimaLineaCambiada = Integer.parseInt(Auxiliar[i]);
						}
					}
					
					System.out.println("Tome la linea " + lineasDeMetro[ultimaLineaCambiada] + " desde " + ultimaEstacionCambiada + " hasta " +  verticeFin.getId() + " " +verticeFin.getNombre());
					System.out.println("Tiempo total: " + tiempoMinimo +"\n");
				}
				k++;
        	}	
			System.out.println("Fin del programa");
			
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
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	/**
     * Recibe un string con el camino entre dos vertices y obtiene el tiempo necesario para recorrelos 
     * @param grafo Grafo con la ciudad de Caracas
     * @param respuesta String con con el recorrido entre dos vertices
     * @return El tiempo que toma en recorrer el camino
     */
	private static String obtenerTiempo(GrafoDirigido grafo, String respuesta) {
		String tiempoDelCamino = "";
		double tiempo = 0;
		
		if(!respuesta.contentEquals("-1")){
			String[] Auxiliar = respuesta.split("\\s+");
			int ultimaLineaCambiada = Integer.parseInt(Auxiliar[2]);
			tiempo += grafo.obtenerVertice(Integer.parseInt(Auxiliar[0])).getPeso();
			
			for(int i=2; i<Auxiliar.length; i+=3) {
				Vertice vi = new Vertice(0,"",0, 0, 0); Vertice vf = new Vertice(0,"",0, 0, 0);
				vi = grafo.obtenerVertice(Integer.parseInt(Auxiliar[i-2]));
				vf = grafo.obtenerVertice(Integer.parseInt(Auxiliar[i+1]));
				tiempo += grafo.obtenerArco(vi, vf, Integer.parseInt(Auxiliar[i])).getPeso();
				if(Integer.parseInt(Auxiliar[i])!=(ultimaLineaCambiada)) {
					ultimaLineaCambiada = Integer.parseInt(Auxiliar[i]);
					tiempo += grafo.obtenerVertice(Integer.parseInt(Auxiliar[i-2])).getPeso();
				}
			}
		}
		return tiempoDelCamino = tiempo==0 ? tiempoDelCamino : Double.toString(tiempo) + " ";
	}
	
	/**
     * Recibe un string con el camino entre dos vertices y obtiene el tiempo necesario para recorrelos 
     * @param grafo Grafo con la ciudad de Caracas
     * @param respuesta String con con el recorrido entre dos vertices
     * @return El tiempo que toma en recorrer el camino
     */
	private static String obtenerTiempo(GrafoNoDirigido grafo, String respuesta) {
		String tiempoDelCamino = "";
		double tiempo = 0;
		
		if(!respuesta.contentEquals("-1")){
			String[] Auxiliar = respuesta.split("\\s+");
			int ultimaLineaCambiada = Integer.parseInt(Auxiliar[2]);
			tiempo += grafo.obtenerVertice(Integer.parseInt(Auxiliar[0])).getPeso();
			
			for(int i=2; i<Auxiliar.length; i+=3) {
				Vertice vi = new Vertice(0,"",0, 0, 0); Vertice vf = new Vertice(0,"",0, 0, 0);
				vi = grafo.obtenerVertice(Integer.parseInt(Auxiliar[i-2]));
				vf = grafo.obtenerVertice(Integer.parseInt(Auxiliar[i+1]));
				tiempo += grafo.obtenerArista(vi, vf, Integer.parseInt(Auxiliar[i])).getPeso();
				if(Integer.parseInt(Auxiliar[i])!=(ultimaLineaCambiada)) {
					ultimaLineaCambiada = Integer.parseInt(Auxiliar[i]);
					tiempo += grafo.obtenerVertice(Integer.parseInt(Auxiliar[i-2])).getPeso();
				}
			}
		}
		return tiempoDelCamino = tiempo==0 ? tiempoDelCamino : Double.toString(tiempo) + " ";
	}
}