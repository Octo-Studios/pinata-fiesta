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
	private final Map<Integer, HitAnimation> hitAnimations = new HashMap<>();

	public PinataRenderer(EntityRendererProvider.Context context) {
		super(context, new PinataModel(context.bakeLayer(ModEntityModelLayers.PINATA)), 0.375f); // 0.375 shadow radius
	}

	@Override
	public void submit(PinataRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		poseStack.pushPose();

		float idlePhase = state.ageInTicks / 24.0F;
		float idleBob = Mth.sin(idlePhase) * 0.11F + Mth.sin(idlePhase * 0.53F + 1.4F) * 0.05F;
		float idleSwayZ = Mth.cos(idlePhase * 0.9F) * 0.045F;
		float idleSwayX = Mth.sin(idlePhase * 0.7F + 0.9F) * 0.03F;
		float idleYaw = Mth.sin(idlePhase * 0.42F + 0.35F) * 3.5F;

		poseStack.translate(0f, 0.25f + idleBob, 0f);
		poseStack.translate(idleSwayX, 0.0F, idleSwayZ);
		poseStack.translate(state.hitXOffset, 0.0F, state.hitZOffset);
		poseStack.translate(0, state.boundingBoxHeight/2f, 0);
		poseStack.mulPose(Axis.YP.rotationDegrees(180f-state.bodyRot));
		poseStack.mulPose(Axis.YP.rotationDegrees(idleYaw));
		poseStack.scale(1.0F, state.hitYScale, 1.0F);
		poseStack.mulPose(Axis.ZP.rotationDegrees(state.hitZRotation));
		poseStack.mulPose(Axis.XP.rotationDegrees(state.hitXRotation));
		poseStack.mulPose(Axis.ZP.rotation(Mth.cos(idlePhase * 1.05F + 0.7F) * 0.095F));
		poseStack.mulPose(Axis.XP.rotation(Mth.sin(idlePhase * 0.86F + 1.2F) * 0.065F));
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
		animation.update(entity.getHitCounter(), entity.getHitDirX(), entity.getHitDirZ(), state.bodyRot);
		state.hitXOffset = animation.xOffset;
		state.hitZOffset = animation.zOffset;
		state.hitXRotation = animation.xRotation;
		state.hitZRotation = animation.zRotation;
		state.hitYScale = animation.yScale;
		state.texture = entity.getSkin().getSkinTexture();
	}

	@Override
	public Identifier getTextureLocation(PinataRenderState state) {
		return state.texture;
	}

	private static final class HitAnimation {
		private int lastHitCounter;
		private Tween tween;

		private float xOffset;
		private float zOffset;

		private float xRotation;
		private float zRotation;

		private float yScale = 1.0F;

		private HitAnimation(int lastHitCounter) {
			this.lastHitCounter = lastHitCounter;
		}

		private void update(int hitCounter, float hitDirX, float hitDirZ, float bodyRot) {
			if (hitCounter == lastHitCounter) {
				return;
			}

			lastHitCounter = hitCounter;

			float yawRad = bodyRot * Mth.DEG_TO_RAD;
			float rightX = Mth.cos(yawRad);
			float rightZ = -Mth.sin(yawRad);
			float forwardX = -Mth.sin(yawRad);
			float forwardZ = Mth.cos(yawRad);
			float side = hitDirX * rightX + hitDirZ * rightZ;
			float frontBack = hitDirX * forwardX + hitDirZ * forwardZ;

			float targetXOffset = hitDirX * 0.33F;
			float targetZOffset = hitDirZ * 0.33F;
			float targetZRotation = side * 16.0F;
			float targetXRotation = frontBack * 16.0F;

			if (tween != null) {
				tween.kill();
			}

			tween = Tween.create();
			tween.setParallel(true);
			tween.tweenMethod(this::setXOffset, targetXOffset, 0.0F, 0.9D)
					.setEaseType(EaseType.EASE_OUT)
					.setTransitionType(TransitionType.ELASTIC);
			tween.tweenMethod(this::setZOffset, targetZOffset, 0.0F, 0.9D)
					.setEaseType(EaseType.EASE_OUT)
					.setTransitionType(TransitionType.ELASTIC);
			tween.tweenMethod(this::setZRotation, targetZRotation, 0.0F, 1.05D)
					.setEaseType(EaseType.EASE_OUT)
					.setTransitionType(TransitionType.ELASTIC);
			tween.tweenMethod(this::setXRotation, targetXRotation, 0.0F, 1.05D)
					.setEaseType(EaseType.EASE_OUT)
					.setTransitionType(TransitionType.ELASTIC);
			tween.tweenMethod(this::setYScale, 0.85F, 1.0F, 1.35D)
					.setEaseType(EaseType.EASE_OUT)
					.setTransitionType(TransitionType.ELASTIC);
			tween.start();
		}

		private void setXOffset(float xOffset) {
			this.xOffset = xOffset;
		}

		private void setZOffset(float zOffset) {
			this.zOffset = zOffset;
		}

		private void setXRotation(float xRotation) {
			this.xRotation = xRotation;
		}

		private void setZRotation(float zRotation) {
			this.zRotation = zRotation;
		}

		private void setYScale(float yScale) {
			this.yScale = yScale;
		}
	}
}
