# Y'a foot !

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=net.andresbustamante%3Ay-a-foot&metric=alert_status)](https://sonarcloud.io/dashboard?id=net.andresbustamante%3Ay-a-foot)

*Y'a foot !* is an application for football amateurs to manage logistics and organisation of matches between colleagues,
friends, classmates and so on.

## Required installation

### Minimum requirements for building

* JDK 11
* Maven 3.5

### Minimum requirements for deploying

* Apache Tomcat 9.0 (for frontend deployment)
* Wildfly 16 or JBoss EAP 7.2 (for backend deployment)

### LDAP directory structure

Scripts to execute

    misc/ldap/01_create_structure.ldif
    
### Application server configuration

#### Modules

* org.postgresql (v42.2.5)

#### Datasources

A datasource named `java:/jdbc/yafoot` is needed. Example:

    <datasource jta="true" jndi-name="java:/jdbc/yafoot" pool-name="yafoot-ds" enabled="true" use-ccm="true">
        <connection-url>jdbc:postgresql://my-database-server:5432/my-database?currentSchema=yafoot</connection-url>
        <driver>postgresql</driver>
        <security>
            <user-name>my-yafoot-granted-user</user-name>
            <password>my-password</password>
        </security>
        <validation>
            <validate-on-match>false</validate-on-match>
            <background-validation>false</background-validation>
        </validation>
        <timeout>
            <set-tx-query-timeout>true</set-tx-query-timeout>
            <blocking-timeout-millis>0</blocking-timeout-millis>
            <idle-timeout-minutes>5</idle-timeout-minutes>
            <query-timeout>30</query-timeout>
            <use-try-lock>0</use-try-lock>
            <allocation-retry>0</allocation-retry>
            <allocation-retry-wait-millis>0</allocation-retry-wait-millis>
        </timeout>
        <statement>
            <share-prepared-statements>false</share-prepared-statements>
        </statement>
    </datasource>

#### Mail sessions

A mail session named `java:/mail/yafoot` is needed for SMTP messaging. Example:

    <mail-session name="yafoot" jndi-name="java:/mail/yafoot">
        <smtp-server outbound-socket-binding-ref="mail-smtp" username="no-reply@my-email-domain.net" password="my-passwd"/>
    </mail-session>

For this example, you also should have an outbound socket binding named `mail-smtp` like this:

    <outbound-socket-binding name="mail-smtp">
        <remote-destination host="smtp.email.com" port="465"/>
    </outbound-socket-binding>

## Application deployment

In order to deploy `y-a-foot`, you must deploy the following artifacts:

 * `y-a-foot-core` (backend) as a Spring Boot WAR application on Wildfly or JBoss
 * `y-a-foot-web` (frontend, optional) as a Web application (WAR) on Tomcat, Wildfly or JBoss
 