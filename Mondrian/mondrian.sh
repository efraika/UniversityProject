#!/bin/sh

function compil {
	ocamlc -c sat_solver.mli
	ocamlc -c sat_solver.ml
	ocamlc graphics.cma -c bsp.ml
	ocamlc graphics.cma -c fnc.ml
	ocamlc graphics.cma -c view.ml
	ocamlc graphics.cma -c mondrian.ml
	ocamlc graphics.cma sat_solver.cmo bsp.cmo fnc.cmo view.cmo mondrian.cmo -o mondrian.byte
}

compil
./mondrian.byte
rm *.cm*
rm mondrian.byte
