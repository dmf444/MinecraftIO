package dmf444.MinecraftIO;

import io.grappl.client.api.Grappl;
import io.grappl.client.api.GrapplBuilder;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldSettings;

import java.util.ArrayList;
import java.util.List;


public class GrapplCommand implements ICommand {

    private List others;

    public GrapplCommand(){
        this.others = new ArrayList();
        this.others.add("grappl");
    }

    @Override
    public int compareTo(Object arg0) {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "grapplio";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/grapple <open|close>";// <optional: username> <optional: password>
    }

    @Override
    public List getCommandAliases() {
        return this.others;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] CMDin) {
        if (CMDin.length < 1) {
            throw new WrongUsageException(getCommandUsage(null), new Object[0]);
        } else {
            if (CMDin[0].equals("open")) {
                openGrapple(sender, CMDin);
            } else if (CMDin[0].equals("close")) {
                closeGrapple(sender);
            }

        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
        List<String> cmds = new ArrayList<String>();
        cmds.add("open");
        cmds.add("close");
        return p_71516_2_.length == 1 ? cmds : null;
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return false;
    }

    protected String[] getPlayers()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }
public static GrapplMC grappl;
    private void openGrapple(ICommandSender send, String[] cmd){
        if(grappl == null) {
            int port = 0;
            if(!MinecraftServer.getServer().isDedicatedServer()){
                String s = MinecraftServer.getServer().shareToLAN(WorldSettings.GameType.SURVIVAL, false);
                //System.out.println(s);
                 port = Integer.parseInt(s);
            }else{
                port = MinecraftServer.getServer().getServerPort();
            }

            /*if (cmd.length == 3) {
                grappl = new GrapplBuilder().useLoginDetails(cmd[1], cmd[2].toCharArray()).login().build();
                grappl.setInternalPort(port);
                grappl.connect("n.grappl.io");
                send.addChatMessage(new ChatComponentText("Server opened at: " + grappl.getRelayServer() +":"+ grappl.getExternalPort()));
            } else*/ if (cmd.length == 1) {
                GrapplMCBuilder g = (GrapplMCBuilder) new GrapplMCBuilder().atLocalPort(port);
                grappl = g.buildMC();
                grappl.connect("n.grappl.io");
                send.addChatMessage(new ChatComponentText("Server opened at: " + grappl.getRelayServer() +":"+ grappl.getExternalPort()));
            } else {
                throw new WrongUsageException(getCommandUsage(null), new Object[0]);
            }
        } else{send.addChatMessage(new ChatComponentText("Server opened at: " + grappl.getPublicAddress()));
        }
        System.out.println(grappl.getPublicAddress());
    }

    private void closeGrapple(ICommandSender sender){
        grappl.disconnect();
        //MinecraftServer.getServer().stopServer();
        grappl = null;
        sender.addChatMessage(new ChatComponentText("Grappl.io Server has been closed"));
    }



}
