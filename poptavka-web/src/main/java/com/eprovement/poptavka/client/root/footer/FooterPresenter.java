package com.eprovement.poptavka.client.root.footer;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.root.footer.texts.FooterInfo;
import com.eprovement.poptavka.client.root.footer.texts.FooterInfo.FooterInfoViews;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import com.eprovement.poptavka.client.root.interfaces.IFooterView;
import com.eprovement.poptavka.client.root.interfaces.IFooterView.IFooterPresenter;
import com.eprovement.poptavka.resources.StyleResource;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import java.util.Date;

@Presenter(view = FooterView.class)
public class FooterPresenter extends BasePresenter<IFooterView, RootEventBus>
        implements IFooterPresenter {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private FooterInfo aboutUs;
    private FooterInfo faq;
    private FooterInfo privacyPolicy;
    private FooterInfo termsAndConditions;

    /**************************************************************************/
    /* Bind events                                                            */
    /**************************************************************************/
    @Override
    public void bind() {
        view.getContactUs().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.sendUsEmail(Constants.SUBJECT_GENERAL_QUESTION, (new Date()).toString());
            }
        });
        view.getAboutUs().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onDisplayAboutUs();
            }
        });
        view.getFAQ().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onDisplayFaq();
            }
        });
        view.getPrivacyPolicy().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onDisplayPrivacyPolicy();
            }
        });
        view.getTermsAndConditions().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onDisplayTermsAndConditions();
            }
        });
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    public void onSetFooter(SimplePanel footerPanel) {
        //Martin - maybe we don't need to check for user, just set both menu styles should be ok
        if (Storage.getUser() == null) {
            eventBus.menuStyleChange(Constants.HOME_WELCOME_MODULE);
        } else {
            eventBus.menuStyleChange(Constants.HOME_WELCOME_MODULE);
        }
        footerPanel.setWidget(view.getWidgetView());
    }

    /**
     * Display About Us widget.
     */
    public void onDisplayAboutUs() {
        eventBus.createCustomToken(FooterInfoViews.ABOUT_US.getValue());

        if (aboutUs == null) {
            createFooterInfo(FooterInfoViews.ABOUT_US);
        }
        eventBus.setBody(aboutUs);
        eventBus.setToolbarContent(Storage.MSGS.aboutUsAboutWSTitle(), null, false);
        eventBus.setFooter(aboutUs.getFooterPanel());
    }

    /**
     * Display FAQ widget.
     */
    public void onDisplayFaq() {
        eventBus.createCustomToken(FooterInfoViews.FAQ.getValue());

        if (faq == null) {
            createFooterInfo(FooterInfoViews.FAQ);
        }
        eventBus.setBody(faq);
        eventBus.setToolbarContent(Storage.MSGS.faq(), null, false);
        eventBus.setFooter(faq.getFooterPanel());
    }

    /**
     * Display privacy policy widget.
     */
    public void onDisplayPrivacyPolicy() {
        eventBus.createCustomToken(FooterInfoViews.PRIVACY_POLICY.getValue());

        if (privacyPolicy == null) {
            createFooterInfo(FooterInfoViews.PRIVACY_POLICY);
        }
        eventBus.setBody(privacyPolicy);
        eventBus.setToolbarContent(Storage.MSGS.privacyPolicy(), null, false);
        eventBus.setFooter(privacyPolicy.getFooterPanel());
    }

    /**
     * Display terms & conditions widget.
     */
    public void onDisplayTermsAndConditions() {
        eventBus.createCustomToken(FooterInfoViews.TERMS_AND_CONDITIONS.getValue());

        if (termsAndConditions == null) {
            createFooterInfo(FooterInfoViews.TERMS_AND_CONDITIONS);
        }
        eventBus.setBody(termsAndConditions);
        eventBus.setToolbarContent(Storage.MSGS.footerTermsConditions(), null, false);
        eventBus.setFooter(termsAndConditions.getFooterPanel());
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void createFooterInfo(final FooterInfoViews footerView) {

        GWT.runAsync(FooterInfo.class, new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                //nothing by default
            }

            @Override
            public void onSuccess() {
                StyleResource.INSTANCE.common().ensureInjected();
                switch (footerView) {
                    case ABOUT_US:
                        aboutUs = FooterInfo.createAboutUs();
                        break;
                    case FAQ:
                        faq = FooterInfo.createFAQ();
                        break;
                    case PRIVACY_POLICY:
                        privacyPolicy = FooterInfo.createPrivacyPolicy();
                        break;
                    case TERMS_AND_CONDITIONS:
                        termsAndConditions = FooterInfo.createTermsAndConditions();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
