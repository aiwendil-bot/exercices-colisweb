Pour cette variante du voyageur de commerce, j'ai choisi deux heuristiques, celle du plus proche voisin (construction)
et une de recherche locale, 2-opt (amélioration)

Je n'ai pas réussi à implémenter la permutation d'arêtes du 2-opt de façon fonctionnelle.

 Choix d'implémentation

Faute d'avoir trouvé une meilleure structure de données pour représenter un cycle, j'ai choisi
la liste pour représenter le cycle.

Comme 2-opt permute les arêtes, j'ai eu besoin d'implémenter deux fonctions renvoyant respectivement l'élément suivant
et l'élément précédent d'un élément (en paramètre) dans une liste.

Pour l'heuristique du plus proche voisin, elle s'implémente rapidement grâce aux fonctions distance et minBy, mais
elle provoque une erreur stackoverflow sur l'instance 5915 nodes pour une raison qui m'échappe

Le pseudo code dont je me suis inspiré pour 2-opt est celui de la page https://fr.wikipedia.org/wiki/2-opt

Le choix de la permutation parmi celles possibles à chaque itération est la première trouvée.

Pour parcourir les sommets du cycle afin de chercher les arêtes "améliorables", j'utilise deux fonctions récursives
sur deux copies du cycle.

Lors d'une permutation, on remplace les arêtes (a,b) et (x,y) par (a,x) et (b,y), mais le chemin entre les sommets
b et x est renversé. (cf illustration page 4 de https://arxiv.org/pdf/2010.02583.pdf)

permEdges permettrait normalement de permuter deux arêtes, et on renverserait alors le chemin entre b et x après son appel
(ligne 76)

Malgré de nombreux essais, je n'ai pas réussi à implémenter cette permutation d'arêtes en utilisant du pattern matching
sur la liste représentant le cycle.

Pour garder trace de s'il y a eu une amélioration lors d'une itération, j'utilise une liste que je remplis
au fur et à mesure de true / false (s'il y a eu ou non amélioration) et je regarde à la fin d'une itération s'il y a au moins un true.

On repart ensuite d'une liste vide après avoir parcouru toutes les paires d'arêtes.