package com.carlos.sky;

import java.util.List;

public class RewardsService implements RewardsServiceInterface {

    private EligibilityServiceInterface eligibilityService;

    public RewardsService(EligibilityServiceInterface eligibilityService) {
        this.eligibilityService = eligibilityService;
    }

    public List<Reward> getRewards(Long accountNumber, List<ChannelSubscriptionCode> channelSubscriptions) {
        eligibilityService.isEligible(accountNumber);
        return null;
    }

}