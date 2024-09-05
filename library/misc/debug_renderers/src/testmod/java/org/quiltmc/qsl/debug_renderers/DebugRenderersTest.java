package org.quiltmc.qsl.debug_renderers;

import net.minecraft.util.Identifier;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.debug_renderers.api.DebugFeature;
import org.quiltmc.qsl.debug_renderers.api.client.DebugRendererRegistrationCallback;

public class DebugRenderersTest implements ClientModInitializer {
	public static final ClientTestDebugRenderer CLIENT_ONLY = new ClientTestDebugRenderer();
	public static final String NAMESPACE = "quilt_debug_renderers_testmod";
	public static final DebugFeature CLIENT_TEST_FEATURE = DebugFeature.register(Identifier.of(NAMESPACE, "test"), false);

	@Override
	public void onInitializeClient(ModContainer mod) {
		DebugRendererRegistrationCallback.EVENT.register(callback -> {
			callback.register(CLIENT_TEST_FEATURE, CLIENT_ONLY);
		});
	}
}
