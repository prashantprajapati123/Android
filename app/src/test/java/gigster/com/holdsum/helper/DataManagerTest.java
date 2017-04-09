package gigster.com.holdsum.helper;

import android.content.Context;
import android.content.SharedPreferences;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.helper.enums.Mode;
import gigster.com.holdsum.services.ServicesManager;
import gigster.com.holdsum.viewmodel.RegistrationViewModel;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by tpaczesny on 2016-09-13.
 */
public class DataManagerTest {

    private DataManager mDataManager;
    private Context mMockedContext;
    private ServicesManager mMockedServicesManager;
    private EventBus mMockedEventBus;
    private SharedPreferences mMockedSharedPreferences;

    private String mTempServerToken;

    @Before
    public void setUp() {
        mMockedContext = mock(Context.class);
        mMockedSharedPreferences = mock(SharedPreferences.class);
        when(mMockedContext.getSharedPreferences(DataManager.PREFERENCES_FILE, Context.MODE_PRIVATE)).thenReturn(mMockedSharedPreferences);

        mMockedEventBus = mock(EventBus.class);
        mMockedServicesManager = mock(ServicesManager.class);

        mDataManager = new DataManager();
        mDataManager.init(mMockedContext, mMockedEventBus, mMockedServicesManager);
    }

    @Test
    public void setMode_invest_eventPostedAndValueCorrect() {
        mDataManager.setMode(Mode.INVEST);
        assertEquals(Mode.INVEST, mDataManager.getMode());
        verify(mMockedEventBus).post(any(Events.ModeChanged.class));
    }

    @Test
    public void setMode_borrow_eventPostedAndValueCorrect() {
        mDataManager.setMode(Mode.BORROW);
        assertEquals(Mode.BORROW, mDataManager.getMode());
        verify(mMockedEventBus).post(any(Events.ModeChanged.class));
    }

    @Test
    public void getResources() {
        mDataManager.getResources();
        verify(mMockedContext).getResources();
    }

    @Test
    public void getServerToken_hasValue() {
        SharedPreferences.Editor mockedEditor = mock(SharedPreferences.Editor.class);
        when(mMockedSharedPreferences.edit()).thenReturn(mockedEditor);
        when(mockedEditor.putString(DataManager.KEY_SERVER_TOKEN, "123")).thenReturn(mockedEditor);
        mDataManager.setServerToken("123");
        assertEquals("123",mDataManager.getServerToken());
    }

    @Test
    public void getServerToken_noValue() {
        when(mMockedSharedPreferences.getString(DataManager.KEY_SERVER_TOKEN, null)).thenReturn(null);
        assertNull(mDataManager.getServerToken());
    }

    @Test
    public void clearServerToken() {
        SharedPreferences.Editor mockedEditor = mock(SharedPreferences.Editor.class);
        when(mMockedSharedPreferences.edit()).thenReturn(mockedEditor);
        when(mockedEditor.remove(DataManager.KEY_SERVER_TOKEN)).thenReturn(mockedEditor);
        mDataManager.clearServerToken();
        verify(mockedEditor).remove(DataManager.KEY_SERVER_TOKEN);
        verify(mockedEditor).apply();
    }

    @Test
    public void saveServerToken() {
        SharedPreferences.Editor mockedEditor = mock(SharedPreferences.Editor.class);
        when(mMockedSharedPreferences.edit()).thenReturn(mockedEditor);
        when(mockedEditor.putString(DataManager.KEY_SERVER_TOKEN, "123")).thenReturn(mockedEditor);
        mDataManager.setServerToken("123");
        verify(mockedEditor).putString(DataManager.KEY_SERVER_TOKEN, "123");
        verify(mockedEditor).apply();
    }

    @Test
    public void getFundsAvailable() {
        mDataManager.setUserRegistrationData(new RegistrationViewModel());
        RegistrationViewModel data = mDataManager.getUserRegistrationData();

        data.monthlyIncome.set("10000");
        assertEquals("500", mDataManager.getFundsAvailable());
        data.monthlyIncome.set("2917");
        assertEquals("500", mDataManager.getFundsAvailable());
        data.monthlyIncome.set("2916");
        assertEquals("400", mDataManager.getFundsAvailable());
        data.monthlyIncome.set("2500");
        assertEquals("400", mDataManager.getFundsAvailable());
        data.monthlyIncome.set("2499");
        assertEquals("300", mDataManager.getFundsAvailable());
        data.monthlyIncome.set("2084");
        assertEquals("300", mDataManager.getFundsAvailable());
        data.monthlyIncome.set("2083");
        assertEquals("200", mDataManager.getFundsAvailable());
        data.monthlyIncome.set("1667");
        assertEquals("200", mDataManager.getFundsAvailable());
        data.monthlyIncome.set("1417");
        assertEquals("100", mDataManager.getFundsAvailable());
        data.monthlyIncome.set("1416");
        assertEquals("0", mDataManager.getFundsAvailable());
        data.monthlyIncome.set("0");
        assertEquals("0", mDataManager.getFundsAvailable());
    }

