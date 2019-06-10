# Y'a foot !

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=net.andresbustamante%3Ay-a-foot&metric=alert_status)](https://sonarcloud.io/dashboard?id=net.andresbustamante%3Ay-a-foot)

Avec *Y'a foot !* les amateurs du foot et du sport en général peuvent mieux gérer la logistique et l'organisation de 
leurs matchs entre collègues, amis, camarades, etc.

## Installation des prérequis

### Prérequis pour compilation

* JDK 8 ou supérieur
* Maven 3.3 ou supérieur

### Prérequis pour déploiement

* Apache Tomcat 8.5 (pour déploiement du frontend avec ZK)
* Wildfly 12 ou supérieur (pour le frontend avec JSF ou ZK)

### Création de l'arborescence sur l'annuaire LDAP

Scripts à exécuter :

    install/ldap/*.ldif

### Authentification et authorisation des utilisateurs (frontend JSF avec JAAS)

Configuration à ajouter :

    <security-domain name="yafoot" cache-type="default">
        <authentication>
            <login-module code="Ldap" flag="required">
                <module-option name="java.naming.factory.initial" value="com.sun.jndi.ldap.LdapCtxFactory"/>
                <module-option name="java.naming.provider.url" value="ldap://localhost:389/"/>
                <module-option name="java.naming.security.authentication" value="simple"/>
                <module-option name="principalDNPrefix" value="uid="/>
                <module-option name="principalDNSuffix" value=",ou=Users,ou=yafoot,dc=andresbustamante,dc=net"/>
                <module-option name="rolesCtxDN" value="ou=Roles,ou=yafoot,dc=andresbustamante,dc=net"/>
                <module-option name="uidAttributeID" value="member"/>
                <module-option name="matchOnUserDN" value="true"/>
                <module-option name="roleAttributeID" value="cn"/>
                <module-option name="roleAttributeIsDN" value="false"/>
                <module-option name="searchTimeLimit" value="5000"/>
                <module-option name="searchScope" value="ONELEVEL_SCOPE"/>
            </login-module>
        </authentication>
    </security-domain>

## Déploiement de l'application

Afin de bien déployer `y-a-foot`, il faut déployer les artifacts suivants :

 * `y-a-foot-core` (backend) en tant qu'application Spring Boot (JAR executable)
 * Soit `y-a-foot-web-jsf` (frontend JSF) en tant qu'application Web (WAR) sur Wildfly 12 (minimum) avec la 
 configuration JAAS mentionnée auparavant, soit `y-a-foot-web-zk` (frontend ZK) en tant qu'application Web (WAR) sur 
 Tomcat 8.5 ou Wildfly 12 (minimum).
 