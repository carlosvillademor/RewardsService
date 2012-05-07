package com.carlos.sky;

import java.util.ArrayList;
import java.util.List;

public class RewardsService implements RewardsServiceInterface {

    private EligibilityServiceInterface eligibilityService;

    public RewardsService(EligibilityServiceInterface eligibilityService) {
        this.eligibilityService = eligibilityService;
    }

    public List<Reward> getRewards(Long accountNumber, List<ChannelSubscriptionCode> channelSubscriptions) {
        List<Reward> rewards = new ArrayList<Reward>();
        try {
            if (!eligibilityService.isEligible(accountNumber)) {
                return rewards;
            }
            rewards = mapRewards(channelSubscriptions);
        } finally {
            return rewards;
        }
    }

    private List<Reward> mapRewards(List<ChannelSubscriptionCode> channelSubscriptions) {
        List<Reward> rewards = new ArrayList<Reward>();
        for (ChannelSubscriptionCode channelSubscription : channelSubscriptions) {
            if (ChannelSubscriptionCode.SPORTS.equals(channelSubscription)) {
                rewards.add(Reward.CHAMPIONS_LEAGUE_FINAL_TICKET);
            }
        }
        return rewards;
    }

}