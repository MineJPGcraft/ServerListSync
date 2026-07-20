package top.alazeprt.sls.mixin.client;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static top.alazeprt.sls.ServerListSyncClient.*;

@Mixin(ServerList.class)
public abstract class ServerListMixin {

	@Shadow public abstract ServerData get(int index);

	@Shadow @Final private List<ServerData> serverList;

	@Shadow @Final private static Logger LOGGER;

	@Unique
	private boolean sls$updating = false;

	@Inject(at = @At("RETURN"), method = "load")
	private void onLoadServerInfo(CallbackInfo ci) {
		try {
			sls$updateServerInfo();
		} catch (Throwable e) {
			LOGGER.error("[ServerListSync] Error updating server info on load: {}", e.toString());
		}
	}

	@Inject(at = @At("TAIL"), method = "remove")
	private void onRemoveServerInfo(ServerData serverData, CallbackInfo ci) {
		try {
			sls$updateServerInfo();
		} catch (Throwable e) {
			LOGGER.error("[ServerListSync] Error updating server info on remove: {}", e.toString());
		}
	}

	@Unique
	private synchronized void sls$updateServerInfo() {
		if (sls$updating) return;
		sls$updating = true;
		try {
			if (!updateData) {
				updateServerInfos();
			}
			List<ServerData> localList = new ArrayList<>(serverInfos);
			if (SLSConfig.order.equals(ServerOrder.RANDOM)) {
				Collections.shuffle(localList);
			}
			localList.forEach(serverData -> serverList.removeIf(origin -> Objects.equals(origin.ip, serverData.ip)));
			for (ServerData serverData : localList) {
				serverList.add(new ServerData(serverData.name, serverData.ip, serverData.type()));
			}
		} finally {
			sls$updating = false;
		}
	}
}