* If installing in a version before JBoss 4.0.4, you should leave javassist.jar and common-softvaluehashmap.jar in place in this folder.

*If installing in jboss 4.0.4 or later, the classes contained in javassist.jar and common-softvaluehashmap.jar will already be available,
and you should do the following to avoid versioning conflicts:
-delete common-softvaluehashmap.jar
-move javassist.jar to ../../lib/javassist.jar

* Note that this will not replace the jboss-aspect-library(-jdk50).jar, which is tied to application
server version. If you are upgrading from JBoss AOP 1.5.x, you should remove the following packages and classes
contained therein from your jboss-aspect-library(-jdk50).jar:
-org.jboss.aop
-org.jboss.aop.deployers
-org.jboss.aop.deployment
-org.jboss.aop.junit