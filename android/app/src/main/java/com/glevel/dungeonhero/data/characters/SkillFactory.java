package com.glevel.dungeonhero.data.characters;

import com.glevel.dungeonhero.models.effects.BuffEffect;
import com.glevel.dungeonhero.models.effects.CamouflageEffect;
import com.glevel.dungeonhero.models.effects.DamageEffect;
import com.glevel.dungeonhero.models.effects.Effect;
import com.glevel.dungeonhero.models.effects.PermanentEffect;
import com.glevel.dungeonhero.models.effects.PoisonEffect;
import com.glevel.dungeonhero.models.effects.StunEffect;
import com.glevel.dungeonhero.models.items.Characteristics;
import com.glevel.dungeonhero.models.skills.ActiveSkill;
import com.glevel.dungeonhero.models.skills.PassiveSkill;
import com.glevel.dungeonhero.models.skills.Skill;

import org.andengine.util.color.Color;

/**
 * Created by guillaume ON 10/6/14.
 */
public class SkillFactory {

    public static Skill buildRage() {
        BuffEffect extra = new BuffEffect(null, Characteristics.PROTECTION, -5, 3, null, 0);
        Effect effect = new BuffEffect(null, Characteristics.DAMAGE, 10, 3, extra, 0, Color.RED);
        return new ActiveSkill("berserker_rage", 0, true, 0, effect);
    }

    public static Skill buildSwirlOfSwords() {
        Effect effect = new DamageEffect("blood.png", -12, 0);
        return new ActiveSkill("swirl_swords", 0, true, 1, effect);
    }

    public static Skill buildWarcry() {
        Effect effect = new StunEffect("blood.png", Characteristics.STRENGTH, 12, 0);
        return new ActiveSkill("war_cry", 0, true, 7, effect);
    }

    public static Skill buildFatalBlow() {
        Effect effect = new PermanentEffect(Characteristics.CRITICAL, 15, null, 0);
        return new PassiveSkill("fatal_blow", 0, effect);
    }

    public static Skill buildCamouflage() {
        Effect effect = new CamouflageEffect(null, 10, 0);
        return new ActiveSkill("camouflage", 0, true, 0, effect);
    }

    public static Skill buildPoisonousDarts() {
        Effect extra = new PoisonEffect("blood.png", -5, 3, null, 0);
        Effect effect = new DamageEffect("blood.png", -8, extra, 0);
        return new ActiveSkill("poisonous_darts", 0, false, 0, effect);
    }

    public static Skill buildDodgeMaster() {
        Effect effect = new PermanentEffect(Characteristics.DODGE, 15, null, 0);
        return new PassiveSkill("dodge_master", 0, effect);
    }

    public static Skill buildGroundSlam() {
        Effect extra = new StunEffect("blood.png", Characteristics.STRENGTH, 3, 0);
        Effect effect = new DamageEffect("blood.png", -12, extra, 0);
        return new ActiveSkill("ground_slam", 0, true, 1, effect);
    }

    public static Skill buildParryScience() {
        Effect effect = new PermanentEffect(Characteristics.BLOCK, 15, null, 0);
        return new PassiveSkill("parry_science", 0, effect);
    }

    public static Skill buildDrunkenMaster() {
        Effect extra = new BuffEffect("blood.png", Characteristics.BLOCK, -100, 8, null, 0);
        Effect effect = new DamageEffect(null, 15, extra, 0);
        return new ActiveSkill("drunken_master", 0, true, 0, effect);
    }

    public static Skill buildFrostArrow() {
        Effect extra = new BuffEffect("blood.png", Characteristics.MOVEMENT, -20, 6, null, 0);
        Effect effect = new DamageEffect("blood.png", -12, extra, 0);
        return new ActiveSkill("frost_arrow", 0, false, 0, effect);
    }

    public static Skill buildCharm() {
        Effect effect = new StunEffect("blood.png", Characteristics.SPIRIT, 8, 0);
        return new ActiveSkill("charm", 0, false, 0, effect);
    }

    public static Skill buildStarFall() {
        Effect effect = new DamageEffect("blood.png", -8, null, 0);
        return new ActiveSkill("star_fall", 0, false, 2, effect);
    }

    public static Skill buildParalysingPlants() {
        Effect extra = new StunEffect("blood.png", Characteristics.STRENGTH, 5, 0);
        Effect effect = new BuffEffect("blood.png", Characteristics.MOVEMENT, -20, 5, extra, 0);
        return new ActiveSkill("paralysing_plant", 0, false, 3, effect);
    }

    public static Skill buildHealingHerbs() {
        Effect effect = new DamageEffect(null, 10, null, 0);
        return new ActiveSkill("healing_plants", 0, true, 0, effect);
    }

    public static Skill buildWolfHowl() {
        Effect extra = new BuffEffect("blood.png", Characteristics.DODGE, 30, 5, null, 0);
        Effect effect = new BuffEffect("blood.png", Characteristics.CRITICAL, 25, 5, extra, 0);
        return new ActiveSkill("wolf_howl", 0, true, 0, effect);
    }

    public static Skill buildCrowCurse() {
        Effect extra = new BuffEffect("blood.png", Characteristics.PROTECTION, -10, 5, null, 0);
        Effect effect = new BuffEffect("blood.png", Characteristics.DAMAGE, -10, 5, extra, 0);
        return new ActiveSkill("crow_curse", 0, false, 3, effect);
    }

    public static Skill buildFireball() {
        Effect extra = new PoisonEffect("blood.png", -3, 2, null, 0);
        Effect effect = new DamageEffect("blood.png", -15, extra, 0);
        return new ActiveSkill("fireball", 0, false, 1, effect);
    }

    public static Skill buildSleep() {
        Effect effect = new StunEffect("blood.png", Characteristics.SPIRIT, 10, 0);
        return new ActiveSkill("sleep", 0, false, 0, effect);
    }

    public static Skill buildThunderStorm() {
        Effect effect = new DamageEffect("blood.png", -7, null, 0);
        return new ActiveSkill("thunder_storm", 0, false, 2, effect);
    }

    public static Skill buildStoneSkin() {
        Effect effect = new BuffEffect(null, Characteristics.PROTECTION, 15, 7, null, 0, new Color(0.65f, 0.24f, 0.08f, 1.0f));
        return new ActiveSkill("stone_skin", 0, true, 0, effect);
    }

    public static Skill buildTerror() {
        Effect extra = new StunEffect("blood.png", Characteristics.SPIRIT, 6, 0);
        Effect effect = new PoisonEffect("blood.png", -3, 5, extra, 0);
        return new ActiveSkill("terror", 0, false, 3, effect);
    }

}