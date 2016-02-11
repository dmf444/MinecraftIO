package dmf444.MinecraftIO;

import io.grappl.GrapplGlobal;
import io.grappl.client.api.Grappl;
import io.grappl.client.api.GrapplBuilder;
import io.grappl.client.gui.GrapplGUI;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by David on 8/15/2015.
 */
public class GrapplMCBuilder extends GrapplBuilder {
    private GrapplMC grappl = new GrapplMC();

    public GrapplMCBuilder() {
    }

    public GrapplBuilder withGUI(GrapplGUI gui) {
        this.grappl.gui = gui;
        return this;
    }

    public GrapplBuilder useLoginDetails(String username, char[] password) {
        this.grappl.username = username;
        this.grappl.password = password;
        return this;
    }

    public GrapplBuilder atLocalPort(int localPort) {
        this.grappl.internalPort = localPort;
        return this;
    }

    public GrapplBuilder login() {
        try {
            Socket e = new Socket(GrapplGlobal.DOMAIN, GrapplGlobal.AUTHENTICATION);
            DataInputStream dataInputStream = new DataInputStream(e.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(e.getOutputStream());
            dataOutputStream.writeByte(0);

            try {
                PrintStream success = new PrintStream(dataOutputStream);
                success.println(this.grappl.username);
                success.println(this.grappl.password);
            } catch (Exception var7) {
                var7.printStackTrace();
            }

            boolean success1 = dataInputStream.readBoolean();
            boolean alpha = dataInputStream.readBoolean();
            int port = dataInputStream.readInt();
            System.out.println(success1);
            this.grappl.prefix = dataInputStream.readLine();
            this.grappl.isAlphaTester = alpha;
            this.grappl.isLoggedIn = success1;
            this.grappl.externalPort = port + "";
            e.close();
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return this;
    }

    public Grappl build() {
        return null;
    }

    public GrapplMC buildMC(){
        return grappl;
    }
}
