package top.alazeprt.sls.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
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

    private EditBox address;
    private Checkbox order_default;
    private Checkbox order_reverse;
    private Checkbox order_alphabetical;
    private Checkbox order_random;
    private EditBox updatePeriod;

    protected SLSConfigScreen(Screen parent) {
        super(Component.literal("ServerListSync 配置界面"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        // 组件
        Button cancel = Button.builder(Component.literal("取消"), button -> onClose())
                .bounds(width / 2 - 180, height*8/9, 160 ,20)
                .build();
        Button complete = Button.builder(Component.literal("完成"), button -> saveConfig())
                .bounds(width / 2 + 20, height*8/9, 160, 20)
                .build();
        address = new EditBox(font, width/2-90, height*2/9, 260, 20, Component.literal("服务器地址"));
        address.setMaxLength(1024);
        order_default = Checkbox.builder(Component.literal("默认"), font)
                .pos(width/2-90, (int) (height*3.5/9))
                .selected(SLSConfig.order == ServerOrder.DEFAULT)
                .onValueChange(callback)
                .build();
        order_reverse = Checkbox.builder(Component.literal("倒序"), font)
                .pos(width/2-40, (int) (height*3.5/9))
                .selected(SLSConfig.order == ServerOrder.REVERSE)
                .onValueChange(callback)
                .build();
        order_alphabetical = Checkbox.builder(Component.literal("字母序"), font)
                .pos(width/2+10, (int) (height*3.5/9))
                .selected(SLSConfig.order == ServerOrder.ALPHABETICAL)
                .onValueChange(callback)
                .build();
        order_random = Checkbox.builder(Component.literal("随机序"), font)
                .pos(width/2+60, (int) (height*3.5/9))
                .selected(SLSConfig.order == ServerOrder.RANDOM)
                .onValueChange(callback)
                .build();
        updatePeriod = new EditBox(font, width/2-90, height*5/9, 260, 20, Component.literal("更新周期"));
        // 组件设置
        updatePeriod.setTooltip(Tooltip.create(Component.literal("多久从服务器地址中同步一次服务器列表, 单位为秒")));
        updatePeriod.setValue(String.valueOf(SLSConfig.updatePeriod));
        address.setValue(SLSConfig.address);
        // 添加组件
        addRenderableWidget(cancel);
        addRenderableWidget(complete);
        addRenderableWidget(address);
        addRenderableWidget(order_default);
        addRenderableWidget(order_reverse);
        addRenderableWidget(order_alphabetical);
        addRenderableWidget(order_random);
        addRenderableWidget(updatePeriod);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        super.extractRenderState(context, mouseX, mouseY, delta);
        context.text(font, "服务器地址", width/2-170, height*2/9+5, 0xFFFFFFFF);
        context.text(font, "排序方式", width/2-170, (int) (height*3.5/9+5), 0xFFFFFFFF);
        context.text(font, "更新时间", width/2-170, height*5/9+5, 0xFFFFFFFF);
        context.centeredText(font, "ServerListSync 配置界面", width/2, height/9, 0xFFFFFFFF);
    }

    @Override
    public void onClose() {
        minecraft.setScreenAndShow(parent);
    }

    private void saveConfig() {
        try {
            Integer.parseInt(updatePeriod.getValue());
        } catch (NumberFormatException e) {
            minecraft.setScreenAndShow(new SLSErrorScreen(parent, "更新时间必须是一个整数"));
            return;
        }
        SLSConfig.address = address.getValue();
        SLSConfig.updatePeriod = Integer.parseInt(updatePeriod.getValue());
        if (order_default.selected()) SLSConfig.order = ServerOrder.DEFAULT;
        else if (order_reverse.selected()) SLSConfig.order = ServerOrder.REVERSE;
        else if (order_alphabetical.selected()) SLSConfig.order = ServerOrder.ALPHABETICAL;
        else if (order_random.selected()) SLSConfig.order = ServerOrder.RANDOM;
        try {
            SLSConfig.save();
        } catch (IOException e) {
            minecraft.setScreenAndShow(new SLSErrorScreen(parent, e.toString()));
            return;
        }
        onClose();
    }

    class SLSCallback implements Checkbox.OnValueChange {
        @Override
        public void onValueChange(Checkbox checkbox, boolean checked) {
            if (order_default != checkbox && order_default.selected()) {
                ((CheckboxWidgetMixin) order_default).setChecked(false);
            }
            if (order_reverse != checkbox && order_reverse.selected()) {
                ((CheckboxWidgetMixin) order_reverse).setChecked(false);
            }
            if (order_alphabetical != checkbox && order_alphabetical.selected()) {
                ((CheckboxWidgetMixin) order_alphabetical).setChecked(false);
            }
            if (order_random != checkbox && order_random.selected()) {
                ((CheckboxWidgetMixin) order_random).setChecked(false);
            }
        }
    }
}
