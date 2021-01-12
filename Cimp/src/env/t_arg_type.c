#include "cimp.h"

#define	ARG_NAME_TAG  "NAME"
#define	ARG_NUM_TAG   "NUM"
#define	ARG_FOCUS_TAG "FOCUS"
#define	ARG_PATH_TAG  "PATH"
#define	ARG_RECT_TAG  "RECT"
#define	ARG_PT_TAG    "POINT"
#define	ARG_COL_TAG   "COLOR"
#define	ARG_COL2_TAG  "COLOR2"

const char * arg_type_to_string(t_arg_type atype) {
	switch (atype) {
		case ARG_NAME:
			return (ARG_NAME_TAG);

		case ARG_NUM:
			return (ARG_NUM_TAG);

		case ARG_FOCUS:
			return (ARG_FOCUS_TAG);

		case ARG_PATH:
			return (ARG_PATH_TAG);

		case ARG_RECT:
			return (ARG_RECT_TAG);

		case ARG_PT:
			return (ARG_PT_TAG);

		case ARG_COLOR:
			return (ARG_COL_TAG);

		case ARG_COLOR2:
			return (ARG_COL2_TAG);
	}
	// Should never happened
	return (NULL);
}
