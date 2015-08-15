package dmf444.MinecraftIO.asm;

import cpw.mods.fml.relauncher.IFMLCallHook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;

/**
 * Created by David on 8/14/2015.
 */
public class MCioSetupClass implements IFMLCallHook {


    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public Void call() throws Exception {
        if(!new File(MCioLoadingPlugin.location + "/mods/1.7.10/GrapplClient.jar").exists()) {
            if(!new File(MCioLoadingPlugin.location + "/mods/1.7.10").exists() || !new File(MCioLoadingPlugin.location + "/mods/1.7.10").isDirectory()){
                new File(MCioLoadingPlugin.location + "/mods/1.7.10").mkdir();
            }
            try {
                System.out.println("Attempting to Install GRAPPLE.io");
                URL website = new URL("http://grappl.io:888/html/GrapplClient.jar");
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = null;
                fos = new FileOutputStream(MCioLoadingPlugin.location + "/mods/1.7.10/GrapplClient.jar");
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                System.out.println("Grapple.io sucessfully installed!");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
}