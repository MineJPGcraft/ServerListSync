package top.alazeprt.sls.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SLSErrorScreen extends Screen {
    private final Screen parent;
    private final String exception;
    private static final Logger LOGGER = LoggerFactory.getLogger(SLSConfigScreen.class);

    protected SLSErrorScreen(Screen parent, String exception) {
        super(Component.literal("ServerListSync 错误界面"));
        this.parent = parent;
        this.exception = exception;
    }

    @Override
    protected void init() {
        super.init();
        Button complete = Button.builder(Component.literal("完成"), button -> onClose())
                .bounds(width / 2 - 80, height*8/9, 160, 20)
                .build();
        addRenderableWidget(complete);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        super.extractRenderState(context, mouseX, mouseY, delta);
        context.centeredText(font, "ServerListSync 在保存配置文件时出现错误!", width/2, height/9, 0xFFFFFFFF);
        context.centeredText(font, "错误信息: " + exception, width/2, height/2, 0xFFFFFFFF);
    }

    @Override
    public void onClose() {
        minecraft.setScreenAndShow(parent);
    }
}
