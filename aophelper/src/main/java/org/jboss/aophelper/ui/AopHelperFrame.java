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
package org.jboss.aophelper.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.jboss.aophelper.ui.compile.CompileMediator;
import org.jboss.aophelper.ui.compile.CompilerPane;

/**
 * A AopHelperFrame.
 * 
 * @author <a href="stale.pedersen@jboss.org">Stale W. Pedersen</a>
 * @version $Revision: 1.1 $
 */
public class AopHelperFrame extends JFrame
{

   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;
   private JFileChooser fc;
   
   public AopHelperFrame()
   {
      super("AopHelper");
      setup();
   }
   
   private void setup()
   {
      // set an icon on the main widget
//    setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/gicon.jpg")));

      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

//      getContentPane().setLayout(new FlowLayout());
//      getContentPane().add(new CompilerPane());
      setContentPane(new CompilerPane());
      
      setSize(1024, 768);
//      setSize(600, 400);
      
      setLocation();
      setVisible(true);

      CompileMediator.instance().setHelperFrame(this);
      fc = new JFileChooser();
      fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
      fc.setMultiSelectionEnabled(true);
      
      
   }
   
   public void setLocation() {
      // Get the size of the screen
      Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

      // Determine the new location of the window
      int w = this.getSize().width;
      int h = this.getSize().height;
      int x = (dim.width - w)/2;
      int y = (dim.height - h)/2;

      // Move the window
      this.setLocation(x, y);
    }

    /** 
     * Get the midpoint based on the main location
     * 
     * @param width 
     * @param height 
     * @return 
     */
    public Point getMidPoint(int width, int height ) {
      Point loc = this.getLocation();
      Dimension mainSize = this.getSize();

      return new Point( (int) (mainSize.getWidth() / 2) - (width / 2) + (int) loc.getX(), 
                        (int) (mainSize.getHeight() / 2) - (height / 2) + (int) loc.getY() );
    }

    public File[] createFileCooser()
    {
       int returnVal = fc.showOpenDialog(this);
       if (returnVal == JFileChooser.APPROVE_OPTION) 
       {
           File[] files = fc.getSelectedFiles();
           //This is where a real application would save the file.
          System.out.println("Saving: " + files[0].getAbsolutePath());
          return files;
       } 
       else 
       {
          System.out.println("Save command cancelled by user.");
          return new File[0];
       }
    }

    public static void main(String[] args)
    {
       new AopHelperFrame();
    }
    
}
