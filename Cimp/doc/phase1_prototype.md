# Phase 1 prototype

Ce document présente les objectifs de la phase 1, à savoir construire une première version simplifiée fonctionnelle.

# Structure

```
typedef struct      s_cimp_screen
{
	SDL_Window      *window;
	SDL_Surface     *buff_screen;
	char            *original_name;
}                   t_cimp_screen;

typedef struct      s_cimp
{
	t_cimp_screen   *screen;
}                   t_cimp;
```

Pour l'instant nous ne gérons qu'une seule fenêtre, sans sélection, sans la possibilité de pouvoir stocker un buffer. (cut/copy/paste)

# Ensemble des actions

Notre prototype devra gérer les commandes suivantes:

```
cimp>> help
 - help
 - open [NAME]
 - close [NAME]
 - list
 - sym_verti
 - sym_hori
 - rotate [ANGLE]
```

Toutes les actions de modification se font sur l'image globale pour l'instant. (puisqu'il n'y a pas de sélection.)
