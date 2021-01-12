open Bsp
open Graphics

type litteral = (bool * string)
type clause = litteral list
type fnc = clause list

module Variables =
    struct
        type t = string
        let compare = String.compare
    end

module Sat = Sat_solver.Make (Variables)

(*Renvoie une fnc contenant toutes les combinaisons de n rectangles possibles*)
let all_comb_rect_fnc (n : int) (rect_list : clause) : fnc =
    let rec loop (nb_loop : int) (current_rect_list : clause) (prev_rect_list : clause) (res : fnc) : fnc =
        match current_rect_list with
        | [] -> res
        | h::t -> if (nb_loop = 1) then loop nb_loop t prev_rect_list ((h::prev_rect_list)::res)
                else loop nb_loop t prev_rect_list (loop (nb_loop - 1) t (h::prev_rect_list) res)
    in loop n rect_list [] []
;;

(*Renvoit un litteral correspondant au rectangle et a la couleur dominante*)
let rect_to_litteral ((name, _) : rect) (couleur : color) : litteral =
    ((if (couleur = blue) then true else false), name)
;;

(*Renvoie une liste de rectangle en une clause selon la couleur dominante*)
let rect_list_to_litteral_list (rect_list : rect list) (couleur : color) : clause =
    let rec aux (current_rect_list : rect list) (res : clause) : clause =
        match current_rect_list with
        | [] -> res
        | h::t -> aux t ((rect_to_litteral h couleur)::res)
    in aux rect_list []
;;

(*Renvoie la negation du litteral*)
let litteral_opp ((is_blue, name) : litteral) : litteral =
    (not(is_blue), name)
;;

(*Renvoie une clause contenant la negation des litteraux de cl*)
let clause_opp (cl : clause) : clause =
    let rec aux (current_clause : clause) (res : clause) : clause =
        match current_clause with
        | [] -> res
        | h::t -> aux t ((litteral_opp h)::res)
    in aux cl []
;;

(*Pour chaque clause de la fnc, ajoute la clause contenant la negation des litteraux de cette clause *)
let add_fnc_opp (fnc : fnc) : fnc =
    let rec aux (f : fnc) (res : fnc) =
        match f with
        | [] -> res
        | h::t -> aux t ((clause_opp h)::res)
    in aux fnc fnc
;;

(*Renvoie une fnc correspondant a l'arete edge*)
let edge_to_fnc (edge : bsp) (name : string) (edge_color : color) : fnc =
    (*print_endline name;
    display_color edge_color;*)
    let rect_list = (adjacent_rect_list edge name) in
    let litteral_list = (rect_list_to_litteral_list rect_list edge_color) in
    (*display_clause litteral_list;*)
    let len = float_of_int (List.length litteral_list) in
    if (edge_color = magenta) then
        let nb_loop = int_of_float (ceil ((1. +. len) /. 2.)) in
        add_fnc_opp (all_comb_rect_fnc nb_loop litteral_list)
    else
        let nb_loop = int_of_float (ceil (len /. 2.))in all_comb_rect_fnc nb_loop litteral_list
;;

(*Renvoie une fnc correspondant au bsp bsp*)
let bsp_to_fnc (bsp : bsp) : fnc =
    let rec parcours (bsp : bsp) (current_name : string) : fnc =
        match bsp with
        | R None -> [[(true, current_name); (false, current_name)]]
        | R (Some c) -> [[((if (c = blue) then true else false), current_name)]]
        | L (lab, left, right) -> (parcours right (current_name^"1"))@(edge_to_fnc bsp current_name lab.col)@(parcours left (current_name^"0"))
    in parcours bsp ""
;;
