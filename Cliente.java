import java.io.BufferedReader;
import java.io.FileReader;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * 
 */

/**
 * @author alejandrojsn
 *
 */
public class Cliente {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if(args.length < 1) {
				System.out.println("Uso: java Cliente <archivo>");
			}
			
			BufferedReader Lector = new BufferedReader(new FileReader(args[0]));
			
			Grafo grafo;
			
			String orientacion = Lector.readLine();
			if(orientacion.contentEquals("N")) {
				grafo = new GrafoNoDirigido();
			} else if(orientacion.contentEquals("D")) {
				grafo = new GrafoDirigido();
			} else {
				System.out.println("La primera linea del archivo debe contener N o D");
				return;
			}
			
			Lector.close();
			
			grafo.cargarGrafo(args[0]);
			
			Scanner input = new Scanner(System.in);
			
			System.out.print(
				"Ingrese:\n" +
				"0: salir\n" +
				"1: ver numero de vertices\n" +
				"2: ver numero de lados\n" +
				"3: agregar nuevo vertice\n" +
				"4: obtener informacion de un vertice\n" +
				"5: ver si un vertice existe\n" +
				"6: eliminar vertice\n" +
				"7: imprimir todos los vertices\n" +
				"8: imprimir todos los lados\n" +
				"9: imprimir el grado de un vertice\n" +
				"10: ver los vertices adyacentes a un vertice\n" +
				"11: ver los lados incidentes a un vertice\n"
			);
			if(grafo instanceof GrafoNoDirigido) {
				System.out.print(
					"12: agregar arista\n" +
					"13: eliminar arista\n" +
					"14: ver si una arista existe\n" +
					"15: obtener informacion de una arista\n"
				);
			} else if (grafo instanceof GrafoDirigido) {
				System.out.print(
						"12: agregar arco\n" +
						"13: eliminar arco\n" +
						"14: ver si un arco existe\n" +
						"15: obtener informacion de un arco\n" +
						"16: obtener predecesores de un vertice\n" +
						"17: obtener sucesores de un vertice\n" +
						"18: obtener grado interior\n" +
						"19: obtener grado exterior\n"
					);
			}
			
