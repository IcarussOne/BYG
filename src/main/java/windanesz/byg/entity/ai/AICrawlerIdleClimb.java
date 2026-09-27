package windanesz.byg.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import windanesz.byg.entity.EntityCrystalCrawler;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Idle behaviour of the {@link EntityCrystalCrawler}: with no target it alternates between short walks and
 * long spells clinging to and crawling over nearby walls and ceilings. Compared with a plain wanderer it looks
 * for a wall very often and stays up for a long time, so it is usually found on the rock, not on the ground.
 */
public class AICrawlerIdleClimb extends EntityAIBase {
    private static final int MIN_CLING_TICKS = 200;
    private static final int MAX_CLING_TICKS = 500;
    private static final int MIN_WALL_SEARCH_COOLDOWN = 20;
    private static final int MAX_WALL_SEARCH_COOLDOWN = 80;
    private static final int WALL_SEARCH_MIN_RADIUS = 1;
    private static final int WALL_SEARCH_MAX_RADIUS = 10;
    private static final int APPROACH_GIVE_UP_TICKS = 100;
    private static final int CEILING_SEEK_MAX_HEIGHT = 8;
    private static final int WALL_TOP_SEEK_MAX_HEIGHT = 12;
    private static final int GO_OVER_PERCENT = 50;
    private static final int SURFACE_WANDER_RANGE = 3;
    private static final int MIN_SURFACE_WANDER_COOLDOWN = 60;
    private static final int MAX_SURFACE_WANDER_COOLDOWN = 140;
    private static final int CEILING_WALL_SEARCH_RANGE = 16;

    private enum Phase {GROUND, APPROACHING, CLINGING}

    private final EntityCrystalCrawler crawler;
    private final double climbSpeed;

    private Phase phase = Phase.GROUND;
    private BlockPos wallPerch;
    private int wallSearchCooldown;
    private int repathCooldown;
    private int approachTicks;
    private int clingTicks;
    private boolean descending;
    // Whether this cling ends by crawling over the top of the wall
    private boolean goOver;
    private BlockPos wanderGoal;
    private int wanderCooldown;

    public AICrawlerIdleClimb(EntityCrystalCrawler crawler, double climbSpeed) {
        this.crawler = crawler;
        this.climbSpeed = climbSpeed;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        return this.crawler.getAttackTarget() == null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.shouldExecute();
    }

    @Override
    public void startExecuting() {
        if (this.crawler.isClimbing()) {
            this.beginClinging(this.crawler.getClimbFacing());
        } else {
            this.phase = Phase.GROUND;
            this.wallPerch = null;
            this.wallSearchCooldown = 0;
        }
    }

    @Override
    public void resetTask() {
        this.crawler.setMayCrossEdges(false);
        this.crawler.getNavigator().clearPath();
        this.wallPerch = null;
        this.wanderGoal = null;
    }

    @Override
    public void updateTask() {
        switch (this.phase) {
            case CLINGING:
                this.handleClinging();
                break;
            case APPROACHING:
                this.handleApproaching();
                break;
            default:
                this.handleGround();
                break;
        }
    }

    private void handleGround() {
        // Latch on straight away if a wall is already right there.
        EnumFacing wall = this.wallSearchCooldown <= 0 ? this.crawler.findAdjacentWall() : null;
        if (wall != null) {
            this.beginClinging(wall);
            return;
        }

        if (this.wallSearchCooldown-- <= 0) {
            this.wallSearchCooldown = MIN_WALL_SEARCH_COOLDOWN + this.crawler.getRNG().nextInt(MAX_WALL_SEARCH_COOLDOWN - MIN_WALL_SEARCH_COOLDOWN);
            this.wallPerch = EntityCrystalCrawler.findNearbyWallPerch(this.crawler.world, this.crawler.getPosition(), WALL_SEARCH_MIN_RADIUS, WALL_SEARCH_MAX_RADIUS);
            if (this.wallPerch != null) {
                this.approachTicks = 0;
                this.repathCooldown = 0;
                this.phase = Phase.APPROACHING;
                return;
            }
        }

        if (this.crawler.getNavigator().noPath() && this.crawler.getRNG().nextInt(60) == 0) {
            Vec3d wander = RandomPositionGenerator.findRandomTarget(this.crawler, 10, 7);
            if (wander != null) {
                this.crawler.getNavigator().tryMoveToXYZ(wander.x, wander.y, wander.z, 1.0D);
            }
        }
    }

