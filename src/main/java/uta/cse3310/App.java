
// This is example code provided to CSE3310 Fall 2022
// You are free to use as is, or changed, any of the code provided

// Please comply with the licensing requirements for the
// open source packages being used.

// This code is based upon, and derived from the this repository
//            https:/thub.com/TooTallNate/Java-WebSocket/tree/master/src/main/example

// http server include is a GPL licensed package from
//            http://www.freeutils.net/source/jlhttp/

/*
 * Copyright (c) 2010-2020 Nathan Rajlich
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following
 *  conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 */

package uta.cse3310;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Iterator;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import uta.cse3310.Events.*;

public class App extends WebSocketServer {
    public App(int port) {
        super(new InetSocketAddress(port));
    }

    public App(InetSocketAddress address) {
        super(address);
    }

    public App(int port, Draft_6455 draft) {
        super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
    }

    public ArrayList<User> onlineUsers = new ArrayList<>();
    public ArrayList<User> allUsers = new ArrayList<>();
    public static ArrayList<Game> activeGames = new ArrayList<>();
    public Lobby[] lobbies = new Lobby[] { new Lobby(2), new Lobby(3), new Lobby(4) };

    private Gson gson = new Gson();

    public static String[] words;

    public User createUser(String name) {
        for (var onlineUser : onlineUsers) {
            if (onlineUser.name.equals(name))
                return null;
        }
        for (var offlineUser : allUsers) {
            if (offlineUser.name.equals(name))
                return offlineUser;
        }

        var user = new User();
        user.name = name;
        allUsers.add(user);
        return user;
    }

    public void broadcastUserList() {
        var event = new EventHolder<>("PlayerListResponse", new PlayerListResponse());
        String[] users = new String[onlineUsers.size()];
        for (int i = 0; i < users.length; i++) {
            users[i] = onlineUsers.get(i).name;
        }
        event.eventData.onlineUsers = users;
        broadcast(gson.toJson(event));
    }

    public void broadcastLobbies() {
        var event = new EventHolder<>("LobbyUpdateResponse", new LobbyUpdateResponse());
        String[][] lobbies = new String[3][];
        for (int i = 0; i < 3; i++) {
            lobbies[i] = new String[this.lobbies[i].usersInLobby.size()];
            for (int j = 0; j < lobbies[i].length; j++) {
                lobbies[i][j] = this.lobbies[i].usersInLobby.get(j).name;
            }
        }
        event.eventData.lobbies = lobbies;
        broadcast(gson.toJson(event));
    }

    public void broadcastLeaderboard() {
        var event = new EventHolder<>("LeaderBoardResponse", new LeaderBoardResponse());
        var size = allUsers.size() > 20 ? 20 : allUsers.size();
        event.eventData.usernames = new String[size];
        event.eventData.scores = new int[size];
        allUsers.sort(new Comparator<User>() {
            @Override
            public int compare(User left, User right) {
                return -Integer.compare(left.totalScore, right.totalScore);
            }
        });
        for (int i = 0; i < size; i++) {
            event.eventData.usernames[i] = allUsers.get(i).name;
            event.eventData.scores[i] = allUsers.get(i).totalScore;
        }
        broadcast(gson.toJson(event));
    }

    public void addToLobby(Lobby lobby, User user) {

    }

    public void removeFromLobby(Lobby lobby, User user) {

    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        // send all the data needed to render the lobby screen:
        // user list, 
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        onlineUsers.remove((User)conn.getAttachment());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        User user = conn.getAttachment();
        System.out.println(message);
        var parser = JsonParser.parseString(message);
        var object = parser.getAsJsonObject();
        switch (object.get("type").getAsString())
        {
            case "UserLoginRequest":
                var loginEvent = gson.fromJson(object.get("eventData"), UserLoginRequest.class);
                user = createUser(loginEvent.username);
                if (user == null) {
                    conn.send("{ \"type\": \"LoginResponse\", \"eventData\": { \"loggedIn\": false } }");
                } else {
                    user.socket = conn;
                    conn.setAttachment(user);
                    onlineUsers.add(user);
                    conn.send("{ \"type\": \"LoginResponse\", \"eventData\": { \"loggedIn\": true } }");
                    broadcastUserList();
                    broadcastLeaderboard();
                }
                break;
            case "JoinLobbyRequest":
                var lobbyJoinEvent = gson.fromJson(object.get("eventData"), JoinLobbyRequest.class);
                for (var l : lobbies) {
                    if (l.usersInLobby.contains(user)) {
                        l.removeUser(user);
                        break;
                    }
                }
                var lobby = lobbies[lobbyJoinEvent.lobby];
                lobby.addUser(user);
                broadcastLobbies();
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }

    @Override
    public void onStart() {

    }

    public static void main(String[] args) {
        // load word list

        // Set up the http server
        String portString = System.getenv("HTTP_PORT");
        int port = portString != null ? Integer.parseInt(portString) : 9080;
        HttpServer H = new HttpServer(port, "./html");
        H.start();
        System.out.println("http Server started on port: " + port);

        // create and start the websocket server

        portString = System.getenv("WEBSOCKET_PORT");
        port = portString != null ? Integer.parseInt(portString) : 9880;
        App A = new App(port);
        A.setReuseAddr(true);
        A.start();
        System.out.println("websocket Server started on port: " + port);

        Timer GameStart = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run(){
                Iterator<Game> iter = activeGames.iterator();
                while(iter.hasNext())
                {
                    Game game = iter.next();
                    game.tick();

                    if(game.gameOver)
                        iter.remove();

                }
            }
        };

        //Count down timer for Game
        GameStart.scheduleAtFixedRate(task, 0 , 1000);

    }
}
