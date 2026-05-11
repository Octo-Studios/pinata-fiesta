package it.hurts.shatterbyte.pinatafiesta.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.hurts.shatterbyte.pinatafiesta.Constants;
import it.hurts.shatterbyte.pinatafiesta.client.entity.PinataModel;
import it.hurts.shatterbyte.pinatafiesta.client.entity.PinataRenderState;
import it.hurts.shatterbyte.pinatafiesta.content.ModEntityModelLayers;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class PinataRenderer extends LivingEntityRenderer<PinataEntity, PinataRenderState, PinataModel> {
	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/pinata.png");

	public PinataRenderer(EntityRendererProvider.Context context) {
		super(context, new PinataModel(context.bakeLayer(ModEntityModelLayers.PINATA)), 0.375f); // 0.375 shadow radius
	}

	@Override
	public void submit(PinataRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		poseStack.pushPose();

		poseStack.translate(0, state.boundingBoxHeight/2f, 0);
		poseStack.mulPose(Axis.YP.rotationDegrees(180f-state.bodyRot));
		poseStack.mulPose(Axis.ZP.rotation(Mth.cos(state.ageInTicks/24f)*0.075f));
		poseStack.mulPose(Axis.XP.rotation(Mth.cos(state.ageInTicks/24f)*0.05f));
		poseStack.mulPose(Axis.YP.rotationDegrees(-180f+state.bodyRot));

		poseStack.translate(0, -state.boundingBoxHeight/2f, 0);

		poseStack.translate(0f, 0.25f + Mth.sin(state.ageInTicks/24f)*0.15f, 0f);
		poseStack.scale(1f, 1f, 1f);

		super.submit(state, poseStack, submitNodeCollector, camera);
		poseStack.popPose();
	}

	@Override
	protected boolean shouldShowName(PinataEntity entity, double distanceToCameraSq) {
		return false;
	}

	@Override
	public PinataRenderState createRenderState() {
		return new PinataRenderState();
	}

	@Override
	public Identifier getTextureLocation(PinataRenderState state) {
		return TEXTURE;
	}
}