    @Test
    public void getNextPaydate() {
        DateFactory mockedDateFactory = mock(DateFactory.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, Calendar.OCTOBER, 1);
        when(mockedDateFactory.getNow()).thenReturn(calendar.getTime());
        mDataManager.setDateFactory(mockedDateFactory);

        // mocked now is 10/01/16
        mDataManager.setUserRegistrationData(new RegistrationViewModel());
        RegistrationViewModel data = mDataManager.getUserRegistrationData();

        data.nextPayDate.set("09/01/16");
        data.payFrequency.set(RegistrationViewModel.PAY_FREQUENCY_WEEKLY);
        assertEquals("10/06/16", Utils.formatDateForDisplay(mDataManager.getNextPaydate()));

        data.payFrequency.set(RegistrationViewModel.PAY_FREQUENCY_BIWEEKLY);
        assertEquals("10/13/16", Utils.formatDateForDisplay(mDataManager.getNextPaydate()));

        data.payFrequency.set(RegistrationViewModel.PAY_FREQUENCY_MONTHLY);
        assertEquals("11/01/16", Utils.formatDateForDisplay(mDataManager.getNextPaydate()));

        data.nextPayDate.set("10/31/16");
        data.payFrequency.set(RegistrationViewModel.PAY_FREQUENCY_WEEKLY);
        assertEquals("10/31/16", Utils.formatDateForDisplay(mDataManager.getNextPaydate()));

        data.payFrequency.set(RegistrationViewModel.PAY_FREQUENCY_BIWEEKLY);
        assertEquals("10/31/16", Utils.formatDateForDisplay(mDataManager.getNextPaydate()));

        data.payFrequency.set(RegistrationViewModel.PAY_FREQUENCY_MONTHLY);
        assertEquals("10/31/16", Utils.formatDateForDisplay(mDataManager.getNextPaydate()));
    }

    @Test
    public void getFollowingPaydate() {
        DateFactory mockedDateFactory = mock(DateFactory.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, Calendar.OCTOBER, 1);
        when(mockedDateFactory.getNow()).thenReturn(calendar.getTime());
        mDataManager.setDateFactory(mockedDateFactory);

        // mocked now is 10/01/16
        mDataManager.setUserRegistrationData(new RegistrationViewModel());
        RegistrationViewModel data = mDataManager.getUserRegistrationData();

        data.nextPayDate.set("09/01/16");
        data.payFrequency.set(RegistrationViewModel.PAY_FREQUENCY_WEEKLY);
        assertEquals("10/13/16", Utils.formatDateForDisplay(mDataManager.getFollowingPaydate()));

        data.payFrequency.set(RegistrationViewModel.PAY_FREQUENCY_BIWEEKLY);
        assertEquals("10/27/16", Utils.formatDateForDisplay(mDataManager.getFollowingPaydate()));

        data.payFrequency.set(RegistrationViewModel.PAY_FREQUENCY_MONTHLY);
        assertEquals("12/01/16", Utils.formatDateForDisplay(mDataManager.getFollowingPaydate()));
    }

    @Test
    public void getNextPaycheckAmount() {
        mDataManager.setUserRegistrationData(new RegistrationViewModel());
        RegistrationViewModel data = mDataManager.getUserRegistrationData();

        data.monthlyIncome.set("1000");
        data.payFrequency.set(RegistrationViewModel.PAY_FREQUENCY_WEEKLY);
        assertEquals(250.0, mDataManager.getNextPaycheckAmount(), 0.001);

        data.payFrequency.set(RegistrationViewModel.PAY_FREQUENCY_BIWEEKLY);
        assertEquals(500.0, mDataManager.getNextPaycheckAmount(), 0.001);

        data.payFrequency.set(RegistrationViewModel.PAY_FREQUENCY_MONTHLY);
        assertEquals(1000.0, mDataManager.getNextPaycheckAmount(), 0.001);
    }

    @Test
    public void getDefaultPaybackDate() {
        DateFactory mockedDateFactory = mock(DateFactory.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, Calendar.OCTOBER, 1);
        when(mockedDateFactory.getNow()).thenReturn(calendar.getTime());
        mDataManager.setDateFactory(mockedDateFactory);

        // mocked now is 10/01/16
        mDataManager.setUserRegistrationData(new RegistrationViewModel());
        RegistrationViewModel data = mDataManager.getUserRegistrationData();

        data.nextPayDate.set("09/01/16");
        data.payFrequency.set(RegistrationViewModel.PAY_FREQUENCY_WEEKLY);
        assertEquals("10/13/16", Utils.formatDateForDisplay(mDataManager.getDefaultPaybackDate()));

        data.payFrequency.set(RegistrationViewModel.PAY_FREQUENCY_BIWEEKLY);
        assertEquals("10/13/16", Utils.formatDateForDisplay(mDataManager.getDefaultPaybackDate()));

        data.payFrequency.set(RegistrationViewModel.PAY_FREQUENCY_MONTHLY);
        assertEquals("11/01/16", Utils.formatDateForDisplay(mDataManager.getDefaultPaybackDate()));
    }



}