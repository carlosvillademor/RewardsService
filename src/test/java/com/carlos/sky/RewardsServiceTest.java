package com.carlos.sky;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RewardsServiceTest {

    @Mock
    private EligibilityServiceInterface eligibilityService;

    private RewardsServiceInterface rewardsService;

    private Long accountNumber;
    private List<ChannelSubscriptionCode> channelSubscriptions;

    @Before
    public void setUp() {
        rewardsService = new RewardsService(eligibilityService);
    }

    @Test
    public void givenAccountNumberAndSubscriptionsWhenGettingRewardsThenItShouldDelegateToEligibilityServiceToDecideIfUserIsEligible() {
        //Given
        accountNumber = 12345L;
        channelSubscriptions = createChannelSubscriptions();
        //When
        rewardsService.getRewards(accountNumber, channelSubscriptions);
        //Then
        verify(eligibilityService).isEligible(accountNumber);
    }

    private List<ChannelSubscriptionCode> createChannelSubscriptions() {
        List<ChannelSubscriptionCode> channelSubscriptions = new ArrayList<ChannelSubscriptionCode>();
        channelSubscriptions.add(ChannelSubscriptionCode.SPORTS);
        return channelSubscriptions;
    }

}