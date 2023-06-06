package de.ballerkind.simplemodule.core.module.modules.RemoteControl;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.types.ExternalModule;

import java.awt.image.BufferedImage;
import java.util.List;

public class ModuleRemoteControl extends ExternalModule {

	BufferedImage view;

	public ModuleRemoteControl(Main main, String userId, String type, String id, String name, String secret, List<String> shared) {
		super(main, userId, type, id, name, secret, shared);
	}

	@Override
	public void onConnect() {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		ImageIO.write(view, "jpg", baos);
//		byte[] bytes = baos.toByteArray();
	}
}