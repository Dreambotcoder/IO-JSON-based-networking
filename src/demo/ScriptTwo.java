package demo;

import io.protocol.Packet;
import io.protocol.connection.impl.ServerConnection;
import io.protocol.json.Json;
import io.protocol.json.JsonObject;
import io.protocol.listener.impl.server.EndConnectionListener;
import io.script.ServerScript;
import io.server.IOServer;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author Articron
 * File: ScriptTwo.java
 * A demo class on how to utilise the IOServer
 */
@ScriptManifest(category = Category.MISC, name = "ScriptTwo", author = "Articron", version = .1)
public class ScriptTwo extends ServerScript{

    @Override
    public void onStart(IOServer server) {
        try {
            server = new IOServer(43594) //define our port
                    .setDebug(true)
                    .addListeners(new EndConnectionListener()) //add listeners for incoming packets...
                    .startListening("password123"); //starts listening for IOClient yells with this password
        } catch (IOException e) {
            log("Unable to start the server!");
            e.printStackTrace();
        }
        /**
         * We can get  all valid connections
         */
        List<ServerConnection> connectedScripts = server.getOpenConnections();

        /**
         * Or get a specific one (off UID)
         */
        Optional<ServerConnection> artisNet =  server.getConnection("Articron");

        if (artisNet.isPresent()) {
            //Sending data to the client (see  Script one on how to send runescape data with contextual suppliers)
            JsonObject payload = Json.object().add("message", "nice net Arti");
            try {
                artisNet.get().getStream().writeJSON(new Packet(PacketData.TEST_PACKET.ordinal(), payload));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int onLoop() {

        return 0;
    }
}
