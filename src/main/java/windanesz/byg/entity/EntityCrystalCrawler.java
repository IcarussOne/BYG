package windanesz.byg.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.client.RenderCrystalCrawler;
import windanesz.byg.entity.ai.AICrawlerHunt;
import windanesz.byg.entity.ai.AICrawlerIdleClimb;

public class EntityCrystalCrawler extends EntityMob {
    public static final ResourceLocation LOOT_TABLE = LootTableList.register(new ResourceLocation("byg", "entities/crystal_crawler"));

    private static final int DAYLIGHT_DESPAWN_ODDS = 1200;
    private static final double DAYLIGHT_DESPAWN_SAFE_RADIUS = 16.0D;

    // Maximum degrees the model may turn per tick while crawling on a surface
    private static final float MAX_CLIMB_TURN_PER_TICK = 12.0F;

    // How hard an attached crawler presses into its surface each tick
    private static final double SURFACE_ADHESION = 0.03D;
    // Distance between the points checked for gaps when an idle crawler moves along a surface
    private static final double MOVE_CHECK_STEP = 0.25D;
    // Lift and forward push when stepping over the top of a wall
    private static final double CLIMB_OVER_LIFT = 0.02D;
    private static final double CLIMB_OVER_PUSH = 0.08D;
    // Leg animation while climbing: movement to limb swing amount, and how fast the legs cycle
    private static final float LIMB_SWING_FROM_SPEED = 4.0F;
    private static final float LIMB_SWING_RATE = 0.6F;

    // Outward normal of the surface being clung to
    private static final DataParameter<EnumFacing> CLIMB_FACING = EntityDataManager.createKey(EntityCrystalCrawler.class, DataSerializers.FACING);

    private boolean restoreClimbSurface;
    // Set by the AI when it wants to crawl over an edge, the gap check in move() would stop it otherwise
    private boolean mayCrossEdges;

