//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.sereneoasis.entity.AI.control;

import com.sereneoasis.entity.SereneHumanEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.Control;

public class BodyRotationControl implements Control {
    private static final int HEAD_STABLE_ANGLE = 15;
    private static final int DELAY_UNTIL_STARTING_TO_FACE_FORWARD = 10;
    private static final int HOW_LONG_IT_TAKES_TO_FACE_FORWARD = 10;
    private final SereneHumanEntity mob;
    private int headStableTime;
    private float lastStableYHeadRot;

    public BodyRotationControl(SereneHumanEntity entity) {
        this.mob = entity;
    }

    public void clientTick() {
        if (this.isMoving()) {
            this.mob.yBodyRot = this.mob.getYRot();
            this.rotateHeadIfNecessary();
            this.lastStableYHeadRot = this.mob.yHeadRot;
            this.headStableTime = 0;
        } else {
            if (this.notCarryingHumanEntityPassengers()) {
                if (Math.abs(this.mob.yHeadRot - this.lastStableYHeadRot) > 15.0F) {
                    this.headStableTime = 0;
                    this.lastStableYHeadRot = this.mob.yHeadRot;
                    this.rotateBodyIfNecessary();
                } else {
                    ++this.headStableTime;
                    if (this.headStableTime > 10) {
                        this.rotateHeadTowardsFront();
                    }
                }
            }

        }
    }

    private void rotateBodyIfNecessary() {
        this.mob.yBodyRot = Mth.rotateIfNecessary(this.mob.yBodyRot, this.mob.yHeadRot, (float) this.mob.getMaxHeadYRot());
    }

    private void rotateHeadIfNecessary() {
        this.mob.yHeadRot = Mth.rotateIfNecessary(this.mob.yHeadRot, this.mob.yBodyRot, (float) this.mob.getMaxHeadYRot());
    }

    private void rotateHeadTowardsFront() {
        int i = this.headStableTime - 10;
        float f = Mth.clamp((float) i / 10.0F, 0.0F, 1.0F);
        float g = (float) this.mob.getMaxHeadYRot() * (1.0F - f);
        this.mob.yBodyRot = Mth.rotateIfNecessary(this.mob.yBodyRot, this.mob.yHeadRot, g);
    }

    private boolean notCarryingHumanEntityPassengers() {
        return !(this.mob.getFirstPassenger() instanceof SereneHumanEntity);
    }

    private boolean isMoving() {
        double d = this.mob.getX() - this.mob.xo;
        double e = this.mob.getZ() - this.mob.zo;
        return d * d + e * e > 2.500000277905201E-7;
    }
}
