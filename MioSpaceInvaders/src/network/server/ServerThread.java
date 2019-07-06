package network.server;

import logic.environment.manager.game.Commands;
import logic.environment.manager.game.Multiplayer;
import logic.player.Player;
import network.data.Connection;
import network.data.PacketHandler;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerThread implements Runnable{
    private Multiplayer multiplayer;
    private Player player;
    private Connection connection;
    private PacketHandler handler;
    private DatagramSocket socket;

    private CopyOnWriteArrayList<String[]> infos;

    private AtomicBoolean running;

    public ServerThread(Player player, Multiplayer multiplayer, Connection connection, DatagramSocket socket) {
        this.player = player;
        this.multiplayer = multiplayer;
        this.connection = connection;
        this.socket = socket;

        infos = new CopyOnWriteArrayList<>();
        handler = new PacketHandler();
        running = new AtomicBoolean(true);
        start();
    }

    public void send(String mex) {
        try {
            //INVIO AL CLIENT IL SUO ID = POSIZIONE NELL'ARRAYLIST DI THREAD
            socket.send(handler.build(String.valueOf(mex), connection));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        Thread listener = new Thread(this);
        listener.start();
    }

    public synchronized void setInfos(String[] data){
        infos.add(data);
        notifyAll();
    }

    public synchronized void run() {
        String[] data;
        running.set(true);
        while(running.get()) {
            if(infos.isEmpty()){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            data = infos.get(0);
            infos.remove(0);
            exe(data);
        }
    }

    public void exe(String[] data){
        switch (Commands.valueOf(data[1])) {
            case MOVE_LEFT:
            case MOVE_RIGHT:
                player.getSpaceShip().setX(Float.parseFloat(data[2]));
                break;
            case SHOT:
                multiplayer.getFieldManager().shipShot(player.getSpaceShip());
                break;
            case EXIT:
                multiplayer.getTeam().removePlayer(Integer.parseInt(data[0]));
                stop();
            default:
                break;
        }
    }

    /**
     * Invio dati al client.
     * Il server invia a tutti i client le informazioni sullo stato del gioco e sulla posizione degli
     * elementi per permettere ai client di renderizzarli.
     */
    public void sender() {
        Thread sender = new Thread(() -> {
            running.set(true);
            while (running.get()) {
                String infos = multiplayer.getInfos();
                try {
                    socket.send(handler.build(infos, connection));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        sender.start();
    }

    public void stop() {
        running.set(false);
    }

    public AtomicBoolean isRunning(){
        return running;
    }

    public Connection getConnection(){
        return connection;
    }

}