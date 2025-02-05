package yezi.skillablereforged.common.abilities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import yezi.skillablereforged.common.capabilities.SkillModel;
import yezi.skillablereforged.common.skills.Requirement;
import yezi.skillablereforged.common.skills.Skill;
import yezi.skillablereforged.common.utils.GetAbilityLevel;

public class ExplosiveMiningAbility extends Ability{
    private static final String name = "explosive_mining";
    private static final String description = "When holding a pickaxe-type tool in the main hand, placing TNT with the off-hand will spawn an instantly exploding TNT. Explosion damage taken is reduced for yourself.";
    private static final int requirement = 22;
    GetAbilityLevel getAbilityLevel = new GetAbilityLevel();

    public int abilityLevel = getAbilityLevel.getAbilityLevelMining2(SkillModel.get().getSkillLevel(Skill.MINING), requirement);
    public ExplosiveMiningAbility()
    {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(
                                Skill.MINING, requirement
                        ),
                        new Requirement(
                                Skill.ATTACK, 18
                        )
                },
                "mining",
                2,
                10,
                true
        );
    }
    public float[] DAMAGE_REDUCTION = {0.4f, 0.45f, 0.5f, 0.55f, 0.65f};

    public boolean isHoldingPickaxe(Player player) {
        return player.getMainHandItem().getItem() instanceof PickaxeItem;
    }
    public boolean isHoldingTNT(Player player) {
        return player.getOffhandItem().is(Items.TNT);
    }
    public void onTNTPlaced(ServerPlayer player, BlockPos pos) {
        if (!isHoldingPickaxe(player) || !isHoldingTNT(player)) {
            return;
        }
        Level level = player.level();
        level.removeBlock(pos, false);
        PrimedTnt primedTnt = EntityType.TNT.create(level);
        if (primedTnt != null) {
            primedTnt.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
            primedTnt.setFuse(0);
            level.addFreshEntity(primedTnt);
        }
    }
}
