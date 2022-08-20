package com.github.iunius118.testcapsmod;

import com.github.iunius118.testcapsapi.api.capabilities.ITestCounter;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

@Mod(TestCapsMod.MODID)
public class TestCapsMod {
    public static final String MODID = "testcapsmod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Capability<ITestCounter> TEST_COUNTER = CapabilityManager.get(new CapabilityToken<>(){});
    private static final ResourceLocation TEST_COUNTER_CAP_ID = new ResourceLocation(MODID, "testcapsapi");

    public TestCapsMod() {
        // Register listener of AttachCapabilitiesEvent<ItemStack>
        MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, this::attachCapability);
    }

    public void attachCapability(AttachCapabilitiesEvent<ItemStack> event) {
        // Attach capability to ItemStack
        event.addCapability(TEST_COUNTER_CAP_ID, new TestCounter.Provider());
    }

    public static class TestCounter implements ITestCounter {
        private int count = 0;

        @Override
        public void increase() {
            count++;
        }

        @Override
        public void increase(int amount) {
            count += amount;
        }

        @Override
        public void decrease() {
            count--;
        }

        @Override
        public void decrease(int amount) {
            count -= amount;
        }

        @Override
        public void set(int number) {
            count = number;
        }

        @Override
        public int get() {
            return count;
        }

        public static class Provider implements ICapabilitySerializable<Tag> {
            protected final LazyOptional<ITestCounter> instance = LazyOptional.of(TestCounter::new);

            @Override
            public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                return TEST_COUNTER.orEmpty(cap, instance);
            }

            private static final String TAG_KET_COUNT = "count";

            @Override
            public Tag serializeNBT() {
                CompoundTag tag = new CompoundTag();
                ITestCounter testCounter = instance.orElseGet(TestCounter::new);
                tag.putInt(TAG_KET_COUNT, testCounter.get());
                return tag;
            }

            @Override
            public void deserializeNBT(Tag nbt) {
                if (!(nbt instanceof CompoundTag tag))
                    return;

                ITestCounter testCounter = instance.orElseGet(TestCounter::new);
                testCounter.set(tag.getInt(TAG_KET_COUNT));
            }
        }
    }
}
