package com.github.alinvasile.jsla.ws.interceptor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.cxf.frontend.MethodDispatcher;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.Service;
import org.apache.cxf.service.model.BindingOperationInfo;

import com.github.alinvasile.jsla.web.util.Util;
import com.github.alinvasile.jsla.ws.annotation.Constraint;


public class SlaInterceptor extends AbstractPhaseInterceptor<Message> {

    public SlaInterceptor() {
        super(Phase.USER_LOGICAL);
    }

    public void handleMessage(Message message) throws Fault {
        Exchange exchange = message.getExchange();
        BindingOperationInfo bop = exchange.get(BindingOperationInfo.class);
        MethodDispatcher methodDispatcher = (MethodDispatcher) 
                        exchange.get(Service.class).get(MethodDispatcher.class.getName());
        Method method = methodDispatcher.getMethod(bop);
        
        Constraint constraint = method.getAnnotation(Constraint.class);
        
        if(constraint != null){
            Map<String, List<String>> headers = CastUtils.cast((Map)message.get(Message.PROTOCOL_HEADERS));
            if (headers != null) {
                List<String> sa = headers.get("Authorization");
                
                if (sa != null && sa.size() > 0) {
                    String user  = Util.extractUsernameFromAuthorizationHeader(sa.get(0));
                } else {
                    // anonymous access
                }
            }
        }
    }
    
}
