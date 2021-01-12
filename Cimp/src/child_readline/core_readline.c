#include "cimp.h"

int nb_completions = 0;
char ** completions;

/* Fonctions internes à readline */
char ** fileman_completion(const char * com, int start, int end);
char * command_generator(const char * com, int num);
/*********************************/

void initialisation_tab_completion() {
	nb_completions = g_command_list_size;
	completions    = malloc(sizeof(char *) * nb_completions);
	if (completions != NULL) {
		for (size_t i = 0; i < g_command_list_size; i++) {
			int len = strlen(g_command_list[i].name);
			completions[i] = malloc(sizeof(char) * (len + 1));
			if (completions[i]) {
				strncpy(completions[i], g_command_list[i].name, len);
				completions[i][len] = '\0';
			}
			else {
				printf("Erreur lors de la création du tableau de complétion");
			}
		}
	}
	else {
		printf("Something went terrebly wrong with readline initialisation");
	}
}

int initialize_readline() {
	initialisation_tab_completion();
	using_history();// initialisation de l'utilisation de la librairie history de readline
	rl_readline_name = ">>";

	/* explicite la complétion souhaitée */
	rl_attempted_completion_function = fileman_completion;
	return 0;
}

/* com : tout ce que l'utilisateur a déjà  écrit sur la ligne de commande
 * start, end : les indices de début et de fin du mot sur lequel TAB a été lancé */
char ** fileman_completion(const char * com, int start, int end) {
	char ** matches;

	matches = (char **) NULL; /* assure la complétion par défaut */

	/* si c'est le premier mot de la ligne de commande, on l'analyse;
	 * sinon on utilise l'analyse par défaut */
	if (start == 0)
		matches = rl_completion_matches(com, command_generator);

	/* cast de end en void car ne peut être enlevé du prototype
	 *  de fonction et n'est pas utilisé pour l'instant donc ne passe pas cppcheck */
	(void) end;

	return (matches);
}

/* fonction qui engendre les complétions possibles :
 * À  chaque num >= 0 correspond une complétion;
 * la fin de la liste des complétions est donnée par NULL
 *
 * ici on complète si c'est un préfixe de "toto" ou de "turlututu",
 * sinon c'est la complétion par défaut */
char * command_generator(const char * com, int num) {
	/* À  l'entrée de cette fonction : com est le début déjÀ  écrit de la commande,
	 * on est en train de chercher la complétion numéro num */
	static int indice, len;


	/* si c'est la première complétion qu'on cherche, on dit qu'on va chercher À  partir
	 * de la premiere case du tableau completion et on garde en mémoire la longueur
	 * du texte tapé */
	if (num == 0) {
		indice = 0;
		len    = strlen(com);
	}

	/* on renvoie une complétion de préfixe le début de la commande écrite */
	while (indice < nb_completions) {
		char * completion = completions[indice++];

		if (strncmp(completion, com, len) == 0)
			return dupstr(completion);
	}

	/* est renvoyé quand num est > au numéro de la dernière complétion
	 * automatique */
	return NULL;
}
