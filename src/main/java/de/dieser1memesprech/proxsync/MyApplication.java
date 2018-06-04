package de.dieser1memesprech.proxsync;

import org.glassfish.jersey.server.ResourceConfig;

public class MyApplication extends ResourceConfig {
    public MyApplication() {
        register(new MyApplicationBinder());
        packages(true, "de.dieser1memesprech.proxsync.rest");
    }
}