    private void handleApproaching() {
        EnumFacing wall = this.crawler.findAdjacentWall();
        if (wall != null) {
            this.beginClinging(wall);
            return;
        }

        // The wall may be unreachable or get mined away: give up rather than stalling on it forever.
        if (++this.approachTicks > APPROACH_GIVE_UP_TICKS) {
            this.wallPerch = null;
            this.phase = Phase.GROUND;
            return;
        }

        if (this.crawler.getNavigator().noPath() || this.repathCooldown-- <= 0) {
            this.crawler.getNavigator().tryMoveToXYZ(this.wallPerch.getX() + 0.5D, this.wallPerch.getY(), this.wallPerch.getZ() + 0.5D, 1.0D);
            this.repathCooldown = 20;
        }
    }

    private void handleClinging() {
        if (!this.descending && --this.clingTicks <= 0) {
            this.descending = true;
            this.wanderGoal = null;
            this.wanderCooldown = 0;
        }

        // Hop onto a ceiling the moment one is directly overhead.
        EnumFacing facing = this.crawler.getClimbFacing();
        if (this.descending && facing == EnumFacing.DOWN) {
            EnumFacing wall = this.crawler.findAdjacentWall();
            if (wall != null) {
                facing = wall;
            }
        } else if (!this.descending && facing.getAxis().isHorizontal() && this.crawler.hasClimbableCeilingAbove()) {
            facing = EnumFacing.DOWN;
        }

        EnumFacing support = this.crawler.findSupportingFace(facing);
        if (support == null || support == EnumFacing.UP) {
            // lost its grip, or came over the top of what it was climbing: step onto it, back to ground physics
            if (support == EnumFacing.UP) {
                this.crawler.climbOver(this.crawler.getClimbFacing());
            }
            this.finishClinging();
            return;
        }
        if (support != this.crawler.getClimbFacing()) {
            this.wanderGoal = null;
            this.wanderCooldown = 0;
        }
        this.crawler.setClimbFacing(support);

        if (this.descending) {
            this.crawler.setMayCrossEdges(false);
            if (support.getAxis().isHorizontal()) {
                if (this.crawler.onGround) {
                    this.finishClinging();
                    return;
                }
                this.crawler.tickClimbMovement(support, this.crawler.getPosition().down(), this.climbSpeed);
            } else {
                if (this.wanderCooldown-- <= 0 || this.wanderGoal != null && this.crawler.getCenterPos().equals(this.wanderGoal)) {
                    this.wanderGoal = this.findCeilingStepToWall();
                    this.wanderCooldown = 20;
                }
                // An isolated ceiling has no safe way down: keep holding on and look again later.
                this.crawler.tickClimbMovement(support, this.wanderGoal, this.climbSpeed);
            }
            return;
        }

        BlockPos goal = null;
        if (support.getAxis() != EnumFacing.Axis.Y) {
            // Only make for a ceiling if the wall actually reaches that high, or it would climb off the top.
            BlockPos ceiling = EntityCrystalCrawler.findCeilingPerch(this.crawler.world, this.crawler.getPosition(), 1, CEILING_SEEK_MAX_HEIGHT);
            if (ceiling != null && EntityCrystalCrawler.isClimbableSurface(this.crawler.world, ceiling.offset(support.getOpposite()))) {
                goal = ceiling;
            }
        }

        boolean headingOver = false;
        if (goal == null && this.goOver && support.getAxis() != EnumFacing.Axis.Y) {
            goal = this.crawler.findWallTop(support, WALL_TOP_SEEK_MAX_HEIGHT);
            headingOver = goal != null;
            if (goal == null) {
                this.goOver = false;
            }
        }

        // Only while heading for the top, otherwise an idle crawler must not be pushed off edges
        this.crawler.setMayCrossEdges(headingOver);

        if (goal == null) {
            // Wander over the surface instead of holding perfectly still.
            if (this.wanderGoal == null || this.wanderCooldown-- <= 0) {
                this.wanderGoal = this.findSurfaceWanderGoal(support);
                this.wanderCooldown = MIN_SURFACE_WANDER_COOLDOWN + this.crawler.getRNG().nextInt(MAX_SURFACE_WANDER_COOLDOWN - MIN_SURFACE_WANDER_COOLDOWN);
            }
            goal = this.wanderGoal;
        } else {
            this.wanderGoal = null;
        }

        this.crawler.tickClimbMovement(support, goal, this.climbSpeed);
    }