    public EntityCrystalCrawler(World world) {
        super(world);
        this.setSize(0.7f, 0.5f);
        this.experienceValue = 12;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CLIMB_FACING, EnumFacing.UP);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new AICrawlerHunt(this, 0.2D));
        this.tasks.addTask(2, new AICrawlerIdleClimb(this, 0.12D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        EntityAINearestAttackableTarget<EntityPlayer> findPlayer = new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true, false);

        // might get blocked vision when clinging to a small ledge so this helps it remember the player for a while
        findPlayer.setUnseenMemoryTicks(200);
        this.targetTasks.addTask(3, findPlayer);
        this.targetTasks.addTask(4, new EntityAIHurtByTarget(this, true));
    }

    @SideOnly(Side.CLIENT)
    public static void preInit(FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityCrystalCrawler.class, RenderCrystalCrawler::new);
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    // Unlike other hostile mobs this one may spawn in daylight as well as at
    // night, so the light-level check of {@link EntityMob} is skipped.
    @Override
    public boolean getCanSpawnHere() {
        return this.world.getDifficulty() != EnumDifficulty.PEACEFUL
                && this.getBlockPathWeight(new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ)) >= 0.0F;
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_TABLE;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SPIDER_AMBIENT;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return SoundEvents.ENTITY_SPIDER_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SPIDER_DEATH;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("CrawlerClimbFacing", this.getClimbFacing().getIndex());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        int index = compound.hasKey("CrawlerClimbFacing", 99) ? compound.getInteger("CrawlerClimbFacing") : EnumFacing.UP.getIndex();
        EnumFacing facing = index >= 0 && index < EnumFacing.values().length ? EnumFacing.byIndex(index) : EnumFacing.UP;
        // Set the facing directly, the world isn't ready yet while loading
        this.dataManager.set(CLIMB_FACING, facing);
        this.restoreClimbSurface = facing != EnumFacing.UP;
        this.setNoGravity(this.restoreClimbSurface);
    }

    public EnumFacing getClimbFacing() {
        return this.dataManager.get(CLIMB_FACING);
    }

    public boolean isClimbing() {
        return this.getClimbFacing() != EnumFacing.UP;
    }

    public void setClimbFacing(EnumFacing facing) {
        if (this.getClimbFacing() != facing) {
            this.dataManager.set(CLIMB_FACING, facing);
            if (facing != EnumFacing.UP) {
                this.snapToSurface(facing);
            }
        }
    }

    // Puts the hitbox flush against the surface it's clinging to. Sets the bounding box directly instead of
    // calling setPosition, since this runs every tick
    private void snapToSurface(EnumFacing facing) {
        BlockPos center = this.getCenterPos();
        double halfWidth = this.width * 0.5D;
        double x = this.posX;
        double y = this.posY;
        double z = this.posZ;
        switch (facing) {
            case EAST:
                x = center.getX() + halfWidth;
                break;
            case WEST:
                x = center.getX() + 1.0D - halfWidth;
                break;
            case SOUTH:
                z = center.getZ() + halfWidth;
                break;
            case NORTH:
                z = center.getZ() + 1.0D - halfWidth;
                break;
            case DOWN:
                y = center.getY() + 1.0D - this.height;
                break;
            default:
                return;
        }
        AxisAlignedBB flush = this.boxAt(x, y, z);
        // Can end up inside a neighbouring block at corners, stay where we are then
        if (!this.world.getCollisionBoxes(this, flush).isEmpty()) {
            return;
        }
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.setEntityBoundingBox(flush);
    }

    // The hitbox this entity would have with its feet at the given position
    private AxisAlignedBB boxAt(double x, double y, double z) {
        double halfWidth = this.width * 0.5D;
        return new AxisAlignedBB(x - halfWidth, y, z - halfWidth, x + halfWidth, y + this.height, z + halfWidth);
    }

    private boolean isFreeAt(double x, double y, double z) {
        return this.world.getCollisionBoxes(this, this.boxAt(x, y, z)).isEmpty();
    }

    // Pushes the hitbox out of any blocks it overlaps, lets go of the surface if there's nowhere to go
    private void freeFromBlocks() {
        if (this.isFreeAt(this.posX, this.posY, this.posZ)) {
            return;
        }
        double[] steps = {0.25D, 0.5D, 0.75D, 1.0D};
        int[][] directions = {{0, 1, 0}, {1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}, {0, -1, 0}};
        for (double step : steps) {
            for (int[] dir : directions) {
                double x = this.posX + dir[0] * step;
                double y = this.posY + dir[1] * step;
                double z = this.posZ + dir[2] * step;
                if (this.isFreeAt(x, y, z)) {
                    this.setPosition(x, y, z);
                    return;
                }
            }
        }
        this.releaseSurface();
    }

    // Gets the crawler onto the top of a wall it just climbed. Letting go would drop it straight back down,
    // the hitbox sits outside the wall while clinging
    public void climbOver(EnumFacing fromFacing) {
        if (fromFacing.getAxis().isHorizontal()) {
            double y = Math.max(this.posY, this.getCenterPos().getY()) + CLIMB_OVER_LIFT;
            this.releaseSurface();
            // Full step first, then shorter ones. A taller block next to the ledge can block the full step
            double[] inwardOptions = {this.width * 0.5D + 0.15D, this.width * 0.5D, 0.2D, 0.0D};
            for (double inward : inwardOptions) {
                double x = this.posX - fromFacing.getXOffset() * inward;
                double z = this.posZ - fromFacing.getZOffset() * inward;
                if (this.isFreeAt(x, y, z)) {
                    this.setPosition(x, y, z);
                    break;
                }
            }
            this.motionX = -fromFacing.getXOffset() * CLIMB_OVER_PUSH;
            this.motionZ = -fromFacing.getZOffset() * CLIMB_OVER_PUSH;
            this.motionY = 0.0D;
        } else {
            this.releaseSurface();
        }
    }

    public void releaseSurface() {
        this.setClimbFacing(EnumFacing.UP);
        this.setNoGravity(false);
    }

    public static boolean isClimbableSurface(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return !state.getBlock().isAir(state, world, pos) && state.getMaterial().isSolid();
    }

    public boolean isSurfaceStillSolid(EnumFacing facing) {
        return isClimbableSurface(this.world, this.getCenterPos().offset(facing.getOpposite()));
    }

    public BlockPos getCenterPos() {
        return new BlockPos(this.posX, this.posY + this.height * 0.5D, this.posZ);
    }

    // Keeps the current surface while it's still solid, otherwise tries to turn a corner.
    public EnumFacing findSupportingFace(EnumFacing current) {
        if (this.isSurfaceStillSolid(current)) {
            return current;
        }
        BlockPos center = this.getCenterPos();
        for (EnumFacing candidate : EnumFacing.values()) {
            if (candidate == current || candidate == current.getOpposite()) {
                continue;
            }
            if (isClimbableSurface(this.world, center.offset(candidate.getOpposite()))) {
                return candidate;
            }
        }
        return null;
    }

    public EnumFacing findAdjacentWall() {
        BlockPos center = this.getCenterPos();
        for (EnumFacing dir : EnumFacing.HORIZONTALS) {
            if (isClimbableSurface(this.world, center.offset(dir))) {
                return dir.getOpposite();
            }
        }
        return null;
    }

    public void setMayCrossEdges(boolean mayCrossEdges) {
        this.mayCrossEdges = mayCrossEdges;
    }

    // The air cell just above the top of the wall being climbed, null if something is in the way or the
    // wall is too tall
    public BlockPos findWallTop(EnumFacing facing, int maxHeight) {
        BlockPos center = this.getCenterPos();
        for (int dy = 0; dy <= maxHeight; dy++) {
            BlockPos cell = center.up(dy);
            if (isClimbableSurface(this.world, cell)) {
                return null;
            }
            if (!isClimbableSurface(this.world, cell.offset(facing.getOpposite()))) {
                return cell;
            }
        }
        return null;
    }

    public boolean hasClimbableCeilingAbove() {
        return isClimbableSurface(this.world, this.getCenterPos().up());
    }

    // Free spot right under a ceiling somewhere above base
    public static BlockPos findCeilingPerch(World world, BlockPos base, int minHeight, int maxHeight) {
        for (int dy = minHeight; dy <= maxHeight; dy++) {
            BlockPos above = base.up(dy);
            if (isClimbableSurface(world, above)) {
                BlockPos perchPos = above.down();
                return !isClimbableSurface(world, perchPos) ? perchPos : null;
            }
        }
        return null;
    }

    // Free spot next to a wall near base
    public static BlockPos findNearbyWallPerch(World world, BlockPos base, int minRadius, int maxRadius) {
        for (int r = minRadius; r <= maxRadius; r++) {
            for (EnumFacing dir : EnumFacing.HORIZONTALS) {
                for (int dy = 0; dy <= 2; dy++) {
                    BlockPos wallPos = base.up(dy).offset(dir, r);
                    if (isClimbableSurface(world, wallPos)) {
                        BlockPos perchPos = wallPos.offset(dir.getOpposite());
                        if (!isClimbableSurface(world, perchPos)) {
                            return perchPos;
                        }
                    }
                }
            }
        }
        return null;
    }

    // Moves one tick along the surface towards the goal, or stays put if the goal is null
    public void tickClimbMovement(EnumFacing facing, BlockPos goal, double speed) {
        this.setNoGravity(true);
        this.fallDistance = 0.0F;

        // Snap back to the surface every tick. Pressing into the wall gets cancelled by collision, and the
        // step-up logic mistakes that for a low step and shoves the entity around
        this.snapToSurface(facing);

        double nx = facing.getXOffset();
        double ny = facing.getYOffset();
        double nz = facing.getZOffset();

        double mx = 0.0D;
        double my = 0.0D;
        double mz = 0.0D;
        if (goal != null) {
            double dx = (goal.getX() + 0.5D) - this.posX;
            double dy = (goal.getY() + 0.5D) - this.posY;
            double dz = (goal.getZ() + 0.5D) - this.posZ;
            // project the desired direction onto the plane perpendicular to the surface normal
            double dot = dx * nx + dy * ny + dz * nz;
            dx -= dot * nx;
            dy -= dot * ny;
            dz -= dot * nz;
            double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (len > 0.05D) {
                mx = dx / len * speed;
                my = dy / len * speed;
                mz = dz / len * speed;
                this.updateYawForMovement(mx, my, mz, facing);
            }
        }

        // a bit of constant adhesion into the surface
        mx -= nx * SURFACE_ADHESION;
        my -= ny * SURFACE_ADHESION;
        mz -= nz * SURFACE_ADHESION;

        this.motionX = mx;
        this.motionY = my;
        this.motionZ = mz;
    }

    @Override
    public void move(MoverType type, double x, double y, double z) {
        EnumFacing facing = this.getClimbFacing();
        if (!this.world.isRemote && this.isClimbing() && this.getAttackTarget() == null && !this.mayCrossEdges && this.isSurfaceStillSolid(facing)) {
            // Stops an idle crawler from being pushed off an edge, checks the whole move for gaps in the wall
            int steps = Math.max(1, (int) Math.ceil(Math.max(Math.abs(x), Math.max(Math.abs(y), Math.abs(z))) / MOVE_CHECK_STEP));
            for (int step = 1; step <= steps; step++) {
                double fraction = (double) step / steps;
                BlockPos center = new BlockPos(this.posX + x * fraction,
                        this.posY + this.height * 0.5D + y * fraction, this.posZ + z * fraction);
                if (!isClimbableSurface(this.world, center.offset(facing.getOpposite()))) {
                    if (facing.getAxis() != EnumFacing.Axis.X) {
                        this.motionX = x = 0.0D;
                    }
                    if (facing.getAxis() != EnumFacing.Axis.Y) {
                        this.motionY = y = 0.0D;
                    }
                    if (facing.getAxis() != EnumFacing.Axis.Z) {
                        this.motionZ = z = 0.0D;
                    }
                    break;
                }
            }
        }
        super.move(type, x, y, z);
    }

    // Turns the model towards the crawl direction, mirrors the tip rotation done in the renderer.
    // The turn speed is capped, crawling right overhead would make the heading jump around otherwise
    private void updateYawForMovement(double mx, double my, double mz, EnumFacing facing) {
        if (mx * mx + my * my + mz * mz > 1.0E-5D) {
            double dx;
            double dz;
            switch (facing) {
                case DOWN:
                    dx = mx;
                    dz = -mz;
                    break;
                case NORTH:
                    dx = mx;
                    dz = my;
                    break;
                case SOUTH:
                    dx = mx;
                    dz = -my;
                    break;
                case EAST:
                    dx = -my;
                    dz = mz;
                    break;
                case WEST:
                    dx = my;
                    dz = mz;
                    break;
                default:
                    dx = mx;
                    dz = mz;
                    break;
            }
            float targetYaw = (float) (MathHelper.atan2(dz, dx) * (180D / Math.PI)) - 90.0F;
            float delta = MathHelper.clamp(MathHelper.wrapDegrees(targetYaw - this.rotationYaw),
                    -MAX_CLIMB_TURN_PER_TICK, MAX_CLIMB_TURN_PER_TICK);
            float yaw = this.rotationYaw + delta;
            this.rotationYaw = yaw;
            this.renderYawOffset = yaw;
        }
    }

    // Avoiding fall damage to keep the crawler alive when jumping and climbing
    @Override
    public void fall(float distance, float damageMultiplier) {
        this.fallDistance = 0.0F;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.isClimbing()) {
            // limb swing only counts horizontal movement, the legs would stop when going straight up
            double dx = this.posX - this.prevPosX;
            double dy = this.posY - this.prevPosY;
            double dz = this.posZ - this.prevPosZ;
            float speed = Math.min(1.0F, (float) Math.sqrt(dx * dx + dy * dy + dz * dz) * LIMB_SWING_FROM_SPEED);
            if (speed > this.limbSwingAmount) {
                this.limbSwingAmount = speed;
            }
            this.limbSwing += speed * LIMB_SWING_RATE;
        }
    }

    // Daylight despawn on top of the normal one, only if no player is near and it isn't fighting
    @Override
    public void onLivingUpdate() {
        if (!this.world.isRemote && this.restoreClimbSurface) {
            this.restoreClimbSurface = false;
            // Get the surface back after loading, let go if it's gone
            EnumFacing support = this.findSupportingFace(this.isClimbing() ? this.getClimbFacing() : EnumFacing.DOWN);
            if (support != null && support != EnumFacing.UP) {
                this.setClimbFacing(support);
                this.setNoGravity(true);
            } else {
                this.releaseSurface();
            }
        }
        super.onLivingUpdate();
        if (!this.world.isRemote && this.isClimbing()) {
            this.freeFromBlocks();
        }
        if (!this.world.isRemote && this.world.isDaytime() && this.getAttackTarget() == null
                && this.rand.nextInt(DAYLIGHT_DESPAWN_ODDS) == 0
                && this.world.getClosestPlayerToEntity(this, DAYLIGHT_DESPAWN_SAFE_RADIUS) == null) {
            this.setDead();
        }
    }

    // Corners can put the eye inside a block for a tick, don't hurt it for that
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return source != DamageSource.IN_WALL && super.attackEntityFrom(source, amount);
    }

    // Distance to the nearest point of the target's hitbox. From the feet it looks too far away when the
    // crawler is up on a wall
    public boolean isInBiteRange(EntityLivingBase target) {
        AxisAlignedBB box = target.getEntityBoundingBox();
        double cx = this.posX;
        double cy = this.posY + this.height * 0.5D;
        double cz = this.posZ;
        double dx = Math.max(Math.max(box.minX - cx, 0.0D), cx - box.maxX);
        double dy = Math.max(Math.max(box.minY - cy, 0.0D), cy - box.maxY);
        double dz = Math.max(Math.max(box.minZ - cz, 0.0D), cz - box.maxZ);
        double reach = 1.0D + this.width * 0.5D;
        return dx * dx + dy * dy + dz * dz <= reach * reach;
    }
}
