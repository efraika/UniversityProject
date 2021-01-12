open Graphics
open Random


type label = { coord : int; col : color; }
type bsp = R of color option | L of label * bsp * bsp
type rect = string * (color option)

(*La racine est a une hauteur de 1 :

-> profondeur impaire correspond a l'abscisse
-> profondeur paire correspond a l'ordonnees
*)


(*Une fonction qui copie un bsp existant en changeant la couleur des feuilles en none*)
let copy (solution : bsp) : bsp =
  let rec aux noeud =
    match noeud with
    | R Some(i)-> R None
    | R None -> R None
    | L (l,g,d) -> L (l, aux g, aux d)
    in aux solution
;;

(*Une fonction qui renvoie la couleur suivante :
-S'il n'y a pas de couleur on renvoie Rouge
-Si la couleur est rouge on renvoie Bleu
-Si la couleur ets bleu on renvoie None (On enleve la couleur)*)
let next_color (couleur : color option) : color option =
  match couleur with
  | None -> Some(red)
  | Some c -> if (c = red) then Some (blue)
              else None
;;

(*Une fonction qui modifie la couleur de la feuille correspondant aux coordonnees x,y*)
let modifie_feuille (arbre : bsp) ((x,y) : int*int) : bsp =
  let rec aux noeud profondeur =
    match noeud with
    | R a -> R (next_color a)
    | L (l,g,d) -> if( ( (profondeur mod 2 ) = 0 && (y > l.coord) ) || ( (profondeur mod 2) = 1 && (x > l.coord) ) ) then L (l, g, aux d (profondeur + 1))
                else L (l, aux g (profondeur + 1), d)
  in aux arbre 1
;;

(*Une fonction qui renvoie un nombre aleatoire entre 0 et borne, pair si parite = 0 et impair si parite = 1*)
let rec get_Random_parite (borne : int) (parite : int) : int =
  let x = Random.int borne in
  if (x mod 2 = parite) then x
  else get_Random_parite borne parite
;;

(*Une fonction qui cree une arbe bsp aleatoire avec une profondeur d'au plus n*)
let randomArbre (n : int) (sizex : int) (sizey : int) (ecart : int ): bsp=
  Random.self_init ();
  let rec aux i xmax xmin ymax ymin =
  if(i<=1 || (xmax - xmin) < ecart || (ymax - ymin) < ecart) then if(Random.int 2 = 1) then R (Some red)
                else R (Some blue)
  else if(i mod 2 = 0) then let x = Random.int (xmax-xmin) in L ({coord = (x+xmin); col = black}, aux (get_Random_parite i 1) (x+xmin-ecart) xmin ymax ymin, aux (get_Random_parite i 1) xmax (x+xmin+ecart) ymax ymin)
  else let y = Random.int (ymax-ymin) in L({coord = (y+ymin); col = black},aux (get_Random_parite i 0) xmax xmin (y+ymin-ecart) ymin, aux (get_Random_parite i 0) xmax xmin ymax (y+ymin+ecart))
  in aux n (sizex-ecart) ecart (sizey-ecart) ecart
;;

(*Renvoie la couleur dominante d'une liste de rectangles (modélisé par une chaine de caractères)*)
let dominant_color (rect_list : rect list) : color =
    let rec aux (l : rect list) ((r,b) : (int*int)) : color =
        match l with
        | [] -> if (r > b) then red else if (r < b) then blue else magenta
        | (_, Some c)::t -> aux t (if (c = red) then (r+1, b) else (r, b+1))
        | (_, None)::t -> aux t (r,b)
    in aux rect_list (0, 0)
;;

(*Renvoie la liste de rectangles adjacents à l'arrete edge*)
let adjacent_rect_list (edge : bsp) (edge_name : string) : rect list =
    let rec parcours (bsp : bsp) (current_name : string) (current_rect_list : rect list) (pair : bool) (left_side : bool) =
        match bsp with
        | R None -> ((current_name, None)::current_rect_list)
        | R (Some c) -> ((current_name, Some c)::current_rect_list)
        | L (lab, left, right) -> if (not(pair)) then
                                        parcours right (current_name^"1")
                                        (parcours left (current_name^"0") current_rect_list (not(pair)) left_side) (not(pair))
                                        left_side
                                  else if (left_side) then
                                        parcours right (current_name^"1") current_rect_list (not(pair)) left_side
                                  else
                                        parcours left (current_name^"0") current_rect_list (not(pair)) left_side
    in match edge with
        | R _ -> failwith "not an edge"
        | L (lab, left, right) -> parcours right (edge_name^"1") (parcours left (edge_name^"0") [] false true) false false
;;

(*Renvoie une copie de randomBbsp ou la couleur des aretes est ajoutée au label correspondant*)
let add_edge_color (randomBsp : bsp) : bsp =
    let rec parcours (bsp : bsp) : bsp =
        match bsp with
        | R _ -> bsp
        | L (lab, left, right) -> L ({coord = lab.coord; col = (dominant_color (adjacent_rect_list bsp ""))}, (parcours left), (parcours right))
    in parcours randomBsp
;;

(*Renvoie une copie de randomBsp ou la couleur des rectangles est enlevée*)
let delete_rect_color (randomBsp : bsp) : bsp =
    let rec parcours (bsp : bsp) : bsp =
        match bsp with
        | R _ -> R None
        | L (lab, left, right) -> L (lab, parcours left, parcours right)
    in parcours randomBsp
;;

(*Renvoie une copie du bsp qui est complété à l'aide de sol. On suppose que sol est une liste contenant tous les rectangles.*)
let complete_bsp (bsp : bsp) (sol : (bool*string) list) : bsp =
        let rec parcours (bsp : bsp) (current_name : string) : bsp =
        match (bsp) with
        | R None -> (if (fst(List.find (fun s -> (String.compare (snd s) current_name) = 0) sol)) then R (Some blue) else R (Some red))
        | R (Some c) -> R (Some c)
        | L (lab, left, right) -> L (lab, (parcours left (current_name^"0")), (parcours right (current_name^"1")))
    in parcours bsp ""
;;
