package it.hurts.shatterbyte.pinatafiesta.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.hurts.shatterbyte.byteapi.client.animation.Tween;
import it.hurts.shatterbyte.byteapi.client.animation.easing.EaseType;
import it.hurts.shatterbyte.byteapi.client.animation.easing.TransitionType;
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

import java.util.HashMap;
import java.util.Map;

public class PinataRenderer extends LivingEntityRenderer<PinataEntity, PinataRenderState, PinataModel> {
	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/pinata.png");
	private final Map<Integer, HitAnimation> hitAnimations = new HashMap<>();

	public PinataRenderer(EntityRendererProvider.Context context) {
		super(context, new PinataModel(context.bakeLayer(ModEntityModelLayers.PINATA)), 0.375f); // 0.375 shadow radius
	}

	@Override
	public void submit(PinataRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		poseStack.pushPose();

		poseStack.translate(0f, 0.25f + Mth.sin(state.ageInTicks/24f)*0.15f, 0f);

		//poseStack.translate(0.0f, state.hitXOffset, 0.0F);
		poseStack.translate(0, state.boundingBoxHeight/2f, 0);
		poseStack.mulPose(Axis.YP.rotationDegrees(180f-state.bodyRot));
		poseStack.scale(1.0F, state.hitYScale, 1.0F);
		poseStack.mulPose(Axis.ZP.rotationDegrees(state.hitZRotation));
		poseStack.mulPose(Axis.ZP.rotation(Mth.cos(state.ageInTicks/24f)*0.075f));
		poseStack.mulPose(Axis.XP.rotation(Mth.cos(state.ageInTicks/24f)*0.05f));
		poseStack.mulPose(Axis.YP.rotationDegrees(-180f+state.bodyRot));

		poseStack.translate(0, -state.boundingBoxHeight/2f, 0);

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
	public void extractRenderState(PinataEntity entity, PinataRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);

		HitAnimation animation = hitAnimations.computeIfAbsent(entity.getId(), ignored -> new HitAnimation(entity.getHitCounter()));
		animation.update(entity.getHitCounter());
		state.hitXOffset = animation.xOffset;
		state.hitZRotation = animation.zRotation;
		state.hitYScale = animation.yScale;
	}

	@Override
	public Identifier getTextureLocation(PinataRenderState state) {
		return TEXTURE;
	}

	private static final class HitAnimation {
		private int lastHitCounter;
		private Tween tween;
		private float xOffset;
		private float zRotation;
		private float yScale = 1.0F;

		private HitAnimation(int lastHitCounter) {
			this.lastHitCounter = lastHitCounter;
		}

		private void update(int hitCounter) {
			if (hitCounter == lastHitCounter) {
				return;
			}

			lastHitCounter = hitCounter;

			if (tween != null) {
				tween.kill();
			}

			tween = Tween.create();
			tween.setParallel(true);
			tween.tweenMethod(this::setXOffset, 0.15F, 0.0F, 2D)
					.setEaseType(EaseType.EASE_OUT)
					.setTransitionType(TransitionType.ELASTIC);
//			tween.tweenMethod(this::setZRotation, 5.0F, 0.0F, 1.34D)
//					.setEaseType(EaseType.EASE_OUT)
//					.setTransitionType(TransitionType.ELASTIC);
			tween.tweenMethod(this::setYScale, 0.75F, 1.0F, 1.75D)
					.setEaseType(EaseType.EASE_OUT)
					.setTransitionType(TransitionType.ELASTIC);
			tween.start();
		}

		private void setXOffset(float xOffset) {
			this.xOffset = xOffset;
		}

		private void setZRotation(float zRotation) {
			this.zRotation = zRotation;
		}

		private void setYScale(float yScale) {
			this.yScale = yScale;
		}
	}
}
