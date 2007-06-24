package org.jboss.maven.plugins;


/**
 * A Simple mojo that use the JBoss AOP compiler to weave in AOP code
 * based on a jboss-aop.xml file.
 * NOTE: This mojo is not finished and will probably be rewritten or completly removed
 * when i learn more about maven...
 * 
 * Use it by running: mvn jbossaop:aopc
 * 
 * Supported options:
 * - aoppath (default src/main/resources/jboss-aop.xml)
 *  - verbose (default true)
 *  - suppress (default false)
 *  - noopt (default false)
 *  - report (default false)
 *  
 * 
 * @author <a href="mailto:stalep@gmail.com">Stale W. Pedersen</a>
 * @goal compile-test
 * @phase process-classes
 * @requiresDependencyResolution
 */
public class JBossTestAOPCMojo extends JBossAOPCMojo
{
   public JBossTestAOPCMojo()
   {
      setTest(true);
   }

}
