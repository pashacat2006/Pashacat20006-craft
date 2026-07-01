package com.pashacat2015.haha_mod.quest;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Set;

/**
 * Рецепты стола готовки и фритюрницы заблокированы, пока игрок не получит их из квеста.
 */
public class RecipeUnlockRegistry {

    private static final Set<String> GATED_RECIPES = Set.of(
            "barbequi_from_cookingtable",
            "sandwith_from_cookingtable",
            "ingredient_from_cookingtable",
            "iron_from_skewer",
            "frenchfrice_from_friteuse",
            "egg_from_friteuse",
            "cookingtable_from_friteuse"
    );

    public static boolean requiresUnlock(String recipeId) {
        return GATED_RECIPES.contains(recipeId);
    }

    public static boolean requiresUnlock(ResourceLocation recipeId) {
        return requiresUnlock(recipeId.getPath());
    }

    public static boolean canUseRecipe(ServerPlayer player, ResourceLocation recipeId) {
        if (!requiresUnlock(recipeId)) {
            return true;
        }
        if (player == null) {
            return false;
        }
        return QuestProgress.isRecipeUnlocked(player, recipeId.getPath());
    }
}
