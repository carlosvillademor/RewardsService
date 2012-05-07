package com.carlos.sky;

import java.util.List;

public interface RewardsServiceInterface {

    public abstract List<Reward> getRewards(Long accountNumber, List<ChannelSubscriptionCode> channelSubscriptions);

}