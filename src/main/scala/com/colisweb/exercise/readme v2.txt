
1/ l'heuristique du plus proche voisin est valide


2/ Shortest Path :

dijkstra utilise comme conseillé une Map de clés les points extrêmes explorés et en valeurs le meilleur chemin
du point de départ à la clé
l'implémentation est plus rapide

sortWith a été remplacé par sortBy

3/ 2-opt

la représentation du cycle hamiltonien est maintenant une liste d'Edge comme conseillé
les noms des variables et fonctions sont plus explicites

twoOptSwap :

cette fonction effectue un mouvement 2-opt : elle remplace deux arêtes (a,b) et (x,y) par les arêtes (a,x) et (b,y),
en renversant le chemin entre b et x pour conserver l'orientation du cycle

@params

le cycle à améliorer : List[Edge]
les deux arêtes à remplacer EdgeToSwap1 et EdgeToSwap2

on évalue les indices des deux arêtes pour pouvoir utiliser les méthodes slice et takeRight (voir ci-après)

on reconstruit le cycle de la façon suivante :

on prend les premières arêtes du cycle jusqu'à edgeToSwap1 (exclus)
on ajoute la première nouvelle arête
on ajoute le chemin entre les deux arêtes à remplacer et on le renverse (pour conserver l'orientation)
on ajoute la deuxième nouvelle arête
on ajoute les dernières arêtes du cycle à partir de EdgeToSwap2 (exclus)

twoOpt & twoOptHelper :

comme la distance est symétrique, on ne veut pas parcourir deux fois cycle pour trouver une paire
d'arêtes améliorable. De plus, on ne peut pas effectuer de mouvement 2-opt sur deux arêtes adjacentes.

twoOptHelper parcourt donc le cycle une première fois de cycle(0) à cycle(n-2) (c'est le premier match avec edge1)
et une deuxième fois de cycle( i(edge1) + 2 ) à cycle(n)   (i pour indice, n = nb d'éléments du cycle)

si une amélioration est trouvée (lorsque l'égalité d(a,b) + d(x,y) > d(a,x) + d(b,y) est vérifiée) alors on stoppe le
parcours et on renvoie ce nouveau cycle
si aucune amélioration est trouvée, on renvoie le cycle de départ (on en garde trace via cycleCopy)

twoOpt appelle une première fois twoOptHelper, et compare le résultat au cycle avant appel :

s'ils ont la même longueur, aucune amélioration n'a été trouvée et on renvoie ce cycle
sinon, on rappelle twoOpt sur ce nouveau cycle

4/ généralités

toutes les méthodes récursives sont tail-recursive
quand on a besoin de comparer des distances (plus proches voisins, condition de 2-opt swap) la méthode distance2
est utilisée pour éviter l'opération sqrt qui est superflue dans ce cas