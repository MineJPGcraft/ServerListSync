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
import top.alazeprt.sls.config.SLSConfig;
import top.alazeprt.sls.util.ServerOrder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static top.alazeprt.sls.ServerListSyncClient.*;

@Mixin(ServerList.class)
public abstract class ServerListMixin {

	@Shadow @Final private List<ServerInfo> servers;

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
	private synchronized void updateServerInfo() {
		if (!updateData) {
			updateServerInfos();
		}
		if (SLSConfig.order.equals(ServerOrder.RANDOM)) {
			Collections.shuffle(serverInfos);
		}
		serverInfos.forEach(serverInfo -> servers.removeIf(origin -> Objects.equals(origin.address, serverInfo.address)));
		for (ServerInfo serverInfo : serverInfos) {
			servers.add(new ServerInfo(serverInfo.name, serverInfo.address, serverInfo.getServerType()));
		}
	}
}