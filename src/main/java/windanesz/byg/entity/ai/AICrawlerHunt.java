package windanesz.byg.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import windanesz.byg.entity.EntityCrystalCrawler;

/**
 * Hunting behaviour of the {@link EntityCrystalCrawler}: it prefers to close in over walls and ceilings, and
 * only walks on the ground when there is nothing to climb. On a surface it crawls towards its target, bites
 * when in reach, and leaps at it, both from the ground and off walls and ceilings.
 */
public class AICrawlerHunt extends EntityAIBase {
    private static final int ATTACK_COOLDOWN_TICKS = 20;
    private static final double WALL_LEAP_MIN_RANGE = 2.0D;
    private static final double WALL_LEAP_MAX_RANGE = 9.0D;
    private static final double GROUND_LEAP_MIN_RANGE = 2.0D;
    private static final double GROUND_LEAP_MAX_RANGE = 6.0D;

    private final EntityCrystalCrawler crawler;
    private final double climbSpeed;

    private int attackCooldown;
    private int leapCooldown;
    /** While positive the crawler stays on the ground instead of latching onto walls, after getting stuck on one. */
    private int noClimbTicks;
    private int stuckTicks;
    private double lastX;
    private double lastY;
    private double lastZ;
    private int repathCooldown;
    private BlockPos perch;
    private int perchCooldown;

    public AICrawlerHunt(EntityCrystalCrawler crawler, double climbSpeed) {
        this.crawler = crawler;
        this.climbSpeed = climbSpeed;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase target = this.crawler.getAttackTarget();
        return target != null && target.isEntityAlive();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.shouldExecute();
    }

    @Override
    public void startExecuting() {
        this.repathCooldown = 0;
        this.perch = null;
        this.perchCooldown = 0;
        this.noClimbTicks = 0;
        this.stuckTicks = 0;
    }

    @Override
    public void resetTask() {
        this.crawler.getNavigator().clearPath();
        this.perch = null;
        // Not releasing the surface: the idle climbing task picks up from exactly where this leaves off.
    }

    @Override
    public void updateTask() {
        EntityLivingBase target = this.crawler.getAttackTarget();
        if (target == null) {
            return;
        }
        this.crawler.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        if (this.attackCooldown > 0) {
            this.attackCooldown--;
        }
        if (this.leapCooldown > 0) {
            this.leapCooldown--;
        }
        if (this.noClimbTicks > 0) {
            this.noClimbTicks--;
        }

        EnumFacing facing = this.crawler.getClimbFacing();
        if (facing != EnumFacing.UP) {
            this.onSurface(target, facing);
        } else {
            this.onGround(target);
        }
    }

    private void bite(EntityLivingBase target) {
        if (this.attackCooldown <= 0 && this.crawler.isInBiteRange(target)) {
            this.crawler.swingArm(EnumHand.MAIN_HAND);
            this.crawler.attackEntityAsMob(target);
            this.attackCooldown = ATTACK_COOLDOWN_TICKS;
        }
    }

    private void onGround(EntityLivingBase target) {
        this.bite(target);

        // The usual spider leap: a springing jump at a target a few blocks away.
        double distance = this.crawler.getDistance(target);
        if (this.crawler.onGround && this.leapCooldown <= 0 && distance >= GROUND_LEAP_MIN_RANGE
                && distance <= GROUND_LEAP_MAX_RANGE && this.crawler.getRNG().nextInt(4) == 0
                && this.crawler.getEntitySenses().canSee(target)) {
            this.leapAt(target);
            return;
        }

        // Prefer climbing: latch onto any wall at hand unless the target is right there to bite.
        EnumFacing wall = this.crawler.findAdjacentWall();
        if (wall != null && this.noClimbTicks <= 0 && this.crawler.onGround && !this.crawler.isInBiteRange(target)) {
            this.crawler.getNavigator().clearPath();
            this.crawler.setClimbFacing(wall);
            return;
        }

        if (this.repathCooldown-- <= 0) {
            this.repathCooldown = 10;
            boolean pathing = this.crawler.getNavigator().tryMoveToEntityLiving(target, 1.2D);
            if (!pathing) {
                // Unreachable on foot (a ledge, a plateau): head for a wall to climb instead.
                if (this.perchCooldown-- <= 0 || this.perch == null) {
                    this.perch = EntityCrystalCrawler.findNearbyWallPerch(this.crawler.world, this.crawler.getPosition(), 1, 8);
                    this.perchCooldown = 20;
                }
                if (this.perch != null) {
                    this.crawler.getNavigator().tryMoveToXYZ(this.perch.getX() + 0.5D, this.perch.getY(), this.perch.getZ() + 0.5D, 1.2D);
                }
            }
        }
    }

