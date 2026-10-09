package pl.autoclicker;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AutoClickerMod implements ClientModInitializer {
    private static final int INTERVAL_TICKS = 20; // 20 ticków = 1 sekunda

    private static KeyBinding holdKey;
    private int ticks = INTERVAL_TICKS; // pierwsze uderzenie od razu po wcisnieciu

    @Override
    public void onInitializeClient() {
        holdKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.autoclicker.hold",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_L,
                "category.autoclicker"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.currentScreen != null || !holdKey.isPressed()) {
                ticks = INTERVAL_TICKS;
                return;
            }

            if (ticks >= INTERVAL_TICKS) {
                ticks = 0;
                click(client);
            }
            ticks++;
        });
    }

    /** Symuluje klikniecie lewego przycisku myszy (tak jak atak/kopanie). */
    private void click(MinecraftClient client) {
        InputUtil.Key attack = InputUtil.fromTranslationKey(client.options.attackKey.getBoundKeyTranslationKey());
        KeyBinding.onKeyPressed(attack);
    }
}
