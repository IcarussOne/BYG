package windanesz.byg.entity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.Config;
import windanesz.byg.registry.ModItems;

import java.util.ArrayList;
import java.util.List;

public class EntityKiwiBird extends EntityAnimal {

    private static final DataParameter<Boolean> IS_SLEEPING =
            EntityDataManager.createKey(EntityKiwiBird.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_FORAGING =
            EntityDataManager.createKey(EntityKiwiBird.class, DataSerializers.BOOLEAN);

    private BlockPos burrowPos = null;

    public EntityKiwiBird(World world) {
        super(world);
        this.setSize(0.4f, 0.7f);
        this.experienceValue = 5;
        this.isImmuneToFire = false;
        this.setNoAI(false);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.6D));
        if (ModItems.salal_berry != null) {
            this.tasks.addTask(2, new EntityAITempt(this, 1.1D, ModItems.salal_berry, false));
        }
        if (ModItems.worm != null) {
            this.tasks.addTask(2, new EntityAITempt(this, 1.1D, ModItems.worm, false));
        }
        this.tasks.addTask(3, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(4, new EntityAIKiwiSleep(this));
        this.tasks.addTask(5, new EntityAIKiwiDigBurrow(this));
        this.tasks.addTask(6, new EntityAIKiwiForage(this));
        this.tasks.addTask(7, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(8, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(9, new EntityAILookIdle(this));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(IS_SLEEPING, false);
        this.dataManager.register(IS_FORAGING, false);
    }

    public boolean isSleeping() { return this.dataManager.get(IS_SLEEPING); }
    public boolean isForaging() { return this.dataManager.get(IS_FORAGING); }
    void setSleeping(boolean v) { this.dataManager.set(IS_SLEEPING, v); }
    void setForaging(boolean v) { this.dataManager.set(IS_FORAGING, v); }

    public BlockPos getBurrowPos() { return burrowPos; }
    public void setBurrowPos(BlockPos pos) { this.burrowPos = pos; }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("BurrowX")) {
            this.burrowPos = new BlockPos(
                compound.getInteger("BurrowX"),
                compound.getInteger("BurrowY"),
                compound.getInteger("BurrowZ")
            );
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.burrowPos != null) {
            compound.setInteger("BurrowX", this.burrowPos.getX());
            compound.setInteger("BurrowY", this.burrowPos.getY());
            compound.setInteger("BurrowZ", this.burrowPos.getZ());
        }
    }

    // Find a suitable burrow location: flat 3x3 area with diggable blocks
    public BlockPos findBurrowLocation() {
        BlockPos start = this.getPosition();
        for (int attempt = 0; attempt < 20; attempt++) {
            int xOff = this.rand.nextInt(16) - 8;
            int zOff = this.rand.nextInt(16) - 8;
            BlockPos candidate = start.add(xOff, 0, zOff);
            
            // Find ground level
            while (candidate.getY() > 0 && this.world.isAirBlock(candidate)) {
                candidate = candidate.down();
            }
            candidate = candidate.up(); // Stand on top of ground
            
            if (isValidBurrowSite(candidate)) {
                return candidate;
            }
        }
        return null;
    }