    private void onSurface(EntityLivingBase target, EnumFacing facing) {
        BlockPos goal = target.getPosition();

        // On a wall with a ceiling overhead and the target above, hop onto the ceiling.
        if (facing.getAxis() != EnumFacing.Axis.Y && goal.getY() > this.crawler.posY && this.crawler.hasClimbableCeilingAbove()) {
            facing = EnumFacing.DOWN;
        }

        EnumFacing support = this.crawler.findSupportingFace(facing);
        if (support == null) {
            // lost its grip: back to ordinary physics
            this.crawler.releaseSurface();
            return;
        }
        if (support == EnumFacing.UP) {
            // reached the flat top of whatever it was climbing: step over the edge onto it
            this.crawler.climbOver(this.crawler.getClimbFacing());
            return;
        }
        this.crawler.setClimbFacing(support);

        this.bite(target);

        // Launch off the wall or ceiling at a target in sight, wherever it is relative to the crawler.
        double distance = this.crawler.getDistance(target);
        if (this.leapCooldown <= 0 && distance >= WALL_LEAP_MIN_RANGE && distance <= WALL_LEAP_MAX_RANGE
                && this.crawler.getEntitySenses().canSee(target)) {
            this.leapAt(target);
            return;
        }

        this.crawler.tickClimbMovement(support, goal, this.climbSpeed);
        this.watchForStall(target);
    }

    /**
     * Crawling towards a target that is round a corner or behind the edge of the wall can pin the crawler
     * against the same spot. If it has barely moved for a second and a half without reaching the target, it
     * lets go and approaches over the ground for a while instead of hanging there.
     */
    private void watchForStall(EntityLivingBase target) {
        double moved = Math.abs(this.crawler.posX - this.lastX) + Math.abs(this.crawler.posY - this.lastY)
                + Math.abs(this.crawler.posZ - this.lastZ);
        this.lastX = this.crawler.posX;
        this.lastY = this.crawler.posY;
        this.lastZ = this.crawler.posZ;
        if (moved < 0.02D && !this.crawler.isInBiteRange(target)) {
            if (++this.stuckTicks > 30) {
                this.stuckTicks = 0;
                this.noClimbTicks = 80;
                this.crawler.releaseSurface();
            }
        } else {
            this.stuckTicks = 0;
        }
    }

    /**
     * Launches the crawler along an arc that lands on the target: the flight time is picked from the distance,
     * then the horizontal and vertical launch speeds are solved for it, allowing for gravity and air drag.
     * Works from the ground as well as from a wall or ceiling.
     */
    private void leapAt(EntityLivingBase target) {
        double dx = target.posX - this.crawler.posX;
        double dz = target.posZ - this.crawler.posZ;
        double dy = (target.posY + target.height * 0.5D) - (this.crawler.posY + this.crawler.height * 0.5D);
        double horizontal = Math.sqrt(dx * dx + dz * dz);

        int ticks = Math.max(6, Math.min(22, (int) (5.0D + horizontal * 1.6D)));
        double horizontalSum = 0.0D;
        double verticalDrive = 0.0D;
        double gravityDrop = 0.0D;
        double airDrag = 1.0D;
        double liftDecay = 1.0D;
        double gravitySpeed = 0.0D;
        for (int i = 0; i < ticks; i++) {
            horizontalSum += airDrag;
            airDrag *= 0.91D;
            verticalDrive += liftDecay;
            liftDecay *= 0.98D;
            gravityDrop += gravitySpeed;
            gravitySpeed = (gravitySpeed - 0.08D) * 0.98D;
        }

        this.crawler.releaseSurface();
        if (horizontal > 1.0E-4D) {
            double speed = Math.min(0.75D, horizontal / horizontalSum);
            this.crawler.motionX = dx / horizontal * speed;
            this.crawler.motionZ = dz / horizontal * speed;
        }
        this.crawler.motionY = Math.max(-0.5D, Math.min(0.75D, (dy - gravityDrop) / verticalDrive));
        this.leapCooldown = 40 + this.crawler.getRNG().nextInt(40);
    }
}
