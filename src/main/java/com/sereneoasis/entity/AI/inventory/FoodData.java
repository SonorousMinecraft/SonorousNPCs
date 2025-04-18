//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.sereneoasis.entity.AI.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.event.CraftEventFactory;
import org.bukkit.event.entity.EntityExhaustionEvent.ExhaustionReason;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodData {
    public int foodLevel = 20;
    public float saturationLevel = 5.0F;
    public float exhaustionLevel;
    public int saturatedRegenRate = 10;
    public int unsaturatedRegenRate = 80;
    public int starvationRate = 80;
    private int tickTimer;
    private Player entityhuman;
    private int lastFoodLevel = 20;

    public FoodData() {
        throw new AssertionError("Whoopsie, we missed the bukkit.");
    }

    public FoodData(Player entityhuman) {
        Validate.notNull(entityhuman);
        this.entityhuman = entityhuman;
    }

    public void eat(int food, float saturationModifier) {
        this.foodLevel = Math.min(food + this.foodLevel, 20);
        this.saturationLevel = Math.min(this.saturationLevel + (float) food * saturationModifier * 2.0F, (float) this.foodLevel);
    }

    public void eat(Item item, ItemStack stack) {
        if (item.isEdible()) {
            FoodProperties foodinfo = item.getFoodProperties();
            int oldFoodLevel = this.foodLevel;
            FoodLevelChangeEvent event = CraftEventFactory.callFoodLevelChangeEvent(this.entityhuman, foodinfo.getNutrition() + oldFoodLevel, stack);
            if (!event.isCancelled()) {
                this.eat(event.getFoodLevel() - oldFoodLevel, foodinfo.getSaturationModifier());
            }

            // ((ServerPlayer)this.entityhuman).getBukkitEntity().sendHealthUpdate();
        }

    }

    public void tick(Player player) {

        Difficulty enumdifficulty = player.level().getDifficulty();
        this.lastFoodLevel = this.foodLevel;
        if (this.exhaustionLevel > 4.0F) {
            this.exhaustionLevel -= 4.0F;
            if (this.saturationLevel > 0.0F) {
                this.saturationLevel = Math.max(this.saturationLevel - 1.0F, 0.0F);
            } else if (enumdifficulty != Difficulty.PEACEFUL) {
                Bukkit.broadcastMessage("foodLevel should be changing");
                FoodLevelChangeEvent event = CraftEventFactory.callFoodLevelChangeEvent(player, Math.max(this.foodLevel - 1, 0));
                if (!event.isCancelled()) {
                    this.foodLevel = event.getFoodLevel();
                }

//                 ((ServerPlayer)player).connection.send(new ClientboundSetHealthPacket(((ServerPlayer)player).getBukkitEntity().getScaledHealth(), this.foodLevel, this.saturationLevel));
            }
        }

        boolean flag = player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
        if (flag && this.saturationLevel > 0.0F && player.isHurt() && this.foodLevel >= 20) {
            ++this.tickTimer;
            if (this.tickTimer >= this.saturatedRegenRate) {
                float f = Math.min(this.saturationLevel, 6.0F);
                player.heal(f / 6.0F, RegainReason.SATIATED, true);
                player.causeFoodExhaustion(f, ExhaustionReason.REGEN);
                this.tickTimer = 0;
            }
        } else if (flag && this.foodLevel >= 18 && player.isHurt()) {
            ++this.tickTimer;
            if (this.tickTimer >= this.unsaturatedRegenRate) {
                player.heal(1.0F, RegainReason.SATIATED);
                player.causeFoodExhaustion(player.level().spigotConfig.regenExhaustion, ExhaustionReason.REGEN);
                this.tickTimer = 0;
            }
        } else if (this.foodLevel <= 0) {
            ++this.tickTimer;
            if (this.tickTimer >= this.starvationRate) {
                if (player.getHealth() > 10.0F || enumdifficulty == Difficulty.HARD || player.getHealth() > 1.0F && enumdifficulty == Difficulty.NORMAL) {
                    player.hurt(player.damageSources().starve(), 1.0F);
                }

                this.tickTimer = 0;
            }
        } else {
            this.tickTimer = 0;
        }

    }

    public void readAdditionalSaveData(CompoundTag nbt) {
        if (nbt.contains("foodLevel", 99)) {
            this.foodLevel = nbt.getInt("foodLevel");
            this.tickTimer = nbt.getInt("foodTickTimer");
            this.saturationLevel = nbt.getFloat("foodSaturationLevel");
            this.exhaustionLevel = nbt.getFloat("foodExhaustionLevel");
        }

    }

    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt("foodLevel", this.foodLevel);
        nbt.putInt("foodTickTimer", this.tickTimer);
        nbt.putFloat("foodSaturationLevel", this.saturationLevel);
        nbt.putFloat("foodExhaustionLevel", this.exhaustionLevel);
    }

    public int getFoodLevel() {
        return this.foodLevel;
    }

    public void setFoodLevel(int foodLevel) {
        this.foodLevel = foodLevel;
    }

    public int getLastFoodLevel() {
        return this.lastFoodLevel;
    }

    public boolean needsFood() {
        return this.foodLevel < 20;
    }

    public void addExhaustion(float exhaustion) {
        this.exhaustionLevel = Math.min(this.exhaustionLevel + exhaustion, 40.0F);
    }

    public float getExhaustionLevel() {
        return this.exhaustionLevel;
    }

    public float getSaturationLevel() {
        return this.saturationLevel;
    }

    public void setSaturation(float saturationLevel) {
        this.saturationLevel = saturationLevel;
    }

    public void setExhaustion(float exhaustion) {
        this.exhaustionLevel = exhaustion;
    }
}
