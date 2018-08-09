package demo;

import io.client.IOClient;
import io.protocol.Packet;
import io.protocol.json.ContextualJsonSupplier;
import io.protocol.json.Json;
import io.protocol.json.JsonObject;
import io.protocol.listener.impl.client.ShutdownListener;
import io.script.ClientScript;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;

import java.io.IOException;

/**
 * @author Articron
 * File: ScriptOne.java
 * A demo class on how to utilise the IOClient
 */
@ScriptManifest(category = Category.MISC, name = "ScriptOne", author = "Articron", version = .1)
public class ScriptOne extends ClientScript {

    @Override
    public void onStart(IOClient client)  {
        /**
         * For adding new input listeners, see Package: {@link io.protocol.listener.impl}
         */
        client = new IOClient(getLocalPlayer().getName(), 43594) //setting the UID (has to be unique) + port
                .setDebug(false) //show networking debug
                .addListeners(new ShutdownListener()) //add all listeners here
                .startYelling("password123"); //password needs  to match the server's password!

        /**
         * Able to create objects from the minimal-json library
         */
        JsonObject object = Json.object()
                .add("name","Articron")
                .add("age", 24);

        /**
         * But also able to integrate the DB API as such:
         */
        JsonObject contextJson = new ContextualJsonSupplier(this) {
            @Override
            public JsonObject create(JsonObject json) {
                json.add("prayer level", getSkills().getRealLevel(Skill.PRAYER));
                json.add("username", getLocalPlayer().getName());
                return json;
            }
        }.getObject();

        /**
         * And we send data like this:
         */
        try {
            client.getConnection().getStream().writeJSON(new Packet(PacketData.TEST_PACKET.ordinal(), object));
            client.getConnection().getStream().writeJSON(new Packet(PacketData.TEST_PACKET.ordinal(), contextJson));
            /**
             * Note, connections are generified into ServerConnection and ClientConnection, you can write custom methods for each
             * Example: authenticate from {@link io.protocol.connection.impl.ClientConnection}
             */
            client.getConnection().getStream().authenticate("password","uid");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int onLoop() {
        return 0;
    }
}
