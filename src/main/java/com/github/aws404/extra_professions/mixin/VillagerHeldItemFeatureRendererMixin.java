package com.github.aws404.extra_professions.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.VillagerHeldItemFeatureRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

@Mixin(VillagerHeldItemFeatureRenderer.class)
public class VillagerHeldItemFeatureRendererMixin<T extends LivingEntity> {

    @Shadow @Final private HeldItemRenderer heldItemRenderer;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", shift = At.Shift.BEFORE, by = 1), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void renderAxes(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float limbAngle, float limbDistance, float tickDelta, float j, float k, float l, CallbackInfo ci, ItemStack itemStack) {
        if (itemStack.getItem() instanceof AxeItem) {
            float m = livingEntity.handSwingProgress < 0.4 ? -6.0F : -3.0F;
            float delta = Math.max(0.0F, m * (float) Math.pow(livingEntity.handSwingProgress - 0.4F, 2) + 1.0F);
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(MathHelper.lerp(delta, 0.0F, 60.0F)));
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
            this.heldItemRenderer.renderItem(livingEntity, itemStack, ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND, false, matrixStack, vertexConsumerProvider, i);
            matrixStack.pop();

            ci.cancel();
        }
    }
}
