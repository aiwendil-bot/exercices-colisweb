
1/ Choix de l'algorithme

J'ai choisi d'utiliser l'aglgorithme de Dijkstra car c'est un problème de plus court chemin dans un graphe pondéré
dont tous les poids sont positifs (distances). Il est de plus assez simple à mettre en œuvre.

2/ Choix d'implémentation et explication de l'algorithme

L'environnement proposé a pour représentation du graphe une liste des arêtes.
Cette représentation n'étant pas très pratique d'utilisation (surtout pour Dijkstra), je crée tout d'abord une liste d'adjacence
sous forme de map Point => Liste[(Point, Distance)] (les valeurs sont les voisins de la clé, avec la distance qui les sépare)

La façon de créer cette liste a un défaut, les points qui peuvent être atteints mais qui n'ont eux mêmes pas d'arêtes
vers d'autres points ne sont pas présents en tant que clé (d'où le getOrElse ligne 23 de shortestPath.scala)

L'algorithme en lui-même se résume à la seule fonction 'dijkstra' qui est tail recursive

@params :

adjList : la liste d'adjacence qui représente le graphe
bestknownPaths : une List[(List[Point,Double)] qui garde trace les meilleurs chemins trouvés pour chaque point atteint
avec leur longueur totale
cette liste sera triée par ordre croissant des longueurs à chaque itération (car l'algorithme repart du chemin le plus
court à chaque itération)
visited : liste des points déjà visités

on commence par regarder bestKnownPaths :

s'il n'est pas la liste vide :

    si le dernier point ajouté dans le premier chemin est la destination, je retourne le chemin (renversé car j'ajoute
    le dernier point exploré en début de chemin)

    si ce n'est pas le cas : je regarde les voisins de ce point (s'il en a, cf adjList, et s'il n'en a pas il n'y a rien à faire)
    et pour chacun des voisins non déjà visités je crée une liste de chemins, constitués du voisin ajouté en tête du
    chemin "actuel" de l'itération. Pour ceux déjà visités il n'y a pas de nouveau chemin de créé mais une liste vide

    enfin, je trie bestKnownPaths par longueur croissante et j'appelle dijkstra sur cette liste triée et sur visited
    auquel j'ai ajouté le point visité lors de l'itération.

si c'est la liste vide, je renvoie None : tous les points ont été visités sans rencontrer end,
il n'existe donc pas de chemin entre start et end

Pour le premier appel, le seul chemin connu est celui avec seulement le point de départ, et donc comme longueur 0.
aucun point n'a été encore exploré donc visited vaut la liste vide

j'ai aussi implementé le calcul de la longueur d'un chemin dans Path.scala

3/ les +

- implémentation purement fonctionnelle, tail recursive
- je n'utilise que des "val" et des structures de donnée immuables

4/ les -

- cette implémentation "galère" pour les grosses instances et stickman, sur ma vieille machine du moins
(ubuntu 20.04, intel i5 2.5 GHz, RAM 8Go)
- une opération coûteuse est le tri de bestKnownPaths, je n'ai pas eu d'autre idée pour éviter ou diminuer ce coût
avec la contrainte de faire entièrement du fonctionnel

5/ Scala

il se prend facilement en main, j'ai le ressenti d'un mélange entre Julia et OCaml, j'aime bien !