			while(true) {
				int opcion = input.nextInt();
				
				int id, a, b, tipo;
				double x, y, peso;
				String nombre, no;
				Vertice v;
				
				switch (opcion) {
					case 0:
						return;
						
					case 1:
						System.out.println("Hay " + grafo.numeroDeVertices() + " vertices");
						break;
					
					case 2:
						System.out.println("Hay " + grafo.numeroDeLados() + " lados");
						break;
					
					case 3:
						System.out.println("Id del nuevo vertice:");
						id = input.nextInt();
						
						System.out.println("Nombre del nuevo vertice:");
						nombre = input.next();
						
						System.out.println("Coordenada X del nuevo vertice:");
						x = input.nextDouble();
						
						System.out.println("Coordenada Y del nuevo vertice:");
						y = input.nextDouble();
						
						System.out.println("Peso del nuevo vertice:");
						peso = input.nextDouble();
						
						no = grafo.agregarVertice(id, nombre, x, y, peso) ? "" : "no";
						System.out.println("Vertice " + no + " agregado");
						break;
					
					case 4:
						System.out.println("Id del vertice:");
						id = input.nextInt();
						try {
							v = grafo.obtenerVertice(id);
							imprimirVertice(v);
						} catch (NoSuchElementException e) {
							System.out.println("El vertice no existe");
						}
						break;
						
					case 5:
						System.out.println("Id del vertice:");
						id = input.nextInt();
						
						no = grafo.estaVertice(id) ? "" : "no";
						System.out.println("El vertice " + no + " existe");
						break;
						
					case 6:
						System.out.println("Id del vertice:");
						id = input.nextInt();
						
						try {
							grafo.eliminarVertice(id);
							System.out.println("Vertice eliminado");
						} catch (NoSuchElementException e) {
							System.out.println("El vertice no existe");
						}
						break;
						
					case 7:
						for(Vertice u: grafo.vertices()) {
							imprimirVertice(u);
						}
						break;
						
					case 8:
						for(Lado l : grafo.lados()) {
							imprimirLado(l);
						}
						break;
					
					case 9:
						System.out.println("Id del vertice:");
						id = input.nextInt();
						
						try {
							System.out.println(grafo.grado(id));
						} catch (NoSuchElementException e) {
							System.out.println("El vertice no existe");
						}
						break;
					
					case 10:
						System.out.println("Id del vertice:");
						id = input.nextInt();
						
						try {
							for(Vertice u: grafo.adyacentes(id)) {
								imprimirVertice(u);
							}
						} catch (NoSuchElementException e) {
							System.out.println("El vertice no existe");
						}
						break;
					
					case 11:
						System.out.println("Id del vertice:");
						id = input.nextInt();
						
						try {
							for(Lado l : grafo.incidentes(id)) {
								imprimirLado(l);
							}
						} catch (NoSuchElementException e) {
							System.out.println("El vertice no existe");
						}
						break;
						
					case 12:
						if (grafo instanceof GrafoNoDirigido) {
							System.out.println("Id del extremo 1:");
							a = input.nextInt();
							System.out.println("Id del extremo 2:");
							b = input.nextInt();
							System.out.println("Tipo de la arista:");
							tipo = input.nextInt();
							System.out.println("Peso de la arista:");
							peso = input.nextDouble();
							no = ((GrafoNoDirigido)grafo).agregarArista(
								grafo.obtenerVertice(a),
								grafo.obtenerVertice(b),
								tipo,
								peso
							) ? "" : "no";
							System.out.println("La arista " + no + " fue agregada");
							
						} else if(grafo instanceof GrafoDirigido) {
							System.out.println("Id del extremo inicial:");
							a = input.nextInt();
							System.out.println("Id del extremo final:");
							b = input.nextInt();
							System.out.println("Tipo de arco:");
							tipo = input.nextInt();
							System.out.println("Peso del arco:");
							peso = input.nextDouble();
							no = ((GrafoDirigido)grafo).agregarArco(
								grafo.obtenerVertice(a),
								grafo.obtenerVertice(b),
								tipo,
								peso
							) ? "" : "no";
							System.out.println("El arco " + no + " fue agregado");
						}
						break;
						
					case 13:
						if (grafo instanceof GrafoNoDirigido) {
							System.out.println("Id del extremo 1:");
							a = input.nextInt();
							System.out.println("Id del extremo 2:");
							b = input.nextInt();
							System.out.println("Tipo de la arista:");
							tipo = input.nextInt();
							try {
								no = ((GrafoNoDirigido)grafo).eliminarArista(
									((GrafoNoDirigido)grafo).obtenerArista(
										grafo.obtenerVertice(a),
										grafo.obtenerVertice(b),
										tipo
									)
								) ? "" : "no";
							} catch(NoSuchElementException e) {
								no = "no";
							}
							System.out.println("La arista " + no + " fue eliminada");
						} else if (grafo instanceof GrafoDirigido) {
							System.out.println("Id del extremo inicial:");
							a = input.nextInt();
							System.out.println("Id del extremo final:");
							b = input.nextInt();
							System.out.println("Tipo del arco:");
							tipo = input.nextInt();
							try {
								no = ((GrafoDirigido)grafo).eliminarArco(
									((GrafoDirigido)grafo).obtenerArco(
										grafo.obtenerVertice(a),
										grafo.obtenerVertice(b),
										tipo
									)
								) ? "" : "no";
							} catch(NoSuchElementException e) {
								no = "no";
							}
							System.out.println("El arco " + no + " fue eliminado");
						}
						break;
					
					case 14:
						if (grafo instanceof GrafoNoDirigido) {
							System.out.println("Id del extremo 1:");
							a = input.nextInt();
							System.out.println("Id del extremo 2:");
							b = input.nextInt();
							System.out.println("Tipo de la arista:");
							tipo = input.nextInt();
							no = ((GrafoNoDirigido)grafo).estaArista(
								grafo.obtenerVertice(a),
								grafo.obtenerVertice(b),
								tipo
							) ? "" : "no";
							System.out.println("La arista " + no + " existe");
						} else if(grafo instanceof GrafoDirigido) {
							System.out.println("Id del extremo inicial:");
							a = input.nextInt();
							System.out.println("Id del extremo final:");
							b = input.nextInt();
							System.out.println("Tipo de arco:");
							tipo = input.nextInt();
							no = ((GrafoDirigido)grafo).estaArco(
								grafo.obtenerVertice(a),
								grafo.obtenerVertice(b),
								tipo
							) ? "" : "no";
							System.out.println("El arco " + no + " existe");
						}
						break;
					
					case 15:
						if (grafo instanceof GrafoNoDirigido) {
							System.out.println("Id del extremo 1:");
							a = input.nextInt();
							System.out.println("Id del extremo 2:");
							b = input.nextInt();
							System.out.println("Tipo de la arista:");
							tipo = input.nextInt();
							try {
								imprimirLado(((GrafoNoDirigido)grafo).obtenerArista(
									grafo.obtenerVertice(a),
									grafo.obtenerVertice(b),
									tipo
								));
							} catch(NoSuchElementException e) {
								System.out.println("La arista no existe");
							}
						} if(grafo instanceof GrafoDirigido) {
							System.out.println("Id del extremo inicial:");
							a = input.nextInt();
							System.out.println("Id del extremo final:");
							b = input.nextInt();
							System.out.println("Tipo de arco:");
							tipo = input.nextInt();
							try {
								imprimirLado(((GrafoDirigido)grafo).obtenerArco(
									grafo.obtenerVertice(a),
									grafo.obtenerVertice(b),
									tipo
								));
							} catch(NoSuchElementException e) {
								System.out.println("La arista no existe");
							}
						}
						break;
						
					case 16:
						if(grafo instanceof GrafoNoDirigido) {
							break;
						}
						System.out.println("Id del vertice:");
						id = input.nextInt();
						try {
							for(Vertice u : ((GrafoDirigido)grafo).predecesores(id)) {
								imprimirVertice(u);
							}
						} catch (NoSuchElementException e) {
							System.out.println("El vertice no existe");
						}
						break;
					
					case 17:
						if(grafo instanceof GrafoNoDirigido) {
							break;
						}
						System.out.println("Id del vertice:");
						id = input.nextInt();
						try {
							for(Vertice u : ((GrafoDirigido)grafo).sucesores(id)) {
								imprimirVertice(u);
							}
						} catch (NoSuchElementException e) {
							System.out.println("El vertice no existe");
						}
						break;
					
					case 18:
						if(grafo instanceof GrafoNoDirigido) {
							break;
						}
						System.out.println("Id del vertice:");
						id = input.nextInt();
						
						try {
							System.out.println(((GrafoDirigido)grafo).gradoInterior(id));
						} catch (NoSuchElementException e) {
							System.out.println("El vertice no existe");
						}
						break;
						
					case 19:
						if(grafo instanceof GrafoNoDirigido) {
							break;
						}
						System.out.println("Id del vertice:");
						id = input.nextInt();
						
						try {
							System.out.println(((GrafoDirigido)grafo).gradoExterior(id));
						} catch (NoSuchElementException e) {
							System.out.println("El vertice no existe");
						}
						break;
				}
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void imprimirVertice(Vertice v)
	{
		System.out.print(
			"Vertice " + v + "\n" +
			"Nombre: " + v.getNombre() + "\n" +
			"X: " + v.getX() + "\n" +
			"Y: " + v.getY() + "\n" +
			"Peso: " + v.getPeso() + "\n"
		);
	}
	
	public static void imprimirLado(Lado l)
	{
		System.out.print(
			"Lado " + l + "\n" +
			"Tipo: " + l.getTipo() + "\n" +
			"Peso: " + l.getPeso() + "\n"
		);
	}

}
