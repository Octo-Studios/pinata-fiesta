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
	private final ModelPart pinata;

	public PinataModel(ModelPart root) {
        super(root);
        this.pinata = root.getChild("pinata");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition pinata = partdefinition.addOrReplaceChild("pinata", CubeListBuilder.create().texOffs(0, 0).addBox(-4.4628F, -4.6784F, -7.0F, 9.0F, 9.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(0, 71).addBox(-4.4628F, -4.6784F, -7.0F, 9.0F, 9.0F, 18.0F, new CubeDeformation(0.25F)), PartPose.offset(-0.0372F, 12.5784F, -2.0F));

		PartDefinition cube_r1 = pinata.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(42, 59).addBox(-1.0F, -8.0F, -2.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.25F))
		.texOffs(28, 59).addBox(4.4745F, -8.0F, -2.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-3.2F, 11.2216F, 8.6372F, 0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r2 = pinata.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(14, 59).addBox(-1.5F, -4.1F, -2.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-2.6628F, 7.2424F, -4.2502F, -0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r3 = pinata.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 59).addBox(-1.5F, -4.1F, -2.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(2.7372F, 7.2424F, -4.2502F, -0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r4 = pinata.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(56, 14).addBox(-2.9628F, -11.5F, -3.3872F, 7.0F, 16.0F, 7.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-0.5F, -2.6784F, -3.8628F, 0.1745F, 0.0F, 0.0F));

		PartDefinition cube_r5 = pinata.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(28, 27).addBox(-2.0F, -5.0F, -7.5F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.9628F, -6.1328F, -4.3837F, 0.1745F, 0.0F, 0.0F));

		PartDefinition cube_r6 = pinata.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 50).addBox(-0.25F, -0.75F, -1.25F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(48, 27).addBox(4.25F, -0.75F, -1.25F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.9628F, -17.2696F, -4.5704F, 0.1745F, 0.0F, 0.0F));

		PartDefinition cube_r7 = pinata.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(42, 47).addBox(-5.2128F, -8.0F, -1.9F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(28, 47).addBox(-10.7128F, -8.0F, -1.9F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, 11.2216F, 8.5372F, 0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r8 = pinata.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(42, 35).addBox(-1.7128F, -8.0F, -3.2F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(28, 35).addBox(3.7872F, -8.0F, -3.2F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 11.2216F, -3.4628F, -0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r9 = pinata.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 27).addBox(-3.5F, -8.0F, -3.5F, 7.0F, 16.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0372F, -6.1328F, -4.3837F, 0.1745F, 0.0F, 0.0F));

		PartDefinition cube_r10 = pinata.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(52, 3).addBox(-4.5F, -4.5F, 0.0F, 9.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.4791F, 12.7593F, 0.3927F, 0.0F, 0.0F));

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