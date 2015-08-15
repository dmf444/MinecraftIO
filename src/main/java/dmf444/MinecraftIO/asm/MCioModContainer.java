package dmf444.MinecraftIO.asm;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import java.util.Arrays;

/**
 * Created by David on 8/14/2015.
 */
public class MCioModContainer extends DummyModContainer {

    public MCioModContainer() {

        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = "GrapplDownloader";
        meta.name = "GrapplDownloader";
        meta.version = "1.7.10-1.0.0"; //String.format("%d.%d.%d.%d", majorVersion, minorVersion, revisionVersion, buildVersion);
        meta.credits = "Roll Credits ...";
        meta.authorList = Arrays.asList("mincrmatt12", "dmf444");
        meta.description = "";
        meta.url = "http://www.google.ca/";
        meta.updateUrl = "";
        meta.screenshots = new String[0];
        meta.logoFile = "";

    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }


    @Subscribe
    public void modConstruction(FMLConstructionEvent evt) {

    }

    @Subscribe
    public void init(FMLInitializationEvent evt) {

    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent evt) {

    }

    @Subscribe
    public void postInit(FMLPostInitializationEvent evt) {

    }

}