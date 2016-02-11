package dmf444.MinecraftIO;


import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraft.client.Minecraft;

import java.io.File;

@Mod(modid = MinecraftIO.MODID, version = MinecraftIO.VERSION)
public class MinecraftIO
{
    public static final String MODID = "minecraftIO";
    public static final String VERSION = "1.0";

    @EventHandler
    public void preinit(FMLPreInitializationEvent e){

    }

    @EventHandler
    public void serverstart(FMLServerStartingEvent event)
    {
        if(new File(Minecraft.getMinecraft().mcDataDir + "/mods/1.8.9/GrapplLauncher.jar").exists()) {
            event.registerServerCommand(new GrapplCommand());
        }
    }
}