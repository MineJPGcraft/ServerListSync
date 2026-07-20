package top.alazeprt.sls.mixin.client;

import net.minecraft.client.gui.components.Checkbox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Checkbox.class)
public interface CheckboxWidgetMixin {
    @Accessor("selected")
    public void setChecked(boolean checked);
}
