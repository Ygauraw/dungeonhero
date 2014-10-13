package com.glevel.dungeonhero.game;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.BookChooserActivity;
import com.glevel.dungeonhero.activities.HomeActivity;
import com.glevel.dungeonhero.game.base.CustomGameActivity;
import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.game.base.interfaces.OnDiscussionReplySelected;
import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.characters.Pnj;
import com.glevel.dungeonhero.models.characters.Unit;
import com.glevel.dungeonhero.models.discussions.Discussion;
import com.glevel.dungeonhero.models.discussions.Reaction;
import com.glevel.dungeonhero.utils.MusicManager;
import com.glevel.dungeonhero.views.CustomAlertDialog;
import com.glevel.dungeonhero.views.LifeBar;

import java.util.List;

public class GUIManager {

    private CustomGameActivity mActivity;

    private Dialog mLoadingScreen, mGameMenuDialog, mHeroInfoDialog, mDiscussionDialog, mRewardDialog;
    private TextView mBigLabel;
    private Animation mBigLabelAnimation;
    private ViewGroup mSelectedElementLayout, mActiveHeroLayout, mQueueLayout;

    public GUIManager(CustomGameActivity activity) {
        mActivity = activity;
    }

    public void initGUI() {
        mQueueLayout = (ViewGroup) mActivity.findViewById(R.id.queue);
        mSelectedElementLayout = (ViewGroup) mActivity.findViewById(R.id.selectedElement);

        mActiveHeroLayout = (ViewGroup) mActivity.findViewById(R.id.hero);
        mActiveHeroLayout.setOnClickListener(mActivity);

        mActivity.findViewById(R.id.bag).setOnClickListener(mActivity);

        // setup big label TV
        mBigLabelAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.big_label_in_game);
        mBigLabel = (TextView) mActivity.findViewById(R.id.bigLabel);
    }

    public void displayBigLabel(final String text, final int color) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBigLabel.setVisibility(View.VISIBLE);
                mBigLabel.setText("" + text);
                mBigLabel.setTextColor(mActivity.getResources().getColor(color));
                mBigLabel.startAnimation(mBigLabelAnimation);
            }
        });
    }

    public void hideLoadingScreen() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoadingScreen.dismiss();
                mActivity.findViewById(R.id.rootLayout).setVisibility(View.VISIBLE);
            }
        });
    }

    public void openGameMenu() {
        mGameMenuDialog = new Dialog(mActivity, R.style.FullScreenDialog);
        mGameMenuDialog.setContentView(R.layout.dialog_game_menu);
        mGameMenuDialog.setCancelable(true);
        Animation menuButtonAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.bottom_in);

        // resume game button
        mGameMenuDialog.findViewById(R.id.resumeGameButton).setAnimation(menuButtonAnimation);
        mGameMenuDialog.findViewById(R.id.resumeGameButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.playSound(mActivity.getApplicationContext(), R.raw.button_sound);
                mGameMenuDialog.dismiss();
            }
        });

        // leave quest button
        mGameMenuDialog.findViewById(R.id.leaveQuestButton).setAnimation(menuButtonAnimation);
        mGameMenuDialog.findViewById(R.id.leaveQuestButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.playSound(mActivity.getApplicationContext(), R.raw.button_sound);
                Dialog confirmDialog = new CustomAlertDialog(mActivity, R.style.Dialog, mActivity.getString(R.string.confirm_leave_quest),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MusicManager.playSound(mActivity.getApplicationContext(), R.raw.button_sound);
                                if (which == R.id.okButton) {
                                    mActivity.startActivity(new Intent(mActivity, BookChooserActivity.class));
                                    mActivity.finish();
                                }
                                dialog.dismiss();
                            }
                        });
                confirmDialog.show();
            }
        });

        // exit button
        mGameMenuDialog.findViewById(R.id.exitButton).setAnimation(menuButtonAnimation);
        mGameMenuDialog.findViewById(R.id.exitButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.playSound(mActivity.getApplicationContext(), R.raw.button_sound);
                mActivity.startActivity(new Intent(mActivity, HomeActivity.class));
                mActivity.finish();
            }
        });

        mGameMenuDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mActivity.resumeGame();
            }
        });

        mGameMenuDialog.show();
        menuButtonAnimation.start();
    }

    public void onPause() {
        if (mGameMenuDialog != null) {
            mGameMenuDialog.dismiss();
        }
        if (mLoadingScreen != null) {
            mLoadingScreen.dismiss();
        }
        if (mHeroInfoDialog != null) {
            mHeroInfoDialog.dismiss();
        }
        if (mDiscussionDialog != null) {
            mDiscussionDialog.dismiss();
        }
        if (mRewardDialog != null) {
            mRewardDialog.dismiss();
        }
    }

    public void showLoadingScreen() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // setup loading screen
                mLoadingScreen = new Dialog(mActivity, R.style.FullScreenDialog);
                mLoadingScreen.setContentView(R.layout.dialog_game_loading);
                mLoadingScreen.setCancelable(false);
                mLoadingScreen.setCanceledOnTouchOutside(false);

                // animate loading dots
                Animation loadingDotsAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.loading_dots);
                ((TextView) mLoadingScreen.findViewById(R.id.loadingDots)).startAnimation(loadingDotsAnimation);

                mLoadingScreen.show();
            }
        });
    }

    public void showGameElementInfo(final GameElement gameElement) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                TextView nameTV = (TextView) mSelectedElementLayout.findViewById(R.id.name);
                LifeBar lifeBar = (LifeBar) mSelectedElementLayout.findViewById(R.id.life);

                nameTV.setText(gameElement.getName());

                if (gameElement instanceof Unit) {
                    Unit unit = (Unit) gameElement;
                    nameTV.setCompoundDrawablesWithIntrinsicBounds(unit.getImage(), 0, 0, 0);

                    lifeBar.updateLife(unit.getLifeRatio());
                    lifeBar.setVisibility(View.VISIBLE);
                } else {
                    nameTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    lifeBar.setVisibility(View.GONE);
                }

                mQueueLayout.setVisibility(View.GONE);
                mSelectedElementLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideGameElementInfo(final boolean isSafe) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSelectedElementLayout.setVisibility(View.GONE);
                if (!isSafe) {
                    mQueueLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void updateActiveHeroLayout(final Hero hero) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LifeBar lifeBar = (LifeBar) mActiveHeroLayout.findViewById(R.id.life);
                lifeBar.updateLife(hero.getLifeRatio());
            }
        });
    }

    public void updateQueue(final Unit activeCharacter, final List<Unit> queue, final boolean isSafe) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isSafe) {
                    mQueueLayout.setVisibility(View.GONE);
                } else {
                    mQueueLayout.setVisibility(View.VISIBLE);
                    updateQueueCharacter((ViewGroup) mQueueLayout.findViewById(R.id.activeCharacter), activeCharacter);
                    if (queue.size() > 1) {
                        updateQueueCharacter((ViewGroup) mQueueLayout.findViewById(R.id.nextCharacter), queue.get(0));
                    } else {
                        mQueueLayout.findViewById(R.id.nextCharacter).setVisibility(View.GONE);
                    }
                    if (queue.size() > 2) {
                        updateQueueCharacter((ViewGroup) mQueueLayout.findViewById(R.id.nextnextCharacter), queue.get(1));
                    } else {
                        mQueueLayout.findViewById(R.id.nextnextCharacter).setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void updateQueueCharacter(ViewGroup view, Unit unit) {
        view.setVisibility(View.VISIBLE);
        ((ImageView) view.findViewById(R.id.image)).setImageResource(unit.getImage());
        ((LifeBar) view.findViewById(R.id.life)).updateLife(unit.getLifeRatio());
    }

    public void showHeroInfo(Hero hero) {
        mHeroInfoDialog = new Dialog(mActivity, R.style.Dialog);
        mHeroInfoDialog.setContentView(R.layout.in_game_hero_details);
        mHeroInfoDialog.setCancelable(true);

        ((TextView) mHeroInfoDialog.findViewById(R.id.name)).setText(hero.getName());
        ((TextView) mHeroInfoDialog.findViewById(R.id.description)).setText(hero.getDescription());
        ((TextView) mHeroInfoDialog.findViewById(R.id.hp)).setText(mActivity.getString(R.string.hp_in_game, hero.getCurrentHP(), hero.getHp()));
        ((TextView) mHeroInfoDialog.findViewById(R.id.level)).setText(mActivity.getString(R.string.level_in_game, hero.getLevel(), hero.getXp(), 1000));
        ((TextView) mHeroInfoDialog.findViewById(R.id.strength)).setText("" + hero.getStrength());
        ((TextView) mHeroInfoDialog.findViewById(R.id.dexterity)).setText("" + hero.getDexterity());
        ((TextView) mHeroInfoDialog.findViewById(R.id.spirit)).setText("" + hero.getSpirit());
        ((TextView) mHeroInfoDialog.findViewById(R.id.attack)).setText("" + hero.getCurrentAttack());
        ((TextView) mHeroInfoDialog.findViewById(R.id.block)).setText("" + hero.getCurrentBlock());
        ((TextView) mHeroInfoDialog.findViewById(R.id.frags)).setText("" + hero.getFrags());

        mHeroInfoDialog.show();
    }

    public void showBag(Hero hero) {

    }

    public void showDiscussion(final Pnj pnj, final Discussion discussion, final OnDiscussionReplySelected callback) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mDiscussionDialog == null || !mDiscussionDialog.isShowing()) {
                    mDiscussionDialog = new Dialog(mActivity, R.style.Dialog);
                    mDiscussionDialog.setContentView(R.layout.in_game_discussion);
                    mDiscussionDialog.setCancelable(false);

                    TextView pnjName = (TextView) mDiscussionDialog.findViewById(R.id.name);
                    pnjName.setText(discussion.getName());
                    pnjName.setCompoundDrawablesWithIntrinsicBounds(discussion.getImage(), 0, 0, 0);
                    ((TextView) mDiscussionDialog.findViewById(R.id.message)).setText(discussion.getMessage());

                    ViewGroup reactionsLayout = (ViewGroup) mDiscussionDialog.findViewById(R.id.reactions);
                    reactionsLayout.removeAllViews();

                    LayoutInflater inflater = mActivity.getLayoutInflater();
                    TextView reactionTV;
                    if (discussion.getReactions() != null) {
                        for (Reaction reaction : discussion.getReactions()) {
                            reactionTV = (TextView) inflater.inflate(R.layout.in_game_discussion_reply, null);
                            reactionTV.setText(reaction.getMessage());
                            reactionTV.setTag(R.string.id, reaction.getSkipNextSteps());
                            reactionTV.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mDiscussionDialog.dismiss();
                                    callback.onReplySelected(pnj, (Integer) view.getTag(R.string.id));
                                }
                            });
                            reactionsLayout.addView(reactionTV);
                        }
                    } else {
                        reactionTV = (TextView) inflater.inflate(R.layout.in_game_discussion_reply, null);
                        reactionTV.setText(R.string.ok);
                        reactionTV.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mDiscussionDialog.dismiss();
                                callback.onReplySelected(pnj, 0);
                            }
                        });
                        reactionsLayout.addView(reactionTV);
                    }

                    mDiscussionDialog.show();
                }
            }
        });
    }

    public void showReward(final Reward reward) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mRewardDialog == null || !mRewardDialog.isShowing()) {
                    mRewardDialog = new Dialog(mActivity, R.style.Dialog);
                    mRewardDialog.setContentView(R.layout.in_game_reward);
                    mRewardDialog.setCancelable(false);

                    TextView itemTV = (TextView) mRewardDialog.findViewById(R.id.item);
                    TextView goldTV = (TextView) mRewardDialog.findViewById(R.id.gold);
                    TextView xpTV = (TextView) mRewardDialog.findViewById(R.id.xp);

                    if (reward == null) {
                        itemTV.setText(R.string.found_nothing);
                        itemTV.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.found_nothing, 0, 0);
                        goldTV.setVisibility(View.GONE);
                        xpTV.setVisibility(View.GONE);
                    } else {
                        if (reward.getItem() != null) {
                            itemTV.setText(mActivity.getString(R.string.found_item, mActivity.getString(reward.getItem().getName())));
                            itemTV.setCompoundDrawablesWithIntrinsicBounds(0, reward.getItem().getImage(), 0, 0);
                            itemTV.setVisibility(View.VISIBLE);
                        } else {
                            itemTV.setVisibility(View.GONE);
                        }

                        if (reward.getGold() > 0) {
                            goldTV.setText(mActivity.getString(R.string.found_gold, reward.getGold()));
                            goldTV.getCompoundDrawables()[0].setColorFilter(goldTV.getCurrentTextColor(), PorterDuff.Mode.MULTIPLY);
                            goldTV.setVisibility(View.VISIBLE);
                        } else {
                            goldTV.setVisibility(View.GONE);
                        }

                        if (reward.getXp() > 0) {
                            xpTV.setText(mActivity.getString(R.string.reward_xp, reward.getXp()));
                            xpTV.setVisibility(View.VISIBLE);
                        } else {
                            xpTV.setVisibility(View.GONE);
                        }
                    }

                    mRewardDialog.findViewById(R.id.dismiss_button).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mRewardDialog.dismiss();
                        }
                    });

                    mRewardDialog.show();
                }
            }
        });
    }

}
