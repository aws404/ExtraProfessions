package com.github.aws404.extra_professions.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

@Mixin(CookingRecipeSerializer.class)
public class CookingRecipeSerializerMixin {

    @Inject(method = "read(Lnet/minecraft/util/Identifier;Lcom/google/gson/JsonObject;)Lnet/minecraft/recipe/AbstractCookingRecipe;", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void allowCountsInRecipeJson(Identifier identifier, JsonObject jsonObject, CallbackInfoReturnable<AbstractCookingRecipe> cir, String string, JsonElement jsonElement, Ingredient ingredient, String string2, Identifier identifier2, ItemStack output, float f, int i) {
        output.setCount(JsonHelper.getInt(jsonObject, "count", 1));
    }

    @Inject(method = "read(Lnet/minecraft/util/Identifier;Lnet/minecraft/network/PacketByteBuf;)Lnet/minecraft/recipe/AbstractCookingRecipe;", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void allowCountsInRecipePacket(Identifier identifier, PacketByteBuf packetByteBuf, CallbackInfoReturnable<AbstractCookingRecipe> cir, String string, Ingredient ingredient, ItemStack output, float f, int i) {
        output.setCount(packetByteBuf.readVarInt());
    }

    @Inject(method = "write", at = @At(value = "RETURN"))
    private void allowCountsInRecipePacket(PacketByteBuf packetByteBuf, AbstractCookingRecipe abstractCookingRecipe, CallbackInfo ci) {
        packetByteBuf.writeInt(abstractCookingRecipe.getOutput().getCount());
    }
}
