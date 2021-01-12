# Doc

Ce document présente la documentation finale de ce projet. Au fur et à mesure du développement nous compléterons ce document en le perfectionnant.

Ce document ne traite que de la partie obligatoire de ce projet.  

## Structure idéale

```
#define NB_SCREENS	4

typedef struct s_cimp_select {
	int      id;
	SDL_Rect surface;
} t_cimp_select;

typedef struct      s_cimp_event {
	SDL_Rect selection;
	int8_t   button_pressed;
} t_cimp_event;

typedef struct          s_cimp_screen {
	SDL_Window *  window;
	SDL_Surface * buff_screen;
	char *        path;
}                       t_cimp_screen;

typedef struct          s_cimp {
	t_cimp_screen * screen[NB_SCREENS];
	int             running;
	int             focus;
	t_cimp_select * select;
	t_cimp_event *  event;
	SDL_Surface *   copy_buffer;
}                       t_cimp;
```

NB_SCREENS correspond à la valeur maximale d' écrans qu'il est permis d'ouvrir simultanément.

Chaque image est représentée par un identifiant (son index dans le tableau).

La structure t_cimp_select stocke la zone sélectionnée ainsi que l'identifiant de l'écran.

La structure t_cimp_event permet a l'utilisateur de selectionner une zone avec sa souris.

La structure t_cimp_screen stocke toutes les informations nécessaires pour manipuler l'image. window est la structure renvoyée par SDL_CreateWindow, et buff_screen est une copie de ce qui est affiché à l'écran.

La structure t_cimp est la structure globale de notre projet. Elle est donc composée de NB_SCREENS screens, des informations concernant la sélection, et un buffer qui désigne la partie copié ou coupé.

## Ensemble des actions

Ici est listé l'ensemble des commandes que doit gérer notre programme.  
Au début se situe la liste complète puis une description plus détaillée.

Le caractère '?' signifie que l'argument est optionnel.  
La notation [...] désigne juste un argument.  
La notation [...|...] désigne un type d'agument ou un autre. (e.g.: [NAME|ID]).  

Pour simplifier la lecture, nous utiliseront les raccourcis suivants :
 - [RECT] <~> [X0] [Y0] [W] [H]
 - [COLOR] <~> [[R] [G] [B]|[RGB]]
 - [NAME] <~> [? [NAME|ID]]

RECT désigne 4 arguments.  
COLOR permet d'écrire une couleur sous la forme décimale ou héxadécimale. (e.g: 255 0 0 ou FF00000)
NAME permet de savoir de qu'elle image on parle.  
Si il y a plus d'une image, l'argument NAME est obligatoire, s'il n'est pas indiqué par l'utilisateur, il est initialise avec la valeur de l'id de la fenetre courante.
Si la commande est open, l'argument NAME est obligatoire et ne peut pas être un ID.  
En particulier si il n'y a qu'une seule image, NAME est un argument optionnelle.

```
cimp>> help
 - help
 - open [NAME]
 - close [NAME]
 - list
 - save [NAME] [?PATH]
 - update [NAME] [?PATH]
 - select [NAME] [RECT]
 - unselect
 - cut [? [NAME] [RECT]]
 - copy [? [NAME] [RECT]]
 - paste [NAME] [X0] [Y0]
 - sym_verti [NAME] [? [RECT]]
 - sym_hori [NAME] [? [RECT]]
 - rotate [NAME] [ANGLE]
 - crop_reduce [NAME] [? [RECT]]
 - crop_extend [NAME] [? [RECT] [? COLOR]]
 - scale_rect [NAME] [[W] [H]]
 - scale_ratio [NAME] [xN]
 - fill [NAME] [? [RECT]] [COLOR]
 - color_replace [NAME] [? [RECT]] [COLOR] [COLOR] [MARGIN]
 - color_negative [NAME] [? [RECT]]
 - color_gray [NAME] [? [RECT]]
 - color_white_black [NAME] [? [RECT]] [? [MARGIN]]
 - ajust_light_contrast [NAME] [? [RECT]] [? [CONTRAST_LEVEL]]
```

Les commandes, mis à part list et help, agissent seulement sur l'image sur [NAME] (dans le cas où il y a plusieurs images).

### open

open permet d'ouvrir une nouvelle image si le nombre total de fenêtres déjà ouvertes n'est pas atteint.

### close

close permet de fermer une image SANS SAUVEGARDER.
on peut egalement effectuer cette action en cliquant sur le bouton pour fermer la fenetre.

### list

list permet de lister les différentes images ouvertes. list affichera un ensemble d'information cohérentes sous forme d'un tableau.

Exemple:
```
cimp>> list

NAME			ID		TAILLE		FILEPATH
fleur.img		1		640x480		/home/.../cimp/tests/fleur.img
```

### save

save permet de sauvegarder l'image ID dans le chemin PATH, si le chemin PATH n'est pas spécifié, on sauvegarde l'image avec le chemin initial (utilisé pour charger l'image).

### update

update permet de recharger une image depuis le chemin donné. Si le chemin donné n'est pas spécifié, on recharge l'image depuis son chemin originel.

### select

select sauvegarde la zone sélectionnée par l'utilisateur ou determinée par le rectangle passé en argument. Le point de coordonnée (X0, Y0) désigne le point le plus en haut à gauche de l'image, et le rectangle est de taille W * H.


### unselect

unselect efface les coordonnées de la zone en mémoire.

### cut/copy/paste

cut copie la zone sélectionnée (ou passée en argument) et la garde en mémoire, et remplace la zone sélectionnée par du noir.  
copy fait la même chose mais n'efface pas la zone sélectionnée.  
paste copie la zone en mémoire aux coordonnées spécifiées.

### sym_verti/sym_hori

Ces deux commandes appliquent une symétrie verticale ou horizontale sur la zone sélectionnée.

### rotate

rotate prend un angle en argument (multiple de 90) et tourne l'image de ANGLE degré dans le sens horaire.

### crop_reduce/crop_extend

Ces commandes correspondent à la modification de l'espace de travail.  
crop_reduce réduit l'image a l'aide d'un découpage rectangulaire.  
crop_extend agrandit l'espace de travail (avec des pixels noirs si on ne passe pas de couleur en argument)

### scale_ratio/scale_rect

scale est une commande qui agrandit/réduit l'espace de travail ainsi que la taille de l'image.
Les arguments sont  soit un facteur multiplicatif pour scale_ratio(e.g: scale x1.2), soit une nouvelle taille pour scale_rect.

### fill

fill est une commande qui remplit un rectangle (ou la zone sélectionnée par une couleur)

### color_replace

color_replace est une commande qui remplace une couleur par une autre dans une zone sélectionnée avec une marge de tolérance pour la couleur remplacée.

### color_negative

color_negative inverse toutes les composantes RGB de tous les pixels de la zone sélectionnée.

### color_gray

color_gray remplace tous les pixels de la zone sélectionnée par son équivalent en variation de gris

### color_white_black

color_white_black remplace toutes les couleurs de la zone sélectionnée par blanc ou noir en respectant la règle suivante:

Si margin n'est pas donnée, elle vaut 128.

Si le représentant en variation de gris du pixel traité est plus grand (ou égale) que le margin alors le nouveau pixel est de couleur noir sinon il est de couleur blanc.

### ajust_light_contrast

ajust_light_contrast contrôle la luminéosité de la photo. Un contrôle de l'ajustement (coefficient compris entre -255 et 255), permet de contrôler la modification de l'image en fonction.
