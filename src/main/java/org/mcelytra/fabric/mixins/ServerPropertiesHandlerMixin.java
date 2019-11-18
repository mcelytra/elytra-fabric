package org.mcelytra.fabric.mixins;

import com.google.common.base.MoreObjects;
import net.minecraft.server.dedicated.AbstractPropertiesHandler;
import net.minecraft.world.level.LevelGeneratorType;
import org.mcelytra.fabric.ElytraEntrypoint;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Properties;
import java.util.function.Function;

// @TODO remove this and make a proper implementation and replace some of the server.properties by the server_config.toml
@Mixin(AbstractPropertiesHandler.class)
public abstract class ServerPropertiesHandlerMixin
{
    @Shadow
    @Nullable
    protected abstract String getStringValue(String string);

    @Shadow @Final private Properties properties;

    @Inject(method = "get", at = @At(value = "HEAD"), cancellable = true)
    private void on_get(String string, Function<String, ?> function, Function<Object, String> function2, Object object, CallbackInfoReturnable<Object> cir)
    {
        if (string.equals("level-type")) {
            function = ElytraEntrypoint::get_generator_from_name;
            String string2 = this.getStringValue(string);
            LevelGeneratorType object2 = (LevelGeneratorType) MoreObjects.firstNonNull(string2 != null ? function.apply(string2) : null, object);
            this.properties.put(string, function2.apply(object2));
            cir.setReturnValue(object2);
            cir.cancel();
        }
    }
}
