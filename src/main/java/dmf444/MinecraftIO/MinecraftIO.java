package dmf444.MinecraftIO;


import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.client.Minecraft;

import java.io.File;

@Mod(modid = MinecraftIO.MODID, version = MinecraftIO.VERSION)
public class MinecraftIO
{
    public static final String MODID = "minecraftIO";
    public static final String VERSION = "1.1";

    @EventHandler
    public void preinit(FMLPreInitializationEvent e){

    }

    @EventHandler
    public void serverstart(FMLServerStartingEvent event)
    {
        if(new File(Minecraft.getMinecraft().mcDataDir + "/mods/1.7.10/GrapplClient.jar").exists()) {
            event.registerServerCommand(new GrapplCommand());
        }
    }
}