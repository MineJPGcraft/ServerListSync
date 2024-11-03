package top.alazeprt.sls.mixin.client;

import net.minecraft.client.gui.widget.CheckboxWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CheckboxWidget.class)
public interface CheckboxWidgetMixin {
    @Accessor("checked")
    public void setChecked(boolean checked);
}
