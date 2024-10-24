package skyveo.foodpouch.component;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import skyveo.foodpouch.FoodPouch;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
//    public static final ComponentType<BundleContentsComponent> BUNDLE_CONTENTS = register("bundle_contents", builder -> builder.build());

    public static <T>ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, FoodPouch.id(name), builderOperator.apply(ComponentType.builder()).build());
    }

    public static void load() {

    }
}
