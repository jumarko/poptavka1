package com.eprovement.poptavka.rest.payment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.eprovement.poptavka.domain.enums.PaypalTransactionStatus;

/**
 * The controller, which handles the paypal payment notification.
 *
 * @author Martin Zupko
 *
 */
@Controller
@RequestMapping("/ipn")
public class PaypalIPN {
    private static final String PARAM_ITEM_NUMBER = "item_number";
    private static final String PARAM_MC_CURRENCY = "mc_currency";
    private static final String PARAM_MC_GROSS = "mc_gross";
    private static final String PARAM_RECEIVER_EMAIL = "receiver_email";
    private static final String PARAM_TXN_ID = "txn_id";
    private static final String PARAM_PAYMENT_STATUS = "payment_status";
    private static final String PARAM_PAYMENT_DATE = "payment_date";
    private static final String PARAM_CUSTOM = "custom";
    private static final Logger LOGGER = LoggerFactory.getLogger(PaypalIPN.class);
    private static final NameValuePair CMD_NOTIFY_VALIDATE = new BasicNameValuePair("cmd", "_notify-validate");

    @Autowired
    private PaymentValidator paymentValidator;

    @Autowired
    private PaymentService paymentService;

    /**
     * Processing of payment notification.
     *
     * @param request
     * @param response
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void notification(HttpServletRequest request, HttpServletResponse response) {
        try {
            PaymentInfo paymentInfo = getPaymentInfo(request);
            if (paymentValidator.isPaymentValid(paymentInfo)) {
                String responseContent = getResponseContent(request).trim();
                LOGGER.info("Paypal verification response = {}", responseContent);
                if (paymentValidator.isResponseValid(responseContent)) {
                    String txID = paymentInfo.getTransactionID();
                    long orderNumber = paymentInfo.getOrderNumber();
                    BigDecimal amount = paymentInfo.getAmount();
                    PaypalTransactionStatus status = paymentInfo.getStatus();
                    Date paymentDate = paymentInfo.getPaymentDate();
                    paymentService.saveCredits(txID, orderNumber, amount, status, paymentDate);
                } else {
                    LOGGER.error("Invalid Paypal IPN :: {}", paymentInfo);
                }
            }
        } catch (IllegalStateException | IOException | ParseException e) {
            LOGGER.error("Paypal IPN ERROR", e);
        }
    }

    /**
     * Returns the payment information.
     *
     * @param request
     * @return the payment information
     * @throws ParseException
     */
    private PaymentInfo getPaymentInfo(HttpServletRequest request) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        String status = request.getParameter(PARAM_PAYMENT_STATUS);
        String paymentDateText = request.getParameter(PARAM_PAYMENT_DATE);
        String transactionID = request.getParameter(PARAM_TXN_ID);
        String receiverEmail = request.getParameter(PARAM_RECEIVER_EMAIL);
        String amount = request.getParameter(PARAM_MC_GROSS);
        String currency = request.getParameter(PARAM_MC_CURRENCY);
        String itemNumber = request.getParameter(PARAM_ITEM_NUMBER);
        String orderNumber = request.getParameter(PARAM_CUSTOM);
        float amountValue = numberFormat.parse(amount).floatValue();
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setStatus(status);
        paymentInfo.setPaymentDate(paymentDateText);
        paymentInfo.setTransactionID(transactionID);
        paymentInfo.setReceiverEmail(receiverEmail);
        paymentInfo.setAmount(new BigDecimal(amountValue));
        paymentInfo.setCurrency(currency);
        paymentInfo.setItemNumber(itemNumber);
        paymentInfo.setOrderNumber(Long.parseLong(orderNumber));
        return paymentInfo;
    }

    /**
     * The list of parameters that will be sent back to Paypal for verification.
     *
     * @param request
     * @return the list of parameters
     */
    @SuppressWarnings("unchecked")
    private List<NameValuePair> getValidateRequestParameters(HttpServletRequest request) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(CMD_NOTIFY_VALIDATE);
        LOGGER.info("Paypal IPN");
        for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
            String name = e.nextElement();
            String value = request.getParameter(name);
            NameValuePair pair = new BasicNameValuePair(name, value);
            LOGGER.info(pair.toString());
            params.add(pair);
        }
        return params;
    }

    /**
     * PayPal sends a single word back - either VERIFIED (if the message matches
     * the original) or INVALID (if the message does not match the original).
     *
     * @param request
     * @return verification result - VERIFIED or INVALID.
     * @throws IOException
     * @throws IllegalStateException
     */
    private String getResponseContent(HttpServletRequest request) throws IOException, IllegalStateException {
        List<NameValuePair> params = getValidateRequestParameters(request);
        HttpClient client = new DefaultHttpClient();
        String paypalURL = paymentValidator.getPaypalValidationURL();
        HttpPost post = new HttpPost(paypalURL);
        post.setEntity(new UrlEncodedFormEntity(params));
        InputStream is = client.execute(post).getEntity().getContent();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String result = "";
        String line = null;
        while ((line = br.readLine()) != null) {
            result += line;
        }
        return result;
    }
}
