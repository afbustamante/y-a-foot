# Y'a foot !

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=net.andresbustamante%3Ay-a-foot&metric=alert_status)](https://sonarcloud.io/dashboard?id=net.andresbustamante%3Ay-a-foot)

Avec *Y'a foot !* les amateurs du foot et du sport en général peuvent mieux gérer la logistique et l'organisation de 
leurs matchs entre collègues, amis, camarades, etc.

## Installation des prérequis

### Prérequis pour compilation

* JDK 8 ou supérieur
* Maven 3.3 ou supérieur

### Prérequis pour déploiement

* Apache Tomcat 9.0 (pour déploiement du frontend avec ZK)
* Wildfly 16 ou supérieur (pour le frontend avec ZK)

### Création de l'arborescence sur l'annuaire LDAP

Scripts à exécuter :

    install/ldap/*.ldif

## Déploiement de l'application

Afin de bien déployer `y-a-foot`, il faut déployer les artifacts suivants :

 * `y-a-foot-core` (backend) en tant qu'application Spring Boot (JAR executable)
 * `y-a-foot-web` (frontend) en tant qu'application Web (WAR) sur Tomcat 9.0 ou Wildfly 16 (minimum).
 