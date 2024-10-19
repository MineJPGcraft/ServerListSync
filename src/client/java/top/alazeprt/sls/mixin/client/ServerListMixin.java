package top.alazeprt.sls.mixin.client;

import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Mixin(ServerList.class)
public abstract class ServerListMixin {

	@Shadow public abstract ServerInfo get(String address);

	@Shadow @Final private List<ServerInfo> servers;

	@Shadow @Final private List<ServerInfo> hiddenServers;

	@Inject(at = @At("RETURN"), method = "loadFile")
    private void onLoadServerInfo(CallbackInfo ci) {
		updateServerInfo();
	}

	@Inject(at = @At("TAIL"), method = "remove")
    private void onRemoveServerInfo(ServerInfo serverInfo, CallbackInfo ci) {
		updateServerInfo();
	}

	@Inject(at = @At("HEAD"), method = "saveFile")
    private void onSaveServerInfo(CallbackInfo ci) {
		updateServerInfo();
	}

	@Unique
	private void updateServerInfo() {
		for (ServerInfo serverInfo : servers) {
			if (!Objects.equals(get(serverInfo.address), serverInfo)) {
				ServerInfo serverInfo1 = get(serverInfo.address);
				if (!servers.remove(serverInfo1)) {
					hiddenServers.remove(serverInfo1);
				}
				servers.add(serverInfo);
			}
		}
	}
}