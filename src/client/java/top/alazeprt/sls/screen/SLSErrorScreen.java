package top.alazeprt.sls.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SLSErrorScreen extends Screen {
    private final Screen parent;
    private final String exception;
    private static final Logger LOGGER = LoggerFactory.getLogger(SLSConfigScreen.class);

    protected SLSErrorScreen(Screen parent, String exception) {
        super(Text.literal("ServerListSync 错误界面"));
        this.parent = parent;
        this.exception = exception;
    }

    @Override
    protected void init() {
        super.init();
        ButtonWidget complete = ButtonWidget.builder(Text.literal("完成"), button -> close())
                .dimensions(width / 2 - 80, height*8/9, 160, 20)
                .build();
        addDrawableChild(complete);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("ServerListSync 在保存配置文件时出现错误!"), width/2, height/9, 0xffffff);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("错误信息: " + exception), width/2, height/2, 0xffffff);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }
}
