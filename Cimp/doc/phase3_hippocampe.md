# Phase 3 Hippocampe

La phase 3 désigne une phase transitoire entre le travail de la phase 2 et la fin de la partie obligatoire du projet.

Il suffit juste de faire fonctionner le multi fenêtre.

```
#define NB_SCREENS	4

typedef struct      s_cimp
{
	t_cimp_screen   screen[NB_SCREENS];
	int             running;
    int             focus;
	t_cimp_select * select;
	t_cimp_event *  event;
	SDL_Surface *   copy_buffer;
}                   t_cimp;
```

Valeur par défaut de focus : -1

Le focus est changé automatiquement sur un appel de open, sur un appel explicite de fonction sur une image ou sur un clic d'une fenêtre.

Si on close une fenêtre sur laquelle était le focus, le focus devient -1.

Si une commande a besoin du focus que celui ci vaut -1. La commande échoue.

Lorsque l'on ferme une fenêtre, on met à jour le tableau screen de g_cimp par exemple si on a 4 fenêtre ouvertes :
|1|2|3|4|
et que l'on ferme la 3 ème on a :
|1|2|NULL|4|
L'id de la fenêtre correspond donc a sa position dans le tableau screen + 1.
Lorsque l'on ouvre une nouvelle fenetre on remplit la case vide la plus "petite" lorsque c'est possible.

La structure
```
typedef struct s_cimp_select {
	int      id;
	SDL_Rect surface;
} t_cimp_select;
```
gagne un champs id pour retenir sur quelle fenetre la selection ete effectuee.

## Modification parser

Pouvoir expliciter la sélection de focus au sein de n'importe quelle commande qui en a besoin .

## Modication commandes actuelles

Il faut adapter toutes les commandes pour qu'elles s'adaptent à ce nouveau fonctionnement.

## Ajout de la commande focus

La commande focus permet de modifier le focus actuel du programme.