    private boolean isValidBurrowSite(BlockPos pos) {
        // Check if 3x3 area is flat and made of diggable material
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos checkPos = pos.add(x, -1, z);
                IBlockState state = this.world.getBlockState(checkPos);
                Block block = state.getBlock();
                
                // Must be solid ground (dirt, grass, etc.)
                if (!state.getMaterial().isSolid() || state.getMaterial() == Material.ROCK) {
                    return false;
                }
                
                // Check that position above ground is air or replaceable
                if (!this.world.isAirBlock(checkPos.up()) && !this.world.getBlockState(checkPos.up()).getBlock().isReplaceable(this.world, checkPos.up())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void init(FMLInitializationEvent event) {
        int spawnWeight = Config.getKiwiSpawnWeight();
        if (spawnWeight == 0 || !Config.isWoodSetEnabled("kiwi_bird")) {
            return;
        }
        List<ResourceLocation> biomeIds = new ArrayList<>();
        for (String configuredBiome : Config.getKiwiSpawnBiomes()) {
            if (configuredBiome == null || configuredBiome.trim().isEmpty()) {
                continue;
            }
            String id = configuredBiome.trim();
            if (id.indexOf(':') < 0) {
                id = "byg:" + id;
            }
            biomeIds.add(new ResourceLocation(id));
        }
        if (biomeIds.isEmpty()) {
            return;
        }
        EntitySpawnHelper.addSpawnIfBiomesPresent(EntityKiwiBird.class, spawnWeight, 3, 4, EnumCreatureType.CREATURE,
                biomeIds.toArray(new ResourceLocation[0]));
    }

    @SideOnly(Side.CLIENT)
    public static void preInit(FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityKiwiBird.class, renderManager -> new RenderLiving(renderManager, new ModelKiwi(), 0.5f) {
            @Override
            protected ResourceLocation getEntityTexture(Entity entity) {
                return new ResourceLocation("byg:textures/kiwi.png");
            }

            @Override
            protected void preRenderCallback(EntityLivingBase entitylivingbaseIn, float partialTickTime) {
                if (entitylivingbaseIn.isChild()) {
                    GlStateManager.scale(0.5F, 0.5F, 0.5F);
                    this.shadowSize = 0.25F;
                } else {
                    this.shadowSize = 0.5F;
                }
            }
        });
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() == ModItems.salal_berry || stack.getItem() == ModItems.worm);
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return new EntityKiwiBird(this.world);
    }

    @Override
    protected Item getDropItem() {
        return Items.FEATHER;
    }

