package mekanism.api.recipes;

import java.util.Collection;
import java.util.Collections;
import java.util.function.BiPredicate;
import javax.annotation.ParametersAreNonnullByDefault;
import mcp.MethodsReturnNonnullByDefault;
import mekanism.api.annotations.NonNull;
import mekanism.api.gas.GasStack;
import mekanism.api.recipes.inputs.GasStackIngredient;
import mekanism.api.recipes.outputs.OreDictSupplier;
import mekanism.common.util.FieldsAreNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

/**
 * Inputs: ItemStack + GasStack
 * Output: ItemStack
 *
 * Ex-AdvancedMachineInput based; InjectionRecipe, OsmiumCompressorRecipe, PurificationRecipe
 */
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemStackGasToItemStackRecipe implements BiPredicate<@NonNull ItemStack, @NonNull GasStack>, OutputDefinition<ItemStack> {

    private final Ingredient itemInput;

    private final GasStackIngredient gasInput;

    private final ItemStack outputDefinition;

    public ItemStackGasToItemStackRecipe(Ingredient itemInput, GasStackIngredient gasInput, ItemStack outputDefinition) {
        this.itemInput = itemInput;
        this.gasInput = gasInput;
        this.outputDefinition = outputDefinition;
    }

    public Ingredient getItemInput() {
        return itemInput;
    }

    public GasStackIngredient getGasInput() {
        return gasInput;
    }

    public ItemStack getOutput(ItemStack inputItem, GasStack inputGas) {
        return outputDefinition.copy();
    }

    @Override
    public boolean test(@NonNull ItemStack itemStack, @NonNull GasStack gasStack) {
        return itemInput.apply(itemStack) && gasInput.test(gasStack);
    }

    @Override
    public @NonNull Collection<@NonNull ItemStack> getOutputDefinition() {
        return Collections.singleton(outputDefinition);
    }

    public static class ItemStackGasToItemStackRecipeOre extends ItemStackGasToItemStackRecipe {

        private final OreDictSupplier outputSupplier;

        public ItemStackGasToItemStackRecipeOre(Ingredient itemInput, GasStackIngredient gasInput, String outputOreName) {
            super(itemInput, gasInput, ItemStack.EMPTY);
            this.outputSupplier = new OreDictSupplier(outputOreName);
        }

        @Override
        public ItemStack getOutput(ItemStack inputItem, GasStack inputGas) {
            return this.outputSupplier.get();
        }

        @Override
        public @NonNull Collection<@NonNull ItemStack> getOutputDefinition() {
            return this.outputSupplier.getPossibleOutputs();
        }
    }
}