#include "cimp.h"

/** Une fontion qui alloue la memoire necessaire et cree un t_cimp_select correspondant
 * au SDL_Rect passe en argument**/
t_cimp_select * cimp_init_select(SDL_Rect rectangle, int id) {
	t_cimp_select * res = malloc(sizeof(t_cimp_select));

	if (res == NULL) {
		printf("erreur de mallox\n");
		return NULL;
	}

	res->surface = rectangle;
	res->id      = id;
	return res;
}

/** Une fonction qui libere la memoire allouee au t_cimp_select selection
**/
void cimp_end_select(t_cimp_select * selection) {
	free(selection);
}
