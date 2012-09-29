/*
 * Distributed as part of mchange-commons-java v.0.2.3
 *
 * Copyright (C) 2012 Machinery For Change, Inc.
 *
 * Author: Steve Waldman <swaldman@mchange.com>
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 2.1, as 
 * published by the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; see the file LICENSE.  If not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */


package com.mchange.v2.beans.swing;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.lang.reflect.*;
import javax.swing.*;
import com.mchange.v2.beans.BeansUtils;

public class PropertyBoundTextField extends JTextField
{
    PropertyComponentBindingUtility pcbu;

    HostBindingInterface myHbi;

    public PropertyBoundTextField( Object bean, String propName, int columns )
	throws IntrospectionException
    {
	super( columns );
	this.myHbi = new MyHbi();
	this.pcbu  = new PropertyComponentBindingUtility( myHbi, bean, propName, true ); 
	pcbu.resync();
    }

    class MyHbi implements HostBindingInterface
	{
	    public void syncToValue( PropertyEditor editor, Object newVal )
	    {
		if (newVal == null)
		    setText("");
		else
		    {
			editor.setValue( newVal );
			String newValStr = editor.getAsText();
			setText( newValStr );
		    }
	    }
	    
	    public void addUserModificationListeners()
	    {
		WeChangedListener wcl = new WeChangedListener();
		addActionListener( wcl );
		addFocusListener( wcl );
	    }
	    
	    public Object fetchUserModification( PropertyEditor editor, Object oldValue )
	    {
		String valAsStr = getText().trim();
		if ("".equals(valAsStr))
		    return null;
		else
		    {
			editor.setAsText( valAsStr );
			return editor.getValue();
		    }
	    }
	    
	    public void alertErroneousInput()
	    { getToolkit().beep(); }
	};


    class WeChangedListener implements ActionListener, FocusListener
    {
	public void actionPerformed( ActionEvent evt )
	{ pcbu.userModification(); }
	
	public void focusGained( FocusEvent evt ) {}
	
	public void focusLost( FocusEvent evt )
	{ pcbu.userModification(); }
    }


    public static void main( String[] argv )
    {
	try
	    {
		TestBean tb = new TestBean();
		PropertyChangeListener pcl = new PropertyChangeListener()
		    {
			public void propertyChange( PropertyChangeEvent evt )
			{ BeansUtils.debugShowPropertyChange( evt ); }
		    };
		tb.addPropertyChangeListener( pcl );
		
		JTextField jt1 = new PropertyBoundTextField( tb, "theString", 20 );
		JTextField jt2 = new PropertyBoundTextField( tb, "theInt", 5);
		JTextField jt3 = new PropertyBoundTextField( tb, "theFloat", 5 );
		JFrame frame = new JFrame();
		BoxLayout bl = new BoxLayout( frame.getContentPane(), BoxLayout.Y_AXIS );
		frame.getContentPane().setLayout( bl );
		frame.getContentPane().add( jt1 );
		frame.getContentPane().add( jt2 );
		frame.getContentPane().add( jt3 );
		frame.pack();
		frame.show();
	    }
	catch ( Exception e )
	    { e.printStackTrace(); }
    }
}
