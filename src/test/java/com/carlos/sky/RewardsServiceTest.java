package com.carlos.sky;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.EMPTY_LIST;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    public void givenAccountNumberAndSubscriptionsWhenGettingRewardsThenItShouldDelegateToEligibilityServiceToDecideIfUserIsEligible() throws TechnicalFailureException {
        //Given
        accountNumber = 12345L;
        channelSubscriptions = createChannelSubscriptions();
        //When
        rewardsService.getRewards(accountNumber, channelSubscriptions);
        //Then
        verify(eligibilityService).isEligible(accountNumber);
    }

    @Test
    public void givenCustomerIsNotEligibleNoRewardsAreReturned() throws TechnicalFailureException {
        //Given
        accountNumber = 12345L;
        channelSubscriptions = createChannelSubscriptions();
        when(eligibilityService.isEligible(accountNumber)).thenReturn(false);
        //When
        List<Reward> rewards = rewardsService.getRewards(accountNumber, channelSubscriptions);
        //Then
        assertThat(rewards, is(EMPTY_LIST));
    }

    @Test
    public void givenTechnicalFailureOnEligibilityServiceNoRewardsAreReturned() throws TechnicalFailureException {
        //Given
        accountNumber = 12345L;
        channelSubscriptions = createChannelSubscriptions();
        when(eligibilityService.isEligible(accountNumber)).thenThrow(TechnicalFailureException.class);
        //When
        List<Reward> rewards = rewardsService.getRewards(accountNumber, channelSubscriptions);
        //Then
        assertThat(rewards, is(EMPTY_LIST));
    }

    private List<ChannelSubscriptionCode> createChannelSubscriptions() {
        List<ChannelSubscriptionCode> channelSubscriptions = new ArrayList<ChannelSubscriptionCode>();
        channelSubscriptions.add(ChannelSubscriptionCode.SPORTS);
        return channelSubscriptions;
    }

}