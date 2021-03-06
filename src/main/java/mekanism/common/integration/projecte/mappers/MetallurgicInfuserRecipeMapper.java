package mekanism.common.integration.projecte.mappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mekanism.api.annotations.NonNull;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.recipes.MetallurgicInfuserRecipe;
import mekanism.common.integration.projecte.NSSInfuseType;
import mekanism.common.recipe.MekanismRecipeType;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.mapper.recipe.IRecipeTypeMapper;
import moze_intel.projecte.api.mapper.recipe.RecipeTypeMapper;
import moze_intel.projecte.api.nss.NSSItem;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

@RecipeTypeMapper
public class MetallurgicInfuserRecipeMapper implements IRecipeTypeMapper {

    @Override
    public String getName() {
        return "MekMetallurgicInfuser";
    }

    @Override
    public String getDescription() {
        return "Maps Mekanism metallurgic infuser recipes.";
    }

    @Override
    public boolean canHandle(IRecipeType<?> recipeType) {
        return recipeType == MekanismRecipeType.METALLURGIC_INFUSING;
    }

    @Override
    public boolean handleRecipe(IMappingCollector<NormalizedSimpleStack, Long> mapper, IRecipe<?> iRecipe) {
        if (!(iRecipe instanceof MetallurgicInfuserRecipe)) {
            //Double check that we have a type of recipe we know how to handle
            return false;
        }
        MetallurgicInfuserRecipe recipe = (MetallurgicInfuserRecipe) iRecipe;
        List<@NonNull InfusionStack> infuseTypeRepresentations = recipe.getInfusionInput().getRepresentations();
        List<@NonNull ItemStack> itemRepresentations = recipe.getItemInput().getRepresentations();
        for (InfusionStack infuseTypeRepresentation : infuseTypeRepresentations) {
            NormalizedSimpleStack nssInfuseType = NSSInfuseType.createInfuseType(infuseTypeRepresentation);
            for (ItemStack itemRepresentation : itemRepresentations) {
                Map<NormalizedSimpleStack, Integer> ingredientMap = new HashMap<>();
                ingredientMap.put(nssInfuseType, infuseTypeRepresentation.getAmount());
                ingredientMap.put(NSSItem.createItem(itemRepresentation), itemRepresentation.getCount());
                ItemStack output = recipe.getOutput(infuseTypeRepresentation, itemRepresentation);
                mapper.addConversion(output.getCount(), NSSItem.createItem(output), ingredientMap);
            }
        }
        return true;
    }
}