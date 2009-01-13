/*
  * JBoss, Home of Professional Open Source
  * Copyright 2005, JBoss Inc., and individual contributors as indicated
  * by the @authors tag. See the copyright.txt in the distribution for a
  * full listing of individual contributors.
  *
  * This is free software; you can redistribute it and/or modify it
  * under the terms of the GNU Lesser General Public License as
  * published by the Free Software Foundation; either version 2.1 of
  * the License, or (at your option) any later version.
  *
  * This software is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  * Lesser General Public License for more details.
  *
  * You should have received a copy of the GNU Lesser General Public
  * License along with this software; if not, write to the Free
  * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
  * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
  */
package org.jboss.aop.pointcut;

import java.lang.reflect.Constructor;
import org.jboss.aop.Advisor;
import org.jboss.aop.pointcut.ast.ASTConstruction;
import org.jboss.aop.pointcut.ast.ASTConstructor;
import org.jboss.aop.pointcut.ast.ASTField;
import org.jboss.aop.pointcut.ast.ASTHas;
import org.jboss.aop.pointcut.ast.ASTHasField;
import org.jboss.aop.pointcut.ast.ASTMethod;
import org.jboss.aop.pointcut.ast.ASTStart;
import org.jboss.aop.pointcut.ast.Node;
import javassist.CtConstructor;
import javassist.NotFoundException;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class ConstructionMatcher extends ConstructorMatcher
{
   public ConstructionMatcher(Advisor advisor, CtConstructor con, ASTStart start) throws NotFoundException
   {
      super(advisor, con, start);
   }

   public ConstructionMatcher(Advisor advisor, Constructor<?> con, ASTStart start)
   {
      super(advisor, con, start);
   }

   public Object visit(ASTConstruction node, Object data)
   {
      return node.jjtGetChild(0).jjtAccept(this, data);
   }

   public Object visit(ASTHas node, Object data)
   {
      Node n = node.jjtGetChild(0);
      if (n instanceof ASTMethod)
      {
         if (ctCon != null)
         {
            return new Boolean(Util.has(ctCon.getDeclaringClass(), (ASTMethod) n, advisor));
         }
         else
         {
            return new Boolean(Util.has(refCon.getDeclaringClass(), (ASTMethod) n, advisor));

         }
      }
      else
      {
         if (ctCon != null)
         {
            return new Boolean(Util.has(ctCon.getDeclaringClass(), (ASTConstructor) n, advisor));
         }
         else
         {
            return new Boolean(Util.has(refCon.getDeclaringClass(), (ASTConstructor) n, advisor));

         }
      }
   }

   public Object visit(ASTHasField node, Object data)
   {
      ASTField f = (ASTField) node.jjtGetChild(0);
      if (ctCon != null)
      {
         return new Boolean(Util.has(ctCon.getDeclaringClass(), f, advisor));
      }
      else
      {
         return new Boolean(Util.has(refCon.getDeclaringClass(), f, advisor));
      }

   }

   protected Boolean resolvePointcut(Pointcut p)
   {
      try
      {
         if (refCon != null) return new Boolean(p.matchesConstruction(advisor, refCon));
         return new Boolean(p.matchesConstruction(advisor, ctCon));
      }
      catch (NotFoundException e)
      {
         throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
      }
   }
}
