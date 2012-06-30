Ideliance
=========

Ideas you never though you would had


Installation :
------

1. Requis: serveur MySQL, serveur Apache Tomcat;

2. Créer les bases (executer les requêtes situées dans idelianceTomCat/SQL);

3. Modifier le fichier de configuration ideliance.properties pour renseigner les bons identifiants au serveur MySQL;

4. Placer le fichier ideliance.properties dans le dossier d'exécution du serveur Tomcat ou modifier son emplacement dans le fichier ideliance.core.config.ApplicationContext.java présent dans le projet IdelianceCore;

5. Ré-exporter le jar du projet IdelianceCore et le placer dans le dossier /WEB-INF/lib/ideliance-core.jar du projet IdelianceTomcat, si vous avez fait des modifications;

6. Copier/coller les fichiers du projet sur le serveur Tomcat;

7. Modifier le fichier ideliance.servlet.connection.ConnectionFilter (ligne 108) pour renseigner la racine du site dans l'URL (ex: /ideliance/);


Links
------
__Website__: [http://ideliance.org](http://ideliance.org "Ideliance.org")

__Mailing list__: [http://groups.google.com/group/ideliance](http://groups.google.com/group/ideliance "Ideliance mailing list")

__JIRA__: [http://ideliance.atlassian.net/](http://ideliance.atlassian.net/ "JIRA Ideliance")