/*
 * Copyright 2024 The Quilt Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.quiltmc.qsl.debug_renderers.api.client;

import net.minecraft.client.render.debug.DebugRenderer;

import org.quiltmc.qsl.base.api.event.client.ClientEventAwareListener;
import org.quiltmc.qsl.debug_renderers.api.DebugFeature;
import org.quiltmc.qsl.base.api.event.Event;
//import org.quiltmc.qsl.base.api.event.EventAwareListener;

@FunctionalInterface
//TODO EventAwareListener?
public interface DebugRendererRegistrationCallback extends ClientEventAwareListener {
	Event<DebugRendererRegistrationCallback> EVENT = Event.create(DebugRendererRegistrationCallback.class, callbacks -> registrar -> {
		for (var callback : callbacks) {
			callback.registerDebugRenderers(registrar);
		}
	});

	void registerDebugRenderers(DebugRendererRegistrationCallback.DebugRendererRegistrar registrar);

	@FunctionalInterface
	interface DebugRendererRegistrar {
		void register(DebugFeature feature, DebugRenderer.Renderer renderer);
	}
}
