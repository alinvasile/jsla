package com.github.alinvasile.jsla.ws;

import javax.jws.WebService;

@WebService(endpointInterface = "com.github.alinvasile.jsla.ws.HelloWorld")
public class HelloWorldService implements HelloWorld {

    public String sayHello() {
        return "Hi there!";
    }

}
