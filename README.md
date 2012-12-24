Ideliance - Outil de gestion de connaissances
=============================================


Abstract
--------
Ideliance est un logiciel de gestion de connaissances. 
Il permet la saisie, la modification et l'exploitation de relations Sujet-Verbe-Complément (ex: [Martin][habite à][Paris]).
Cette manière simple d'exprimer une connaissance, bien que limitée, permet d'utiliser des puissants outils d'exploitation
de l'information. Un exemple est le Quoi-Entre, permettant de retrouver les liaisons existantes entre deux entités données.
Certaines liaisons seront connus, d'autres seront inattendues. Ideliance permet ainsi l'émergence d'idées nouvelles.


Technologies
------------
Ideliance est une application web, développée en HTML5 et Javascript.
Le coeur du logiciel est sa base de donnée orientée graphe : OrientDB.
La partie IHM est largement inspirée de l'interface d'administration web d'OrientDB.

Le projet contient la distribution OrientDB (qui embarque un serveur web) et l'application web déployée : Ideliance.
Une base de test est présente, les identifiants de connexion sont ceux par défaut (cf. HOWTO.txt)

Pour toute information technique complémentaire, veuillez consulter la documentation d'OrientDB : 
Ancienne URL : http://code.google.com/p/orient/wiki/Main
Nouvelle URL : https://github.com/nuvolabase/orientdb/wiki


Instructions de lancement du projet
-----------------------------------

Requis : Java SE6 (minimum) doit être installé. Vous pouvez vérifier votre version de Java en tapant java -version dans une console. Il est conseillé de disposer du JDK.

1. Une fois que vous disposez du projet, allez dans le dossier Ideliance/bin et lancez (par double-clic) le script server.bat (sous Windows) ou server.sh (pour les systèmes Unix).
Il sera peut-être nécessaire d'attribuer les droits d'exécution aux scripts.

2. Une fois le serveur actif, accédez à l'URL suivante dans votre navigateur : localhost:2480

Vous devez maintenant vous trouver sur l'interface de connexion d'Ideliance. 

A tout moment, vous pouvez arrêter le serveur en exécutant le script shutdown.bat (sous Windows) ou shutdown.sh (sous Linux).


Configuration
-------------

* Par défaut, le serveur est configuré pour se placer dans le premier port disponible entre le port 2480 et le port 2490. Si aucun n'est libre, une exception est levée.
  La liste des ports peut être modifiée dans le fichier de configuration Ideliance/config/orientdb-server-config.xml, à la ligne suivante :
	<listener ip-address="0.0.0.0" port-range="2480-2490" protocol="http">
	Si l'on souhaite que le serveur ne se place que dans un seul port, placer uniquement ce numéro de port comme valeur de l'attribut port-range.
	
* Il est vivement conseillé de modifier dans le fichier de configuration Ideliance/config/orientdb-server-config.xml, les identifiants de l'administrateur du serveur.
Ils se trouvent à la ligne : <user name="root" resources="*" password="095F17F6488FF5416ED24E"/>
	
	
Erreur au lancement
-------------------

Si à l'exécution du script server.bat (ou server.sh) le serveur ne se lance pas, il est probable que vous ne disposiez pas du JDK ou que celui-ci ne soit pas utilisé par défaut.
Nous vous invitons à installer le JDK ou à configurer votre environnement pour utiliser le JDK, afin d'obtenir les meilleurs performances.
Toutefois si cela n'est pas possible vous pouvez en solution de secours, dans le script de lancement, renommer en fin de fichier "-server" par "-client". Les performances seront moindres.

Liens
-----

Website : http://ideliance.org

JIRA : https://ideliance.atlassian.net/
