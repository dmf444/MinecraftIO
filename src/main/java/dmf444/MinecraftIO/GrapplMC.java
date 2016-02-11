package dmf444.MinecraftIO;

import io.grappl.GrapplGlobal;
import io.grappl.client.ClientLog;
import io.grappl.client.api.Grappl;
import io.grappl.client.api.StatsManager;
import io.grappl.client.gui.GrapplGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 8/15/2015.
 */
public class GrapplMC {
    protected String username;
    protected char[] password;
    protected String externalPort;
    protected int internalPort;
    private String relayServerIP;
    protected GrapplGUI gui;
    protected boolean isLoggedIn = false;
    protected boolean isAlphaTester = false;
    protected String prefix;
    private StatsManager statsManager = new StatsManager();
    private List<Socket> sockets = new ArrayList();

    public GrapplMC() {
    }

    public void connect(String relayServer) {
        ClientLog.log("Connecting: relayserver=" + relayServer + " localport=" + this.internalPort);
        this.relayServerIP = relayServer;

        try {
            Socket e = new Socket(relayServer, GrapplGlobal.MESSAGE_PORT);
            this.sockets.add(e);
            DataInputStream messageInputStream = new DataInputStream(e.getInputStream());
            this.externalPort = messageInputStream.readLine();
            ClientLog.log("Hosting on: " + relayServer + ":" + this.externalPort);
            if(this.gui != null) {
                this.gui.initializeGUI(relayServer, this.externalPort, this.internalPort);
                ClientLog.log("GUI aspects initialized");
            }

            this.createHeartbeatThread();
            this.createExClientHandler(e, messageInputStream);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    private void createHeartbeatThread() {
        Thread heartBeatThread = new Thread(new Runnable() {
            public void run() {
                DataOutputStream dataOutputStream = null;

                try {
                    Socket heartBeat = new Socket(GrapplMC.this.relayServerIP, GrapplGlobal.HEARTBEAT);
                    GrapplMC.this.sockets.add(heartBeat);
                    dataOutputStream = new DataOutputStream(heartBeat.getOutputStream());
                } catch (IOException var6) {
                    var6.printStackTrace();
                }

                ClientLog.log("Connected to heartbeat server");

                while(true) {
                    try {
                        dataOutputStream.writeInt(0);
                    } catch (IOException var5) {
                        var5.printStackTrace();
                        GrapplMC.this.isDown();
                        return;
                    }

                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException var4) {
                        var4.printStackTrace();
                    }
                }
            }
        });
        heartBeatThread.setName("Grappl Heartbeat Thread");
        heartBeatThread.start();
    }

    private void isDown() {
        ClientLog.log("Lost connection to remote");
        this.closeAllSockets();
        (new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {
                        Socket e = new Socket(GrapplGlobal.DOMAIN, GrapplGlobal.HEARTBEAT);
                        e.close();
                        return;
                    } catch (IOException var3) {
                        System.out.println("Attempting reconnect");
                    }
                }
            }
        })).start();
    }

    private void closeAllSockets() {
        for(int i = 0; i < this.sockets.size(); ++i) {
            try {
                ((Socket)this.sockets.get(i)).close();
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        }

//        ClientLog.log("Sockets closed");
    }

    public int getInternalPort() {
        return this.internalPort;
    }

    private void createExClientHandler(final Socket messageSocket, final DataInputStream messageInputStream) {
        Thread exClientHandler = new Thread(new Runnable() {
            public void run() {
                try {
                    if(GrapplMC.this.gui != null) {
                        GrapplMC.this.getGui().jLabel3 = new JLabel("Connected clients: " + GrapplMC.this.getStatsManager().getOpenConnections());
                        GrapplMC.this.getGui().jLabel3.setBounds(5, 45, 450, 20);
                        GrapplMC.this.gui.getjFrame().add(GrapplMC.this.getGui().jLabel3);
                        GrapplMC.this.gui.getjFrame().repaint();
                    }

                    while(true) {
                        String e = messageInputStream.readLine();
                        //ClientLog.log("A user has connected from ip " + e);
                        GrapplMC.this.statsManager.openConnection();

                        try {
                            final Socket e1 = new Socket("127.0.0.1", GrapplMC.this.internalPort);
                            GrapplMC.this.sockets.add(e1);
                            ClientLog.log(GrapplMC.this.relayServerIP);
                            final Socket toRemote = new Socket(GrapplMC.this.relayServerIP, Integer.parseInt(GrapplMC.this.externalPort) + 1);
                            GrapplMC.this.sockets.add(toRemote);
                            Thread localToRemote = new Thread(new Runnable() {
                                public void run() {
                                    byte[] buffer = new byte[4096];

                                    int size;
                                    try {
                                        while((size = e1.getInputStream().read(buffer)) != -1) {
                                            toRemote.getOutputStream().write(buffer, 0, size);
                                            GrapplMC.this.statsManager.sendBlock();
                                        }
                                    } catch (IOException var7) {
                                        try {
                                            e1.close();
                                            toRemote.close();
                                        } catch (IOException var6) {
                                            var6.printStackTrace();
                                        }
                                    }

                                    try {
                                        e1.close();
                                        toRemote.close();
                                    } catch (IOException var5) {
                                        var5.printStackTrace();
                                    }

                                }
                            });
                            localToRemote.start();
                            Thread remoteToLocal = new Thread(new Runnable() {
                                public void run() {
                                    byte[] buffer = new byte[4096];

                                    int size;
                                    try {
                                        while((size = toRemote.getInputStream().read(buffer)) != -1) {
                                            e1.getOutputStream().write(buffer, 0, size);
                                            GrapplMC.this.statsManager.receiveBlock();
                                        }
                                    } catch (IOException var7) {
                                        var7.printStackTrace();

                                        try {
                                            e1.close();
                                            toRemote.close();
                                        } catch (IOException var6) {
                                            ;
                                        }
                                    }

                                    try {
                                        e1.close();
                                        toRemote.close();
                                    } catch (IOException var5) {
                                        var5.printStackTrace();
                                    }

                                }
                            });
                            remoteToLocal.start();
                        } catch (Exception var7) {
                            //ClientLog.log("Failed to connect to local server!");
                        }
                    }
                } catch (IOException var8) {
                    try {
                        messageSocket.close();
                        ClientLog.log("Error in connection with message server");
                    } catch (IOException var6) {
                        ;
                    }

                }
            }
        });
        exClientHandler.start();
    }

    public void restart() {
        ClientLog.log("Reconnecting...");
        if(this.isLoggedIn) {
            try {
                Socket e = new Socket(GrapplGlobal.DOMAIN, GrapplGlobal.AUTHENTICATION);
                DataInputStream dataInputStream = new DataInputStream(e.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(e.getOutputStream());
                dataOutputStream.writeByte(0);
                PrintStream printStream = new PrintStream(dataOutputStream);
                printStream.println(this.username.toLowerCase());
                printStream.println(this.password);
                boolean success = dataInputStream.readBoolean();
                boolean alpha = dataInputStream.readBoolean();
                int port = dataInputStream.readInt();
                this.isAlphaTester = alpha;
                this.isLoggedIn = success;
                if(success) {
                    ClientLog.log("Logged in as " + this.username);
                    ClientLog.log("Alpha tester: " + alpha);
                    ClientLog.log("Static port: " + port);
                    String prefix = dataInputStream.readLine();
                    String domain = prefix + "." + GrapplGlobal.DOMAIN;
                    ClientLog.log(domain);
                    if(this.gui != null) {
                        int wX = this.gui.getjFrame().getX();
                        int wY = this.gui.getjFrame().getY();
                        this.gui.getjFrame().setVisible(false);
                        this.gui.jFrame = new JFrame(GrapplGlobal.APP_NAME + " Client (" + this.username + ")");
                        this.gui.getjFrame().setSize(new Dimension(300, 240));
                        this.gui.getjFrame().setLocation(wX, wY);
                        this.gui.getjFrame().setVisible(true);
                        this.gui.getjFrame().setLayout((LayoutManager)null);
                        this.gui.getjFrame().setDefaultCloseOperation(3);
                        JButton jButton = new JButton("Close " + GrapplGlobal.APP_NAME + " Client");
                        jButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                System.exit(0);
                            }
                        });
                        this.gui.getjFrame().add(jButton);
                        jButton.setBounds(0, 95, 280, 100);
                    }
                } else {
                    ClientLog.log("Login failed!");
                }
            } catch (Exception var13) {
                var13.printStackTrace();
            }
        }

        this.connect(this.relayServerIP);
    }

    public String getPublicAddress() {
        String publicAddress = "grappl.io:" + this.getExternalPort();
        return this.getPrefix() != null?this.getPrefix() + "." + publicAddress:"";
    }

    public void disconnect() {
        this.closeAllSockets();
    }

    public String getExternalPort() {
        return this.externalPort;
    }

    public void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public void setAlphaTester(boolean isAlphaTester) {
        this.isAlphaTester = isAlphaTester;
    }

    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isAlphaTester() {
        return this.isAlphaTester;
    }

    public String getRelayServer() {
        return this.relayServerIP;
    }

    public GrapplGUI getGui() {
        return this.gui;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setInternalPort(int internalPort) {
        this.internalPort = internalPort;
    }

    public StatsManager getStatsManager() {
        return this.statsManager;
    }
}
