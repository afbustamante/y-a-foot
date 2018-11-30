# Y'a foot !

Avec *Y'a foot !* les amateurs du foot et du sport en général peuvent mieux gérer la logistique et l'organisation de 
leurs matchs entre collègues, amis, camarades, etc.

## Installation des prérequis

### Création de la base des données

Scripts à exécuter :

    install/sql/postgresql/*.sql

### Création de l'arborescence sur l'annuaire LDAP

Scripts à exécuter :

    install/ldap/*.ldif

## Configuration sur Wildfly 10

### Connexions JDBC

Configuration à ajouter :

    <datasource jta="true" jndi-name="java:/jdbc/yafoot" pool-name="yafoot-ds" enabled="true" use-ccm="true">
        <connection-url>jdbc:postgresql://localhost:5432/desarrollo</connection-url>
        <driver>postgresql</driver>
        <security>
            <user-name>yafoot</user-name>
            <password>yafoot</password>
        </security>
        <validation>
            <validate-on-match>false</validate-on-match>
            <background-validation>false</background-validation>
        </validation>
        <timeout>
            <set-tx-query-timeout>false</set-tx-query-timeout>
            <blocking-timeout-millis>0</blocking-timeout-millis>
            <idle-timeout-minutes>0</idle-timeout-minutes>
            <query-timeout>0</query-timeout>
            <use-try-lock>0</use-try-lock>
            <allocation-retry>0</allocation-retry>
            <allocation-retry-wait-millis>0</allocation-retry-wait-millis>
        </timeout>
        <statement>
            <share-prepared-statements>false</share-prepared-statements>
        </statement>
    </datasource>

### Authentification et authorisation des utilisateurs (JAAS)

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

### Déploiement de l'application

Afin de bien déployer `y-a-foot`, il faut déployer les artifacts suivants :

 * `y-a-foot-ws` (backend) en tant qu'application Web (WAR)
 * `y-a-foot-web` (frontend) en tant qu'application Web (WAR)