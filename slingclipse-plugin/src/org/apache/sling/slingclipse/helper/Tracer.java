 /*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.slingclipse.helper;

import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.sling.slingclipse.SlingclipsePlugin;
import org.eclipse.osgi.service.debug.DebugOptions;
import org.eclipse.osgi.service.debug.DebugOptionsListener;
import org.eclipse.osgi.service.debug.DebugTrace;
import org.eclipse.osgi.util.NLS;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Tracer implements DebugOptionsListener {

    private boolean debugEnabled;
    private DebugTrace trace;
	private ServiceRegistration<?> serviceRegistration;
    
    public void register(BundleContext bundleContext) {
        
        Dictionary<String, String> properties = new Hashtable<String, String>(1);
        properties.put(DebugOptions.LISTENER_SYMBOLICNAME, SlingclipsePlugin.PLUGIN_ID);
        
        serviceRegistration = bundleContext.registerService(DebugOptionsListener.class.getName(), this, properties );
    }
    
    public void unregister() {
    	
    	if ( serviceRegistration != null )
    		serviceRegistration.unregister();
    }
    
    @Override
    public void optionsChanged(DebugOptions options) {
    	
    	debugEnabled = options.getBooleanOption(SlingclipsePlugin.PLUGIN_ID + "/debug", false);
    	trace = options.newDebugTrace(SlingclipsePlugin.PLUGIN_ID, getClass());
    }
    
    public void trace(String message, Object... arguments) {
    	if ( !debugEnabled )
    		return;
    	
    	if ( arguments.length > 0 )
    		message = NLS.bind(message, arguments);
    	
    	trace.trace("/debug", message);
    }
}
