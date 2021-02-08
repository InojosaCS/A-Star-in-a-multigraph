# A Star in a multigraph
Proyecto 3 CI2693

# Objetivo
Programa que, dado un mapa, líneas operativas y los nombres de dos paradas, indique las líneas que se deben tomar, y dónde realizar la transferencia entre ellas para realizar el trayecto lo mas rapido posible. 

# Algoritmo
El algoritmo utilizado es A estrella. Se utiliza la distancia euclideana como funcion (junto al costo real hasta una estacion dada) para estimar el tiempo faltante, se promedio el tiempo entre varios tramos del recorrido para calcular  una constante que multiplica a la distancia euclideana para que esta sea representativa. Luego se comparan los resultados cuando se minimizan los trasbordos.

Para el minimo de trasbordos se usa el mismo algoritmo que en el proyecto 2, el cual retorna el camino y en este archivo hay una funcion que calcula el tiempo del mismo

# Estructura del grafo
Para la estructura interna en la que se guardó el grafo se uso la estructura: 
HashMap<sup>1</sup><Integer,Pair<Vertice,HashMap<sup>2</sup><Vertice,HashMap<sup>3</sup><Integer,Arco»».

Donde en el primer HashMap<sup>1</sup> , las clave son enteros, esto porque el acceso a los vértices se hacía mediante su id, como el id era un entero, entonces era más rapido saber si un vertice existá o no con esta estructura, que, por ejemplo, con Lists.

Lo que contiene este HashMap<sup>1</sup> es un par, donde el primer elemento del par es el Vértice principal, y el segundo valor del par, es otro HashMap<sup>2</sup>.

Dicho HashMap<sup>2</sup>, contiene los vértices con los que el vértice principal está relacionado, este HashMap<sup>2</sup> está indexado por objetos vértices, es decir, esas son las claves, y el contenido de él, es otro HashMap<sup>3</sup>.

Este, a su vez está indexado por enteros, y el contenido son aristas o arcos, dependiendo si el grafo es dirigido o no dirigido. Esto nos permite tener varias aristas o arcos de diferente tipo, aquí el entero del HashMap<sup>3</sup> (que lo indexa) representa el tipo del arco o arista.

Otro detalle de implementación es que para saber cúantos vértices o lados tiene el grafo, usamos un contador que va aumentando cuando se agrega un vértices o lado, y disminuyendo cuando se elimina alguno, de esta manera no tenemos que complicarnos recorriendo la estructura. Esto, no sólo nos hace más fácil la implentación, sino que nos ahorra bastante tiempo de ejecución pues la complejidad de esta operaciń pasa a ser es O(1).

Nota: Se usaron superíndices para representar los HashMap<sup> </sup>para facilidad del lector
