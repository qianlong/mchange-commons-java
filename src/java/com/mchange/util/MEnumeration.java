/*
 * Distributed as part of mchange-commons-java v.0.2.3.2
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


package com.mchange.util;

import java.util.*;
import com.mchange.io.*;

public interface MEnumeration extends IOEnumeration, Enumeration
{
  public static MEnumeration EMPTY = com.mchange.util.impl.EmptyMEnumeration.SINGLETON;

  public Object  nextElement();
  public boolean hasMoreElements();
}
