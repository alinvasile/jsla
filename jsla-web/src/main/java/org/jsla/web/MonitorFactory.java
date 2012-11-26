package org.jsla.web;

import javax.servlet.ServletContext;

import org.jsla.core.AccessMonitor;

public interface MonitorFactory {
    
    AccessMonitor buildAccessMonitor(ServletContext context);

}