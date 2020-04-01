package com.gammon.pcms;

import java.io.File;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Application {
    public static void main(String[] args) throws Exception {
        String webAppDirLocation = "target/ROOT";
        Tomcat tomcat = new Tomcat();

        //Set Port #
        tomcat.setPort(7207);

        StandardContext ctx = (StandardContext) tomcat.addWebapp("/pcms", new File(webAppDirLocation).getAbsolutePath());
        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        ((StandardJarScanner) ctx.getJarScanner()).setScanManifest(false);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);
        tomcat.start();
        tomcat.getServer().await();
    }
}