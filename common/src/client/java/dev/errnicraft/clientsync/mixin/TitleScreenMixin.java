package dev.errnicraft.clientsync.mixin;

import dev.errnicraft.clientsync.installer.InstallerLauncher;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    protected TitleScreenMixin() {
        super(Component.empty());
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void clientsync$addButton(CallbackInfo ci) {
        int spacing = 24;
        int topPos = this.height / 4 + 48;
        int y = topPos + spacing;
        int x = this.width / 2 + 100 + 4;
        this.addRenderableWidget(
            Button.builder(Component.literal("S"), btn -> this.minecraft.setScreen(new ConfirmScreen(
                    confirmed -> {
                        if (confirmed) {
                            java.util.Optional<String> launchError = InstallerLauncher.launch(
                                    net.minecraft.SharedConstants.getCurrentVersion().getName());
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
            )))
                .bounds(x, y, 20, 20)
                .tooltip(net.minecraft.client.gui.components.Tooltip.create(Component.translatable("clientsync.button.tooltip")))
                .build()
        );
    }
}
