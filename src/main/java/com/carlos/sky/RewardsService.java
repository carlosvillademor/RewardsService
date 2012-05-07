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
        if (!eligibilityService.isEligible(accountNumber)) {
            return rewards;
        }
        return rewards;
    }

}