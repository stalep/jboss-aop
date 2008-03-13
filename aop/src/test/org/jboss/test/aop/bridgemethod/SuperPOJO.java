package org.jboss.test.aop.bridgemethod;

/**
 *
 * @author <a href="mailto:stalep@conduct.no">Stale W. Pedersen</a>
 * @version $Revision
 */
@SuppressWarnings({"unused", "cast", "unchecked", "serial"})
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
