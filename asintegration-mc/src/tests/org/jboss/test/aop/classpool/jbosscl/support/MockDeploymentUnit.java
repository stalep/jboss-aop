/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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
package org.jboss.test.aop.classpool.jbosscl.support;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.jboss.classloading.spi.metadata.ClassLoadingMetaData;
import org.jboss.dependency.spi.CallbackItem;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.dependency.spi.ErrorHandlingMode;
import org.jboss.dependency.spi.LifecycleCallbackItem;
import org.jboss.dependency.spi.ScopeInfo;
import org.jboss.deployers.client.spi.main.MainDeployer;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.attachments.MutableAttachments;
import org.jboss.deployers.structure.spi.ClassLoaderFactory;
import org.jboss.deployers.structure.spi.DeploymentResourceLoader;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.deployers.structure.spi.DeploymentUnitVisitor;
import org.jboss.deployers.vfs.plugins.classloader.InMemoryClassesDeployer;
import org.jboss.deployers.vfs.plugins.classloader.VFSClassLoaderClassPathDeployer;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.MutableMetaData;
import org.jboss.metadata.spi.scope.ScopeKey;
import org.jboss.util.JBossObject;
import org.jboss.util.id.GUID;
import org.jboss.virtual.MemoryFileFactory;
import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;

class MockDeploymentUnit implements DeploymentUnit
{
   private static final long serialVersionUID = 1L;

   String name;
   
   Map<String, Object> attachments = new HashMap<String, Object>();
   
   public MockDeploymentUnit(String name, ClassLoadingMetaData clmd, URL... urls) throws Exception
   {
      this.name = name;

      List<VirtualFile> roots = new ArrayList<VirtualFile>();
      //Prepend the dynamic URL to the classpath
      VirtualFile dynamicClasses = getInMemoryClassesURL();
      roots.add(dynamicClasses);
      for (URL url : urls)
      {
         VirtualFile file = VFS.getRoot(url);
         roots.add(file);
      }
      addAttachment(VFSClassLoaderClassPathDeployer.VFS_CLASS_PATH, roots);
      
      addAttachment(ControllerContext.class, new MockControllerContext());
      addAttachment(ClassLoadingMetaData.class, clmd);
   }
   
   private VirtualFile getInMemoryClassesURL() throws Exception
   {
      URL dynamicClassRoot = new URL("vfsmemory", GUID.asString(), "");
      VirtualFile classes = MemoryFileFactory.createRoot(dynamicClassRoot).getRoot();
      addAttachment(InMemoryClassesDeployer.DYNAMIC_CLASS_URL_KEY, dynamicClassRoot);
      addAttachment(InMemoryClassesDeployer.DYNAMIC_CLASS_KEY, classes);
      return classes;
   }
   
   public DeploymentUnit addComponent(String name)
   {
      return null;
   }

   public void addControllerContextName(Object name)
   {
   }

   public void addIDependOn(DependencyItem dependency)
   {
   }

   public boolean createClassLoader(ClassLoaderFactory factory) throws DeploymentException
   {
      return false;
   }

   public <T> Set<? extends T> getAllMetaData(Class<T> type)
   {
      return null;
   }

   public List<DeploymentUnit> getChildren()
   {
      return null;
   }

   public ClassLoader getClassLoader()
   {
      return null;
   }

   public DeploymentUnit getComponent(String name)
   {
      return null;
   }

   public List<DeploymentUnit> getComponents()
   {
      return null;
   }

   public Set<Object> getControllerContextNames()
   {
      return null;
   }

   public DependencyInfo getDependencyInfo()
   {
      return null;
   }

   public MainDeployer getMainDeployer()
   {
      return null;
   }

   public MetaData getMetaData()
   {
      return null;
   }

   public MutableMetaData getMutableMetaData()
   {
      return null;
   }

   public ScopeKey getMutableScope()
   {
      return null;
   }

   public String getName()
   {
      return name;
   }

   public DeploymentUnit getParent()
   {
      return null;
   }

   public String getRelativePath()
   {
      return null;
   }

   public ClassLoader getResourceClassLoader()
   {
      return null;
   }

   public DeploymentResourceLoader getResourceLoader()
   {
      return null;
   }

   public ScopeKey getScope()
   {
      return null;
   }

   public String getSimpleName()
   {
      return null;
   }

   public DeploymentUnit getTopLevel()
   {
      return this;
   }

   public MutableAttachments getTransientManagedObjects()
   {
      return null;
   }

   public boolean isComponent()
   {
      return false;
   }

   public boolean isTopLevel()
   {
      return false;
   }

   public void removeClassLoader(ClassLoaderFactory factory)
   {
   }

   public boolean removeComponent(String name)
   {
      return false;
   }

   public void removeControllerContextName(Object name)
   {
   }

   public void removeIDependOn(DependencyItem dependency)
   {
   }

   public void setMutableScope(ScopeKey key)
   {
   }

   public void setScope(ScopeKey key)
   {
   }

   public void visit(DeploymentUnitVisitor visitor) throws DeploymentException
   {
   }

   public Object addAttachment(String name, Object attachment)
   {
      return attachments.put(name, attachment);
   }

   public <T> T addAttachment(Class<T> type, T attachment)
   {
      return (T)attachments.put(type.getName(), attachment);
   }

   public <T> T addAttachment(String name, T attachment, Class<T> expectedType)
   {
      return (T)attachments.put(name, attachment);
   }

   public void clear()
   {
   }

   public void clearChangeCount()
   {
   }

