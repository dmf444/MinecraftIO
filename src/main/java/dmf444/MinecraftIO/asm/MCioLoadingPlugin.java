package dmf444.MinecraftIO.asm;

import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.io.File;
import java.util.Map;

/**
 * Created by David on 8/14/2015.
 */
public class MCioLoadingPlugin implements IFMLLoadingPlugin {


    public static File location;


    @Override
    public String[] getASMTransformerClass() {
        //This will return the name of the class "mod.culegooner.CreeperBurnCore.CBClassTransformer"
        //return new String[]{DMClassTransformer.class.getName()};
        return null;
    }

    @Override
    public String getModContainerClass() {
        //This is the name of our dummy container "mod.culegooner.CreeperBurnCore.CBDummyContainer"
        return MCioModContainer.class.getName();
    }

    @Override
    public String getSetupClass() {
        return MCioSetupClass.class.getName();
    }

    @Override
    public void injectData(Map<String, Object> data) {
        //This will return the jar file of this mod CreeperBurnCore_dummy.jar"
        location = (File) FMLInjectionData.data()[6];
    }

    @Override
    public String getAccessTransformerClass() {
        // TODO Auto-generated method stub
        return null;
    }
}