package com.pashacat2015.haha_mod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pashacat2015.haha_mod.cat_mod;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class FriteuseRecipe implements Recipe<SimpleContainer> {
    private static final int SLOT_COUNT = 3;

    private final NonNullList<Ingredient> inpuItem;
    private final NonNullList<Integer> ingredientCounts;
    private final ItemStack output;
    private final ResourceLocation id;

    private FriteuseRecipe(NonNullList<Ingredient> inpuItem, NonNullList<Integer> ingredientCounts, ItemStack output, ResourceLocation id) {
        this.inpuItem = inpuItem;
        this.ingredientCounts = ingredientCounts;
        this.output = output;
        this.id = id;
    }

    /** Сколько предметов нужно в слоте (1 по умолчанию) */
    public int getIngredientCount(int slot) {
        return ingredientCounts.get(slot);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inpuItem;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        for (int i = 0; i < INPUT_SLOTS; i++) {
            Ingredient ingredient = inpuItem.get(i);
            if (ingredient.isEmpty()) {
                continue;
            }
            ItemStack stack = container.getItem(i);
            if (!ingredient.test(stack) || stack.getCount() < ingredientCounts.get(i)) {
                return false;
            }
        }
        return true;
    }

    private static final int INPUT_SLOTS = 2;
    private static final int OUTPUT_SLOT = 2;

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer, RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipreMod.FRITEUSE_SERIALEZATOR.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipreMod.FRITEUSE_TYPE.get();
    }
    public static class Type implements RecipeType<FriteuseRecipe> {
        public static final ResourceLocation ID = new ResourceLocation(cat_mod.MODID, "friteuse");
        public static final Type INSTANCE = new Type();
    }

    public static class Serializer implements RecipeSerializer<FriteuseRecipe> {
        public static final Serializer INSTATE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(cat_mod.MODID,"friteuse");

        @Override
        public FriteuseRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "output"));
            JsonArray ingredients = GsonHelper.getAsJsonArray(jsonObject, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(SLOT_COUNT, Ingredient.EMPTY);
            NonNullList<Integer> counts = NonNullList.withSize(SLOT_COUNT, 0);

            for (int i = 0; i < ingredients.size(); i++) {
                JsonObject entry = ingredients.get(i).getAsJsonObject();
                // "slot" — номер слота; без него индекс в массиве = слот (старый формат)
                int slot = entry.has("slot") ? GsonHelper.getAsInt(entry, "slot") : i;
                if (slot < 0 || slot >= INPUT_SLOTS) {
                    continue;
                }
                inputs.set(slot, parseIngredient(entry));
                counts.set(slot, GsonHelper.getAsInt(entry, "count", 1));
            }
            return new FriteuseRecipe(inputs, counts, output, resourceLocation);
        }

        @Override
        public @Nullable FriteuseRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(SLOT_COUNT, Ingredient.EMPTY);
            NonNullList<Integer> counts = NonNullList.withSize(SLOT_COUNT, 0);
            for (int i = 0; i < SLOT_COUNT; i++) {
                inputs.set(i, Ingredient.fromNetwork(friendlyByteBuf));
                counts.set(i, friendlyByteBuf.readVarInt());
            }
            ItemStack output = friendlyByteBuf.readItem();
            return new FriteuseRecipe(inputs, counts, output, resourceLocation);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, FriteuseRecipe cookingtableRecipe) {
            for (int i = 0; i < SLOT_COUNT; i++) {
                cookingtableRecipe.inpuItem.get(i).toNetwork(friendlyByteBuf);
                friendlyByteBuf.writeVarInt(cookingtableRecipe.ingredientCounts.get(i));
            }
            friendlyByteBuf.writeItem(cookingtableRecipe.getResultItem(null));
        }

        private static Ingredient parseIngredient(JsonObject entry) {
            JsonObject ingredientJson = entry.deepCopy();
            ingredientJson.remove("slot");
            ingredientJson.remove("count");
            return Ingredient.fromJson(ingredientJson);
        }
    }
}
