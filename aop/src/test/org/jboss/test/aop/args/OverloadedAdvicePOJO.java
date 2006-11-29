package org.jboss.test.aop.args;

/**
 * Plain old java object used on overloaded advice tests.
 * 
 * @author Flavia Rainone
 */
public class OverloadedAdvicePOJO
{
   public String text;
   
   public void method(int arg1, long arg2) {}
}