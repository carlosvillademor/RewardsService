package com.carlos.sky;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static com.carlos.sky.ChannelSubscriptionCode.*;
import static com.carlos.sky.Reward.*;
import static java.util.Arrays.asList;
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
    public void givenAccountNumberAndSubscriptionsWhenGettingRewardsThenItShouldDelegateToEligibilityServiceToDecideIfUserIsEligible() throws TechnicalFailureException, InvalidAccountNumberException {
        //Given
        accountNumber = 12345L;
        channelSubscriptions = createChannelSubscriptions(asList(SPORTS));
        //When
        rewardsService.getRewards(accountNumber, channelSubscriptions);
        //Then
        verify(eligibilityService).isEligible(accountNumber);
    }

    @Test
    public void givenCustomerIsNotEligibleNoRewardsAreReturned() throws TechnicalFailureException, InvalidAccountNumberException {
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
    public void givenTechnicalFailureOnEligibilityServiceNoRewardsAreReturned() throws TechnicalFailureException, InvalidAccountNumberException {
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
    public void givenCustomerIsEligibleRewardsAreReturnedBasedOnTheirSubscription() throws TechnicalFailureException, InvalidAccountNumberException {
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
    public void KidsChannelsDoNotAddRewardsToCustomers() throws TechnicalFailureException, InvalidAccountNumberException {
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
    public void NewsChannelsDoNotAddRewardsToCustomers() throws TechnicalFailureException, InvalidAccountNumberException {
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

    @Test
    public void MusicChannelsAddRewardsToCustomers() throws TechnicalFailureException, InvalidAccountNumberException {
        //Given
        accountNumber = 12345L;
        channelSubscriptions = createChannelSubscriptions(asList(MUSIC, SPORTS));
        when(eligibilityService.isEligible(accountNumber)).thenReturn(true);
        //When
        List<Reward> rewards = rewardsService.getRewards(accountNumber, channelSubscriptions);
        //Then
        assertThat(rewards.size(), is(2));
        assertThat(rewards.containsAll(asList(CHAMPIONS_LEAGUE_FINAL_TICKET, KARAOKE_PRO_MICROPHONE)), is(true));
    }

    @Test
    public void MoviesChannelsAddRewardsToCustomers() throws TechnicalFailureException, InvalidAccountNumberException {
        //Given
        accountNumber = 12345L;
        channelSubscriptions = createChannelSubscriptions(asList(MOVIES, MUSIC, SPORTS));
        when(eligibilityService.isEligible(accountNumber)).thenReturn(true);
        //When
        List<Reward> rewards = rewardsService.getRewards(accountNumber, channelSubscriptions);
        //Then
        assertThat(rewards.size(), is(3));
        assertThat(rewards.containsAll(asList(PIRATES_OF_THE_CARIBBEAN_COLLECTION, CHAMPIONS_LEAGUE_FINAL_TICKET, KARAOKE_PRO_MICROPHONE)), is(true));
    }

    @Test
    public void givenInvalidCustomerNumberNoSubscriptionsAreReturnedAndUserIsNotified() throws TechnicalFailureException, InvalidAccountNumberException {
        //Given
        accountNumber = 12345L;
        channelSubscriptions = createChannelSubscriptions(asList(MOVIES, MUSIC, SPORTS));
        when(eligibilityService.isEligible(accountNumber)).thenThrow(InvalidAccountNumberException.class);
        //When
        try {
            rewardsService.getRewards(accountNumber, channelSubscriptions);
        } catch (InvalidAccountNumberException exception) {
            //Then
            assertThat(exception.getMessage(), is("Invalid account number"));
        }
    }

    private List<ChannelSubscriptionCode> createChannelSubscriptions(List<ChannelSubscriptionCode> channelSubscriptionCodes) {
        List<ChannelSubscriptionCode> channelSubscriptions = new ArrayList<ChannelSubscriptionCode>();
        for (ChannelSubscriptionCode channelSubscriptionCode : channelSubscriptionCodes) {
            channelSubscriptions.add(channelSubscriptionCode);
        }
        return channelSubscriptions;
    }

}