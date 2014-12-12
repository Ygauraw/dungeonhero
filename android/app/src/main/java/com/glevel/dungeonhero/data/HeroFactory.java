package com.glevel.dungeonhero.data;

import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.characters.Ranks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/6/14.
 */
public class HeroFactory {

    public static List<Hero> getAll() {
        List<Hero> lst = new ArrayList<Hero>();
        lst.add(buildBerserker());
        lst.add(buildElf());
        lst.add(buildWizard());
        lst.add(buildThief());
        lst.add(buildDwarfWarrior());
        lst.add(buildDruid());
        return lst;
    }

    public static Hero buildBerserker() {
        Hero hero = new Hero("berserker", Ranks.ME, 18, 18, 12, 12, 6, 4, null, 0, 1);
        hero.equip(WeaponFactory.buildSword());
        hero.getSkills().add(SkillFactory.buildRage());
        hero.getSkills().add(SkillFactory.buildSwirlOfSwords());
        hero.getSkills().add(SkillFactory.buildWarcry());
        return hero;
    }

    public static Hero buildDwarfWarrior() {
        Hero hero = new Hero("dwarf_warrior", Ranks.ME, 25, 25, 14, 7, 9, 3, "dwarf_warrior", 0, 1);
        hero.equip(WeaponFactory.buildSword());
        hero.getSkills().add(SkillFactory.buildGroundSlam());
        hero.getSkills().add(SkillFactory.buildParryScience());
        hero.getSkills().add(SkillFactory.buildDrunkenMaster());
        return hero;
    }

    public static Hero buildElf() {
        Hero hero = new Hero("elf_ranger", Ranks.ME, 12, 12, 6, 12, 12, 5, null, 0, 1);
        hero.equip(WeaponFactory.buildSword());
        hero.getSkills().add(SkillFactory.buildFrostArrow());
        hero.getSkills().add(SkillFactory.buildCharm());
        hero.getSkills().add(SkillFactory.buildDodgeMaster());
        hero.getSkills().add(SkillFactory.buildStarFall());
        return hero;
    }

    public static Hero buildWizard() {
        Hero hero = new Hero("wizard", Ranks.ME, 10, 10, 6, 10, 14, 4, null, 0, 1);
        hero.equip(WeaponFactory.buildSword());
        hero.getSkills().add(SkillFactory.buildFireball());
        hero.getSkills().add(SkillFactory.buildSleep());
        hero.getSkills().add(SkillFactory.buildThunderStorm());
        hero.getSkills().add(SkillFactory.buildStoneSkin());
        hero.getSkills().add(SkillFactory.buildTerror());
        return hero;
    }

    public static Hero buildDruid() {
        Hero hero = new Hero("druid", Ranks.ME, 14, 14, 11, 8, 11, 4, null, 0, 1);
        hero.equip(WeaponFactory.buildSword());
        hero.getSkills().add(SkillFactory.buildParalysingPlants());
        hero.getSkills().add(SkillFactory.buildHealingHerbs());
        hero.getSkills().add(SkillFactory.buildWolfHowl());
        hero.getSkills().add(SkillFactory.buildCrowCurse());
        return hero;
    }

    public static Hero buildThief() {
        Hero hero = new Hero("thief", Ranks.ME, 15, 15, 8, 13, 9, 5, null, 0, 1);
        hero.equip(WeaponFactory.buildSword());
        hero.getSkills().add(SkillFactory.buildCamouflage());
        hero.getSkills().add(SkillFactory.buildFatalBlow());
        hero.getSkills().add(SkillFactory.buildPoisonousDarts());
        hero.getSkills().add(SkillFactory.buildDodgeMaster());
        return hero;
    }

}
