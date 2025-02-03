package yezi.skillablereforged.common.commands.abilities;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class PassiveAbilityApplier {

    private boolean isGrazieryPassive0Active = false; // 避免重复注册
    private final AbilityManager abilityManager;
    private final AidSupportAbility aidSupportAbility = new AidSupportAbility();

    public PassiveAbilityApplier(Player player) {
        this.abilityManager = new AbilityManager(player);
      //  MinecraftForge.EVENT_BUS.register(this);
    }
    public void applyUnlockedAbilities() {
        List<Pair<String, Integer>> unlockedAbilities = abilityManager.getUnlockedPassiveAbilities();

        for (Pair<String, Integer> ability : unlockedAbilities) {
            String abilityType = ability.getLeft();
            int index = ability.getRight();

            switch (abilityType) {
                case "graziery" -> {
                    if (index == 0)
                        applyGrazieryPassive0();
                    if (index == 1)
                        applyGrazieryPassive1();
                    if (index == 2)
                        applyGrazieryPassive2();
                    if (index == 3)
                        applyGrazieryPassive3();
                }
                case "mining" -> {
                    if (index == 0)
                        applyMiningPassive0();
                    if (index == 1)
                        applyMiningPassive1();
                    if (index == 2)
                        applyMiningPassive2();
                }
                case "gathering" -> {
                    if (index == 0)
                        applyGatheringPassive0();
                    if (index == 1)
                        applyGatheringPassive1();
                    if (index == 2)
                        applyGatheringPassive2();
                }
                case "attack" -> {
                    if (index == 0)
                        applyAttackPassive0();
                    if (index == 1)
                        applyAttackPassive1();
                    if (index == 2)
                        applyAttackPassive2();
                    if (index == 3)
                        applyAttackPassive3();
                }
                case "defense" -> {
                    if (index == 0)
                        applyDefensePassive0();
                    if (index == 1)
                        applyDefensePassive1();
                    if (index == 2)
                        applyDefensePassive2();
                    if (index == 3)
                        applyDefensePassive3();
                }
                case "building" -> {
                    if (index == 0)
                        applyBuildingPassive0();
                    if (index == 1)
                        applyBuildingPassive1();
                    if (index == 2)
                        applyBuildingPassive2();
                }
                case "farming" -> {
                    if (index == 0)
                        applyFarmingPassive0();
                    if (index == 1)
                        applyFarmingPassive1();
                    if (index == 2)
                        applyFarmingPassive2();
                }
                case "agility" -> {
                    if (index == 0)
                        applyAgilityPassive0();
                    if (index == 1)
                        applyAgilityPassive1();
                    if (index == 2)
                        applyAgilityPassive2();
                    if (index == 3)
                        applyAgilityPassive3();
                }
                case "magic" -> {
                    if (index == 0)
                        applyMagicPassive0();
                    if (index == 1)
                        applyMagicPassive1();
                    if (index == 2)
                        applyMagicPassive2();
                    if (index == 3)
                        applyMagicPassive3();
                }
                case "shooting" -> {
                    if (index == 0)
                        applyShootingPassive0();
                    if (index == 1)
                        applyShootingPassive1();
                    if (index == 2)
                        applyShootingPassive2();
                    if (index == 3)
                        applyShootingPassive3();
                }
                default -> System.out.println("未知被动能力: " + abilityType);
            }
        }
    }
    private void applyGrazieryPassive0() {
        System.out.println("应用 graziery 被动能力, 索引: 0");
      //  AidSupportAbility ability = new AidSupportAbility();
        if (isGrazieryPassive0Active) return; // 防止重复注册监听器
        isGrazieryPassive0Active = true;

     //   System.out.println("启用 Graziery 0 级被动能力: 自动喂食强化");
        MinecraftForge.EVENT_BUS.register(new GrazieryPassive0Listener());
    }
    private void applyGrazieryPassive1() {
        System.out.println("应用 graziery 被动能力, 索引: 1");
        // 在这里实现具体逻辑
    }
    private void applyGrazieryPassive2() {
        System.out.println("应用 graziery 被动能力, 索引: 2");
        // 在这里实现具体逻辑
    }
    private void applyGrazieryPassive3() {
        System.out.println("应用 graziery 被动能力, 索引: 3");
        // 在这里实现具体逻辑
    }

    private void applyMiningPassive0() {
        System.out.println("应用 mining 被动能力, 索引: 0");
        // 在这里实现具体逻辑
    }
    private void applyMiningPassive1() {
        System.out.println("应用 mining 被动能力, 索引: 1");
        // 在这里实现具体逻辑
    }
    private void applyMiningPassive2() {
        System.out.println("应用 mining 被动能力, 索引: 2");
        // 在这里实现具体逻辑
    }
    private void applyGatheringPassive0() {
        System.out.println("应用 gathering 被动能力, 索引: 0");
        // 在这里实现具体逻辑
    }
    private void applyGatheringPassive1() {
        System.out.println("应用 gathering 被动能力, 索引: 1");
        // 在这里实现具体逻辑
    }
    private void applyGatheringPassive2() {
        System.out.println("应用 gathering 被动能力, 索引: 2");
        // 在这里实现具体逻辑
    }
    private void applyAttackPassive0() {
        System.out.println("应用 attack 被动能力, 索引: 0");
        // 在这里实现具体逻辑
    }
    private void applyAttackPassive1() {
        System.out.println("应用 attack 被动能力, 索引: 1");
        // 在这里实现具体逻辑
    }
    private void applyAttackPassive2() {
        System.out.println("应用 attack 被动能力, 索引: 2");
        // 在这里实现具体逻辑
    }
    private void applyAttackPassive3() {
        System.out.println("应用 attack 被动能力, 索引: 3");
        // 在这里实现具体逻辑
    }
    private void applyDefensePassive0() {
        System.out.println("应用 defense 被动能力, 索引: 0");
        // 在这里实现具体逻辑
    }
    private void applyDefensePassive1() {
        System.out.println("应用 defense 被动能力, 索引: 1");
        // 在这里实现具体逻辑
    }
    private void applyDefensePassive2() {
        System.out.println("应用 defense 被动能力, 索引: 2");
        // 在这里实现具体逻辑
    }
    private void applyDefensePassive3() {
        System.out.println("应用 defense 被动能力, 索引: 3");
        // 在这里实现具体逻辑
    }
    private void applyBuildingPassive0() {
        System.out.println("应用 building 被动能力, 索引: 0");
        // 在这里实现具体逻辑
    }
    private void applyBuildingPassive1() {
        System.out.println("应用 building 被动能力, 索引: 1");
        // 在这里实现具体逻辑
    }
    private void applyBuildingPassive2() {
        System.out.println("应用 building 被动能力, 索引: 2");
        // 在这里实现具体逻辑
    }
    private void applyFarmingPassive0() {
        System.out.println("应用 farming 被动能力, 索引: 0");
        // 在这里实现具体逻辑
    }
    private void applyFarmingPassive1() {
        System.out.println("应用 farming 被动能力, 索引: 1");
        // 在这里实现具体逻辑
    }
    private void applyFarmingPassive2() {
        System.out.println("应用 farming 被动能力, 索引: 2");
        // 在这里实现具体逻辑
    }
    private void applyAgilityPassive0() {
        System.out.println("应用 agility 被动能力, 索引: 0");
        // 在这里实现具体逻辑
    }
    private void applyAgilityPassive1() {
        System.out.println("应用 agility 被动能力, 索引: 1");
        // 在这里实现具体逻辑
    }
    private void applyAgilityPassive2() {
        System.out.println("应用 agility 被动能力, 索引: 2");
        // 在这里实现具体逻辑
    }
    private void applyAgilityPassive3() {
        System.out.println("应用 magic 被动能力, 索引: 2");
        // 在这里实现具体逻辑
    }
    private void applyMagicPassive0() {
        System.out.println("应用 magic 被动能力, 索引: 0");
        // 在这里实现具体逻辑
    }
    private void applyMagicPassive1() {
        System.out.println("应用 magic 被动能力, 索引: 1");
        // 在这里实现具体逻辑
    }
    private void applyMagicPassive2() {
        System.out.println("应用 magic 被动能力, 索引: 2");
        // 在这里实现具体逻辑
    }
    private void applyMagicPassive3() {
        System.out.println("应用 magic 被动能力, 索引: 2");
        // 在这里实现具体逻辑
    }
    private void applyShootingPassive0() {
        System.out.println("应用 shooting 被动能力, 索引: 0");
        // 在这里实现具体逻辑
    }
    private void applyShootingPassive1() {
        System.out.println("应用 shooting 被动能力, 索引: 1");
        // 在这里实现具体逻辑
    }
    private void applyShootingPassive2() {
        System.out.println("应用 shooting 被动能力, 索引: 2");
        // 在这里实现具体逻辑
    }
    private void applyShootingPassive3() {
        System.out.println("应用 shooting 被动能力, 索引: 3");
        // 在这里实现具体逻辑
    }
}