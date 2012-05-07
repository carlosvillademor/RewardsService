package com.carlos.sky;

import java.util.ArrayList;
import java.util.List;

import static com.carlos.sky.ChannelSubscriptionCode.MOVIES;
import static com.carlos.sky.ChannelSubscriptionCode.MUSIC;
import static com.carlos.sky.ChannelSubscriptionCode.SPORTS;
import static com.carlos.sky.Reward.CHAMPIONS_LEAGUE_FINAL_TICKET;
import static com.carlos.sky.Reward.KARAOKE_PRO_MICROPHONE;
import static com.carlos.sky.Reward.PIRATES_OF_THE_CARIBBEAN_COLLECTION;

public class RewardsService implements RewardsServiceInterface {

    public static final String INVALID_ACCOUNT_NUMBER = "Invalid account number";
    private EligibilityServiceInterface eligibilityService;

    public RewardsService(EligibilityServiceInterface eligibilityService) {
        this.eligibilityService = eligibilityService;
    }

    public List<Reward> getRewards(Long accountNumber, List<ChannelSubscriptionCode> channelSubscriptions) throws InvalidAccountNumberException {
        List<Reward> rewards = new ArrayList<Reward>();
        try {
            if (!eligibilityService.isEligible(accountNumber)) {
                return rewards;
            }
            return mapRewards(channelSubscriptions);
        } catch (TechnicalFailureException technicalException) {
            return rewards;
        } catch (InvalidAccountNumberException invalidAccountNumberException) {
            throw new InvalidAccountNumberException(INVALID_ACCOUNT_NUMBER, invalidAccountNumberException);
        }
    }

    private List<Reward> mapRewards(List<ChannelSubscriptionCode> channelSubscriptions) {
        List<Reward> rewards = new ArrayList<Reward>();
        for (ChannelSubscriptionCode channelSubscription : channelSubscriptions) {
            if (SPORTS.equals(channelSubscription)) {
                rewards.add(CHAMPIONS_LEAGUE_FINAL_TICKET);
            } else if (MUSIC.equals(channelSubscription)) {
                rewards.add(KARAOKE_PRO_MICROPHONE);
            } else if (MOVIES.equals(channelSubscription)) {
                rewards.add(PIRATES_OF_THE_CARIBBEAN_COLLECTION);
            }
        }
        return rewards;
    }

}