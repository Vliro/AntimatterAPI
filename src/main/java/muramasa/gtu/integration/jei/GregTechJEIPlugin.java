package muramasa.gtu.integration.jei;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import muramasa.gtu.api.data.Guis;
import muramasa.gtu.api.data.Machines;
import muramasa.gtu.api.gui.GuiData;
import muramasa.gtu.api.machines.MachineFlag;
import muramasa.gtu.api.machines.MachineStack;
import muramasa.gtu.api.machines.Tier;
import muramasa.gtu.api.machines.types.Machine;
import muramasa.gtu.api.recipe.Recipe;
import muramasa.gtu.api.recipe.RecipeMap;
import muramasa.gtu.integration.jei.category.RecipeMapCategory;
import muramasa.gtu.integration.jei.wrapper.RecipeWrapper;
import net.minecraft.util.Tuple;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static muramasa.gtu.api.machines.MachineFlag.*;

@JEIPlugin
public class GregTechJEIPlugin implements IModPlugin {

    private static IJeiRuntime runtime;
    private static HashMap<String, Tuple<RecipeMap, GuiData>> REGISTRY = new HashMap<>();

    public static void registerCategory(RecipeMap map, GuiData gui) {
        REGISTRY.put(map.getCategoryId(), new Tuple<>(map, gui));
    }

    @Nullable
    public static Tuple<RecipeMap, GuiData> getRegisteredCategory(String name) {
        return REGISTRY.get(name);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        RecipeMapCategory.setGuiHelper(registry.getJeiHelpers().getGuiHelper());
        for (Machine type : MachineFlag.RECIPE.getTypes()) {
            if (type.hasFlag(BASIC)) {
                if (REGISTRY.containsKey(type.getRecipeMap().getCategoryId())) continue;
                registry.addRecipeCategories(new RecipeMapCategory(Machines.get(type, Tier.LV)));
            } else if (type.hasFlag(MULTI)) {
                if (type.getGui().hasSlots()) {
                    registry.addRecipeCategories(new RecipeMapCategory(Machines.get(type, type.getFirstTier())));
                } else {
                    registry.addRecipeCategories(new RecipeMapCategory(Machines.get(type, type.getFirstTier()), Guis.MULTI_DISPLAY));
                }
            }
        }
        REGISTRY.values().forEach(t -> registry.addRecipeCategories(new RecipeMapCategory(t.getFirst(), t.getSecond())));
    }

    @Override
    public void register(IModRegistry registry) {
        for (Machine type : MachineFlag.RECIPE.getTypes()) {
            registry.addRecipes(type.getRecipeMap().getRecipes(true), type.getRecipeMap().getCategoryId());
            registry.handleRecipes(Recipe.class, RecipeWrapper::new, type.getRecipeMap().getCategoryId());
            for (Tier tier : type.getTiers()) {
                registry.addRecipeCatalyst(new MachineStack(type, tier).asItemStack(), type.getRecipeMap().getCategoryId());
            }
        }
        for (Tuple<RecipeMap, GuiData> pair : REGISTRY.values()) {
            registry.addRecipes(pair.getFirst().getRecipes(true), pair.getFirst().getCategoryId());
            registry.handleRecipes(Recipe.class, RecipeWrapper::new, pair.getFirst().getCategoryId());
        }
    }

    public static void showCategory(Machine... types) {
        if (runtime != null) {
            List<String> list = new LinkedList<>();
            for (int i = 0; i < types.length; i++) {
                if (!types[i].hasFlag(RECIPE)) continue;
                list.add(types[i].getRecipeMap().getCategoryId());
            }
            runtime.getRecipesGui().showCategories(list);
        }
    }
}