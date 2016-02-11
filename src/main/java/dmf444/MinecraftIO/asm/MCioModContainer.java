package dmf444.MinecraftIO.asm;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Arrays;

/**
 * Created by DMf444 on 8/14/2015.
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