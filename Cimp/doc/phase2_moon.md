# Phase 2 Moon

La phase 2 désigne la phase d'après prototype. Elle permet d'avancer grandement le projet et d'approfondir les bases que l'on a déjà sur le projet.

## SDL improve

On doit pouvoir fermer une fenêtre et faire une sélection. On doit pouvoir également changer de workspace ou de focus (alt + tab) sans que les images affichées perdent leurs pixels (lol).

- Gestion des events SDL
- Gestion d'une "game loop"

## Séparation de readline

Pour gérer la mise à jour des fenêtres, on va mettre en place une "game loop" qui permet de réafficher (toutes les 30 ms par exemple) toutes les images qui sont censés être à jour.

Pour cela, le main va changer, et readline sera séparé dans un nouveau thread fils. Le thread principal qui contiendra le main fera alors une demande au fils (via un pipe) pour savoir si l'utilisateur a fini de taper une commande. Puis traitera l'information si il y a sinon il continuera de réafficher les images.

On ne peut pas conserver la partie readline dans le thread principal car readline est bloquant. On utilise alors le fait qu'un pipe puisse être non bloquant pour séparer les tâches, à savoir l'interaction avec l'utilisateur et le maintien des images (réaffichage).

## Libcheck

Mise en place de la libcheck et donc des tests unitaires sur les fonctions et du code coverage. Un bon objectif serait qu'au moins 60% du code soit testé et qu'on ait une bonne batterie de tests sur les parties importantes : parser, distribution de tâches, open, close.

## Implémentation de nouvelles commandes

Pour toutes les commandes, voir le fichier principale general.md

- save
- update

Ces deux commandes permettent alors d'utiliser la librairie SDL_Image, pour que l'on puisse manipuler tous types de fichiers images (png, jpg, etc...). Tout ce que SDL Image permet de manipuler. Normalement il y a pas beaucoup de changement à faire, mais il faut s'y intéresser et voir si il y a des cas particuliers.

- select
- unselect

Ces deux commandes sont dans la continuité du click sur une window SDL. Il faut aussi implémenter la structure t_cimp_select, ainsi que les outils de manipulation associés (alloc, free, set, get, etc...)

- cut
- copy
- paste

Ces deux commandes permettent de couper/copier/coller depuis la structure copy_buffer, qui est dans t_cimp. Idem il faut intégrer ce nouveau champ dans les free notamment.

Il faut aussi voir les comportements adaptés, et les cas particuliers (ouvrir une issue en temps voulu).

- fill
- color_replace
- color_negative
- color_gray
- color_white_black
- ajust_light_contrast

Les commandes précédentes sont rapides à partir du moment où le parser est suffisamment fourni pour accepter le format de toutes ces commandes.
