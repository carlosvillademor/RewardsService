package com.carlos.sky;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.carlos.sky.ChannelSubscriptionCode.KIDS;
import static com.carlos.sky.ChannelSubscriptionCode.NEWS;
import static com.carlos.sky.ChannelSubscriptionCode.SPORTS;
import static com.carlos.sky.Reward.CHAMPIONS_LEAGUE_FINAL_TICKET;
import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_LIST;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
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
        channelSubscriptions = createChannelSubscriptions(asList(SPORTS));
        //When
        rewardsService.getRewards(accountNumber, channelSubscriptions);
        //Then
        verify(eligibilityService).isEligible(accountNumber);
    }

    @Test
    public void givenCustomerIsNotEligibleNoRewardsAreReturned() throws TechnicalFailureException {
        //Given
        accountNumber = 12345L;
        channelSubscriptions = createChannelSubscriptions(asList(SPORTS));
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
        channelSubscriptions = createChannelSubscriptions(asList(SPORTS));
        when(eligibilityService.isEligible(accountNumber)).thenThrow(TechnicalFailureException.class);
        //When
        List<Reward> rewards = rewardsService.getRewards(accountNumber, channelSubscriptions);
        //Then
        assertThat(rewards, is(EMPTY_LIST));
    }

    @Test
    public void givenCustomerIsEligibleRewardsAreReturnedBasedOnTheirSubscription() throws TechnicalFailureException {
        //Given
        accountNumber = 12345L;
        channelSubscriptions = createChannelSubscriptions(asList(SPORTS));
        when(eligibilityService.isEligible(accountNumber)).thenReturn(true);
        //When
        List<Reward> rewards = rewardsService.getRewards(accountNumber, channelSubscriptions);
        //Then
        assertThat(rewards.size(), is(1));
        assertThat(rewards.get(0), is(CHAMPIONS_LEAGUE_FINAL_TICKET));
    }

    @Test
    public void KidsChannelsDoNotAddRewardsToCustomers() throws TechnicalFailureException {
        //Given
        accountNumber = 12345L;
        channelSubscriptions = createChannelSubscriptions(asList(KIDS, SPORTS));
        when(eligibilityService.isEligible(accountNumber)).thenReturn(true);
        //When
        List<Reward> rewards = rewardsService.getRewards(accountNumber, channelSubscriptions);
        //Then
        assertThat(rewards.size(), is(1));
        assertThat(rewards.get(0), is(CHAMPIONS_LEAGUE_FINAL_TICKET));
    }

    @Test
    public void NewsChannelsDoNotAddRewardsToCustomers() throws TechnicalFailureException {
        //Given
        accountNumber = 12345L;
        channelSubscriptions = createChannelSubscriptions(asList(NEWS, SPORTS));
        when(eligibilityService.isEligible(accountNumber)).thenReturn(true);
        //When
        List<Reward> rewards = rewardsService.getRewards(accountNumber, channelSubscriptions);
        //Then
        assertThat(rewards.size(), is(1));
        assertThat(rewards.get(0), is(CHAMPIONS_LEAGUE_FINAL_TICKET));
    }

    private List<ChannelSubscriptionCode> createChannelSubscriptions(List<ChannelSubscriptionCode> channelSubscriptionCodes) {
        List<ChannelSubscriptionCode> channelSubscriptions = new ArrayList<ChannelSubscriptionCode>();
        for (ChannelSubscriptionCode channelSubscriptionCode : channelSubscriptionCodes) {
            channelSubscriptions.add(channelSubscriptionCode);
        }
        return channelSubscriptions;
    }

}