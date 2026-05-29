package it.hurts.shatterbyte.pinatafiesta.client.entity;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class PinataRenderState extends LivingEntityRenderState {
    public float hitXOffset;
    public float hitZOffset;
    public float hitXRotation;
    public float hitZRotation;
    public float hitYScale = 1.0F;
    public Identifier texture;
}
