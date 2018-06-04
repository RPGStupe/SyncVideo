package de.dieser1memesprech.proxsync.listener;

/**
 * Created by Jeremias on 22.09.2017.
 */

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.FirebaseDatabase;
import de.dieser1memesprech.proxsync.config.Configuration;
import net.thegreshams.firebase4j.error.FirebaseException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StartupListener implements ServletContextListener {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Starting up!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Shutting down!");
        scheduler.shutdown();
        try {
            scheduler.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            scheduler.shutdownNow();
        }
        System.out.println("Scheduler shut down");
        FirebaseApp.getInstance().delete();
        System.out.println("Firebase shut down");
    }

    void makeRatingsLong() {
        try {
            String raw = Configuration.instance.getFirebase().get("users").getRawBody();
            System.out.println(raw);
            //Database.database.getReference("users").setValue(raw);
            for (int i = 0; i < 11; i++) {
                raw = raw.replaceAll("\"rating\":\"" + i + "\"", "\"rating\":" + i);
            }
            System.out.println(raw);
            Configuration.instance.getFirebase().put("users", raw);
        } catch (FirebaseException | UnsupportedEncodingException e) {

        }
    }
}
