package gigster.com.holdsum.presenters;

import org.junit.Before;
import org.junit.Test;

import gigster.com.holdsum.R;
import gigster.com.holdsum.activities.ConfirmationActivity;
import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.helper.FlowController;
import gigster.com.holdsum.model.SigningUrl;
import gigster.com.holdsum.services.APIError;
import gigster.com.holdsum.services.RetrofitRequestHandle;
import gigster.com.holdsum.services.ServicesManager;
import gigster.com.holdsum.views.DocuSignWebView;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by tpaczesny on 2016-09-26.
 */
public class ConfirmationPresenterTest extends AbstractHoldsumPresenterTest<ConfirmationPresenter> {

    private ConfirmationActivity mMockedConfirmationActivity;

    @Before
    public void setUp() {
        setUpBasicMocks(ConfirmationPresenter.class, ConfirmationActivity.class);
        mMockedConfirmationActivity = (ConfirmationActivity) mMockedActivity;
    }

    @Test
    public void onViewCreated_startRequestForURL() {
        mPresenter.setLoanId(1);
        mPresenter.onViewCreated();

        verify(mMockedConfirmationActivity).showProgressDialog(R.string.loading_docs);
        verify(mMockedServicesManager).getSigningUrl(1);
    }

    @Test
    public void onRequestSuccess_loadUrl() {
        SigningUrl signingUrl = new SigningUrl();
        signingUrl.url = "http://test.com";
        mPresenter.onEvent(new Events.ServiceRequestSuccess<>(
                new RetrofitRequestHandle(null, ServicesManager.RequestType.SIGNING_URL), signingUrl));

        verify(mMockedConfirmationActivity).hideProgressDialog();
        verify(mMockedConfirmationActivity).loadUrl("http://test.com");
    }

    @Test
    public void onRequestFailed_showError() {
        APIError mockApiError = mock(APIError.class);
        when(mockApiError.getMessage()).thenReturn("message");

        Events.ServiceRequestFailed failEvent = new Events.ServiceRequestFailed(
                new RetrofitRequestHandle(null, ServicesManager.RequestType.SIGNING_URL), mockApiError);
        mPresenter.onEvent(failEvent);
        verify(mMockedConfirmationActivity).hideProgressDialog();
        verify(mMockedConfirmationActivity).showMessage("message", R.string.doc_get_signing_url_failed);
    }


    @Test
    public void onConfirmLoan_signingIncomplete_showPrompt() {
        mPresenter.setSigningStatus(null);
        mPresenter.onConfirmLoan();
        verify(mMockedConfirmationActivity).showMessage(R.string.doc_signing_prompt);
    }

    @Test
    public void onConfirmLoan_signingCanceled_showPrompt() {
        mPresenter.setSigningStatus(DocuSignWebView.SigningStatus.CANCEL);
        mPresenter.onConfirmLoan();
        verify(mMockedConfirmationActivity).showMessage(R.string.doc_signing_prompt);
    }

    @Test
    public void onConfirmLoan_signingCompleted_goToHomeScreen() {
        mPresenter.setSigningStatus(DocuSignWebView.SigningStatus.SIGNING_COMPLETE);
        mPresenter.onConfirmLoan();
        verify(mMockedFlowController).setTo(mMockedConfirmationActivity, FlowController.Screen.HOME);
    }

    @Test
    public void onSigningFinished_signingCanceled_showErrorAndGoBack() {
        mPresenter.onSigningFinished(DocuSignWebView.SigningStatus.CANCEL);
        verify(mMockedConfirmationActivity).showMessage(R.string.doc_signing_error);
        verify(mMockedFlowController).navigateBack(mMockedConfirmationActivity);
    }

    @Test
    public void onSigningFinished_signingDeclined_showErrorAndGoBack() {
        mPresenter.onSigningFinished(DocuSignWebView.SigningStatus.DECLINE);
        verify(mMockedConfirmationActivity).showMessage(R.string.doc_signing_error);
        verify(mMockedFlowController).navigateBack(mMockedConfirmationActivity);
    }

    @Test
    public void onSigningFinished_signingException_showErrorAndGoBack() {
        mPresenter.onSigningFinished(DocuSignWebView.SigningStatus.EXCEPTION);
        verify(mMockedConfirmationActivity).showMessage(R.string.doc_signing_error);
        verify(mMockedFlowController).navigateBack(mMockedConfirmationActivity);
    }

    @Test
    public void onSigningFinished_signingCompleted_hideSignUI() {
        mPresenter.onSigningFinished(DocuSignWebView.SigningStatus.SIGNING_COMPLETE);
        verify(mMockedConfirmationActivity).hideSignUI();
    }

}