   public int getChangeCount()
   {
      return 0;
   }

   public Object removeAttachment(String name)
   {
      return attachments.remove(name);
   }

   public <T> T removeAttachment(Class<T> type)
   {
      return (T)attachments.remove(type.getName());
   }

   public <T> T removeAttachment(String name, Class<T> expectedType)
   {
      return (T)attachments.remove(name);
   }

   public void setAttachments(Map<String, Object> map)
   {
      attachments = map;
   }

   public Object getAttachment(String name)
   {
      return attachments.get(name);
   }

   public <T> T getAttachment(Class<T> type)
   {
      return (T)attachments.get(type.getName());
   }

   public <T> T getAttachment(String name, Class<T> expectedType)
   {
      return (T)attachments.get(name);
   }

   public Map<String, Object> getAttachments()
   {
      return attachments;
   }

   public boolean hasAttachments()
   {
      return attachments.size() > 0;
   }

   public boolean isAttachmentPresent(String name)
   {
      return attachments.keySet().contains(name);
   }

   public boolean isAttachmentPresent(Class<?> type)
   {
      return attachments.keySet().contains(type.getName());
   }

   public boolean isAttachmentPresent(String name, Class<?> expectedType)
   {
      return attachments.keySet().contains(name);
   }
   
   private class MockControllerContext extends JBossObject implements ControllerContext
   {
      DependencyInfo dependencies = new MockDependencyInfo();
      public Set<Object> getAliases()
      {
         return null;
      }

      public Controller getController()
      {
         return null;
      }

      public DependencyInfo getDependencyInfo()
      {
         return dependencies;
      }

      public Throwable getError()
      {
         return null;
      }

      public ErrorHandlingMode getErrorHandlingMode()
      {
         return null;
      }

      public ControllerMode getMode()
      {
         return null;
      }

      public Object getName()
      {
         return name;
      }

      public ControllerState getRequiredState()
      {
         return null;
      }

      public ScopeInfo getScopeInfo()
      {
         return null;
      }

      public ControllerState getState()
      {
         return null;
      }

      public Object getTarget()
      {
         return null;
      }

      public void install(ControllerState fromState, ControllerState toState) throws Throwable
      {
      }

      public void setController(Controller controller)
      {
      }

      public void setError(Throwable error)
      {
      }

      public void setMode(ControllerMode mode)
      {
      }

      public void setRequiredState(ControllerState state)
      {
      }

      public void setState(ControllerState state)
      {
      }

      public void uninstall(ControllerState fromState, ControllerState toState)
      {
      }
   }
   
   private class MockDependencyInfo extends JBossObject implements DependencyInfo
   {
      private Set<DependencyItem> dependsOnMe = new HashSet<DependencyItem>();
      private Set<DependencyItem> iDependOn = new HashSet<DependencyItem>();
      private Set<DependencyItem> unresolved = new CopyOnWriteArraySet<DependencyItem>();

      public void addDependsOnMe(DependencyItem dependency)
      {
         dependsOnMe.add(dependency);
      }

      public void addIDependOn(DependencyItem dependency)
      {
         iDependOn.add(dependency);
      }

      public <T> void addInstallItem(CallbackItem<T> callbackItem)
      {
         throw new RuntimeException("NYI");
      }

      public void addLifecycleCallback(LifecycleCallbackItem lifecycleCallbackItem)
      {
         throw new RuntimeException("NYI");
      }

      public <T> void addUninstallItem(CallbackItem<T> callbackItem)
      {
         throw new RuntimeException("NYI");
      }

      public Set<DependencyItem> getDependsOnMe(Class<?> type)
      {
         return dependsOnMe;
      }

      public Set<DependencyItem> getIDependOn(Class<?> type)
      {
         return iDependOn;
      }

      public Set<CallbackItem<?>> getInstallItems()
      {
         throw new RuntimeException("NYI");
      }

      public List<LifecycleCallbackItem> getLifecycleCallbacks()
      {
         throw new RuntimeException("NYI");
      }

      public Set<CallbackItem<?>> getUninstallItems()
      {
         throw new RuntimeException("NYI");
      }

      public boolean isAutowireCandidate()
      {
         return false;
      }

      public void removeDependsOnMe(DependencyItem dependency)
      {
         dependsOnMe.remove(dependency);
      }

      public void removeIDependOn(DependencyItem dependency)
      {
         iDependOn.remove(dependency);
      }

      public <T> void removeInstallItem(CallbackItem<T> callbackItem)
      {
         throw new RuntimeException("NYI");
      }

      public <T> void removeUninstallItem(CallbackItem<T> callbackItem)
      {
         throw new RuntimeException("NYI");
      }
 
      public void setAutowireCandidate(boolean candidate)
      {
         throw new RuntimeException("NYI");
      }
      
      public boolean resolveDependencies(Controller controller, ControllerState state)
      {
         boolean resolved = true;
         Set<DependencyItem> items = getUnresolvedDependencies(state);
         if (items.isEmpty() == false)
         {
            for (DependencyItem item : items)
            {
               if (item.resolve(controller) == false)
                  resolved = false;
            }
         }
         return resolved;
      }

      public Set<DependencyItem> getUnresolvedDependencies(ControllerState state)
      {
         if (unresolved.isEmpty())
            return Collections.emptySet();

         Set<DependencyItem> result = new HashSet<DependencyItem>();
         for (DependencyItem item : unresolved)
         {
            if (state == null || state.equals(item.getWhenRequired()))
               result.add(item);
         }
         return result;
      }
   }
}