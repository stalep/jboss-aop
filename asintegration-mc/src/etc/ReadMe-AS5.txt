To upgrade your jboss instance:
1) Copy the contents of the lib folder into the JBoss-5.x/lib/ folder
2) Copy the jboss-aop-jdk50.deployer over the JBoss-5.x/server/xxx/deployers/jboss-aop-jdk50.deployer/ folder
Note that this will not replace the jboss-aspect-library-jdk50.jar, which is tied to application
server version