    @Override
    public net.minecraft.util.SoundEvent getAmbientSound() {
        // Kiwis are nocturnal - silent during day
        long time = this.world.getWorldTime() % 24000;
        boolean isNight = time >= 13000 && time < 23000;
        if (!isNight) return null;
        
        return net.minecraft.init.SoundEvents.ENTITY_CHICKEN_AMBIENT;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return net.minecraft.init.SoundEvents.ENTITY_CHICKEN_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return net.minecraft.init.SoundEvents.ENTITY_CHICKEN_DEATH;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0.0D);
    }

    // Sleep AI: active during day, pathfinds to burrow or sleeps in place
    static class EntityAIKiwiSleep extends EntityAIBase {
        private final EntityKiwiBird kiwi;
        private int sleepTimer;

        EntityAIKiwiSleep(EntityKiwiBird kiwi) {
            this.kiwi = kiwi;
            this.setMutexBits(3);
        }

        @Override
        public boolean shouldExecute() {
            if (kiwi.isInWater()) return false;
            long time = kiwi.world.getWorldTime() % 24000;
            boolean isDay = time >= 0 && time < 13000;
            return isDay;
        }

        @Override
        public void startExecuting() {
            this.sleepTimer = 0;
            kiwi.getNavigator().clearPath();
            
            // If has burrow, pathfind to it
            BlockPos burrow = kiwi.getBurrowPos();
            if (burrow != null && kiwi.world.isBlockLoaded(burrow)) {
                // Check burrow still exists
                if (isBurrowValid(burrow)) {
                    kiwi.getNavigator().tryMoveToXYZ(burrow.getX() + 0.5, burrow.getY(), burrow.getZ() + 0.5, 1.0);
                } else {
                    kiwi.setBurrowPos(null); // Burrow destroyed
                }
            }
            
            kiwi.setSleeping(true);
        }

        @Override
        public boolean shouldContinueExecuting() {
            long time = kiwi.world.getWorldTime() % 24000;
            boolean isDay = time >= 0 && time < 13000;
            return isDay && !kiwi.isInWater();
        }

        @Override
        public void updateTask() {
            this.sleepTimer++;
            kiwi.getNavigator().clearPath();
        }

        @Override
        public void resetTask() {
            kiwi.setSleeping(false);
        }

        private boolean isBurrowValid(BlockPos burrow) {
            // Check if the burrow blocks are still air (not filled in)
            return kiwi.world.isAirBlock(burrow.down()) && kiwi.world.isAirBlock(burrow.down().east());
        }
    }

    // Dig Burrow AI: runs occasionally, finds location and digs stair pattern
    static class EntityAIKiwiDigBurrow extends EntityAIBase {
        private final EntityKiwiBird kiwi;
        private BlockPos targetPos;
        private int digProgress;

        EntityAIKiwiDigBurrow(EntityKiwiBird kiwi) {
            this.kiwi = kiwi;
            this.setMutexBits(3);
        }

        @Override
        public boolean shouldExecute() {
            if (kiwi.isChild()) return false;
            // Only dig at night, randomly, if no burrow exists
            if (kiwi.isInWater()) return false;
            if (kiwi.getBurrowPos() != null) return false;
            
            long time = kiwi.world.getWorldTime() % 24000;
            boolean isNight = time >= 13000 && time < 23000;
            if (!isNight) return false;
            
            // Random trigger: ~1/600 chance per tick when conditions met
            if (kiwi.getRNG().nextInt(600) != 0) return false;
            
            // Find a burrow location
            this.targetPos = kiwi.findBurrowLocation();
            return this.targetPos != null;
        }

        @Override
        public void startExecuting() {
            this.digProgress = 0;
            kiwi.getNavigator().tryMoveToXYZ(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, 1.0);
        }

        @Override
        public boolean shouldContinueExecuting() {
            return this.digProgress < 60 && this.targetPos != null;
        }

        @Override
        public void updateTask() {
            this.digProgress++;
            
            // Move toward burrow site
            if (kiwi.getDistanceSq(targetPos) > 4.0) {
                kiwi.getNavigator().tryMoveToXYZ(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, 1.0);
                return;
            }
            
            // Close enough - dig the burrow
            if (this.digProgress == 30 && !kiwi.world.isRemote) {
                digBurrow(targetPos);
                kiwi.setBurrowPos(targetPos);
            }
        }

        @Override
        public void resetTask() {
            this.targetPos = null;
            this.digProgress = 0;
        }

        private void digBurrow(BlockPos start) {
            // Pattern: DDX
            //          XDD
            // start is the top-left position
            // Layer 1 (surface level): dig start and start.east()
            kiwi.world.setBlockState(start.down(), Blocks.AIR.getDefaultState(), 3);
            kiwi.world.setBlockState(start.down().east(), Blocks.AIR.getDefaultState(), 3);
            
            // Layer 2 (one down): dig start.south() and start.south().east()
            kiwi.world.setBlockState(start.down(2).south(), Blocks.AIR.getDefaultState(), 3);
            kiwi.world.setBlockState(start.down(2).south().east(), Blocks.AIR.getDefaultState(), 3);
        }
    }

    // Forage AI: active at night, random head-bobbing with worm drops
    static class EntityAIKiwiForage extends EntityAIBase {
        private final EntityKiwiBird kiwi;
        private int duration;

        EntityAIKiwiForage(EntityKiwiBird kiwi) {
            this.kiwi = kiwi;
            this.setMutexBits(3);
        }

        @Override
        public boolean shouldExecute() {
            if (kiwi.isChild()) return false;
            if (kiwi.isInWater()) return false;
            long time = kiwi.world.getWorldTime() % 24000;
            boolean isNight = time >= 13000 && time < 23000;
            return isNight && kiwi.getRNG().nextInt(Config.getKiwiForageAttemptInterval()) == 0;
        }

        @Override
        public void startExecuting() {
            this.duration = 40 + kiwi.getRNG().nextInt(40);
            kiwi.getNavigator().clearPath();
            kiwi.setForaging(true);
        }

        @Override
        public boolean shouldContinueExecuting() {
            return this.duration > 0;
        }

        @Override
        public void updateTask() {
            this.duration--;
            kiwi.getNavigator().clearPath();
            
            // Drop worm when foraging completes naturally
            if (this.duration == 0 && !kiwi.world.isRemote) {
                if (kiwi.getRNG().nextDouble() < Config.getKiwiWormFindChance()) {
                    kiwi.entityDropItem(new ItemStack(ModItems.worm, 1), 0.0f);
                }
            }
        }

        @Override
        public void resetTask() {
            kiwi.setForaging(false);
        }
    }

    @SideOnly(Side.CLIENT)
    public static class ModelKiwi extends ModelBase {
        public ModelRenderer Body;
        public ModelRenderer Head;
        public ModelRenderer Beak;
        public ModelRenderer legpart1;
        public ModelRenderer legpart2;
        public ModelRenderer rightleg;
        public ModelRenderer leftleg;
        public ModelRenderer Shape1;
        public ModelRenderer Shape2;
        public ModelRenderer Shape3;
        public ModelRenderer Shape4;
        public ModelRenderer Shape5;
        public ModelRenderer Shape6;
        public ModelRenderer rightfoot;
        public ModelRenderer leftfoot;

        public ModelKiwi() {
            this.textureWidth = 64;
            this.textureHeight = 32;

            this.Body = new ModelRenderer(this, 27, 0);
            this.Body.addBox(0.0f, 0.0f, 0.0f, 9, 9, 9);
            this.Body.setRotationPoint(-4.0f, 9.0f, -4.0f);
            this.Body.setTextureSize(64, 32);
            this.Body.mirror = true;
            this.setRotation(this.Body, -0.2082002f, 0.0f, 0.0f);

            this.Head = new ModelRenderer(this, 29, 19);
            this.Head.addBox(-3.0f, 0.0f, -7.0f, 6, 6, 7);
            this.Head.setRotationPoint(0.5f, 10.0f, -4.0f);
            this.Head.setTextureSize(64, 32);
            this.Head.mirror = true;
            this.setRotation(this.Head, 0.2974289f, 0.0f, 0.0f);

            this.Beak = new ModelRenderer(this, 10, 22);
            this.Beak.addBox(0.0f, 0.0f, 0.0f, 1, 1, 7);
            this.Beak.setRotationPoint(-0.5f, 7.0f, -11.0f);
            this.Beak.setTextureSize(64, 32);
            this.Beak.mirror = true;
            this.setRotation(this.Beak, 0.4461433f, 0.0f, 0.0f);
            this.Head.addChild(this.Beak);

            this.legpart1 = new ModelRenderer(this, 11, 0);
            this.legpart1.addBox(0.0f, 0.0f, 0.0f, 2, 1, 2);
            this.legpart1.setRotationPoint(3.0f, 19.0f, 0.0f);
            this.legpart1.setTextureSize(64, 32);
            this.legpart1.mirror = true;
            this.setRotation(this.legpart1, 0.0f, 0.0f, 0.0f);

            this.legpart2 = new ModelRenderer(this, 12, 0);
            this.legpart2.addBox(0.0f, 0.0f, 0.0f, 2, 1, 2);
            this.legpart2.setRotationPoint(-4.0f, 19.0f, 0.0f);
            this.legpart2.setTextureSize(64, 32);
            this.legpart2.mirror = true;
            this.setRotation(this.legpart2, 0.0f, 0.0f, 0.0f);

            this.rightleg = new ModelRenderer(this, 0, 9);
            this.rightleg.addBox(0.0f, 0.0f, 0.0f, 1, 4, 1);
            this.rightleg.setRotationPoint(-3.5f, 20.0f, 0.5f);
            this.rightleg.setTextureSize(64, 32);
            this.rightleg.mirror = true;
            this.setRotation(this.rightleg, 0.0f, 0.0f, 0.0f);

            this.leftleg = new ModelRenderer(this, 0, 9);
            this.leftleg.addBox(0.0f, 0.0f, 0.0f, 1, 4, 1);
            this.leftleg.setRotationPoint(3.5f, 20.0f, 0.5f);
            this.leftleg.setTextureSize(64, 32);
            this.leftleg.mirror = true;
            this.setRotation(this.leftleg, 0.0f, 0.0f, 0.0f);

            this.Shape1 = new ModelRenderer(this, 0, 0);
            this.Shape1.addBox(0.0f, 0.0f, 0.0f, 0, 1, 1);
            this.Shape1.setRotationPoint(0.0f, 9.0f, 0.0f);
            this.Shape1.setTextureSize(64, 32);
            this.Shape1.mirror = true;
            this.setRotation(this.Shape1, 0.0f, 0.0f, 0.0f);

            this.Shape2 = new ModelRenderer(this, 0, 0);
            this.Shape2.addBox(0.0f, 0.0f, 0.0f, 0, 1, 1);
            this.Shape2.setRotationPoint(0.0f, 12.0f, 4.0f);
            this.Shape2.setTextureSize(64, 32);
            this.Shape2.mirror = true;
            this.setRotation(this.Shape2, 0.0f, 0.0f, 0.0f);

            this.Shape3 = new ModelRenderer(this, 0, 0);
            this.Shape3.addBox(0.0f, 0.0f, 0.0f, 1, 1, 0);
            this.Shape3.setRotationPoint(5.0f, 11.0f, -1.0f);
            this.Shape3.setTextureSize(64, 32);
            this.Shape3.mirror = true;
            this.setRotation(this.Shape3, 0.0f, 0.0f, 0.0f);

            this.Shape4 = new ModelRenderer(this, 0, 0);
            this.Shape4.addBox(0.0f, 0.0f, 0.0f, 1, 1, 0);
            this.Shape4.setRotationPoint(-5.0f, 11.0f, -1.0f);
            this.Shape4.setTextureSize(64, 32);
            this.Shape4.mirror = true;
            this.setRotation(this.Shape4, 0.0f, 0.0f, 0.0f);

            this.Shape5 = new ModelRenderer(this, 0, 0);
            this.Shape5.addBox(0.0f, 0.0f, 0.0f, 1, 1, 0);
            this.Shape5.setRotationPoint(5.0f, 17.0f, 3.0f);
            this.Shape5.setTextureSize(64, 32);
            this.Shape5.mirror = true;
            this.setRotation(this.Shape5, 0.0f, 0.0f, 0.0f);

            this.Shape6 = new ModelRenderer(this, 0, 0);
            this.Shape6.addBox(0.0f, 0.0f, 0.0f, 1, 1, 0);
            this.Shape6.setRotationPoint(0.0f, 19.0f, 0.0f);
            this.Shape6.setTextureSize(64, 32);
            this.Shape6.mirror = true;
            this.setRotation(this.Shape6, 0.0f, 0.0f, 0.0f);

            this.rightfoot = new ModelRenderer(this, 40, 0);
            this.rightfoot.setRotationPoint(0.5f, 4.0f, 0.5f);
            this.rightfoot.addBox(-1.5f, 0.0f, -2.0f, 3, 0, 4, 0.0f);
            this.rightfoot.setTextureSize(64, 32);
            this.rightfoot.mirror = true;
            this.setRotation(this.rightfoot, 0.0f, 0.0f, 0.0f);
            this.rightleg.addChild(this.rightfoot);

            this.leftfoot = new ModelRenderer(this, 40, 0);
            this.leftfoot.setRotationPoint(0.5f, 4.0f, 0.5f);
            this.leftfoot.addBox(-1.5f, 0.0f, -2.0f, 3, 0, 4, 0.0f);
            this.leftfoot.setTextureSize(64, 32);
            this.leftfoot.mirror = true;
            this.setRotation(this.leftfoot, 0.0f, 0.0f, 0.0f);
            this.leftleg.addChild(this.leftfoot);
        }

        @Override
        public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
            super.render(entity, f, f1, f2, f3, f4, f5);
            this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
            boolean sleeping = entity instanceof EntityKiwiBird && ((EntityKiwiBird) entity).isSleeping();
            this.Body.render(f5);
            this.Head.render(f5);
            this.Shape1.render(f5);
            this.Shape2.render(f5);
            this.Shape3.render(f5);
            this.Shape4.render(f5);
            this.Shape5.render(f5);
            this.Shape6.render(f5);
            if (!sleeping) {
                this.legpart1.render(f5);
                this.legpart2.render(f5);
                this.rightleg.render(f5);
                this.leftleg.render(f5);
            }
        }

        @Override
        public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
            super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);

            this.Body.rotateAngleX = -0.2082002f;
            this.Body.rotateAngleY = 0.0f;
            this.Body.rotateAngleZ = 0.0f;
            this.Body.setRotationPoint(-4.0f, 9.0f, -4.0f);

            this.Head.rotateAngleX = 0.2974289f;
            this.Head.rotateAngleY = 0.0f;
            this.Head.rotateAngleZ = 0.0f;
            this.Head.setRotationPoint(0.5f, 10.0f, -4.0f);

            this.Beak.rotateAngleX = 0.4461433f;
            this.Beak.rotateAngleY = 0.0f;
            this.Beak.rotateAngleZ = 0.0f;

            this.legpart1.rotateAngleX = 0.0f;
            this.legpart1.rotateAngleY = 0.0f;
            this.legpart1.rotateAngleZ = 0.0f;
            this.legpart2.rotateAngleX = 0.0f;
            this.legpart2.rotateAngleY = 0.0f;
            this.legpart2.rotateAngleZ = 0.0f;
            this.rightleg.rotateAngleX = 0.0f;
            this.rightleg.rotateAngleY = 0.0f;
            this.rightleg.rotateAngleZ = 0.0f;
            this.leftleg.rotateAngleX = 0.0f;
            this.leftleg.rotateAngleY = 0.0f;
            this.leftleg.rotateAngleZ = 0.0f;
            this.rightfoot.rotateAngleX = 0.0f;
            this.rightfoot.rotateAngleY = 0.0f;
            this.rightfoot.rotateAngleZ = 0.0f;
            this.leftfoot.rotateAngleX = 0.0f;
            this.leftfoot.rotateAngleY = 0.0f;
            this.leftfoot.rotateAngleZ = 0.0f;

            this.legpart1.showModel = true;
            this.legpart2.showModel = true;
            this.rightleg.showModel = true;
            this.leftleg.showModel = true;
            this.rightfoot.showModel = true;
            this.leftfoot.showModel = true;

            this.Shape1.rotateAngleX = 0.0f;
            this.Shape1.rotateAngleY = 0.0f;
            this.Shape1.rotateAngleZ = 0.0f;
            this.Shape1.setRotationPoint(0.0f, 9.0f, 0.0f);
            this.Shape2.rotateAngleX = 0.0f;
            this.Shape2.rotateAngleY = 0.0f;
            this.Shape2.rotateAngleZ = 0.0f;
            this.Shape2.setRotationPoint(0.0f, 12.0f, 4.0f);
            this.Shape3.rotateAngleX = 0.0f;
            this.Shape3.rotateAngleY = 0.0f;
            this.Shape3.rotateAngleZ = 0.0f;
            this.Shape3.setRotationPoint(5.0f, 11.0f, -1.0f);
            this.Shape4.rotateAngleX = 0.0f;
            this.Shape4.rotateAngleY = 0.0f;
            this.Shape4.rotateAngleZ = 0.0f;
            this.Shape4.setRotationPoint(-5.0f, 11.0f, -1.0f);
            this.Shape5.rotateAngleX = 0.0f;
            this.Shape5.rotateAngleY = 0.0f;
            this.Shape5.rotateAngleZ = 0.0f;
            this.Shape5.setRotationPoint(5.0f, 17.0f, 3.0f);
            this.Shape6.rotateAngleX = 0.0f;
            this.Shape6.rotateAngleY = 0.0f;
            this.Shape6.rotateAngleZ = 0.0f;
            this.Shape6.setRotationPoint(0.0f, 19.0f, 0.0f);

            if (entity instanceof EntityKiwiBird) {
                EntityKiwiBird kiwi = (EntityKiwiBird) entity;
                if (kiwi.isSleeping()) {
                    float breathe = MathHelper.sin(ageInTicks * 0.05f) * 0.04f;
                    this.legpart1.showModel = false;
                    this.legpart2.showModel = false;
                    this.rightleg.showModel = false;
                    this.leftleg.showModel = false;
                    this.rightfoot.showModel = false;
                    this.leftfoot.showModel = false;
                    this.Body.rotateAngleX = 0.0f;
                    this.Body.rotateAngleZ = 1.57f + breathe;
                    this.Body.setRotationPoint(-4.0f, 15.0f, -4.0f);
                    this.Head.rotateAngleX = 0.2f + breathe;
                    this.Head.rotateAngleY = 0.0f;
                    this.Head.rotateAngleZ = 1.57f + breathe;
                    this.Head.setRotationPoint(0.5f, 21.0f, -4.0f);
                    this.Beak.rotateAngleX = 0.35f;
                    this.Shape1.rotateAngleZ = 1.57f + breathe;
                    this.Shape1.setRotationPoint(0.0f, 15.0f, 0.0f);
                    this.Shape2.rotateAngleZ = 1.57f + breathe;
                    this.Shape2.setRotationPoint(0.0f, 18.0f, 4.0f);
                    this.Shape3.rotateAngleZ = 1.57f + breathe;
                    this.Shape3.setRotationPoint(5.0f, 17.0f, -1.0f);
                    this.Shape4.rotateAngleZ = 1.57f + breathe;
                    this.Shape4.setRotationPoint(-5.0f, 17.0f, -1.0f);
                    this.Shape5.rotateAngleZ = 1.57f + breathe;
                    this.Shape5.setRotationPoint(5.0f, 23.0f, 3.0f);
                    this.Shape6.rotateAngleZ = 1.57f + breathe;
                    this.Shape6.setRotationPoint(0.0f, 25.0f, 0.0f);
                } else if (kiwi.isForaging()) {
                    this.Body.rotateAngleX = -0.12f;
                    this.Body.rotateAngleZ = MathHelper.sin(ageInTicks * 0.18f) * 0.04f;
                    this.Head.rotateAngleX = 0.8f + MathHelper.sin(ageInTicks * 0.35f) * 0.55f;
                    this.Head.rotateAngleY = 0.0f;
                } else {
                    this.Body.rotateAngleZ = MathHelper.cos(limbSwing * 0.6662f) * 0.15f * limbSwingAmount;

                    this.Head.rotateAngleY = netHeadYaw * 0.017453292f;
                    this.Head.rotateAngleX = 0.2974289f + headPitch * 0.017453292f;

                    float rightThigh = MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
                    float leftThigh = MathHelper.cos(limbSwing * 0.6662f + (float) Math.PI) * 1.4f * limbSwingAmount;

                    this.legpart1.rotateAngleX = leftThigh;
                    this.legpart2.rotateAngleX = rightThigh;

                    float rightLower = MathHelper.cos(limbSwing * 0.6662f + (float) Math.PI / 4) * 1.2f * limbSwingAmount;
                    float leftLower = MathHelper.cos(limbSwing * 0.6662f + (float) Math.PI + (float) Math.PI / 4) * 1.2f * limbSwingAmount;

                    this.rightleg.rotateAngleX = rightLower;
                    this.leftleg.rotateAngleX = leftLower;
                }
            }
        }

        public void setRotation(ModelRenderer modelRenderer, float x, float y, float z) {
            modelRenderer.rotateAngleX = x;
            modelRenderer.rotateAngleY = y;
            modelRenderer.rotateAngleZ = z;
        }
    }
}
