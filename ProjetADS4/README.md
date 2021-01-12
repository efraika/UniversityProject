# Projet d'ADS4

Ce projet a été réalisé  par Marie Bétend ([efraika](https://github.com/efraika)) et ([CTPaul](https://github.com/CTPaul)), dans le cadre de notre enseignement d'Analyse de Données Structurées.
Le répertoir initial de ce projet peut être trouvé [ici](https://github.com/efraika/ProjetADS4).

Dans ce répertoire, vous trouverez tous les fichiers nécessaires à l'interprétation du langage ci dessous, permettant de tracer des formes géométriques simples.

## Grammaire du langage

```
prog -> suiteInst $

inst -> Begin suiteInst End
        | DrawCircle(expr, expr, expr, couleur)
   	| FillCircle (expr, expr, expr, couleur)
        | DrawRect(expr, expr, expr, expr, couleur)
      	| FillRect(expr, expr, expr, expr, couleur)
        | If expr Then Inst Else Inst
     	| Const identificateur = expr
    	| Var identificateur = expr
    	| identificateur = expr
        | Def id (args) suiteInst End
        | Do id (nombres)
	| While expr Do suiteInst End
      	| For (identificateur, expr, expr, expr ) suiteInst End

 suiteInst -> inst ; suiteInst | ε

 expr -> nombre | (expr operateur expr) | identificateur

 args -> id suiteArgs | ε

 suiteArgs -> , id suiteArgs | ε

 nombres -> expr suiteNombres | ε

 suiteNombres -> , expr suiteNombres | ε

```
## Parties et extensions implémentées

Nous avons implémenté :
* toutes les parties obligatoires du projet,
* les variables,
* les ``While expr Do instruction``,
* les ``If Then Else``,
* des boucles For inspirées par les For de python avec la synthaxe suivante :`` For (identificatuer, expression, expression,expression) suiteInstruction End``,
* la possibilitée de définir des fonctions avec la synthaxe suivante :`` Def id (nomsArg) suiteInst End ``; que l'on appelle avec : `` Do id (exprArg)``.

## Execution du projet

Pour lancer le projet il faut lancer les instructions suivantes, après vous être placé dans le répertoire du projet:

```
jflex projet.flex

javac *.java

java Main *le nom du fichier test de votre choix*
```