    private void finishClinging() {
        this.crawler.setMayCrossEdges(false);
        this.crawler.releaseSurface();
        this.crawler.motionX = this.crawler.motionY = this.crawler.motionZ = 0.0D;
        this.phase = Phase.GROUND;
        this.wanderGoal = null;
        this.wallSearchCooldown = MIN_WALL_SEARCH_COOLDOWN + this.crawler.getRNG().nextInt(MAX_WALL_SEARCH_COOLDOWN - MIN_WALL_SEARCH_COOLDOWN);
    }

    /** Breadth-first search over connected ceiling cells, keeping the first step so the route cannot cut across holes. */
    private BlockPos findCeilingStepToWall() {
        BlockPos start = this.crawler.getCenterPos();
        ArrayDeque<BlockPos> pending = new ArrayDeque<>();
        Map<BlockPos, BlockPos> firstSteps = new HashMap<>();
        pending.add(start);
        firstSteps.put(start, null);
        while (!pending.isEmpty()) {
            BlockPos current = pending.removeFirst();
            for (EnumFacing direction : EnumFacing.HORIZONTALS) {
                BlockPos next = current.offset(direction);
                if (EntityCrystalCrawler.isClimbableSurface(this.crawler.world, next)) {
                    return firstSteps.get(current);
                }
                if (Math.abs(next.getX() - start.getX()) > CEILING_WALL_SEARCH_RANGE
                        || Math.abs(next.getZ() - start.getZ()) > CEILING_WALL_SEARCH_RANGE
                        || firstSteps.containsKey(next) || !EntityCrystalCrawler.isClimbableSurface(this.crawler.world, next.up())) {
                    continue;
                }
                firstSteps.put(next, current.equals(start) ? next : firstSteps.get(current));
                pending.addLast(next);
            }
        }
        return null;
    }

    /** A random nearby open point on the plane of the surface, still backed by a climbable block. */
    private BlockPos findSurfaceWanderGoal(EnumFacing facing) {
        BlockPos center = this.crawler.getCenterPos();
        Random rand = this.crawler.getRNG();
        for (int attempt = 0; attempt < 4; attempt++) {
            int a = rand.nextInt(SURFACE_WANDER_RANGE * 2 + 1) - SURFACE_WANDER_RANGE;
            int b = rand.nextInt(SURFACE_WANDER_RANGE * 2 + 1) - SURFACE_WANDER_RANGE;
            BlockPos candidate = facing.getAxis() == EnumFacing.Axis.X ? center.add(0, a, b)
                    : facing.getAxis() == EnumFacing.Axis.Z ? center.add(a, b, 0)
                    : center.add(a, 0, b);
            if (EntityCrystalCrawler.isClimbableSurface(this.crawler.world, candidate.offset(facing.getOpposite()))
                    && !EntityCrystalCrawler.isClimbableSurface(this.crawler.world, candidate)) {
                return candidate;
            }
        }
        return null;
    }

    private void beginClinging(EnumFacing facing) {
        this.crawler.getNavigator().clearPath();
        this.crawler.setClimbFacing(facing);
        this.phase = Phase.CLINGING;
        this.clingTicks = MIN_CLING_TICKS + this.crawler.getRNG().nextInt(MAX_CLING_TICKS - MIN_CLING_TICKS);
        this.descending = false;
        this.goOver = facing.getAxis().isHorizontal() && this.crawler.getRNG().nextInt(100) < GO_OVER_PERCENT;
        this.wanderGoal = null;
        this.wanderCooldown = 0;
    }
}
