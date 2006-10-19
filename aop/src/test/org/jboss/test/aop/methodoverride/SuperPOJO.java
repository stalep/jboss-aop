package org.jboss.test.aop.methodoverride;

/**
 *
 * @author <a href="mailto:stalep@conduct.no">Stale W. Pedersen</a>
 * @version $Revision
 */
public class SuperPOJO<T extends java.util.AbstractList>
{
   private T fooObject;
   
   public void setFoo(T arg)
   {
      fooObject = arg;
   }
   
   public T getFoo()
   {
      return fooObject;
   }

}
