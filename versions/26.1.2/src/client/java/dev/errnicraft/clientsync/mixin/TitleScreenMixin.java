package dev.errnicraft.clientsync.mixin;

import dev.errnicraft.clientsync.installer.InstallerLauncher;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    protected TitleScreenMixin() {
        super(Component.empty());
    }

    private static final Identifier CLIENTSYNC_ICON = Identifier.fromNamespaceAndPath("clientsync", "installer_button");
    private static final int ICON_SIZE = 20;
    private static final int BUTTON_SIZE = 20;

    @Inject(method = "init", at = @At("TAIL"))
    private void clientsync$addButton(CallbackInfo ci) {
        AbstractWidget realmsButton = null;
        for (GuiEventListener child : this.children()) {
            if (child instanceof AbstractWidget widget
                    && widget.getMessage().equals(Component.translatable("menu.multiplayer"))) {
                realmsButton = widget;
                break;
            }
        }

        int x;
        int y;
        if (realmsButton != null) {
            x = realmsButton.getX() + realmsButton.getWidth() + 4;
            y = realmsButton.getY();
        } else {
            x = this.width / 2 + 100 + 4;
            y = this.height / 4 + 48 + 24;
        }

        SpriteIconButton clientsyncButton = SpriteIconButton.builder(Component.translatable("clientsync.button.tooltip"), btn -> this.minecraft.setScreen(new ConfirmScreen(
                confirmed -> {
                    if (confirmed) {
                        java.util.Optional<String> launchError = InstallerLauncher.launch(
                                net.fabricmc.loader.api.FabricLoader.getInstance().getModContainer("minecraft").map(c -> c.getMetadata().getVersion().getFriendlyString()).orElse("unknown"));
                        if (launchError.isPresent()) {
                            this.minecraft.setScreen(new ConfirmScreen(
                                    c -> this.minecraft.setScreen((TitleScreen)(Object)this),
                                    Component.translatable("clientsync.error.title"),
                                    Component.translatable(launchError.get()),
                                    Component.translatable("clientsync.error.ok"),
                                    Component.translatable("clientsync.error.ok")
                            ));
                        } else {
                            this.minecraft.stop();
                        }
                    } else {
                        this.minecraft.setScreen((TitleScreen)(Object)this);
                    }
                },
                Component.translatable("clientsync.confirm.title"),
                Component.translatable("clientsync.confirm.message")
        )), true)
                .sprite(CLIENTSYNC_ICON, ICON_SIZE, ICON_SIZE)
                .size(BUTTON_SIZE, BUTTON_SIZE)
                .build();
        clientsyncButton.setPosition(x, y);
        clientsyncButton.setTooltip(net.minecraft.client.gui.components.Tooltip.create(Component.translatable("clientsync.button.tooltip")));
        this.addRenderableWidget(clientsyncButton);
    }
}
