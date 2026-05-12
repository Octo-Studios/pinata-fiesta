package it.hurts.shatterbyte.pinatafiesta.client.entity;// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.hurts.shatterbyte.pinatafiesta.Constants;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

public class PinataModel extends EntityModel<PinataRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "pinata"), "main");
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart tail;
	private final ModelPart left_leg_2;
	private final ModelPart right_leg_2;
	private final ModelPart left_leg_1;
	private final ModelPart right_leg_1;

	public PinataModel(ModelPart root) {
        super(root);

        this.body = root.getChild("body");
		this.head = this.body.getChild("head");
		this.tail = this.body.getChild("tail");
		this.left_leg_2 = this.body.getChild("left_leg_2");
		this.right_leg_2 = this.body.getChild("right_leg_2");
		this.left_leg_1 = this.body.getChild("left_leg_1");
		this.right_leg_1 = this.body.getChild("right_leg_1");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 71).addBox(-4.4954F, -7.7291F, -9.457F, 9.0F, 9.0F, 18.0F, new CubeDeformation(0.25F))
				.texOffs(0, 0).addBox(-4.4954F, -7.7291F, -9.457F, 9.0F, 9.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.0046F, 15.6291F, 0.457F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0046F, -3.5307F, -5.8597F));

		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(56, 14).addBox(-2.9628F, -11.5F, -3.3872F, 7.0F, 16.0F, 7.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-0.5372F, -2.1984F, -0.4601F, 0.1745F, 0.0F, 0.0F));

		PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(28, 27).addBox(-2.0F, -5.0F, -7.5F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 27).addBox(-2.5F, -8.0F, -3.5F, 7.0F, 16.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -5.6528F, -0.981F, 0.1745F, 0.0F, 0.0F));

		PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 50).addBox(-0.25F, -0.75F, -1.25F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(48, 27).addBox(4.25F, -0.75F, -1.25F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -16.7896F, -1.1677F, 0.1745F, 0.0F, 0.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(-0.0326F, -6.6478F, 8.585F));

		PartDefinition cube_r4 = tail.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(52, 3).addBox(-4.5F, -4.5F, 0.0F, 9.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0761F, 1.7173F, 0.3927F, 0.0F, 0.0F));

		PartDefinition left_leg_2 = body.addOrReplaceChild("left_leg_2", CubeListBuilder.create(), PartPose.offsetAndRotation(2.7483F, 1.1817F, 5.6314F, 0.3491F, 0.0F, -0.3927F));

		PartDefinition cube_r5 = left_leg_2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(42, 47).addBox(-5.2128F, -8.0F, -1.9F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.7191F, 6.9891F, 0.4488F, 0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r6 = left_leg_2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(28, 59).addBox(-1.0F, -8.0F, -2.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-0.5064F, 6.9891F, 0.5488F, 0.0873F, 0.0F, 0.0F));

		PartDefinition right_leg_2 = body.addOrReplaceChild("right_leg_2", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.739F, 1.1817F, 5.6314F, 0.3491F, 0.0F, 0.3491F));

		PartDefinition cube_r7 = right_leg_2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(28, 47).addBox(-0.7128F, -8.0F, -1.9F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7936F, 6.9891F, 0.4488F, 0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r8 = right_leg_2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(42, 59).addBox(-1.0F, -8.0F, -2.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-0.4936F, 6.9891F, 0.5488F, 0.0873F, 0.0F, 0.0F));

		PartDefinition left_leg_1 = body.addOrReplaceChild("left_leg_1", CubeListBuilder.create(), PartPose.offsetAndRotation(2.7296F, 1.1366F, -6.537F, -0.3491F, 0.0F, -0.3491F));

		PartDefinition cube_r9 = left_leg_1.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(28, 35).addBox(-5.2128F, -8.0F, -3.2F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.7378F, 7.0343F, 0.6171F, -0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r10 = left_leg_1.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 59).addBox(-1.5F, -4.0F, -2.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-0.025F, 3.0551F, -0.1703F, -0.0873F, 0.0F, 0.0F));

		PartDefinition right_leg_1 = body.addOrReplaceChild("right_leg_1", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.7204F, 1.1366F, -6.537F, -0.3491F, 0.0F, 0.3491F));

		PartDefinition cube_r11 = right_leg_1.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(14, 59).addBox(-1.5F, -4.0F, -2.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.025F, 3.0551F, -0.1703F, -0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r12 = right_leg_1.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(42, 35).addBox(-1.7128F, -8.0F, -3.2F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1878F, 7.0343F, 0.6171F, -0.0873F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(PinataRenderState state) {
		super.setupAnim(state);
	}

//	@Override
//	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//		pinata.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//	}
}