# Workflow

Ce fichier décrit comment le projet est organisé pour le travail en groupe, ou le workflow.

Nous avons 2 branches principales :

- master
- dev  

**master** est la branche par défaut (vocabulaire git) où le code désigne la dernière version majeure de notre projet.  
**dev** est la branche de développement. C'est sur cette branche que l'on met à jour les différentes features de notre projet. Tout code allant sur master doit d'abord passer sur master.

Chaque fonctionnalité doit être développée sur une nouvelle branche et passer toute une série de tests avant de pouvoir être merge sur dev puis sur master.

Pour chaque changement (Pull request), 3 séries de tests sont effectuées :

L'ensemble est géné par intégration continue via le service Travis externe via l'API GitHub.

[Lien Travis Cimp](https://travis-ci.com/FauconFan/Cimp)

## Base

Base vérifie les 3 choses suivantes :

- .travis.yml n'a pas été changé directement mais grâce au script .travis_gen.py
- le projet doit bel et bien compilé
- faire en sorte qu'aucun fichier non indexé ne soit présent dans le dépôt

Le premier point permet d'assurer que tout changement apporté aux tests se fait de manière automatisé via le script python et non via un changement local dans le fichier .travis.yml directement.

Le deuxième point permet d'éviter les pushs trop rapides et d'assurer que le projet reste pertinent.

Le dernier point permet d'assurer que les linters (Phase 2) puissent avoir accès au bon fichier sans que certains fichiers n'échappent aux tests unitaires. Un fichier indexé est un fichier listé dans le fichier files.mk.

## Lint

Lint applique 4 outils de vérification statique externe ainsi qu'une vérification de style.

La vérification de style se fait grâce à uncrustify et via la norme mise en place dans le fichier UNCRUSTIFY.cfg.

Les 4 outils de vérification statiques sont cpplint, cppcheck, clang-tidy, infer qui sont connus pour leur pertinence.

[Link uncrustify](https://github.com/uncrustify/uncrustify)  
[Link cpplint](https://github.com/google/styleguide/tree/gh-pages/cpplint)  
[Link cppcheck](http://cppcheck.sourceforge.net/)  
[Link clang-tidy](https://releases.llvm.org/6.0.0/tools/clang/tools/extra/docs/clang-tidy/index.html)  
[Link infer](https://fbinfer.com/)  

## Test

Test est une série de test qui évolue au fur et à mesure des features.  
Les seuls tests effectués sont les tests unitaires spécifiés dans le sous dossier tests, en utilisant la libcheck.

Un code coverage est alors généré.

```bash
$> make cimp_check
```