package org.dhbw.mosbach.ai.syncvideo;

import org.glassfish.jersey.server.ResourceConfig;

public class MyApplication extends ResourceConfig {
    public MyApplication() {
        register(new MyApplicationBinder());
        packages(true, "org.dhbw.mosbach.ai.syncvideo.rest");
    }
}