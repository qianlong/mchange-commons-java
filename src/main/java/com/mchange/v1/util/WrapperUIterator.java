/*
 * Distributed as part of mchange-commons-java 0.2.6.1
 *
 * Copyright (C) 2013 Machinery For Change, Inc.
 *
 * Author: Steve Waldman <swaldman@mchange.com>
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of EITHER:
 *
 *     1) The GNU Lesser General Public License (LGPL), version 2.1, as 
 *        published by the Free Software Foundation
 *
 * OR
 *
 *     2) The Eclipse Public License (EPL), version 1.0
 *
 * You may choose which license to accept if you wish to redistribute
 * or modify this work. You may offer derivatives of this work
 * under the license you have chosen, or you may provide the same
 * choice of license which you have been offered here.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received copies of both LGPL v2.1 and EPL v1.0
 * along with this software; see the files LICENSE-EPL and LICENSE-LGPL.
 * If not, the text of these licenses are currently available at
 *
 * LGPL v2.1: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 *  EPL v1.0: http://www.eclipse.org/org/documents/epl-v10.php 
 * 
 */

package com.mchange.v1.util;

import java.util.*;
import com.mchange.v1.util.DebugUtils;

/**
 *  This implementation does not yet support removes once hasNext() has
 *  been called... will add if necessary.
 */
public abstract class WrapperUIterator implements UIterator
{
    protected final static Object SKIP_TOKEN = new Object();

    final static boolean DEBUG = true;

    UIterator inner;
    boolean  supports_remove;
    Object   lastOut = null;
    Object   nextOut = SKIP_TOKEN;
    
    public WrapperUIterator(UIterator inner, boolean supports_remove)
    { 
	this.inner = inner; 
	this.supports_remove = supports_remove;
    }

    public WrapperUIterator(UIterator inner)
    { this( inner, false ); }

    public boolean hasNext() throws Exception
    {
	findNext();
	return nextOut != SKIP_TOKEN; 
    }

    private void findNext() throws Exception
    {
	if (nextOut == SKIP_TOKEN)
	    {
		while (inner.hasNext() && nextOut == SKIP_TOKEN)
		    this.nextOut = transformObject( inner.next() );
	    }
    }

    public Object next() throws NoSuchElementException, Exception
    {
	findNext();
	if (nextOut != SKIP_TOKEN)
	    {
		lastOut = nextOut;
		nextOut = SKIP_TOKEN;
	    }
	else
	    throw new NoSuchElementException();

	//postcondition
	if (DEBUG)
	    DebugUtils.myAssert( nextOut == SKIP_TOKEN && lastOut != SKIP_TOKEN );

	assert( nextOut == SKIP_TOKEN && lastOut != SKIP_TOKEN );

	return lastOut;
    }
    
    public void remove() throws Exception
    { 
	if (supports_remove)
	    {
		if (nextOut != SKIP_TOKEN)
		    throw new UnsupportedOperationException(this.getClass().getName() +
							    " cannot support remove after" +
							    " hasNext() has been called!");
		if (lastOut != SKIP_TOKEN)
		    inner.remove();
		else
		    throw new NoSuchElementException();
	    }
	else
	    throw new UnsupportedOperationException(); 
    }

    public void close() throws Exception
    { inner.close(); }

    /**
     * return SKIP_TOKEN to indicate an object should be
     * skipped, i.e., not exposed as part of the iterator.
     * (we don't use null, because we want to support iterators
     * over null-accepting Collections.)
     */
    protected abstract Object transformObject(Object o) throws Exception;
}






