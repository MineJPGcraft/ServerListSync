package top.alazeprt.sls.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.alazeprt.sls.config.SLSConfig;
import top.alazeprt.sls.mixin.client.CheckboxWidgetMixin;
import top.alazeprt.sls.util.ServerOrder;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class SLSConfigScreen extends Screen {
    private final Screen parent;
    private static final Logger LOGGER = LoggerFactory.getLogger(SLSConfigScreen.class);
    private final SLSCallback callback = new SLSCallback();

    private TextFieldWidget address;
    private CheckboxWidget order_default;
    private CheckboxWidget order_reverse;
    private CheckboxWidget order_alphabetical;
    private CheckboxWidget order_random;
    private TextFieldWidget updatePeriod;

    protected SLSConfigScreen(Screen parent) {
        super(Text.literal("ServerListSync 配置界面"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        // 组件
        ButtonWidget cancel = ButtonWidget.builder(Text.literal("取消"), button -> close())
                .dimensions(width / 2 - 180, height*8/9, 160 ,20)
                .build();
        ButtonWidget complete = ButtonWidget.builder(Text.literal("完成"), button -> saveConfig())
                .dimensions(width / 2 + 20, height*8/9, 160, 20)
                .build();
        address = new TextFieldWidget(textRenderer, width/2-90, height*2/9, 260, 20, Text.literal("服务器地址"));
        address.setMaxLength(1024);
        order_default = CheckboxWidget.builder(Text.literal("默认"), textRenderer)
                .pos(width/2-90, (int) (height*3.5/9))
                .checked(SLSConfig.order == ServerOrder.DEFAULT)
                .callback(callback)
                .build();
        order_reverse = CheckboxWidget.builder(Text.literal("倒序"), textRenderer)
                .pos(width/2-40, (int) (height*3.5/9))
                .checked(SLSConfig.order == ServerOrder.REVERSE)
                .callback(callback)
                .build();
        order_alphabetical = CheckboxWidget.builder(Text.literal("字母序"), textRenderer)
                .pos(width/2+10, (int) (height*3.5/9))
                .checked(SLSConfig.order == ServerOrder.ALPHABETICAL)
                .callback(callback)
                .build();
        order_random = CheckboxWidget.builder(Text.literal("随机序"), textRenderer)
                .pos(width/2+60, (int) (height*3.5/9))
                .checked(SLSConfig.order == ServerOrder.RANDOM)
                .callback(callback)
                .build();
        updatePeriod = new TextFieldWidget(textRenderer, width/2-90, height*5/9, 260, 20, Text.literal("更新周期"));
        // 组件设置
        updatePeriod.setTooltip(Tooltip.of(Text.literal("多久从服务器地址中同步一次服务器列表, 单位为秒")));
        updatePeriod.setText(String.valueOf(SLSConfig.updatePeriod));
        address.setText(SLSConfig.address);
        // 添加组件
        addDrawableChild(cancel);
        addDrawableChild(complete);
        addDrawableChild(address);
        addDrawableChild(order_default);
        addDrawableChild(order_reverse);
        addDrawableChild(order_alphabetical);
        addDrawableChild(order_random);
        addDrawableChild(updatePeriod);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(textRenderer, Text.literal("服务器地址"), width/2-170, height*2/9+5, 0xffffff);
        context.drawTextWithShadow(textRenderer, Text.literal("排序方式"), width/2-170, (int) (height*3.5/9+5), 0xffffff);
        context.drawTextWithShadow(textRenderer, Text.literal("更新时间"), width/2-170, height*5/9+5, 0xffffff);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("ServerListSync 配置界面"), width/2, height/9, 0xffffff);
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }

    private void saveConfig() {
        try {
            Integer.parseInt(updatePeriod.getText());
        } catch (NumberFormatException e) {
            client.setScreen(new SLSErrorScreen(parent, "更新时间必须是一个整数"));
            return;
        }
        SLSConfig.address = address.getText();
        SLSConfig.updatePeriod = Integer.parseInt(updatePeriod.getText());
        if (order_default.isChecked()) SLSConfig.order = ServerOrder.DEFAULT;
        else if (order_reverse.isChecked()) SLSConfig.order = ServerOrder.REVERSE;
        else if (order_alphabetical.isChecked()) SLSConfig.order = ServerOrder.ALPHABETICAL;
        else if (order_random.isChecked()) SLSConfig.order = ServerOrder.RANDOM;
        try {
            SLSConfig.save();
        } catch (IOException e) {
            client.setScreen(new SLSErrorScreen(parent, e.toString()));
            return;
        }
        close();
    }

    class SLSCallback implements CheckboxWidget.Callback {
        @Override
        public void onValueChange(CheckboxWidget checkbox, boolean checked) {
            if (order_default != checkbox && order_default.isChecked()) {
                ((CheckboxWidgetMixin) order_default).setChecked(false);
            }
            if (order_reverse != checkbox && order_reverse.isChecked()) {
                ((CheckboxWidgetMixin) order_reverse).setChecked(false);
            }
            if (order_alphabetical != checkbox && order_alphabetical.isChecked()) {
                ((CheckboxWidgetMixin) order_alphabetical).setChecked(false);
            }
            if (order_random != checkbox && order_random.isChecked()) {
                ((CheckboxWidgetMixin) order_random).setChecked(false);
            }
        }
    }
}
